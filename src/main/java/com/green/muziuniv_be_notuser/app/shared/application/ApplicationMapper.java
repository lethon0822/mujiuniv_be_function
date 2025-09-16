package com.green.muziuniv_be_notuser.app.shared.application;


import com.green.muziuniv_be_notuser.app.shared.application.model.AppPostReq;
import com.green.muziuniv_be_notuser.app.shared.application.model.ApplicationListRow;
import com.green.muziuniv_be_notuser.app.shared.application.model.ScheduleWindow;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApplicationMapper {

    Integer selectNextSemesterId(@Param("currentSemesterId") Integer currentSemesterId);

    Integer isScheduleOpenNow(@Param("scheduleId") Integer scheduleId);

    void insertAppForReason(AppPostReq req);

    void insertApplication(AppPostReq req);

    List<ApplicationListRow> selectMyApplications(@Param("userId") Integer userId);

    int cancelIfPending(@Param("appId") Long appId, @Param("userId") Integer userId);
}
