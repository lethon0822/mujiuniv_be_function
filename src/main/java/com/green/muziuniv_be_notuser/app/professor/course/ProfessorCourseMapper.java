package com.green.muziuniv_be_notuser.app.professor.course;

import com.green.muziuniv_be_notuser.app.professor.coursemanage.model.ProfessorGetReq;
import com.green.muziuniv_be_notuser.app.professor.coursemanage.model.ProfessorGetRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProfessorCourseMapper {
    List<ProfessorGetRes> findByUserId(ProfessorGetReq req);
}
