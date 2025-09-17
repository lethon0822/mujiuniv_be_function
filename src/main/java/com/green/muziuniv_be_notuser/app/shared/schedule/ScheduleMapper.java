package com.green.muziuniv_be_notuser.app.shared.schedule;

import com.green.muziuniv_be_notuser.app.shared.schedule.model.ScheduleCreateReq;
import com.green.muziuniv_be_notuser.app.shared.schedule.model.ScheduleRes;
import com.green.muziuniv_be_notuser.app.shared.schedule.model.ScheduleUpdateReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ScheduleMapper {
    ScheduleRes selectById(@Param("scheduleId") Integer scheduleId);

    int insertSchedule(ScheduleCreateReq req);

    int updateSchedule(@Param("scheduleId") Integer scheduleId,
                       @Param("scheduleType") String scheduleType,
                       @Param("startDatetime") LocalDateTime startDatetime,
                       @Param("endDatetime") LocalDateTime endDatetime,
                       @Param("description") String description);

    int deleteSchedule(@Param("scheduleId") Integer scheduleId);

    ScheduleRes selectBySemesterAndType(@Param("semesterId") Integer semesterId,
                                        @Param("scheduleType") String scheduleType);

    List<ScheduleRes> selectByMonth(@Param("start") String start,
                                    @Param("end") String end,
                                    @Param("semesterId") Integer semesterId);
}
