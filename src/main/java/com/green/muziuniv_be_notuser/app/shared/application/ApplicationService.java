package com.green.muziuniv_be_notuser.app.shared.application;

import com.green.muziuniv_be_notuser.app.shared.application.model.AppPostReq;
import com.green.muziuniv_be_notuser.app.shared.application.model.ApplicationListRow;
import com.green.muziuniv_be_notuser.configuration.model.SignedUser;
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

    /** 정규 신청 (중복체크 + userId 세팅) */
    @Transactional
    public void createApplication(AppPostReq req, SignedUser signedUser){
        req.setUserId(signedUser.signedUserId);

        // 학기와 유형은 schedule에서 조회
        Integer semesterId = applicationMapper.findSemesterIdByScheduleId(req.getScheduleId());
        String scheduleType = applicationMapper.findScheduleTypeByScheduleId(req.getScheduleId());

        // 중복 신청 체크
        int exists = applicationMapper.existsActiveApplication(req.getUserId(), semesterId, scheduleType);
        if (exists > 0) {
            throw new IllegalStateException("이미 동일 학기/유형의 신청이 존재합니다.");
        }

        applicationMapper.insertApplication(req);
    }

    /** 내 신청 목록 */
    @Transactional(readOnly = true)
    public List<ApplicationListRow> getMyApplications(Integer userId) {
        return applicationMapper.selectMyApplications(userId);
    }

    /** 신청 취소 (처리중인 건만) */
    @Transactional
    public boolean cancelApplication(Long appId, Long userId) {
        return applicationMapper.cancelIfPending(appId, userId) > 0;
    }
}
