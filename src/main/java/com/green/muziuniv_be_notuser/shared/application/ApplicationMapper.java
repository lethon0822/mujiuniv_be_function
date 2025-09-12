package com.green.muziuniv_be_notuser.shared.application;


import com.green.muziuniv_be_notuser.shared.application.model.AppPostReq;
import com.green.muziuniv_be_notuser.shared.application.model.ApplicationListRow;
import com.green.muziuniv_be_notuser.shared.application.model.ScheduleWindow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApplicationMapper {
    int insertAppForReason(AppPostReq req);

    int insertApplication(@Param("userId") Long userId,
                          @Param("scheduleId") Integer scheduleId,
                          @Param("reason") String reason);

    Integer selectNextSemesterId(@Param("currentSemesterId") Integer currentSemesterId);

    ScheduleWindow selectScheduleForSemesterAndType(@Param("semesterId") Integer semesterId,
                                                    @Param("scheduleType") String scheduleType);

    Integer isScheduleOpenNow(@Param("scheduleId") Integer scheduleId);

    List<ApplicationListRow> selectMyApplications(@Param("userId") Long userId,
                                                  @Param("status") String status);
    int cancelIfPending(@Param("appId") Long appId, @Param("userId") Long userId);
}
