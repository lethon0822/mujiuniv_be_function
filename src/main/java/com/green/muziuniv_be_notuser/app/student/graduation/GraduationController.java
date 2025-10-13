package com.green.muziuniv_be_notuser.app.student.graduation;

import com.green.muziuniv_be_notuser.app.student.graduation.model.GraduationCheckRes;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.configuration.model.SignedUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/student/graduation")
@RequiredArgsConstructor
public class GraduationController {
    private final GraduationService graduationService;

    // 졸업 자가 진단
    @GetMapping()
    public ResponseEntity<?> getMyGraduationDiagnosis(@AuthenticationPrincipal SignedUser signedUser, @RequestParam Long semesterId){
        Long userId = signedUser.signedUserId;
        GraduationCheckRes res = graduationService.getMyGraduationDiagnosis(userId, semesterId);
        return ResponseEntity.ok(new ResultResponse<>("졸업 자가진단 완료", res));
    }


}
