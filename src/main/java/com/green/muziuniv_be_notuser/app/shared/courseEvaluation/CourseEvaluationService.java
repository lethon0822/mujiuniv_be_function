package com.green.muziuniv_be_notuser.app.shared.courseEvaluation;

import com.green.muziuniv_be_notuser.app.shared.courseEvaluation.model.CourseEvaluationReq;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseEvaluationService {
    private final CourseEvaluationRepository courseEvaluationRepository;
    // 강의 평가를 할 수 있다. (학생용 기능)
    public ResponseEntity<?> courseEvaluation(Long userId, CourseEvaluationReq req){
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
