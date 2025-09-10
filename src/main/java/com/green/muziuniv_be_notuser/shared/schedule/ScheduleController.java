package com.green.muziuniv_be_notuser.shared.schedule;

import com.green.muziuniv_be_notuser.shared.schedule.model.ScheduleCreateReq;
import com.green.muziuniv_be_notuser.shared.schedule.model.ScheduleRes;
import com.green.muziuniv_be_notuser.shared.schedule.model.ScheduleUpdateReq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    // 학기별 조회: /api/schedule?semesterId=12
    @GetMapping
    @Transactional(readOnly = true)
    public List<ScheduleRes> list(
            @RequestParam(required = false) Integer semesterId,
            @RequestParam(required = false) String yyyymm // 예: "2025-09"
    ) {
        if (semesterId != null) {
            return scheduleService.listBySemester(semesterId);
        }
        if (yyyymm != null) {
            LocalDate first = LocalDate.parse(yyyymm + "-01");
            return scheduleService.listByMonth(first.atStartOfDay());
        }
        // 기본: 이번 달
        return scheduleService.listByMonth(LocalDate.now().withDayOfMonth(1).atStartOfDay());
    }

    // 등록
    @PostMapping
    public ScheduleRes create(@Valid @RequestBody ScheduleCreateReq req) {
        return scheduleService.create(req);
    }

    // 수정: /api/schedule/{id}
    @PutMapping("/{scheduleId}")
    public ScheduleRes update(@PathVariable Integer scheduleId,
                              @Valid @RequestBody ScheduleUpdateReq req) {
        return scheduleService.update(scheduleId, req);
    }

    // 삭제: /api/schedule/{id}
    @DeleteMapping("/{scheduleId}")
    public void delete(@PathVariable Integer scheduleId) {
        scheduleService.delete(scheduleId);
    }
}