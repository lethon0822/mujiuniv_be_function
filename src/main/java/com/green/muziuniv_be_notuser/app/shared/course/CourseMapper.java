package com.green.muziuniv_be_notuser.app.shared.course;

import com.green.muziuniv_be_notuser.app.shared.course.model.CourseFilterReq;
import com.green.muziuniv_be_notuser.app.shared.course.model.CourseFilterRes;
import com.green.muziuniv_be_notuser.app.shared.course.model.TodayCourseReq;
import com.green.muziuniv_be_notuser.app.shared.course.model.TodayCourseStuRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseMapper {
    // 개설 강의 조회
    List<CourseFilterRes> findCoursesByFilter(CourseFilterReq req);

    // 오늘의 강의 조회(학생용)
    List<TodayCourseStuRes> findTodayCourse(TodayCourseReq req);

    // 오늘의 강의 조회(교수용
    List<TodayCourseStuRes> findTodayCoursePro(TodayCourseReq req);
}
