package com.green.muziuniv_be_notuser.app.shared.schedule;

import com.green.muziuniv_be_notuser.app.shared.schedule.model.ScheduleRes;
import com.green.muziuniv_be_notuser.entity.schedule.Schedule;
import com.green.muziuniv_be_notuser.entity.schedule.TimeSetting;
import com.green.muziuniv_be_notuser.entity.semester.Semester;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ScheduleMapper {

    // 엔티티 생성용
    public Schedule toEntity(Semester semester,
                             String scheduleType,
                             LocalDateTime startDatetime,
                             LocalDateTime endDatetime,
                             String description) {
        return Schedule.builder()
                .semester(semester)
                .scheduleType(scheduleType)
                .timeSetting(new TimeSetting(startDatetime, endDatetime))
                .description(description)
                .build();
    }

    // 응답 DTO 변환용
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