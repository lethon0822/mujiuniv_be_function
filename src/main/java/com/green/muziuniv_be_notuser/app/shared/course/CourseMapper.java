package com.green.muziuniv_be_notuser.app.shared.course;

import com.green.muziuniv_be_notuser.app.shared.course.model.CourseFilterReq;
import com.green.muziuniv_be_notuser.app.shared.course.model.CourseFilterRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseMapper {
    // 개설 강의 조회
    List<CourseFilterRes> findCoursesByFilter(CourseFilterReq req);
}
