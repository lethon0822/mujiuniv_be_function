package com.green.muziuniv_be_notuser.app.shared.course;

import com.green.muziuniv_be_notuser.app.shared.course.model.*;
import com.green.muziuniv_be_notuser.configuration.model.SignedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;

    // 강의 리스트 조회
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

    //오늘의 강의 조회
    @GetMapping("/today")
    public ResponseEntity<?> todayCourseStd(@ModelAttribute TodayCourseReq req,
                                            @AuthenticationPrincipal SignedUser signedUser ){
        req.setUserId(signedUser.signedUserId);
        List<TodayCourseStuRes> result;
        if(req.getRole().equals("student")){
            result = courseService.todayCourseStu(req);
        }else {
            result = courseService.todayCoursePro(req);
        }
        return ResponseEntity.ok(result);
    }

}
