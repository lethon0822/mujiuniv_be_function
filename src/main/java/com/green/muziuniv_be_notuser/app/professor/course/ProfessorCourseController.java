package com.green.muziuniv_be_notuser.app.professor.course;

import com.green.muziuniv_be_notuser.app.professor.course.model.*;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.configuration.model.SignedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professor/course")
@RequiredArgsConstructor
public class ProfessorCourseController {
    private final ProfessorCourseService professorCourseService;

    // 강의 등록
    @PostMapping
    public ResultResponse<?> saveCourse(
            @AuthenticationPrincipal SignedUser signedUser,
            @RequestBody ProfessorPostReq req) {
       req.setUserId(signedUser.signedUserId);
       professorCourseService.saveCourse(req);

        return new ResultResponse<>("강의 등록 성공", null);
    }

    // 내가 등록한 강의 목록 조회
    @GetMapping
    public ResultResponse<?> findMyCourse(
            @AuthenticationPrincipal SignedUser signedUser,
            @ModelAttribute ProfessorGetReq req) {

        req.setUserId(signedUser.signedUserId);
        List<ProfessorGetRes> result = professorCourseService.findMyCourse(req);

        return new ResultResponse<>("내 강의 목록 조회 성공", result);
    }

    // 강의 평가 조회
    @GetMapping("/survey")
    public ResultResponse<?> checkSurvey(
            @RequestParam("id") int courseId) {
        List<ProfessorSurveyCheckRes> result = professorCourseService.checkSurvey(courseId);
        return new ResultResponse<>(result.isEmpty() ? "등록된 강의평가가 없습니다" : "강의평 내용", result);
    }

    // 강의별 학생 리스트 조회
    @GetMapping("/{courseId}/students")
    public ResponseEntity<List<CourseStudentRes>> getCourseStudents(@PathVariable Long courseId) {
        return ResponseEntity.ok(professorCourseService.getCourseStudents(courseId));
    }

    // 강의 계획서 수정
    @PutMapping
    public ResultResponse<?> modify(@RequestBody ProfessorPutReq req) {
        professorCourseService.modify(req);
        return new ResultResponse<>("강의 계획서 수정 성공", null);
    }

//    @DeleteMapping("/course/{id}")
//    public ResultResponse<Integer> deleteCourse(
//            @PathVariable("id") int courseId,
//            @AuthenticationPrincipal SignedUser signedUser) {
//
//        int result = professorService.deleteCourse(courseId, signedUser.signedUserId);
//        return new ResultResponse<>("강의 삭제 성공", result);
//    }
}
