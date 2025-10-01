package com.green.muziuniv_be_notuser.app.shared.schedule;

import com.green.muziuniv_be_notuser.app.shared.schedule.model.DateRes;
import com.green.muziuniv_be_notuser.app.shared.schedule.model.ScheduleCreateReq;
import com.green.muziuniv_be_notuser.app.shared.schedule.model.ScheduleRes;
import com.green.muziuniv_be_notuser.app.shared.schedule.model.ScheduleUpdateReq;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.configuration.model.SignedUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
@Slf4j
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

    @GetMapping("/by-semester-and-type")
    public ScheduleRes getBySemesterAndType(
            @RequestParam Integer semesterId,
            @RequestParam String scheduleType
    ) {
        return scheduleService.getBySemesterAndType(semesterId, scheduleType);
    }

    @GetMapping("/for")
    public ScheduleRes getFor(
            @RequestParam Integer semesterId,
            @RequestParam String scheduleType
    ) {
        log.info(">>> Controller 들어온 scheduleType = {}", scheduleType);
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

    //프론트 기능 막아두기 위해서 start datetime을 가져온다
    @GetMapping("/date")
    public ResponseEntity<?> findStartDate(@RequestParam String type){
        DateRes result =scheduleService.findStartDate(type);
     return ResponseEntity.ok(result);
    }
}
