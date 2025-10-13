package com.green.muziuniv_be_notuser.app.shared.application;

import com.green.muziuniv_be_notuser.app.shared.application.ApplicationMapper;
import com.green.muziuniv_be_notuser.app.shared.application.model.AppPostReq;
import com.green.muziuniv_be_notuser.app.shared.application.model.ApplicationListRow;
import com.green.muziuniv_be_notuser.configuration.model.SignedUser;
import com.green.muziuniv_be_notuser.openfeign.user.UserClient;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {

    private final ApplicationMapper applicationMapper;
    private final UserClient userClient;

    /** 신청 생성 (휴직/복직 각각 독립 처리) */
    @Transactional
    public void createApplication(AppPostReq req, SignedUser signedUser) {
        if (signedUser != null) {
            req.setUserId(signedUser.signedUserId);
        }

        // 1. 신청기간 확인
        if (!isScheduleOpenNow(req.getScheduleId())) {
            throw new IllegalStateException("현재는 신청 기간이 아닙니다.");
        }

        // 2. 학기 + 유형 조회
        Integer semesterId = applicationMapper.findSemesterIdByScheduleId(req.getScheduleId());
        String scheduleType = applicationMapper.findScheduleTypeByScheduleId(req.getScheduleId());

        // 3. 중복신청 방지
        int exists = applicationMapper.existsActiveApplication(req.getUserId(), semesterId, scheduleType);
        if (exists > 0) {
            throw new IllegalStateException("이미 동일 학기/유형의 신청이 존재합니다.");
        }

        // 4. DB insert
        applicationMapper.insertApplication(req);
        log.info("{} 신청 완료: userId={}, semesterId={}", scheduleType, req.getUserId(), semesterId);
    }

    @Transactional(readOnly = true)
    public boolean isScheduleOpenNow(Integer scheduleId) {
        Integer result = applicationMapper.isScheduleOpenNow(scheduleId);
        return result != null && result == 1;
    }

    @Transactional(readOnly = true)
    public List<ApplicationListRow> getMyApplications(Integer userId) {
        return applicationMapper.selectMyApplications(userId);
    }

    @Transactional
    public void deleteApplication(Long userId, Long appId) {
        int deleted = applicationMapper.deleteApplication(appId, userId);
        if (deleted == 0) {
            throw new IllegalStateException("삭제할 수 없거나 이미 삭제된 신청입니다.");
        }
    }
    @Transactional(readOnly = true)
    public Integer getNextSemesterId(Integer currentSemesterId) {
        return applicationMapper.selectNextSemesterId(currentSemesterId);
    }
}
