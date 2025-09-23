package com.green.muziuniv_be_notuser.app.shared.courseEvaluation;

import com.green.muziuniv_be_notuser.entity.enrollment.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CourseEvaluationRepository extends JpaRepository<Enrollment,Long> {

    // 강의평가 등록 (학생)
    @Modifying
    @Transactional
    @Query("UPDATE Enrollment e " +
            "SET e.review = :review, " +
            "e.evScore = :evScore " +
            "WHERE e.enrollmentId = :enrollmentId " +
            "AND e.userId = :userId " )
    int updateEvaluation(Long userId, Long enrollmentId, String review, Integer evScore);
}
