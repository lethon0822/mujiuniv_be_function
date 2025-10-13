package com.green.muziuniv_be_notuser.app.shared.application;

import com.green.muziuniv_be_notuser.app.shared.application.model.AppPostReq;
import com.green.muziuniv_be_notuser.app.shared.application.model.ApplicationListRow;
import com.green.muziuniv_be_notuser.configuration.model.SignedUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    /**
     * 다음 학기 ID 조회
     */
    @GetMapping("/next-semester")
    public Integer getNextSemesterId(@RequestParam Integer currentSemesterId) {
        return applicationService.getNextSemesterId(currentSemesterId);
    }

    /**
     * 일정 창 열려있는지 확인
     */
    @GetMapping("/is-open")
    public boolean isScheduleOpenNow(@RequestParam Integer scheduleId) {
        return applicationService.isScheduleOpenNow(scheduleId);
    }

    /**
     * 신청 생성 (휴직 / 복직 신청 모두 포함)
     */
    @PostMapping
    public void createApplication(@Valid @RequestBody AppPostReq req,
                                  @AuthenticationPrincipal SignedUser signedUser) {
        if (signedUser != null) {
            req.setUserId(signedUser.signedUserId);
        } else if (req.getUserId() == null) {
            throw new IllegalArgumentException("userId가 필요합니다.");
        }

        applicationService.createApplication(req, signedUser);
    }

    /**
     * 내 신청 목록 조회
     */
    @GetMapping("/my")
    public List<ApplicationListRow> getMyApplications(@RequestParam Integer userId) {
        return applicationService.getMyApplications(userId);
    }

    /**
     * 신청 삭제
     */
    @DeleteMapping("/{appId}")
    public void deleteApplication(@PathVariable Long appId,
                                  @RequestParam Long userId) {
        applicationService.deleteApplication(userId, appId);
    }
}
