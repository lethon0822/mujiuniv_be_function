package com.green.muziuniv_be_notuser.app.staff;

import com.green.muziuniv_be_notuser.app.staff.model.ApprovalCoursePatchReq;
import com.green.muziuniv_be_notuser.app.staff.model.ApprovalCoursePatchRes;
import com.green.muziuniv_be_notuser.app.staff.model.CoursePendingRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff/course")
@RequiredArgsConstructor
public class StaffCourseController {
    private final StaffCourseService staffCourseService;

    // 처리중 강의조회
    @GetMapping("/pending")
    public ResponseEntity<List<CoursePendingRes>> getPendingCourses() {
        return ResponseEntity.ok(staffCourseService.getPendingCourses());
    }

//   강의 승인 or 거부
    @PatchMapping("/approval")
    public ResponseEntity<ApprovalCoursePatchRes> updateCourseStatus(
            @RequestBody ApprovalCoursePatchReq approvalCoursePatchReq
    ) {
        return ResponseEntity.ok(staffCourseService.updateCourseStatus(approvalCoursePatchReq));
    }
}