package com.green.muziuniv_be_notuser.shared.semester;

import com.green.muziuniv_be_notuser.entity.semester.Semester;
import com.green.muziuniv_be_notuser.shared.semester.model.SemesterRes;
import org.springframework.stereotype.Component;

@Component
public class SemesterMapper {

    public Semester toEntity(Integer year, Integer semester) {
        return Semester.builder()
                .year(year)
                .semester(semester)
                .build();
    }

    public SemesterRes toRes(Semester s) {
        return SemesterRes.builder()
                .semesterId(s.getSemesterId())
                .year(s.getYear())
                .semester(s.getSemester())
                .build();
    }
}
