package com.green.muziuniv_be_notuser.app.student.enrollment;

import com.green.muziuniv_be_notuser.app.student.enrollment.model.GetMyCurrentEnrollmentsCoursesRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EnrollmentMapper {
    List<GetMyCurrentEnrollmentsCoursesRes> getMyCurrentEnrollmentsCourses(Long userId, Long semesterId);
}
