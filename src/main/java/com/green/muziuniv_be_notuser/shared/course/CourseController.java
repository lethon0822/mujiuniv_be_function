package com.green.muziuniv_be_notuser.shared.course;

import com.green.muziuniv_be_notuser.shared.course.model.CourseDetailRes;
import com.green.muziuniv_be_notuser.shared.course.model.CourseFilterReq;
import com.green.muziuniv_be_notuser.shared.course.model.CourseFilterRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;

    // 필터바에 띄울 학과 목록을 가져옴.

    @GetMapping
    public ResponseEntity<?> findAllCourses(@ModelAttribute CourseFilterReq courseFilterReq) {
        List<CourseFilterRes> courseList = courseService.findCoursesByFilter(courseFilterReq);
        return ResponseEntity.ok(courseList);
    }

    //강의 계획서 조회
    @GetMapping("/{course_id}")
    public ResponseEntity<?> findByCourseId(@PathVariable("course_id") Long id){
        CourseDetailRes result = courseService.findCourseDetail(id);
        return ResponseEntity.ok(result);
    }
}
