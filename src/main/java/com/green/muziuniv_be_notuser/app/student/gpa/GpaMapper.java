package com.green.muziuniv_be_notuser.app.student.gpa;

import com.green.muziuniv_be_notuser.app.student.gpa.model.GpaRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GpaMapper {
    List<GpaRes> getMyGpa(Long userId);
}
