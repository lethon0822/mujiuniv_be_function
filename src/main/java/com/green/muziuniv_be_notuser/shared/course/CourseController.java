package com.green.muziuniv_be_notuser.shared.course;

import com.green.muziuniv_be_notuser.shared.course.model.CourseFilterReq;
import com.green.muziuniv_be_notuser.shared.course.model.CourseFilterRes;
import com.green.muziuniv_be_notuser.shared.department.model.DepartmentRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
public class CourseController {
    private final CourseService courseService;


    @GetMapping
    public ResponseEntity<?> findAllCourses(@ModelAttribute CourseFilterReq courseFilterReq) {
        List<CourseFilterRes> courseList = courseService.findCoursesByFilter(courseFilterReq);
        return ResponseEntity.ok(courseList);
    }
}
