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

    /** 다음 학기 ID 조회 */
    @GetMapping("/next-semester")
    public Integer getNextSemesterId(@RequestParam Integer currentSemesterId) {
        return applicationService.getNextSemesterId(currentSemesterId);
    }

    /** 일정 창 열려있는지 확인 */
    @GetMapping("/is-open")
    public boolean isScheduleOpenNow(@RequestParam Integer scheduleId) {
        return applicationService.isScheduleOpenNow(scheduleId);
    }

    /** 정규 신청 */
    @PostMapping
    public void createApplication(@Valid @RequestBody AppPostReq req,
                                  @AuthenticationPrincipal SignedUser signedUser) {
        applicationService.createApplication(req, signedUser);
    }

    /** 내 신청 목록 */
    @GetMapping("/my")
    public List<ApplicationListRow> getMyApplications(@RequestParam Integer userId) {
        return applicationService.getMyApplications(userId);
    }

    /** 신청 취소 */
    @PutMapping("/cancel/{appId}")
    public boolean cancelApplication(@PathVariable Long appId,
                                     @AuthenticationPrincipal SignedUser signedUser) {
        return applicationService.cancelApplication(appId, signedUser.signedUserId);
    }
}
