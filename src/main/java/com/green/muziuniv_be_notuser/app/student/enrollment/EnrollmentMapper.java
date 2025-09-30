package com.green.muziuniv_be_notuser.app.student.enrollment;

import com.green.muziuniv_be_notuser.app.student.enrollment.model.EnrollmentFilterReq;
import com.green.muziuniv_be_notuser.app.student.enrollment.model.EnrollmentFilterRes;
import com.green.muziuniv_be_notuser.app.student.enrollment.model.GetMyCurrentEnrollmentsCoursesRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EnrollmentMapper {
    // 금학기 수강신청 내역
    List<GetMyCurrentEnrollmentsCoursesRes> getMyCurrentEnrollmentsCourses(Long userId, Long semesterId);
    // 수강 신청 가능한 강의 조회
    List<EnrollmentFilterRes> getAvailableEnrollmentsCourses(EnrollmentFilterReq req);
}
