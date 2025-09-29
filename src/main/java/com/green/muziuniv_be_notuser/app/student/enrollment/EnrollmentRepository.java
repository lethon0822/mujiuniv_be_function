package com.green.muziuniv_be_notuser.app.student.enrollment;

import com.green.muziuniv_be_notuser.entity.enrollment.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // 중복 수강신청 방지
    boolean existsByUserIdAndCourse_CourseId(Long userId, Long courseId);

    // 잔여 인원 확인
    @Query("SELECT c.remStd FROM Course c WHERE c.courseId= :courseId")
    Integer checkRemainingSeats(Long courseId);

    // 총 신청 학점 확인 (18학점 이하 여부)
    @Query("SELECT COALESCE(SUM(e.course.credit), 0) " +
    "FROM Enrollment e " +
    "WHERE e.userId = :userId "+
    "AND e.course.semesterId.semesterId = :semesterId " +
    "AND e.status = '수강중'")
    int getCurrentTotalCredits(Long userId, Long semesterId);

    // 수강 신청 성공시 잔여 인원 감소
    @Modifying
    @Query("UPDATE Course c SET c.remStd = c.remStd-1 WHERE c.courseId= :courseId")
    void decreaseRemainingSeats(Long courseId);

    // 수강 신청 취소
    @Modifying
    @Query("DELETE FROM Enrollment e WHERE e.course.courseId= :courseId AND e.userId= :userId AND e.status='수강중'")
    int deleteMyEnrollmentCourse(Long userId, Long courseId);

    // 수강 신청 취소시 잔여 인원 증가
    @Modifying
    @Query("UPDATE Course c SET c.remStd = c.remStd-1 WHERE c.courseId= :courseId")
    void increaseRemainingSeats(Long courseId);

    List<Enrollment> findByCourse_CourseId(Long courseId);
}
