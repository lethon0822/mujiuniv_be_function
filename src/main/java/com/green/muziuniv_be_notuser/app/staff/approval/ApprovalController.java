package com.green.muziuniv_be_notuser.app.staff.approval;



import com.green.muziuniv_be_notuser.app.staff.approval.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/staff/approval")
@RequiredArgsConstructor
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
    @GetMapping("/test")
    public ResponseEntity<?> test(){
        return ResponseEntity.ok("통신되냐?");
    }

    // 처리중 강의조회
    @GetMapping("/course")
    public ResponseEntity<?> getPendingCourses(@ModelAttribute CoursePendingReq req) {
        log.info("옹냐:{}",req.getSid());
        List<CoursePendingRes> result = approvalService.getPendingCourses(req);
        return ResponseEntity.ok(result);
    }

    //   강의 승인 or 거부
    @PatchMapping("/course")
    public ResponseEntity<?> updateCourseStatus(@RequestBody ApprovalCoursePatchReq approvalCoursePatchReq) {
        return ResponseEntity.ok(approvalService.updateCourseStatus(approvalCoursePatchReq));
    }
}
