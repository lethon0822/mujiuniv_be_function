package com.green.muziuniv_be_notuser.app.shared.schedule;

import com.green.muziuniv_be_notuser.app.shared.schedule.model.ScheduleCreateReq;
import com.green.muziuniv_be_notuser.app.shared.schedule.model.ScheduleRes;
import com.green.muziuniv_be_notuser.app.shared.schedule.model.ScheduleUpdateReq;
import com.green.muziuniv_be_notuser.app.shared.semester.SemesterRepository;
import com.green.muziuniv_be_notuser.entity.schedule.Schedule;
import com.green.muziuniv_be_notuser.entity.schedule.TimeSetting;
import com.green.muziuniv_be_notuser.entity.semester.Semester;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final SemesterRepository semesterRepository;
    private final ScheduleMapper mapper;

    private void validateTime(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || !end.isAfter(start)) {
            throw new IllegalArgumentException("시간 범위가 올바르지 않습니다.");
        }
    }

    public ScheduleRes create(ScheduleCreateReq req) {
        validateTime(req.getStartDatetime(), req.getEndDatetime());
        Semester semester = semesterRepository.findById(req.getSemesterId())
                .orElseThrow(() -> new IllegalArgumentException("학기를 찾을 수 없습니다."));

        // 맵퍼 일원화
        Schedule entity = mapper.toEntity(
                semester, req.getScheduleType(),
                req.getStartDatetime(), req.getEndDatetime(),
                req.getDescription()
        );
        return mapper.toRes(scheduleRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<ScheduleRes> listByMonth(YearMonth ym, Integer semesterId) {
        LocalDateTime monthStart = ym.atDay(1).atStartOfDay();
        LocalDateTime monthEndExclusive = ym.plusMonths(1).atDay(1).atStartOfDay();

        List<Schedule> rows = (semesterId == null)
                ? scheduleRepository
                .findByTimeSetting_StartDatetimeLessThanAndTimeSetting_EndDatetimeGreaterThanOrderByTimeSetting_StartDatetimeAsc(
                        monthEndExclusive, monthStart)
                : scheduleRepository
                .findBySemester_SemesterIdAndTimeSetting_StartDatetimeLessThanAndTimeSetting_EndDatetimeGreaterThanOrderByTimeSetting_StartDatetimeAsc(
                        semesterId, monthEndExclusive, monthStart);

        return rows.stream().map(mapper::toRes).toList();
    }

    @Transactional(readOnly = true)
    public List<ScheduleRes> listBySemester(Integer semesterId) {
        return scheduleRepository.findBySemester_SemesterId(semesterId)
                .stream().map(mapper::toRes).toList();
    }

    public ScheduleRes update(Integer scheduleId, ScheduleUpdateReq req) {
        validateTime(req.getStartDatetime(), req.getEndDatetime());
        Schedule entity = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다."));

        entity.change(
                req.getScheduleType(),
                new TimeSetting(req.getStartDatetime(), req.getEndDatetime()),
                req.getDescription()
        );
        return mapper.toRes(entity);
    }

    public void delete(Integer scheduleId) {
        if (!scheduleRepository.existsById(scheduleId)) {
            throw new IllegalArgumentException("이미 삭제되었거나 존재하지 않는 일정입니다.");
        }
        scheduleRepository.deleteById(scheduleId);
    }
}