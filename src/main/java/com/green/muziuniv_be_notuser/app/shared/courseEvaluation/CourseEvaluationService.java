package com.green.muziuniv_be_notuser.app.shared.courseEvaluation;

import com.green.muziuniv_be_notuser.app.shared.courseEvaluation.model.CourseEvaluationReq;
import com.green.muziuniv_be_notuser.app.shared.schedule.ScheduleValidator;
import com.green.muziuniv_be_notuser.app.student.enrollment.EnrollmentRepository;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.entity.enrollment.Enrollment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseEvaluationService {
    private final CourseEvaluationRepository courseEvaluationRepository;
    private final ScheduleValidator scheduleValidator;
    private final EnrollmentRepository enrollmentRepository;

    // 강의 평가를 할 수 있다. (학생용 기능)
    public ResponseEntity<?> courseEvaluation(Long userId, CourseEvaluationReq req){
        // 강의 평가 기간 확인 ( 성적 조회 기간이랑 같음 )
        Enrollment enrollment = enrollmentRepository.findById(req.getEnrollmentId())
                        .orElseThrow(() -> new IllegalArgumentException("수강 내역을 찾을 수 없습니다."));
        Long semesterId = enrollment.getCourse().getSemesterId().getSemesterId();
        scheduleValidator.validateOpen(semesterId, "성적조회");

        Long enrollmentId = req.getEnrollmentId();
        String review = req.getReview();
        Integer evScore = req.getEvScore();
        int result = courseEvaluationRepository.updateEvaluation(userId, enrollmentId, review, evScore);
        if(result > 0){
            return ResponseEntity.ok(new ResultResponse<>("강의 평가 완료", 1));
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResultResponse<>("강의 평가 실패", 0));
        }
    }
}
