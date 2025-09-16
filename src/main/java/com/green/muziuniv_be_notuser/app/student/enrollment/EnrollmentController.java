package com.green.muziuniv_be_notuser.app.student.enrollment;

import com.green.muziuniv_be_notuser.app.student.enrollment.model.EnrollmentReq;
import com.green.muziuniv_be_notuser.configuration.model.SignedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student/enrollment")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    // 수강 신청
    @PostMapping
    public ResponseEntity<?> enrollment(@RequestBody EnrollmentReq req, @AuthenticationPrincipal SignedUser signedUser) {
        Long userId = signedUser.signedUserId;
        req.setUserId(userId);
        return enrollmentService.enrollment(req);
    }

    // 금학기 수강 신청한 과목 리스트 조회
    @GetMapping("current")
    public ResponseEntity<?> getMyCurrentEnrollmentsCourses(@AuthenticationPrincipal SignedUser signedUser, @RequestParam Long semesterId){
        Long userId = signedUser.signedUserId;
        return enrollmentService.getMyCurrentEnrollmentsCourses(userId, semesterId);
    }


}
