package com.green.muziuniv_be_notuser.app.staff.approval;



import com.green.muziuniv_be_notuser.app.staff.approval.model.ApprovalAppGetReq;
import com.green.muziuniv_be_notuser.app.staff.approval.model.ApprovalAppGetRes;
import com.green.muziuniv_be_notuser.app.staff.approval.model.ApprovalPatchReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/staff/approval")
@RestController
@Slf4j
public class ApprovalController {
    private final ApprovalService approvalService;
    // 신청서 목록
    @GetMapping
    public ResponseEntity<List<ApprovalAppGetRes>> list(@ModelAttribute ApprovalAppGetReq req) {
        return ResponseEntity.ok(approvalService.getApplications(req));
    }

    // 승인/거부
    @PatchMapping
    public ResponseEntity<String> decide(@RequestBody ApprovalPatchReq req) {
        log.info("승인 요청: appId={}, userId={}, status={}, scheduleType={}",
                req.getAppId(), req.getUserId(), req.getStatus(), req.getScheduleType());

        String result = approvalService.decideApplication(req);
        return ResponseEntity.ok(result);
    }
}
