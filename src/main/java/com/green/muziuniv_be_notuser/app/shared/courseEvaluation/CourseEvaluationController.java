package com.green.muziuniv_be_notuser.app.shared.courseEvaluation;

import com.green.muziuniv_be_notuser.app.shared.courseEvaluation.model.CourseEvaluationReq;
import com.green.muziuniv_be_notuser.configuration.model.SignedUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/course/evaluation")
@RequiredArgsConstructor
public class CourseEvaluationController {
    private final CourseEvaluationService courseEvaluationService;

    // 금학기 수강한 강의에 대한 강의평가를 할 수 있다. (학생)
    @PutMapping
    public ResponseEntity<?> courseEvaluation(@AuthenticationPrincipal SignedUser signedUser, @RequestBody CourseEvaluationReq req) {
        Long userId = signedUser.signedUserId;
        log.info(">>> 강의 평가 요청 들어옴");
        return courseEvaluationService.courseEvaluation(userId, req);
    }
}
