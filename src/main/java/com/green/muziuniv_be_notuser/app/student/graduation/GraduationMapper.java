package com.green.muziuniv_be_notuser.app.student.graduation;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GraduationMapper {
    int findGraduationCandidates();
}
