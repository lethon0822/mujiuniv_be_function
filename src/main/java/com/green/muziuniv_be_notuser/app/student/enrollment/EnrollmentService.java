package com.green.muziuniv_be_notuser.app.student.enrollment;

import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.entity.course.Course;
import com.green.muziuniv_be_notuser.entity.enrollment.Enrollment;
import com.green.muziuniv_be_notuser.openfeign.user.UserClient;
import com.green.muziuniv_be_notuser.openfeign.user.model.ProGetRes;
import com.green.muziuniv_be_notuser.app.shared.course.CourseRepository;
import com.green.muziuniv_be_notuser.app.student.enrollment.exception.EnrollmentException;
import com.green.muziuniv_be_notuser.app.student.enrollment.model.EnrollmentReq;
import com.green.muziuniv_be_notuser.app.student.enrollment.model.EnrollmentRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserClient userClient;

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
        Enrollment enrollment = new Enrollment();
        enrollment.setUserId(userId);
        enrollment.setCourse(course);
        enrollment.setStatus("수강중");
        enrollmentRepository.save(enrollment);

        // 4. 수강 신청 성공 시 해당 강의의 잔여인원을 한 명 감소
        enrollmentRepository.decreaseRemainingSeats(courseId);

        // 5. 수강 신청 완료한 강의 정보 조회 ( 이 시점에 학과, 교수명은 null )
        EnrollmentRes res = courseRepository.findCourseByCourseId(courseId);

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

        return ResponseEntity.ok(res);

    }
}
