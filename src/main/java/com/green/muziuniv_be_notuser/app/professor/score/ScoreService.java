package com.green.muziuniv_be_notuser.app.professor.score;

import com.green.muziuniv_be_notuser.app.professor.score.model.ScorePostReq;
import com.green.muziuniv_be_notuser.app.professor.score.model.ScorePutReq;
import com.green.muziuniv_be_notuser.app.professor.score.model.ScoreRes;
import com.green.muziuniv_be_notuser.app.student.enrollment.EnrollmentRepository;
import com.green.muziuniv_be_notuser.entity.enrollment.Enrollment;
import com.green.muziuniv_be_notuser.entity.score.Score;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final EnrollmentRepository enrollmentRepository;

    /* 성적 기입(POST) */
    public ScoreRes saveScore(ScorePostReq req) {
        Enrollment enrollment = enrollmentRepository.findById(req.getEnrollmentId())
                .orElseThrow(() -> new RuntimeException("등록된 수강내역이 없음"));

        int total = req.getMidScore() + req.getFinScore()
                + req.getAttendanceScore() + req.getOtherScore();

        String rank = calcRank(total);
        int grade = calcGrade(rank);

        Score score = Score.builder()
                .enrollment(enrollment)
                .midScore(req.getMidScore())
                .finScore(req.getFinScore())
                .attendanceScore(req.getAttendanceScore())
                .otherScore(req.getOtherScore())
                .rank(rank)
                .grade(grade)
                .build();

        Score saved = scoreRepository.save(score);

        return new ScoreRes(
                saved.getScoreId(),
                saved.getEnrollment().getEnrollmentId(),
                saved.getRank(),
                saved.getGrade()
        );
    }
    /* 성적 수정 (PUT) */
    public ScoreRes updateScore(ScorePutReq req) {
        Score score = scoreRepository.findByEnrollment_EnrollmentId(req.getEnrollmentId())
                .orElseThrow(() -> new RuntimeException("해당 수강의 성적이 존재하지 않음"));

        int total = req.getMidScore() + req.getFinScore()
                + req.getAttendanceScore() + req.getOtherScore();

        String rank = calcRank(total);
        int grade = calcGrade(rank);

        score.setMidScore(req.getMidScore());
        score.setFinScore(req.getFinScore());
        score.setAttendanceScore(req.getAttendanceScore());
        score.setOtherScore(req.getOtherScore());
        score.setRank(rank);
        score.setGrade(grade);

        Score updated = scoreRepository.save(score);

        return new ScoreRes(
                updated.getScoreId(),
                updated.getEnrollment().getEnrollmentId(),
                updated.getRank(),
                updated.getGrade()
        );
    }
    /* 등급 계산 로직 */
    private String calcRank(int total) {
        if (total >= 95) return "A+";
        else if (total >= 90) return "A";
        else if (total >= 85) return "B+";
        else if (total >= 80) return "B";
        else if (total >= 75) return "C+";
        else if (total >= 70) return "C";
        else if (total >= 60) return "D";
        else return "F";
    }
    /* 학점 변환 */
    private int calcGrade(String rank) {
        return switch (rank) {
            case "A+" -> 4;
            case "A"  -> 4;
            case "B+" -> 3;
            case "B"  -> 3;
            case "C+" -> 2;
            case "C"  -> 2;
            case "D"  -> 1;
            default   -> 0; // F
        };
    }

}

