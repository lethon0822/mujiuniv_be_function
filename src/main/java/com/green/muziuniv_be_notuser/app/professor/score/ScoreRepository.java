package com.green.muziuniv_be_notuser.app.professor.score;

import com.green.muziuniv_be_notuser.entity.score.Score;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    // Enrollment ID 기준으로 성적 조회
    Optional<Score> findByEnrollment_EnrollmentId(Long enrollmentId);
}
