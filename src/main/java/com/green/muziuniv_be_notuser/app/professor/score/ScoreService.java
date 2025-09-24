package com.green.muziuniv_be_notuser.app.professor.score;

import com.green.muziuniv_be_notuser.app.professor.score.model.ScorePostReq;
import com.green.muziuniv_be_notuser.app.professor.score.model.ScorePutReq;
import com.green.muziuniv_be_notuser.app.professor.score.model.ScoreRes;
import com.green.muziuniv_be_notuser.app.student.enrollment.EnrollmentRepository;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.entity.enrollment.Enrollment;
import com.green.muziuniv_be_notuser.entity.score.Score;
import com.green.muziuniv_be_notuser.openfeign.course.CourseUserClient;
import com.green.muziuniv_be_notuser.openfeign.course.model.UserResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseUserClient courseUserClient;

    /* -------------------------------
       방법 1) 성적 기입 (POST, 중복 방지)
       → 이미 등록된 성적이 있으면 예외 발생
    -------------------------------- */
    public ScoreRes saveScore(ScorePostReq req) {
        // 중복 체크
        scoreRepository.findByEnrollment_EnrollmentId(req.getEnrollmentId())
                .ifPresent(s -> {
                    throw new RuntimeException("이미 성적이 등록된 수강입니다. 수정 기능을 사용하세요.");
                });

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

    /* -------------------------------
       방법 2) 성적 기입 (POST, 자동 수정)
       → 이미 있으면 update, 없으면 insert
    -------------------------------- */
    public ScoreRes saveOrUpdateScore(ScorePostReq req) {
        Enrollment enrollment = enrollmentRepository.findById(req.getEnrollmentId())
                .orElseThrow(() -> new RuntimeException("등록된 수강내역이 없음"));

        int rawTotal = req.getMidScore() + req.getFinScore()
                + req.getAttendanceScore() + req.getOtherScore();
        double total = rawTotal / 4.0;

        String rank = calcRank(total);

        // 이미 존재하면 가져오고, 없으면 새로 생성
        Score score = scoreRepository.findByEnrollment_EnrollmentId(req.getEnrollmentId())
                .orElse(new Score());

        score.setEnrollment(enrollment);
        score.setMidScore(req.getMidScore());
        score.setFinScore(req.getFinScore());
        score.setAttendanceScore(req.getAttendanceScore());
        score.setOtherScore(req.getOtherScore());
        score.setRank(rank);
//        score.setGrade(grade);

        Score saved = scoreRepository.save(score);

        enrollment.setStatus("수강완료");
        enrollmentRepository.save(enrollment);

        // user-service 호출해서 학년 가져오기
        Map<String, List<Long>> request = Map.of("userIds", List.of(enrollment.getUserId()));
        ResultResponse<List<UserResDto>> response = courseUserClient.getUsersByIds(request);

        UserResDto userInfo = response.getResult().get(0);

        return new ScoreRes(
                saved.getScoreId(),
                saved.getEnrollment().getEnrollmentId(),
                saved.getRank(),
                userInfo.getGradeYear()
        );
    }

    /* -------------------------------
       성적 수정 (PUT)
       → 반드시 기존 데이터가 있어야 수정 가능
    -------------------------------- */
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

    /* -------------------------------
       등급 계산 로직
    -------------------------------- */
    private String calcRank(double total) {
        if (total >= 95) return "A+";
        else if (total >= 90) return "A";
        else if (total >= 85) return "B+";
        else if (total >= 80) return "B";
        else if (total >= 75) return "C+";
        else if (total >= 70) return "C";
        else if (total >= 60) return "D";
        else return "F";
    }

    /* -------------------------------
       학점 변환 로직
    -------------------------------- */
    private int calcGrade(String rank) {
        return switch (rank) {
            case "A+", "A" -> 4;
            case "B+", "B" -> 3;
            case "C+", "C" -> 2;
            case "D" -> 1;
            default -> 0; // F
        };
    }
}
