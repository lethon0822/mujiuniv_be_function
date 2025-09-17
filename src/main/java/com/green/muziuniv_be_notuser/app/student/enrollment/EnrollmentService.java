package com.green.muziuniv_be_notuser.app.student.enrollment;

import com.green.muziuniv_be_notuser.app.shared.course.CourseRepository;
import com.green.muziuniv_be_notuser.app.student.enrollment.model.GetMyCurrentEnrollmentsCoursesRes;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.entity.course.Course;
import com.green.muziuniv_be_notuser.entity.enrollment.Enrollment;
import com.green.muziuniv_be_notuser.openfeign.user.UserClient;
import com.green.muziuniv_be_notuser.openfeign.user.model.ProGetRes;
import com.green.muziuniv_be_notuser.app.student.enrollment.exception.EnrollmentException;
import com.green.muziuniv_be_notuser.app.student.enrollment.model.EnrollmentReq;
import com.green.muziuniv_be_notuser.app.student.enrollment.model.EnrollmentRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentMapper enrollmentMapper;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserClient userClient;

    // 수강 신청 ( + 중복, 잔여 인원 예외 처리 )
    @Transactional
    public ResponseEntity<?> enrollment(EnrollmentReq req) {

        //JPA는 DTO를 통째로 못받고 엔티티 필드 단위로 줘야함.
        Long userId = req.getUserId();
        Long courseId = req.getCourseId();

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
        // course를 jpa가 관리하는 영속 객체로 만들어줘야함.
        Course course = courseRepository.getReferenceById(courseId);
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
        ResultResponse<List<ProGetRes>> response = userClient.getProInfo(request);

        // 매핑
        if(!response.getResult().isEmpty()){
            ProGetRes proGetRes = response.getResult().get(0);
            res.setProfessorName(proGetRes.getUserName());
            res.setDeptName(proGetRes.getDeptName());
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
        Map<String, List<Long>> request = new HashMap<>();
        request.put("userId", professorIds);

        // 유저 서버 호출
        ResultResponse<List<ProGetRes>> response = userClient.getProInfo(request);
        List<ProGetRes> professorsInfos = response.getResult();

        Map<Long, ProGetRes> proGetResMap = professorsInfos.stream()
                .collect(Collectors.toMap(professor -> professor.getUserId(), professor -> professor));

        // 기존의 강의 데이터에 교수, 학과 정보 주입
        for (GetMyCurrentEnrollmentsCoursesRes course : courseList) {
            ProGetRes proGetRes = proGetResMap.get(course.getUserId());
            if (proGetRes != null) {
                course.setProfessorName(proGetRes.getUserName());
                course.setDeptName(proGetRes.getDeptName());
            }

        }
        return ResponseEntity.ok(courseList);
    }

    // 수강 취소
    @Transactional
    public int deleteMyEnrollmentCourse(Long userId, Long courseId) {
        return enrollmentRepository.deleteMyEnrollmentCourse(userId, courseId);
    }

}
