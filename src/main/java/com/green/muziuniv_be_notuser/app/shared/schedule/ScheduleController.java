package com.green.muziuniv_be_notuser.app.shared.schedule;

import com.green.muziuniv_be_notuser.app.shared.schedule.model.ScheduleCreateReq;
import com.green.muziuniv_be_notuser.app.shared.schedule.model.ScheduleRes;
import com.green.muziuniv_be_notuser.app.shared.schedule.model.ScheduleUpdateReq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    // 예: /api/schedules?month=2025-09&semesterId=12
    @GetMapping
    public List<ScheduleRes> list(
            @RequestParam(required = false) String month,      // "yyyy-MM"
            @RequestParam(required = false) Integer semesterId
    ) {
        if (month != null && !month.isBlank()) {
            return scheduleService.listByMonth(YearMonth.parse(month), semesterId);
        }
        // month 없으면 현재달
        return scheduleService.listByMonth(YearMonth.now(), semesterId);
    }

    @PostMapping
    public ScheduleRes create(@Valid @RequestBody ScheduleCreateReq req) {
        return scheduleService.create(req);
    }

    @PutMapping("/{scheduleId}")
    public ScheduleRes update(@PathVariable Integer scheduleId,
                              @Valid @RequestBody ScheduleUpdateReq req) {
        return scheduleService.update(scheduleId, req);
    }

    @DeleteMapping("/{scheduleId}")
    public void delete(@PathVariable Integer scheduleId) {
        scheduleService.delete(scheduleId);
    }
}