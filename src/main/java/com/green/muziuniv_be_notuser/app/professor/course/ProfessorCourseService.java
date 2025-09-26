package com.green.muziuniv_be_notuser.app.professor.course;

import com.green.muziuniv_be_notuser.app.professor.course.model.*;
import com.green.muziuniv_be_notuser.app.shared.course.CourseRepository;
import com.green.muziuniv_be_notuser.app.shared.schedule.ScheduleValidator;
import com.green.muziuniv_be_notuser.app.student.enrollment.EnrollmentRepository;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.entity.UserId;
import com.green.muziuniv_be_notuser.entity.course.Course;
import com.green.muziuniv_be_notuser.entity.semester.Semester;
import com.green.muziuniv_be_notuser.openfeign.course.CourseUserClient;
import com.green.muziuniv_be_notuser.openfeign.course.model.UserResDto;
import com.green.muziuniv_be_notuser.entity.enrollment.Enrollment;
import com.green.muziuniv_be_notuser.openfeign.user.UserClient;
import com.green.muziuniv_be_notuser.openfeign.user.model.UserInfoDto;
import lombok.RequiredArgsConstructor;
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
    private final ScheduleValidator scheduleValidator;


    // 강의 등록
    public void saveCourse(ProfessorPostReq req) {
        try {
            boolean saved = false;
            do {
                // 1. 중복 없는 courseCode 생성
                String courseCode = createCourseCode(req.getUserId());

                // 2. DB에 이미 존재하는지 확인
                int duplicate = professorCourseMapper.findSameCode(courseCode, req.getSemesterId());

                if (duplicate == 0) {
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
                    courseRepository.save(course);

                    saved = true; // 성공하면 루프 종료
                }
                // 중복이면 do-while 반복해서 새 코드 생성
            } while (!saved);

        } catch (Exception e) {
            // 예상치 못한 DB 오류나 네트워크 오류 등
            throw new RuntimeException("오류 발생. 잠시 후 다시 시도하십시오.", e);
        }
    }

    private String createCourseCode(Long userId){
        //Random random = new Random();
//        for(int i = 0; i<3; i++){
//            courseCode += (char)(random.nextInt(26)+65);
//        }
        String courseCode = userClient.getProDeptCode(userId);

        for(int i = 0; i <4; i++){
            courseCode += (int)(Math.random()*10);
        }
        return courseCode;
    }


    // 내 강의 목록 조회
    public List<ProfessorGetRes> findMyCourse(ProfessorGetReq req) {
        // 1. notuser DB에서 강의 목록 가져오기
        List<ProfessorGetRes> courses = professorCourseMapper.findByUserId(req);
        if (courses.isEmpty()){ return null ;}
        Set<Long> userInfo = courses.stream().map(item -> item.getUserId()).collect(Collectors.toSet());

        // 2. 교수 학과 가져오기 (단일)
        Map<String, List<Long>> request = new HashMap<>();
        request.put("userId", new ArrayList<>(userInfo));

        ResultResponse<Map<Long, UserInfoDto>> proInfo = userClient.getUserInfo(request);
        Map<Long, UserInfoDto> proGetResMap = proInfo.getResult();

        // 3. 각 강의 DTO에 학과명 채워넣기
        for (ProfessorGetRes course : courses) {
            course.setDeptName(proGetResMap.get(course.getUserId()).getDeptName());
            course.setUserName(proGetResMap.get(course.getUserId()).getUserName());
        }
        return courses;
    }

    //강의평가 조회
    public List<ProfessorSurveyCheckRes> checkSurvey(int courseId){
        return professorCourseMapper.findSurveyByCourseId(courseId);
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

        ResultResponse<List<UserResDto>> response = courseUserClient.getUsersByInfo(request);
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
                            (user != null && user.getGrade() != null) ? user.getGrade() : 0,
                            user != null ? user.getDeptName() : null
                    );
                })
                .toList();
    }

    //강의 계획서 수정
    public void modify(ProfessorPutReq req) {
        Course course = courseRepository.findById(req.getCourseId())
                .orElseThrow(() -> new RuntimeException("해당 강의가 존재하지 않습니다"));


        course.setClassroom(req.getClassroom());
        course.setType(req.getType());
        course.setTime(req.getTime());
        course.setTitle(req.getTitle());
        course.setCredit(req.getCredit());
        course.setWeekPlan(req.getWeekPlan());
        course.setTextBook(req.getTextBook());
        course.setGoal(req.getGoal());
        course.setMaxStd(req.getMaxStd());
        course.setGrade(req.getGrade());

        courseRepository.save(course);
    }

    //강의 삭제
    // 자기강의만 삭제
    public int deleteCourse(int courseId, Long userId) {
        int result = professorCourseMapper.deleteCourse(courseId, userId);
        if (result == 0) {
            throw new RuntimeException("삭제할 권한이 없거나 강의가 존재하지 않습니다.");
        }
        return result;
    }
}