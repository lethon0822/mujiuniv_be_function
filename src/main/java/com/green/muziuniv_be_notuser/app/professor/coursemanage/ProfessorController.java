package com.green.muziuniv_be_notuser.app.professor.coursemanage;



import com.green.muziuniv_be_notuser.app.professor.coursemanage.model.*;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.configuration.model.SignedUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/professor")
public class ProfessorController {
    private final ProfessorService professorService;

    // 강의 등록
    @PostMapping("/course")
    public ResultResponse<Integer> saveCourse(
            @AuthenticationPrincipal SignedUser signedUser,
            @RequestBody ProfessorPostReq req) {

        req.setUserId(signedUser.signedUserId);
        int result = professorService.saveCourse(req);

        return new ResultResponse<>("강의 등록 성공", result);
    }

    // 내가 등록한 강의 목록 조회
    @GetMapping("/course")
    public ResultResponse<List<ProfessorGetRes>> findMyCourse(
            @AuthenticationPrincipal SignedUser signedUser,
            @ModelAttribute ProfessorGetReq req) {

        req.setUserId(signedUser.signedUserId);
        List<ProfessorGetRes> result = professorService.findMyCourse(req);

        return new ResultResponse<>("내 강의 목록 조회 성공", result);
    }

    // 강의별 학생 리스트 조회
    @GetMapping("/student")
    public ResultResponse<List<CourseStudentGetRes>> courseStudentList(
            @RequestParam("id") int courseId) {

        List<CourseStudentGetRes> result = professorService.courseStudentList(courseId);
        return new ResultResponse<>("강의 학생 목록 조회 성공", result);
    }

    // 강의 평가 조회
    @GetMapping("/course/survey")
    public ResultResponse<List<ProfessorSurveyCheckRes>> checkSurvey(
            @RequestParam("id") int courseId) {

        List<ProfessorSurveyCheckRes> result = professorService.checkSurvey(courseId);
        return new ResultResponse<>("강의 평가 조회 성공", result);
    }

    // 강의 계획서 수정
    @PutMapping("/course")
    public ResultResponse<Integer> modify(@RequestBody ProfessorPutReq req) {
        int result = professorService.modify(req);
        return new ResultResponse<>("강의 계획서 수정 성공", result);
    }

    // 강의 삭제
    @DeleteMapping("/course/{id}")
    public ResultResponse<Integer> deleteCourse(
            @PathVariable("id") int courseId,
            @AuthenticationPrincipal SignedUser signedUser) {

        int result = professorService.deleteCourse(courseId, signedUser.signedUserId);
        return new ResultResponse<>("강의 삭제 성공", result);
    }
}

