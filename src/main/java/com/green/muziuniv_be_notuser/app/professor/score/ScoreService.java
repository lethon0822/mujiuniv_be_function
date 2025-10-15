package com.green.muziuniv_be_notuser.app.professor.score;

import com.green.muziuniv_be_notuser.app.professor.attendance.AttendanceService;
import com.green.muziuniv_be_notuser.app.professor.score.model.ScorePostReq;
import com.green.muziuniv_be_notuser.app.professor.score.model.ScorePutReq;
import com.green.muziuniv_be_notuser.app.professor.score.model.ScoreRes;
import com.green.muziuniv_be_notuser.app.shared.course.CourseRepository;
import com.green.muziuniv_be_notuser.app.shared.schedule.ScheduleValidator;
import com.green.muziuniv_be_notuser.app.student.enrollment.EnrollmentRepository;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.entity.course.Course;
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
    private final CourseRepository courseRepository;
    private final CourseUserClient courseUserClient;
    private final AttendanceService attendanceService;
    private final ScheduleValidator scheduleValidator;

    /*  출결 점수 계산 로직 (결석 수 기준) */
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
        성적 등록 (POST)
       - 성적입력 기간 검증 추가
    -------------------------------- */
    public ScoreRes saveOrUpdateScore(ScorePostReq req) {
        Enrollment enrollment = enrollmentRepository.findById(req.getEnrollmentId())
                .orElseThrow(() -> new RuntimeException("등록된 수강내역이 없음"));

        //  성적입력 기간 검증
        Course course = courseRepository.findById(enrollment.getCourse().getCourseId())
                .orElseThrow(() -> new RuntimeException("해당 강의가 존재하지 않음"));
        Long semesterId = course.getSemesterId().getSemesterId();
        scheduleValidator.validateOpen(semesterId, "성적입력");

        int attended = req.getAttendanceScore();
        if (attended > 50) attended = 50;
        int absent = 50 - attended;
        int attendanceScore = calculateAttendanceScore(absent);
        double total = calcTotal(req.getMidScore(), req.getFinScore(), attendanceScore, req.getOtherScore());

        Score score = scoreRepository.findByEnrollment_EnrollmentId(req.getEnrollmentId())
                .orElse(new Score());

        String rank = (req.getRank() != null && !req.getRank().isEmpty())
                ? req.getRank()
                : calcRank(total);

        score.setRank(rank);
        score.setEnrollment(enrollment);
        score.setMidScore(req.getMidScore());
        score.setFinScore(req.getFinScore());
        score.setAttendanceScore(attendanceScore);
        score.setAttendanceDays(attended);
        score.setOtherScore(req.getOtherScore());
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
                calcGpa(saved.getRank()),
                saved.getAttendanceDays(),
                absent
        );
    }


    /* -------------------------------
        성적 수정 (PUT)
       - 성적입력 기간 검증 추가
    -------------------------------- */
    public ScoreRes updateScore(ScorePutReq req) {
        //  1. 수강내역(enrollment) 조회 추가
        Enrollment enrollment = enrollmentRepository.findById(req.getEnrollmentId())
                .orElseThrow(() -> new RuntimeException("해당 수강내역이 존재하지 않음"));

        Score score = scoreRepository.findByEnrollment_EnrollmentId(req.getEnrollmentId())
                .orElseThrow(() -> new RuntimeException("해당 수강의 성적이 존재하지 않음"));

        //  2. 성적입력 기간 검증
        Course course = courseRepository.findById(enrollment.getCourse().getCourseId())
                .orElseThrow(() -> new RuntimeException("해당 강의가 존재하지 않음"));
        Long semesterId = course.getSemesterId().getSemesterId();

        scheduleValidator.validateOpen(semesterId, "성적입력");

        //  3. 점수 계산
        int attended = Math.min(req.getAttendanceScore(), 50);
        int absent = 50 - attended;
        int attendanceScore = calculateAttendanceScore(absent);
        double total = calcTotal(req.getMidScore(), req.getFinScore(), attendanceScore, req.getOtherScore());

        String rank = (req.getRank() != null && !req.getRank().isEmpty())
                ? req.getRank()
                : calcRank(total);

        //  4. 값 세팅
        score.setMidScore(req.getMidScore());
        score.setFinScore(req.getFinScore());
        score.setAttendanceScore(attendanceScore);
        score.setAttendanceDays(attended);
        score.setOtherScore(req.getOtherScore());
        score.setGrade(req.getGrade());
        score.setRank(rank);

        Score updated = scoreRepository.save(score);

        //  5. 유저 학년 정보 반영
        UserResDto userInfo = getUserInfo(enrollment.getUserId());
        int grade = (userInfo != null && userInfo.getGrade() != 0)
                ? userInfo.getGrade()
                : req.getGrade();

        //  6. 응답 DTO 반환
        return new ScoreRes(
                updated.getScoreId(),
                enrollment.getEnrollmentId(),
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
