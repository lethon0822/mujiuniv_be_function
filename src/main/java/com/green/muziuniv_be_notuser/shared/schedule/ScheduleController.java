package com.green.muziuniv_be_notuser.shared.schedule;

import com.green.muziuniv_be_notuser.shared.schedule.model.ScheduleCreateReq;
import com.green.muziuniv_be_notuser.shared.schedule.model.ScheduleRes;
import com.green.muziuniv_be_notuser.shared.schedule.model.ScheduleUpdateReq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping
    public List<ScheduleRes> list(
            @RequestParam(required = false) String month,
            @RequestParam(required = false) Integer semesterId
    ) {
        YearMonth ym = (month != null && !month.isBlank())
                ? YearMonth.parse(month)
                : YearMonth.now();
        return scheduleService.listByMonth(ym, semesterId);
    }

    @GetMapping("/for")
    public ScheduleRes getFor(
            @RequestParam Integer semesterId,
            @RequestParam String scheduleType
    ) {
        return scheduleService.listBySemesterAndType(semesterId, scheduleType);
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
