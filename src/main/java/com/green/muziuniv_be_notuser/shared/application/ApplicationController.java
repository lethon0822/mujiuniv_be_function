package com.green.muziuniv_be_notuser.shared.application;


import com.green.muziuniv_be_notuser.shared.application.model.AppPostReq;
import com.green.muziuniv_be_notuser.shared.application.model.ApplicationListRow;
import com.green.muziuniv_be_notuser.shared.application.model.ApplyNextReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/application")
public class ApplicationController {
    private final ApplicationService applicationService;

    // 학생, 교수의 신청
    @PostMapping
    public ResponseEntity<?> insertAppForReason (@RequestBody AppPostReq req) {
        return ResponseEntity.ok(applicationService.insertAppForReason(req));
    }
    // 다음 학기 휴/복학 신청
    @PostMapping("/apply-next")
    public void applyNext(@AuthenticationPrincipal(expression = "id") Long userId,
                          @RequestBody ApplyNextReq req) {
        applicationService.applyForNextSemester(userId, req, true); // 기간 강제하려면 true
    }

    // 내 신청 목록
    @GetMapping("/me")
    public List<ApplicationListRow> myList(@AuthenticationPrincipal Long userId,
                                           @RequestParam(required = false) String status) {
        return applicationService.myApplications(userId, status);
    }
}


