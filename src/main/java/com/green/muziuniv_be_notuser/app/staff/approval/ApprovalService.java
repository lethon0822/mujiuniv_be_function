package com.green.muziuniv_be_notuser.app.staff.approval;

import com.green.muziuniv_be_notuser.app.staff.approval.model.*;
import com.green.muziuniv_be_notuser.openfeign.user.UserClient;
import com.green.muziuniv_be_notuser.openfeign.user.model.UserInfoDto;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovalService {
    private final ApprovalMapper approvalMapper;
    private final UserClient userClient;

    /**
     * 신청 목록 조회
     */
    @Transactional(readOnly = true)
    public List<ApprovalAppGetRes> getApplications(ApprovalAppGetReq req) {
        List<ApprovalAppGetRes> apps = approvalMapper.selectApplications(req);
        if (apps.isEmpty()) {
            return apps;
        }

        List<Long> userIds = apps.stream()
                .map(ApprovalAppGetRes::getUserId)
                .distinct()
                .toList();

        Map<String, List<Long>> request = Map.of("userId", userIds);
        ResultResponse<Map<Long, UserInfoDto>> response = userClient.getUserInfo(request);

        Map<Long, UserInfoDto> userInfoMap =
                response != null && response.getResult() != null
                        ? response.getResult()
                        : Map.of();

        apps.forEach(app -> {
            UserInfoDto info = userInfoMap.get(app.getUserId());
            if (info != null) {
                app.setUserName(info.getUserName());
                app.setDeptName(info.getDeptName());
            }
        });

        return apps;
    }

    @Transactional
    public String decideApplication(ApprovalPatchReq req) {
        // 현재 유저 정보 조회
        ResultResponse<UserInfoDto> userRes = userClient.getUserById(req.getUserId());
        UserInfoDto userInfo = userRes.getResult();

        if (userInfo == null) {
            throw new RuntimeException("사용자 정보를 찾을 수 없습니다.");
        }

        Integer currentStatus = userInfo.getStatus(); // 1 = 재학/재직, 0 = 휴학/휴직
        log.info("현재 status(0/1) = {}, 요청 타입 = {}", currentStatus, req.getScheduleType());

        // Validation
        if ("승인".equals(req.getStatus())) {
            switch (req.getScheduleType()) {
                case "휴학신청", "휴직신청" -> {
                    if (currentStatus == 0) {
                        throw new IllegalStateException("이미 휴학/휴직 상태입니다. 신청 불가");
                    }
                }
                case "복학신청", "복직신청" -> {
                    if (currentStatus == 1) {
                        throw new IllegalStateException("이미 재학/재직 상태입니다. 신청 불가");
                    }
                }
                default -> throw new RuntimeException("알 수 없는 scheduleType: " + req.getScheduleType());
            }
        }


        // 1. application 상태 업데이트
        int updated = approvalMapper.updateApplicationStatus(req.getAppId(), req.getStatus());
        if (updated != 1) {
            throw new RuntimeException("신청서 상태 업데이트 실패");
        }

        // 2. 승인 시 user-service 상태 업데이트
        if ("승인".equals(req.getStatus())) {
            Integer newStatus = switch (req.getScheduleType()) {
                case "휴학신청", "휴직신청" -> 0; // 휴학/휴직 → 0
                case "복학신청", "복직신청" -> 1; // 복학/복직 → 1
                default -> throw new RuntimeException("알 수 없는 scheduleType");
            };

            userClient.updateStatus(req.getUserId(), newStatus);
            return "승인 완료";
        }

        // 3. 거부 처리
        if ("거부".equals(req.getStatus())) {
            return "거부 처리 완료";
        }

        return "알 수 없는 처리 상태";
    }
}
