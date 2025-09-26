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
    -------------------------------- */
    public ScoreRes saveScore(ScorePostReq req) {
        scoreRepository.findByEnrollment_EnrollmentId(req.getEnrollmentId())
                .ifPresent(s -> {
                    throw new RuntimeException("이미 성적이 등록된 수강입니다. 수정 기능을 사용하세요.");
                });

        Enrollment enrollment = enrollmentRepository.findById(req.getEnrollmentId())
                .orElseThrow(() -> new RuntimeException("등록된 수강내역이 없음"));

        double total = calcTotal(req);
        String rank = calcRank(total);

        Score score = Score.builder()
                .enrollment(enrollment)
                .midScore(req.getMidScore())
                .finScore(req.getFinScore())
                .attendanceScore(req.getAttendanceScore())
                .otherScore(req.getOtherScore())
                .rank(rank)
                .build();

        Score saved = scoreRepository.save(score);
        UserResDto userInfo = getUserInfo(enrollment.getUserId());

        return new ScoreRes(
                saved.getScoreId(),
                saved.getEnrollment().getEnrollmentId(),
                saved.getRank(),
                userInfo.getGrade()
        );
    }

    /* -------------------------------
       방법 2) 성적 기입 (POST, 자동 수정)
    -------------------------------- */
    public ScoreRes saveOrUpdateScore(ScorePostReq req) {
        Enrollment enrollment = enrollmentRepository.findById(req.getEnrollmentId())
                .orElseThrow(() -> new RuntimeException("등록된 수강내역이 없음"));

        double total = calcTotal(req);
        String rank = calcRank(total);

        Score score = scoreRepository.findByEnrollment_EnrollmentId(req.getEnrollmentId())
                .orElse(new Score());

        score.setEnrollment(enrollment);
        score.setMidScore(req.getMidScore());
        score.setFinScore(req.getFinScore());
        score.setAttendanceScore(req.getAttendanceScore());
        score.setOtherScore(req.getOtherScore());
        score.setRank(rank);

        Score saved = scoreRepository.save(score);

        enrollment.setStatus("수강완료");
        enrollmentRepository.save(enrollment);

        UserResDto userInfo = getUserInfo(enrollment.getUserId());

        return new ScoreRes(
                saved.getScoreId(),
                saved.getEnrollment().getEnrollmentId(),
                saved.getRank(),
                userInfo.getGrade()
        );
    }

    /* -------------------------------
       성적 수정 (PUT)
    -------------------------------- */
    public ScoreRes updateScore(ScorePutReq req) {
        Score score = scoreRepository.findByEnrollment_EnrollmentId(req.getEnrollmentId())
                .orElseThrow(() -> new RuntimeException("해당 수강의 성적이 존재하지 않음"));

        double total = calcTotal(req);
        String rank = calcRank(total);

        score.setMidScore(req.getMidScore());
        score.setFinScore(req.getFinScore());
        score.setAttendanceScore(req.getAttendanceScore());
        score.setOtherScore(req.getOtherScore());
        score.setRank(rank);

        Score updated = scoreRepository.save(score);
        UserResDto userInfo = getUserInfo(score.getEnrollment().getUserId());

        return new ScoreRes(
                updated.getScoreId(),
                updated.getEnrollment().getEnrollmentId(),
                updated.getRank(),
                userInfo.getGrade()   // ✅ 수정: 학년 반환
        );
    }

    /* -------------------------------
       점수 합산 → 평균(100점 만점 기준)
    -------------------------------- */
    private double calcTotal(ScorePostReq req) {
        return (req.getMidScore()
                + req.getFinScore()
                + req.getAttendanceScore()
                + req.getOtherScore()) / 4.0;
    }

    private double calcTotal(ScorePutReq req) {
        return (req.getMidScore()
                + req.getFinScore()
                + req.getAttendanceScore()
                + req.getOtherScore()) / 4.0;
    }

    /* -------------------------------
       등급 계산 로직
    -------------------------------- */
    private String calcRank(double total) {
        if (total < 50) return "F";
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
       user-service 호출해서 학년 가져오기
    -------------------------------- */
    private UserResDto getUserInfo(Long userId) {
        try {
            Map<String, List<Long>> request = Map.of("userIds", List.of(userId));
            ResultResponse<List<UserResDto>> response = courseUserClient.getUsersByInfo(request);

            List<UserResDto> users = response.getResult();
            if (users != null && !users.isEmpty()) {
                return users.get(0);
            }
        } catch (Exception e) {
            System.err.println(" user-service 호출 실패: " + e.getMessage());
        }
        // fallback: 기본값 반환 (학년=0)
        UserResDto dto = new UserResDto();
        dto.setUserId(userId);
        dto.setUserName("unknown");
        dto.setGrade(0);
        dto.setLoginId(null);
        dto.setDeptName(null);
        return dto;
    }
}

