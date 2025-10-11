package com.green.muziuniv_be_notuser.app.professor.score;

import com.green.muziuniv_be_notuser.app.professor.attendance.AttendanceService;
import com.green.muziuniv_be_notuser.app.professor.attendance.model.AttendanceSummaryRes;
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
    private final AttendanceService attendanceService;

    /* ✅ 출결 점수 계산 로직 (결석 수 기준) */
    private int calculateAttendanceScore(int absent) {
        if (absent <= 5) return 100;
        else if (absent <= 9) return 90;
        else if (absent <= 13) return 80;
        else if (absent <= 17) return 70;
        else if (absent <= 21) return 60;
        else if (absent <= 25) return 50;
        else return 0;
    }

    /* -------------------------------
       성적 등록 (POST, 중복 방지)
    -------------------------------- */
    public ScoreRes saveScore(ScorePostReq req) {
        Enrollment enrollment = enrollmentRepository.findById(req.getEnrollmentId())
                .orElseThrow(() -> new RuntimeException("등록된 수강내역이 없음"));

        scoreRepository.findByEnrollment(enrollment)
                .ifPresent(s -> {
                    throw new RuntimeException("이미 성적이 등록된 수강입니다. 수정 기능을 사용하세요.");
                });

        // ✅ 프론트에서 전달된 출석일수 기반 계산
        int attended = req.getAttendanceScore(); // 출석일수
        if (attended > 50) attended = 50; // 50 이상 방지
        int absent = 50 - attended; // 결석일수
        int attendanceScore = calculateAttendanceScore(absent);

        double total = calcTotal(req.getMidScore(), req.getFinScore(), attendanceScore, req.getOtherScore());
        String rank = calcRank(total);

        Score score = Score.builder()
                .enrollment(enrollment)
                .midScore(req.getMidScore())
                .finScore(req.getFinScore())
                .attendanceScore(attendanceScore)
                .attendanceDays(attended) // ✅ 출석일수 저장
                .otherScore(req.getOtherScore())
                .rank(rank)
                .grade(req.getGrade())
                .build();

        Score saved = scoreRepository.save(score);

        // ✅ 학년 정보 가져오기 (user-service 실패 시 fallback)
        UserResDto userInfo = getUserInfo(enrollment.getUserId());
        int grade = (userInfo != null && userInfo.getGrade() != 0)
                ? userInfo.getGrade()
                : req.getGrade();

        return new ScoreRes(
                saved.getScoreId(),
                saved.getEnrollment().getEnrollmentId(),
                saved.getRank(),
                grade,
                saved.getMidScore(),
                saved.getFinScore(),
                saved.getAttendanceScore(),
                saved.getOtherScore(),
                total,
                calcGpa(rank),
                saved.getAttendanceDays(),
                absent
        );
    }

    /* -------------------------------
       성적 등록 or 수정 (POST)
    -------------------------------- */
    public ScoreRes saveOrUpdateScore(ScorePostReq req) {
        Enrollment enrollment = enrollmentRepository.findById(req.getEnrollmentId())
                .orElseThrow(() -> new RuntimeException("등록된 수강내역이 없음"));

        int attended = req.getAttendanceScore();
        if (attended > 50) attended = 50;
        int absent = 50 - attended;
        int attendanceScore = calculateAttendanceScore(absent);

        double total = calcTotal(req.getMidScore(), req.getFinScore(), attendanceScore, req.getOtherScore());
        String rank = calcRank(total);

        Score score = scoreRepository.findByEnrollment_EnrollmentId(req.getEnrollmentId())
                .orElse(new Score());

        score.setEnrollment(enrollment);
        score.setMidScore(req.getMidScore());
        score.setFinScore(req.getFinScore());
        score.setAttendanceScore(attendanceScore);
        score.setAttendanceDays(attended); // ✅ 출석일수 반영
        score.setOtherScore(req.getOtherScore());
        score.setRank(rank);
        score.setGrade(req.getGrade());

        Score saved = scoreRepository.save(score);
        enrollment.setStatus("수강완료");
        enrollmentRepository.save(enrollment);

        UserResDto userInfo = getUserInfo(enrollment.getUserId());
        int grade = (userInfo != null && userInfo.getGrade() != 0)
                ? userInfo.getGrade()
                : req.getGrade();

        return new ScoreRes(
                saved.getScoreId(),
                saved.getEnrollment().getEnrollmentId(),
                saved.getRank(),
                grade,
                saved.getMidScore(),
                saved.getFinScore(),
                saved.getAttendanceScore(),
                saved.getOtherScore(),
                total,
                calcGpa(rank),
                saved.getAttendanceDays(),
                absent
        );
    }

    /* -------------------------------
       성적 수정 (PUT)
    -------------------------------- */
    public ScoreRes updateScore(ScorePutReq req) {
        Score score = scoreRepository.findByEnrollment_EnrollmentId(req.getEnrollmentId())
                .orElseThrow(() -> new RuntimeException("해당 수강의 성적이 존재하지 않음"));

        int attended = req.getAttendanceScore();
        if (attended > 50) attended = 50;
        int absent = 50 - attended;
        int attendanceScore = calculateAttendanceScore(absent);

        double total = calcTotal(req.getMidScore(), req.getFinScore(), attendanceScore, req.getOtherScore());
        String rank = calcRank(total);

        score.setMidScore(req.getMidScore());
        score.setFinScore(req.getFinScore());
        score.setAttendanceScore(attendanceScore);
        score.setAttendanceDays(attended);
        score.setOtherScore(req.getOtherScore());
        score.setRank(rank);
        score.setGrade(req.getGrade());

        Score updated = scoreRepository.save(score);
        UserResDto userInfo = getUserInfo(score.getEnrollment().getUserId());
        int grade = (userInfo != null && userInfo.getGrade() != 0)
                ? userInfo.getGrade()
                : req.getGrade();

        return new ScoreRes(
                updated.getScoreId(),
                updated.getEnrollment().getEnrollmentId(),
                updated.getRank(),
                grade,
                updated.getMidScore(),
                updated.getFinScore(),
                updated.getAttendanceScore(),
                updated.getOtherScore(),
                total,
                calcGpa(rank),
                updated.getAttendanceDays(),
                absent
        );
    }

    /* -------------------------------
       점수 합산 (100점 만점 기준)
    -------------------------------- */
    private double calcTotal(int mid, int fin, int attendance, int other) {
        return (mid + fin + attendance + other) / 4.0;
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
        else if (total >= 65) return "D+";
        else if (total >= 60) return "D";
        else return "F";
    }

    /* -------------------------------
       평점(GPA) 계산 로직
    -------------------------------- */
    private double calcGpa(String rank) {
        return switch (rank) {
            case "A+" -> 4.5;
            case "A"  -> 4.0;
            case "B+" -> 3.5;
            case "B"  -> 3.0;
            case "C+" -> 2.5;
            case "C"  -> 2.0;
            case "D+" -> 1.5;
            case "D"  -> 1.0;
            default   -> 0.0;
        };
    }

    /* -------------------------------
       학년 정보 조회 (user-service 연동)
    -------------------------------- */
    private UserResDto getUserInfo(Long userId) {
        try {
            Map<String, List<Long>> request = Map.of("userId", List.of(userId));
            ResultResponse<List<UserResDto>> response = courseUserClient.getUsersByInfo(request);
            List<UserResDto> users = response.getResult();
            if (users != null && !users.isEmpty()) return users.get(0);
        } catch (Exception e) {
            System.err.println("user-service 호출 실패: " + e.getMessage());
        }
        return null;
    }

    /* -------------------------------
       강의별 전체 성적 조회
    -------------------------------- */
    public List<ScoreRes> getScoresByCourse(Long courseId) {
        List<Enrollment> enrollments = enrollmentRepository.findByCourse_CourseId(courseId);

        return enrollments.stream().map(e -> {
            Score score = scoreRepository.findByEnrollment(e).orElse(null);
            UserResDto userInfo = getUserInfo(e.getUserId());
            int grade = (userInfo != null && userInfo.getGrade() != 0) ? userInfo.getGrade() : 0;

            if (score != null) {
                double total = calcTotal(score.getMidScore(), score.getFinScore(),
                        score.getAttendanceScore(), score.getOtherScore());
                int absent = 50 - score.getAttendanceDays();

                return new ScoreRes(
                        score.getScoreId(),
                        e.getEnrollmentId(),
                        score.getRank(),
                        grade,
                        score.getMidScore(),
                        score.getFinScore(),
                        score.getAttendanceScore(),
                        score.getOtherScore(),
                        total,
                        calcGpa(score.getRank()),
                        score.getAttendanceDays(),
                        absent
                );
            } else {
                return new ScoreRes(
                        null,
                        e.getEnrollmentId(),
                        "미등록",
                        grade,
                        0, 0, 0, 0,
                        0.0,
                        0.0,
                        0,
                        50
                );
            }
        }).toList();
    }
}
