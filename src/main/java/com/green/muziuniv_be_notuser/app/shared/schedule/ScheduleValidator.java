package com.green.muziuniv_be_notuser.app.shared.schedule;

import com.green.muziuniv_be_notuser.configuration.exception.ScheduleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleValidator {
    public final ScheduleMapper scheduleMapper;

    public void validateOpen(Long semesterId, String scheduleType){
        int open = scheduleMapper.isScheduleOpenNowBySemesterAndType(semesterId, scheduleType);
        if(open == 0){
            throw new ScheduleException(scheduleType + " 기간이 아닙니다.");
        }
    }
}
