package com.green.muziuniv_be_notuser.app.professor.coursemanage;



import com.green.muziuniv_be_notuser.app.professor.coursemanage.model.*;
import com.green.muziuniv_be_notuser.configuration.model.SignedUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/professor")
public class ProfessorController {
    private final ProfessorService professorService;


    //강의 등록
    @PostMapping("/course")
    public ResponseEntity <?> saveCourse(@AuthenticationPrincipal SignedUser signedUser, @RequestBody ProfessorPostReq req){

        req.setUserId(signedUser.signedUserId);

        int result = professorService.saveCourse(req);
        return ResponseEntity.ok(result);
    }

    //등록한 강의 보기
    @GetMapping("/course")
    public ResponseEntity <?> findMyCourse(@AuthenticationPrincipal SignedUser signedUser, @ModelAttribute ProfessorGetReq req){
        req.setUserId(signedUser.signedUserId);
        List<ProfessorGetRes> result = professorService.findMyCourse(req);
        return ResponseEntity.ok(result);
    }

    //강의별 학생 리스트 조회
    @GetMapping("/student")
    public ResponseEntity<?> courseStudentList(@RequestParam("id") int courseId){
        List<CourseStudentGetReq> result = professorService.courseStudentList(courseId);
        return ResponseEntity.ok(result);
    }



    //강의 평가 조회
    @GetMapping("/course/survey")
    public ResponseEntity<?> checkSurvey(@RequestParam("id") int courseId){
        List<ProfessorSurveyCheckRes> result = professorService.checkSurvey(courseId);
        return ResponseEntity.ok(result);
    }

    //강의 계획서 수정
    @PutMapping("/course")
    public ResponseEntity<?> modify(@RequestBody ProfessorPutReq req){
        int result = professorService.modify(req);
        return ResponseEntity.ok(result);
    }
}
