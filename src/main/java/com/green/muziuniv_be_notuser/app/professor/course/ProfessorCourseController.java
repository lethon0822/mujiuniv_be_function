package com.green.muziuniv_be_notuser.app.professor.course;

import com.green.muziuniv_be_notuser.app.professor.course.model.CourseStudentRes;
import com.green.muziuniv_be_notuser.app.professor.coursemanage.model.ProfessorGetReq;
import com.green.muziuniv_be_notuser.app.professor.coursemanage.model.ProfessorGetRes;
import com.green.muziuniv_be_notuser.app.professor.coursemanage.model.ProfessorPostReq;
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
    @PostMapping()
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

    // 강의별 학생 리스트 조회
    @GetMapping("/{courseId}/students")
    public ResponseEntity<List<CourseStudentRes>> getCourseStudents(@PathVariable Long courseId) {
        return ResponseEntity.ok(professorCourseService.getCourseStudents(courseId));
    }
}
