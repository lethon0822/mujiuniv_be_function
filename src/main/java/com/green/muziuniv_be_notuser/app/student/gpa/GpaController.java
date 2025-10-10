package com.green.muziuniv_be_notuser.app.student.gpa;

import com.green.muziuniv_be_notuser.app.student.gpa.model.GpaRes;
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

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/student/gpa")
@RequiredArgsConstructor
public class GpaController {
    private final GpaService gpaService;

    @GetMapping
    public ResponseEntity<?> getGpa(@AuthenticationPrincipal SignedUser signedUser, @RequestParam Long semesterId) {
        Long userId = signedUser.signedUserId;
        List<GpaRes> res = gpaService.getMyGpa(userId, semesterId);
        return ResponseEntity.ok(new ResultResponse<>("평점 조회 성공", res));
    }
}
