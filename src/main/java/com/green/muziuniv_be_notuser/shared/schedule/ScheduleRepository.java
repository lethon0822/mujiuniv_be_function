package com.green.muziuniv_be_notuser.shared.schedule;

import com.green.muziuniv_be_notuser.entity.schedule.Schedule;
import com.green.muziuniv_be_notuser.entity.semester.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    List<Schedule> findBySemester_SemesterId(Integer semesterId);

    // start < monthEndExclusive AND end >= monthStart
    List<Schedule> findByTimeSetting_StartDatetimeLessThanAndTimeSetting_EndDatetimeGreaterThanOrderByTimeSetting_StartDatetimeAsc(
            LocalDateTime monthEndExclusive, LocalDateTime monthStart
    );

    List<Schedule> findBySemester_SemesterIdAndTimeSetting_StartDatetimeLessThanAndTimeSetting_EndDatetimeGreaterThanOrderByTimeSetting_StartDatetimeAsc(
            Integer semesterId, LocalDateTime monthEndExclusive, LocalDateTime monthStart
    );
}