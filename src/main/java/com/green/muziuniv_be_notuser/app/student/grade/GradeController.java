package com.green.muziuniv_be_notuser.app.student.grade;

import com.green.muziuniv_be_notuser.app.student.grade.model.GetAllPermanentGradeReq;
import com.green.muziuniv_be_notuser.app.student.grade.model.GetAllPermanentGradeRes;
import com.green.muziuniv_be_notuser.configuration.model.SignedUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/student/grade")
@RequiredArgsConstructor
public class GradeController {
    private final GradeService gradeService;

    // 영구 성적 조회 (금학기 제외)
    @GetMapping("/permanent")
    public ResponseEntity<?> getMyAllGrades(@AuthenticationPrincipal SignedUser signedUser,
                                            @ModelAttribute GetAllPermanentGradeReq req) {
        Long userId = signedUser.signedUserId;

        log.info("semester id {}", req.getSemesterId());


        return gradeService.getAllPermanentGrade(userId, req);

    }
}
