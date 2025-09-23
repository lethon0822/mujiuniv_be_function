package com.green.muziuniv_be_notuser.app.professor.course;

import com.green.muziuniv_be_notuser.app.professor.coursemanage.model.ProfessorGetReq;
import com.green.muziuniv_be_notuser.app.professor.coursemanage.model.ProfessorGetRes;
import com.green.muziuniv_be_notuser.app.professor.coursemanage.model.ProfessorSurveyCheckRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProfessorCourseMapper {
    List<ProfessorGetRes> findByUserId(ProfessorGetReq req);

    //강의평가 조회
    List<ProfessorSurveyCheckRes> findSurveyByCourseId(int courseId);

    int findSameCode(@Param("courseCode") String courseCode, @Param("semesterId") Long semesterId);
}
