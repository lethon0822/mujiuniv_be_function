package com.green.muziuniv_be_notuser.app.student.gpa;

import com.green.muziuniv_be_notuser.app.student.gpa.model.GpaRes;
import com.green.muziuniv_be_notuser.app.student.gpa.model.GpaRowDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GpaMapper {
    List<GpaRowDTO> findAllGradesForGpa (@Param("userId") Long userId, @Param("semesterId") Long semesterId);
}
