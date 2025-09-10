package com.green.muziuniv_be_notuser.shared.schedule;

import com.green.muziuniv_be_notuser.entity.schedule.Schedule;
import com.green.muziuniv_be_notuser.entity.semester.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    // ScheduleService.listBySemester()에서 사용
    List<Schedule> findBySemester_SemesterId(Integer semesterId);

    // ScheduleService.listByMonth()에서 사용 (해당 기간과 겹치는 일정)
    List<Schedule> findByTimeSetting_StartDatetimeLessThanEqualAndTimeSetting_EndDatetimeGreaterThanEqual(
            LocalDateTime end, LocalDateTime start
    );

}