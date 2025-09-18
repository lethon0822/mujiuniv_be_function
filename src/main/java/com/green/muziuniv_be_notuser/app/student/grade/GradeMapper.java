package com.green.muziuniv_be_notuser.app.student.grade;

import com.green.muziuniv_be_notuser.app.student.grade.model.GetAllPermanentGradeReq;
import com.green.muziuniv_be_notuser.app.student.grade.model.GetAllPermanentGradeRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GradeMapper {
    // 영구 성적 조회
    List<GetAllPermanentGradeRes> getAllPermanentGrade(Long userId, GetAllPermanentGradeReq req);
}
