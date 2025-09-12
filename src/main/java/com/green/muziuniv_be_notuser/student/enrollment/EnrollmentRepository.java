package com.green.muziuniv_be_notuser.student.enrollment;

import com.green.muziuniv_be_notuser.entity.enrollment.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // 중복 수강신청 방지
    boolean existsByUserIdAndCourse_CourseId(Long userId, Long courseId);

    // 잔여 인원 확인
    @Query("SELECT c.remStd FROM Course c WHERE c.courseId= :courseId")
    int checkRemainingSeats(Long courseId);

    // 수강 신청 성공시 잔여 인원 감소
    @Modifying
    @Query("UPDATE Course c SET c.remStd = c.remStd-1 WHERE c.courseId= :courseId")
    void decreaseRemainingSeats(Long courseId);
}
