package com.green.muziuniv_be_notuser.app.shared.application;

import com.green.muziuniv_be_notuser.app.shared.application.model.AppPostReq;
import com.green.muziuniv_be_notuser.app.shared.application.model.ApplicationListRow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApplicationMapper {

    int countByScheduleId(@Param("scheduleId") Integer scheduleId);

    Integer selectNextSemesterId(@Param("currentSemesterId") Integer currentSemesterId);

    Integer isScheduleOpenNow(@Param("scheduleId") Integer scheduleId);

    void insertApplication(AppPostReq req);

    List<ApplicationListRow> selectMyApplications(@Param("userId") Integer userId);


    int deleteApplication(@Param("appId") Long appId,
                          @Param("userId") Long userId);

//    int cancelIfPending(@Param("appId") Long appId, @Param("userId") Long userId);

    // 학기 조회
    Integer findSemesterIdByScheduleId(@Param("scheduleId") Integer scheduleId);

    // 유형 조회
    String findScheduleTypeByScheduleId(@Param("scheduleId") Integer scheduleId);

    // 특정 학기/유형 중복 신청 여부 (int → >0이면 존재)
    int existsActiveApplication(
            @Param("userId") Long userId,
            @Param("semesterId") Integer semesterId,
            @Param("scheduleType") String scheduleType
    );
}
