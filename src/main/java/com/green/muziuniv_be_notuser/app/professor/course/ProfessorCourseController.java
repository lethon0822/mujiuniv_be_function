package com.green.muziuniv_be_notuser.app.professor.course;

import com.green.muziuniv_be_notuser.app.professor.course.model.CourseStudentRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professor/course")
@RequiredArgsConstructor
public class ProfessorCourseController {

    private final ProfessorCourseService professorCourseService;

    // 강의별 학생 리스트 조회
    @GetMapping("/{courseId}/students")
    public ResponseEntity<List<CourseStudentRes>> getCourseStudents(@PathVariable Long courseId) {
        return ResponseEntity.ok(professorCourseService.getCourseStudents(courseId));
    }
}
