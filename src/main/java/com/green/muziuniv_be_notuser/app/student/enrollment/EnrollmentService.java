package com.green.muziuniv_be_notuser.app.student.enrollment;

import com.green.muziuniv_be_notuser.app.shared.course.CourseRepository;
import com.green.muziuniv_be_notuser.app.shared.schedule.ScheduleValidator;
import com.green.muziuniv_be_notuser.app.student.enrollment.model.*;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.entity.course.Course;
import com.green.muziuniv_be_notuser.entity.enrollment.Enrollment;
import com.green.muziuniv_be_notuser.openfeign.department.DepartmentClient;
import com.green.muziuniv_be_notuser.openfeign.department.model.DepartmentHeadNameRes;
import com.green.muziuniv_be_notuser.openfeign.user.UserClient;
import com.green.muziuniv_be_notuser.openfeign.user.model.UserInfoDto;
import com.green.muziuniv_be_notuser.app.student.enrollment.exception.EnrollmentException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentMapper enrollmentMapper;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserClient userClient;
    private final DepartmentClient departmentClient;
    private final ScheduleValidator scheduleValidator;

    // 수강 신청 가능한 강의 조회
    public List<EnrollmentFilterRes> getAvailableEnrollmentsCourses(EnrollmentFilterReq req){
        // req에 학과 조건이 있는 경우
        if(req.getDeptId() != null) {
            // 1. 유저 서비스 호출해서 해당 학과의 교수 리스트를 가져옴.
            ResultResponse<List<DepartmentHeadNameRes>> deptProfessors = departmentClient.findDeptHeadList(req.getDeptId());
            // 2. 교수의 userId 리스트만 추출
            List<Long> professorIds = deptProfessors.getResult().stream()
                    .map(d -> d.getUserId())
                    .collect(Collectors.toList());
            // 만약 해당 학과의 교수들이 개설한 강의가 하나도 없는 경우
            if (professorIds.isEmpty()) {
                return Collections.emptyList();
            }
            // 3.EnrollmentFilterReq req 객체에 교수 리스트 세팅 (Mapper에서 in 조건으로 사용)
            req.setProfessorIds(professorIds);
        }
        // 4. req에 따른 courseList 조회
        List<EnrollmentFilterRes> courseList = enrollmentMapper.getAvailableEnrollmentsCourses(req);
        // courseList가 빈 리스트라면 유저 서버를 호출하지 말고 빈 리스트 그대로 반환
        if(courseList.isEmpty()){
            return Collections.emptyList();
        }
        // 5. 강의 리스트에서 교수 Id (userId)만 추출 ( 현재 교수명, 학과명이 비어있으므로 )
        List<Long> professorIds = courseList.stream()
                .map(course -> course.getUserId())
                .distinct()
                .collect(Collectors.toList());

        // 6. 요청용 Map 생성
        Map<String, List<Long>> request = new HashMap<>();
        request.put("userId", professorIds);

        // 유저 서버 호출
        ResultResponse<Map<Long, UserInfoDto>> proInfo = userClient.getUserInfo(request);
        Map<Long, UserInfoDto> proGetResMap = proInfo.getResult();

        // 기존의 강의 데이터에 교수, 학과 정보 주입
        for (EnrollmentFilterRes course : courseList) {
            UserInfoDto userInfoDto = proGetResMap.get(course.getUserId());
            if (userInfoDto != null) {
                course.setProfessorName(userInfoDto.getUserName());

                if (course.getGrade() != 0) {  // 학년이 0이면 학과를 교양학부로
                    course.setDeptName(userInfoDto.getDeptName());
                } else {
                    course.setDeptName("교양학부");
                }

            }
        }

        return courseList;
    }


    // 수강 신청 ( + 중복, 잔여 인원 예외 처리 )
    @Transactional
    public ResponseEntity<?> enrollment(EnrollmentReq req) {

        Long userId = req.getUserId();
        Long courseId = req.getCourseId();

        // 0. 수강신청 기간 체크
        // courseId 기반으로 semesterId 추출. (req에 semesterId를 포함하면 조작해서 수강신청이 가능하므로 막기 위함)
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new EnrollmentException("존재하지 않는 강의입니다."));
        Long semesterId = course.getSemesterId().getSemesterId();
        scheduleValidator.validateOpen(semesterId, "수강신청");

        // 1. 중복 수강신청 여부 체크
        if(enrollmentRepository.existsByUserIdAndCourse_CourseId(userId, courseId)) {
            throw new EnrollmentException("수강신청 실패! 이미 신청한 강의입니다.");
        }

        // 2. 잔여 인원 체크
        int remStd = enrollmentRepository.checkRemainingSeats(courseId);
        if(remStd <= 0) {
            throw new EnrollmentException("수강신청 실패! 잔여 인원이 없습니다.");
        }

        // 3. 수강 신청 시도
        // 승인 강의만 신청 가능
        if(!"승인".equals(course.getStatus())) {
            throw new EnrollmentException("수강신청 실패! 승인된 강의만 신청할 수 있습니다.");
        }
        Enrollment enrollment = new Enrollment();
        enrollment.setUserId(userId);
        enrollment.setCourse(course);
        enrollment.setStatus("수강중");
        enrollmentRepository.save(enrollment);

        // 4. 수강 신청 성공 시 해당 강의의 잔여인원을 한 명 감소
        enrollmentRepository.decreaseRemainingSeats(courseId);

        // 5. 수강 신청 완료한 강의 정보 조회 ( 이 시점에 학과, 교수명은 null )
        EnrollmentRes res = new EnrollmentRes();
        res.setStatus(course.getStatus());
        res.setCourseId(course.getCourseId());
        res.setCourseCode(course.getCourseCode());
        res.setTitle(course.getTitle());
        res.setClassroom(course.getClassroom());
        res.setType(course.getType());
        res.setUserId(course.getUserId().getUserId());
        res.setGrade(course.getGrade());
        res.setYear(course.getSemesterId().getYear());
        res.setSemester(course.getSemesterId().getSemester());
        res.setTime(course.getTime());
        res.setCredit(course.getCredit());
        res.setMaxStd(course.getMaxStd());
        res.setRemStd(course.getRemStd());

        // 6. user 서버 통신

        // req 세팅
        Map<String, List<Long>> request = Map.of("userId", List.of(res.getUserId()));

        // 유저 서버 호출
        ResultResponse<Map<Long, UserInfoDto>> response = userClient.getUserInfo(request);

        // 매핑
        UserInfoDto userInfoDto = response.getResult().get(res.getUserId());
        if (userInfoDto != null) {
            res.setProfessorName(userInfoDto.getUserName());
            res.setDeptName(userInfoDto.getDeptName());
        }

        return ResponseEntity.ok(new ResultResponse<>("수강 신청 성공", res));

    }

    // 금학기 수강 신청 내역 조회
    public  ResponseEntity<?> getMyCurrentEnrollmentsCourses(Long userId, Long semesterId) {

        // 금학기 수강 신청 내역 가져옴 ( 교수명, 학과 null )
        List<GetMyCurrentEnrollmentsCoursesRes> courseList = enrollmentMapper.getMyCurrentEnrollmentsCourses(userId, semesterId);
        if (courseList.isEmpty()) { // 금학기 수강 신청 내역이 없을 때
            return ResponseEntity.ok(new ResultResponse<>("수강 신청 내역이 없습니다.", courseList)); // 빈 배열 리턴
        }

        // 강의 리스트에서 교수 Id (userId)만 추출
        List<Long> professorIds = courseList.stream()
                .map(course -> course.getUserId())
                .distinct()
                .collect(Collectors.toList()); //stream을 다시 list로 바꿈.

        // 요청용 Map 생성
        Map<String, List<Long>> request = Map.of("userId", professorIds);

        // 유저 서버 호출
                ResultResponse<Map<Long, UserInfoDto>> response = userClient.getUserInfo(request);
                Map<Long, UserInfoDto> proGetResMap = response.getResult();

        // 기존 강의 데이터에 교수, 학과 정보 주입
                for (GetMyCurrentEnrollmentsCoursesRes course : courseList) {
                    UserInfoDto userInfoDto = proGetResMap.get(course.getUserId());
                    if (userInfoDto != null) {
                        course.setProfessorName(userInfoDto.getUserName());
                        course.setDeptName(userInfoDto.getDeptName());
                    }
                }

        return ResponseEntity.ok(courseList);
    }

    // 수강 취소
    @Transactional
    public int deleteMyEnrollmentCourse(Long userId, Long courseId) {
        // 0. 수강 취소 기간 체크 ( 수강 신청 기간에 이루어지는 행위이므로 수강 신청 기간이랑 동일함 )
        // courseId 기반으로 semesterId 추출. (req에 semesterId를 포함하면 조작해서 수강취소가 가능해지므로 막기 위함)
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new EnrollmentException("존재하지 않는 강의입니다."));
        Long semesterId = course.getSemesterId().getSemesterId();
        scheduleValidator.validateOpen(semesterId, "수강신청");
        return enrollmentRepository.deleteMyEnrollmentCourse(userId, courseId);
    }

}
