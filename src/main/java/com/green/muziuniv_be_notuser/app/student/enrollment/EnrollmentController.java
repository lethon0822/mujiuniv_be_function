package com.green.muziuniv_be_notuser.app.student.enrollment;

import com.green.muziuniv_be_notuser.app.student.enrollment.model.EnrollmentFilterReq;
import com.green.muziuniv_be_notuser.app.student.enrollment.model.EnrollmentFilterRes;
import com.green.muziuniv_be_notuser.app.student.enrollment.model.EnrollmentReq;
import com.green.muziuniv_be_notuser.app.student.enrollment.model.EnrollmentRes;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.configuration.model.SignedUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student/enrollment")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    // 수강 신청 가능한 강의 목록 조회 ( 원래는 강의 조회 페이지에서 땡겨썻는데 그렇게 하니 문제 개많아서 추가함 )
    @GetMapping
    public ResponseEntity<?> getAvailableEnrollmentsCourses(@AuthenticationPrincipal SignedUser signedUser,@ModelAttribute EnrollmentFilterReq req){
        Long userId = signedUser.signedUserId;
        req.setUserId(userId);
        List<EnrollmentFilterRes> courseList = enrollmentService.getAvailableEnrollmentsCourses(req);
        return ResponseEntity.ok(courseList);
    }

    // 수강 신청
    @PostMapping
    public ResponseEntity<?> enrollment(@RequestBody EnrollmentReq req, @AuthenticationPrincipal SignedUser signedUser) {
        Long userId = signedUser.signedUserId;
        req.setUserId(userId);
        return enrollmentService.enrollment(req);
    }

    // 금학기 수강 신청한 과목 리스트 조회
    @GetMapping("/current")
    public ResponseEntity<?> getMyCurrentEnrollmentsCourses(@AuthenticationPrincipal SignedUser signedUser, @RequestParam Long semesterId){
        Long userId = signedUser.signedUserId;
        return enrollmentService.getMyCurrentEnrollmentsCourses(userId, semesterId);
    }

    // 수강 신청 취소
    @DeleteMapping("/cancel/{courseId}")
    public ResponseEntity<?> deleteMyEnrollmentCourse(@AuthenticationPrincipal SignedUser signedUser, @PathVariable Long courseId) {
        Long userId = signedUser.signedUserId;

        int result = enrollmentService.deleteMyEnrollmentCourse(userId, courseId);

        if (result > 0) {
            return ResponseEntity.ok(new ResultResponse<>("수강 신청 취소 완료", result));
        }
        else{
            return ResponseEntity.badRequest().body(new ResultResponse<>("수강 취소 실패. 적절하지 않은 시도", result));
        }
    }



}
