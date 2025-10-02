package com.green.muziuniv_be_notuser.app.shared.schedule;

import com.green.muziuniv_be_notuser.app.shared.application.ApplicationMapper;
import com.green.muziuniv_be_notuser.app.shared.schedule.model.DateRes;
import com.green.muziuniv_be_notuser.app.shared.schedule.model.ScheduleCreateReq;
import com.green.muziuniv_be_notuser.app.shared.schedule.model.ScheduleRes;
import com.green.muziuniv_be_notuser.app.shared.schedule.model.ScheduleUpdateReq;
import com.green.muziuniv_be_notuser.configuration.model.SignedUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {
    private final ScheduleMapper scheduleMapper;
    private final ApplicationMapper applicationMapper;

    @Transactional(readOnly = true)
    public List<ScheduleRes> listByMonth(YearMonth ym, Integer semesterId) {
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();
        return scheduleMapper.selectByMonth(
                start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                semesterId
        );
    }

    @Transactional(readOnly = true)
    public ScheduleRes listBySemesterAndType(Integer semesterId, String scheduleType) {
        return scheduleMapper.selectBySemesterAndType(semesterId, scheduleType);
    }

    @Transactional
    public ScheduleRes create(ScheduleCreateReq req) {
        scheduleMapper.insertSchedule(req);
        return scheduleMapper.selectById(req.getScheduleId().intValue());
    }

    @Transactional
    public ScheduleRes update(Integer scheduleId, ScheduleUpdateReq req) {
        int updated = scheduleMapper.updateSchedule(scheduleId,
                req.getScheduleType(),
                req.getStartDatetime(),
                req.getEndDatetime(),
                req.getDescription());
        if (updated == 0) {
            throw new RuntimeException("수정할 일정이 없습니다. id=" + scheduleId);
        }
        return scheduleMapper.selectById(scheduleId);
    }

    @Transactional
    public ScheduleRes getBySemesterAndType(Integer semesterId, String scheduleType) {
        return scheduleMapper.selectBySemesterAndType(semesterId, scheduleType);
    }

    @Transactional
    public void delete(Integer scheduleId) {
        log.info("삭제 시도 scheduleId={}", scheduleId);

        // 1. 신청 내역 있는지 검사
        int cnt = applicationMapper.countByScheduleId(scheduleId);
        if (cnt > 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,   // 409 Conflict
                    "이미 신청이 존재하는 일정은 삭제할 수 없습니다. id=" + scheduleId);
        }

        // 2. 신청이 없으면 삭제
        int deleted = scheduleMapper.deleteSchedule(scheduleId);
        if (deleted == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제할 일정이 없습니다. id=" + scheduleId);
        }
    }

    public DateRes findStartDate(String type){
        String replaceType = type.replace(" ","");
        return scheduleMapper.findStartDate(replaceType);
    }
}
