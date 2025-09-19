package com.green.muziuniv_be_notuser.app.shared.courseEvaluation;

import com.green.muziuniv_be_notuser.app.shared.courseEvaluation.model.CourseEvaluationReq;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseEvaluationService {
    private final CourseEvaluationRepository courseEvaluationRepository;
    // 강의 평가를 할 수 있다. (학생용 기능)
    public ResponseEntity<?> courseEvaluation(Long userId, CourseEvaluationReq req){
        /// ///// req에서 get 해서 레포지토리에 넘기는 작업 해야함.
        return ResponseEntity.ok(new ResultResponse<>("강의 평가 완료", null));
    }
}
