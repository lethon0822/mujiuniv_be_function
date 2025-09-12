package com.green.muziuniv_be_notuser.student.enrollment;

import com.green.muziuniv_be_notuser.entity.enrollment.Enrollment;
import com.green.muziuniv_be_notuser.shared.course.CourseRepository;
import com.green.muziuniv_be_notuser.student.enrollment.exception.EnrollmentException;
import com.green.muziuniv_be_notuser.student.enrollment.model.EnrollmentReq;
import com.green.muziuniv_be_notuser.student.enrollment.model.EnrollmentRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;


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
        Enrollment enrollment = new Enrollment(userId, courseId);
        enrollmentRepository.save(enrollment);

        // 4. 수강 신청 성공 시 해당 강의의 잔여인원을 한 명 감소
        enrollmentRepository.decreaseRemainingSeats(courseId);

        // 5. 수강 신청 완료한 강의 정보 조회 ( 이 시점에 학과, 교수명은 null )
        EnrollmentRes res = courseRepository.findCourseByCourseId(courseId);



        return null;
    }
}
