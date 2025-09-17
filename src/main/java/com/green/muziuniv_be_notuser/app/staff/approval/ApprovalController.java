package com.green.muziuniv_be_notuser.app.staff.approval;

import com.green.muziuniv_be_notuser.app.staff.approval.model.ApprovalAppGetReq;
import com.green.muziuniv_be_notuser.app.staff.approval.model.ApprovalAppGetRes;
import com.green.muziuniv_be_notuser.app.staff.approval.model.ApprovalPatchReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/staff/approval")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    @GetMapping
    public ResponseEntity<?> applicationList(@ModelAttribute ApprovalAppGetReq req) {
        List<ApprovalAppGetRes> result = approvalService.applicationList(req);
        return ResponseEntity.ok(result);
    }

    @PatchMapping
    public ResponseEntity<?> decideApplication(@RequestBody ApprovalPatchReq req){
        String result = approvalService.modifyStatus(req);
        return ResponseEntity.ok(Map.of("message", result));
    }
}