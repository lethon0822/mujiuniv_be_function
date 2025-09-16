package com.green.muziuniv_be_notuser.app.shared.application;



import com.green.muziuniv_be_notuser.app.shared.application.model.AppPostReq;
import com.green.muziuniv_be_notuser.app.shared.application.model.ApplicationListRow;
import com.green.muziuniv_be_notuser.app.shared.application.model.ApplyNextReq;
import com.green.muziuniv_be_notuser.app.shared.application.model.ScheduleWindow;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationMapper applicationMapper;

    /** 다음 학기 ID 조회 */
    @Transactional(readOnly = true)
    public Integer getNextSemesterId(Integer currentSemesterId) {
        return applicationMapper.selectNextSemesterId(currentSemesterId);
    }

    /** 일정창 열려있는지 여부 */
    @Transactional(readOnly = true)
    public boolean isScheduleOpenNow(Integer scheduleId) {
        Integer result = applicationMapper.isScheduleOpenNow(scheduleId);
        return result != null && result == 1;
    }

    /** 단순 사유 신청 */
    @Transactional
    public void createAppForReason(AppPostReq req) {
        applicationMapper.insertAppForReason(req);
    }

    /** 정규 신청 (apply-next 로직) */
    @Transactional
    public void createApplication(AppPostReq req) {
        applicationMapper.insertApplication(req);
    }

    /** 내 신청 목록 */
    @Transactional(readOnly = true)
    public List<ApplicationListRow> getMyApplications(Integer userId) {
        return applicationMapper.selectMyApplications(userId);
    }

    /** 신청 취소 (처리중인 건만) */
    @Transactional
    public boolean cancelApplication(Long appId, Integer userId) {
        return applicationMapper.cancelIfPending(appId, userId) > 0;
    }
}
