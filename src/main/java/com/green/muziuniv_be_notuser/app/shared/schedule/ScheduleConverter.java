package com.green.muziuniv_be_notuser.app.shared.schedule;

import com.green.muziuniv_be_notuser.entity.schedule.Schedule;
import com.green.muziuniv_be_notuser.entity.schedule.TimeSetting;
import com.green.muziuniv_be_notuser.entity.semester.Semester;
import com.green.muziuniv_be_notuser.app.shared.schedule.model.ScheduleRes;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ScheduleConverter {

    public Schedule toEntity(
            Semester semester,
            String scheduleType,
            LocalDateTime startDatetime,
            LocalDateTime endDatetime,
            String description
    ) {
        return Schedule.builder()
                .semester(semester)
                .scheduleType(scheduleType)
                .timeSetting(new TimeSetting(startDatetime, endDatetime))
                .description(description)
                .build();
    }

    public ScheduleRes toRes(Schedule s) {
        return ScheduleRes.builder()
                .scheduleId(s.getScheduleId())
                .semesterId(s.getSemester().getSemesterId())
                .scheduleType(s.getScheduleType())
                .startDatetime(s.getTimeSetting().getStartDatetime())
                .endDatetime(s.getTimeSetting().getEndDatetime())
                .description(s.getDescription())
                .createdAt(s.getCreatedAt())
                .build();
    }
}
