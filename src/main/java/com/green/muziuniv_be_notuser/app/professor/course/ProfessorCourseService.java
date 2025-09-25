package com.green.muziuniv_be_notuser.app.professor.course;

import com.green.muziuniv_be_notuser.app.professor.coursemanage.model.ProfessorGetReq;
import com.green.muziuniv_be_notuser.app.professor.coursemanage.model.ProfessorGetRes;
import com.green.muziuniv_be_notuser.app.professor.coursemanage.model.ProfessorPostReq;
import com.green.muziuniv_be_notuser.app.shared.course.CourseRepository;
import com.green.muziuniv_be_notuser.app.student.enrollment.EnrollmentRepository;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.entity.UserId;
import com.green.muziuniv_be_notuser.entity.course.Course;
import com.green.muziuniv_be_notuser.entity.semester.Semester;
import com.green.muziuniv_be_notuser.openfeign.course.CourseUserClient;
import com.green.muziuniv_be_notuser.app.professor.course.model.CourseStudentRes;
import com.green.muziuniv_be_notuser.openfeign.course.model.UserResDto;
import com.green.muziuniv_be_notuser.entity.enrollment.Enrollment;
import com.green.muziuniv_be_notuser.openfeign.user.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessorCourseService {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final ProfessorCourseMapper professorCourseMapper;
    private final CourseUserClient courseUserClient;
    private final UserClient userClient;
    private final List list;

    // 강의 등록
    public void saveCourse(ProfessorPostReq req) {
        boolean saved = false;

        while(!saved) {
            try {
                Course course = insertCourse(req);
                courseRepository.save(course);
                saved = true;
            } catch (DataIntegrityViolationException e) {
            }
        }
    }

    private Course insertCourse(ProfessorPostReq req){
        //Random random = new Random();
        ResultResponse<String> response = userClient.getProDeptCode(req.getUserId());
        String courseCode = response.getResult();
//        for(int i = 0; i<3; i++){
//            courseCode += (char)(random.nextInt(26)+65);
//        }
        for(int i = 0; i <4; i++){
            courseCode += (int)(Math.random()*10);
        }

        UserId userId = new UserId(req.getUserId());
        Semester semester = Semester.builder()
                .semesterId(req.getSemesterId())
                .build();

        Course course = Course.builder()
                .courseCode(courseCode)
                .userId(userId)
                .semesterId(semester)
                .classroom(req.getClassroom())
                .type(req.getType())
                .time(req.getTime())
                .title(req.getTitle())
                .credit(req.getCredit())
                .weekPlan(req.getWeekPlan())
                .textBook(req.getTextBook())
                .goal(req.getGoal())
                .maxStd(req.getMaxStd())
                .grade(req.getGrade())
                .build();

        return course;
    }

    // TODO: 교수 학과 가져오기 문제임
    // 내 강의 목록 조회
    public List<ProfessorGetRes> findMyCourse(ProfessorGetReq req) {
        // 1. notuser DB에서 강의 목록 가져오기
        List<ProfessorGetRes> courses = professorCourseMapper.findByUserId(req);
        if (courses.isEmpty()){ return null ;}
        Set<Long> userInfo = courses.stream().map(item -> item.getUserId()).collect(Collectors.toSet());

        // 2. 교수 학과 가져오기 (단일)
        ResultResponse<String> deptRes = userClient.getProDeptCode(req.getUserId());
        String deptName = deptRes.getResult();

        // 3. 각 강의 DTO에 학과명 채워넣기
        for (ProfessorGetRes course : courses) {
            course.setDeptName(deptName);
        }

        return courses;
    }

    // 강의별 학생 리스트 조회
    public List<CourseStudentRes> getCourseStudents(Long courseId) {
        // 1. Enrollment 에서 학생 userId 추출
        List<Enrollment> enrollments = enrollmentRepository.findByCourse_CourseId(courseId);
        List<Long> userIds = enrollments.stream()
                .map(Enrollment::getUserId)
                .toList();

        if (userIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. User-service 호출
        Map<String, List<Long>> request = new HashMap<>();
        request.put("userId", userIds);

        ResultResponse<List<UserResDto>> response = courseUserClient.getUsersByIds(request);
        List<UserResDto> users = response.getResult();

        // 3. userId -> UserResDto 매핑
        Map<Long, UserResDto> userMap = users.stream()
                .collect(Collectors.toMap(UserResDto::getUserId, u -> u));

        // 4. 응답 조합
        return enrollments.stream()
                .map(e -> {
                    UserResDto user = userMap.get(e.getUserId());
                    return new CourseStudentRes(
                            e.getEnrollmentId(),
                            e.getUserId(),
                            user != null ? user.getLoginId() : null,
                            user != null ? user.getUserName() : null,
                            (user != null && user.getGradeYear() != null) ? user.getGradeYear() : 0,
                            user != null ? user.getDeptName() : null
                    );
                })
                .toList();
    }
}