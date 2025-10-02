package com.green.muziuniv_be_notuser.app.shared.application;

import com.green.muziuniv_be_notuser.app.shared.application.model.AppPostReq;
import com.green.muziuniv_be_notuser.app.shared.application.model.ApplicationListRow;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.configuration.model.SignedUser;
import com.green.muziuniv_be_notuser.openfeign.user.UserClient;
import com.green.muziuniv_be_notuser.openfeign.user.model.UserInfoDto;
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

    /** 정규 신청 (기간체크 + 중복체크 + 상태체크 + userId 세팅) */
    @Transactional
    public void createApplication(AppPostReq req, SignedUser signedUser) {
        // 1. userId 세팅
        if (signedUser != null) {
            req.setUserId(signedUser.signedUserId);
        }

        // 2. 신청 가능 기간 확인
        if (!isScheduleOpenNow(req.getScheduleId())) {
            throw new IllegalStateException("현재는 신청 기간이 아닙니다.");
        }

        // 3. 학기와 유형 조회
        Integer semesterId = applicationMapper.findSemesterIdByScheduleId(req.getScheduleId());
        String scheduleType = applicationMapper.findScheduleTypeByScheduleId(req.getScheduleId());

        // 4. 중복 신청 체크
        int exists = applicationMapper.existsActiveApplication(req.getUserId(), semesterId, scheduleType);
        if (exists > 0) {
            throw new IllegalStateException("이미 동일 학기/유형의 신청이 존재합니다.");
        }

        // 5. 유저 현재 상태 조회
        ResultResponse<UserInfoDto> userRes = userClient.getUserById(req.getUserId());
        UserInfoDto userInfo = userRes.getResult();

        if (userInfo == null) {
            throw new IllegalStateException("사용자 정보를 찾을 수 없습니다.");
        }

        int status = userInfo.getStatus(); // 1 = 재학/재직, 0 = 휴학/휴직
        log.info("createApplication - userId={}, currentStatus={}, scheduleType={}",
                req.getUserId(), status, scheduleType);

        // 6. Validation: 상태와 신청 타입 매칭 검증
        switch (scheduleType) {
            case "휴학신청" -> {
                if (status == 0) {
                    throw new IllegalStateException("이미 휴학/휴직 상태입니다. 휴학 신청 불가");
                }
            }
            case "복학신청" -> {
                if (status == 1) {
                    throw new IllegalStateException("재학/재직 상태에서는 복학 신청 불가");
                }
            }
            case "휴직신청" -> {
                if (status == 0) {
                    throw new IllegalStateException("이미 휴직 상태입니다. 휴직 신청 불가");
                }
            }
            case "복직신청" -> {
                if (status == 1) {
                    throw new IllegalStateException("재직 상태에서는 복직 신청 불가");
                }
            }
            default -> throw new IllegalStateException("알 수 없는 신청 유형: " + scheduleType);
        }

        // 7. 정상 insert
        applicationMapper.insertApplication(req);
        log.info("신청 완료: userId={}, scheduleType={}", req.getUserId(), scheduleType);
    }

    /** 내 신청 목록 */
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
    /** 신청 취소 (처리중인 건만) */
//    @Transactional
//    public boolean cancelApplication(Long appId, Long userId) {
//        return applicationMapper.cancelIfPending(appId, userId) > 0;
//    }
}
