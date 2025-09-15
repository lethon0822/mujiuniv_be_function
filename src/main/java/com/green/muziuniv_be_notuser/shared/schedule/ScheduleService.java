package com.green.muziuniv_be_notuser.shared.schedule;

import com.green.muziuniv_be_notuser.shared.schedule.model.ScheduleCreateReq;
import com.green.muziuniv_be_notuser.shared.schedule.model.ScheduleRes;
import com.green.muziuniv_be_notuser.shared.schedule.model.ScheduleUpdateReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleMapper scheduleMapper;

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
        // TODO: insert 쿼리 추가 필요 (학사일정 등록 로직)
        throw new UnsupportedOperationException("미구현");
    }

    @Transactional
    public ScheduleRes update(Integer scheduleId, ScheduleUpdateReq req) {
        // TODO: update 쿼리 추가 필요
        throw new UnsupportedOperationException("미구현");
    }

    @Transactional
    public void delete(Integer scheduleId) {
        // TODO: delete 쿼리 추가 필요
        throw new UnsupportedOperationException("미구현");
    }
}
