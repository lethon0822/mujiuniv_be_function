package com.green.muziuniv_be_notuser.app.shared.schedule;


import com.green.muziuniv_be_notuser.app.shared.schedule.model.ScheduleRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;



import java.util.List;

@Mapper
public interface ScheduleMapper {

    // 학기+유형별 일정 1건
    ScheduleRes selectBySemesterAndType(@Param("semesterId") Integer semesterId,
                                        @Param("scheduleType") String scheduleType);

    // 월별 일정 목록
    List<ScheduleRes> selectByMonth(@Param("start") String start,
                                    @Param("end") String end,
                                    @Param("semesterId") Integer semesterId);
}
