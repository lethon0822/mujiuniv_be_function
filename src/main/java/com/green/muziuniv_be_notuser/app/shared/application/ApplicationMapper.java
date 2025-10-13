package com.green.muziuniv_be_notuser.app.shared.application;

import com.green.muziuniv_be_notuser.app.shared.application.model.AppPostReq;
import com.green.muziuniv_be_notuser.app.shared.application.model.ApplicationListRow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApplicationMapper {

    Integer selectNextSemesterId(@Param("currentSemesterId") Integer currentSemesterId);

    Integer isScheduleOpenNow(@Param("scheduleId") Integer scheduleId);

    void insertApplication(AppPostReq req);

    List<ApplicationListRow> selectMyApplications(@Param("userId") Integer userId);

    int deleteApplication(@Param("appId") Long appId, @Param("userId") Long userId);

    Integer findSemesterIdByScheduleId(@Param("scheduleId") Integer scheduleId);

    String findScheduleTypeByScheduleId(@Param("scheduleId") Integer scheduleId);

    int existsActiveApplication(@Param("userId") Long userId,
                                @Param("semesterId") Integer semesterId,
                                @Param("scheduleType") String scheduleType);
    /** ✅ 특정 일정(schedule_id)에 연결된 신청(application) 개수 조회 */
    int countByScheduleId(@Param("scheduleId") Integer scheduleId);
}
