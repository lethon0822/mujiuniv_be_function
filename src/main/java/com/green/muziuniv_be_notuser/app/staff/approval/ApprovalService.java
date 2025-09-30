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
        // 1) notuser DB에서 application 목록 가져오기
        List<ApprovalAppGetRes> apps = approvalMapper.selectApplications(req);
        if (apps.isEmpty()) {
            return apps;
        }

        // 2) userId 리스트 뽑아서 user-service 호출
        List<Long> userIds = apps.stream()
                .map(ApprovalAppGetRes::getUserId)
                .distinct()
                .toList();

        // ✅ key 이름 "userId" 로 맞추기
        Map<String, List<Long>> request = Map.of("userId", userIds);
        ResultResponse<Map<Long, UserInfoDto>> response = userClient.getUserInfo(request);

        // ✅ null-safe
        Map<Long, UserInfoDto> userInfoMap =
                response != null && response.getResult() != null
                        ? response.getResult()
                        : Map.of();

        // 3) userName, deptName 매핑
        apps.forEach(app -> {
            UserInfoDto info = userInfoMap.get(app.getUserId());
            if (info != null) {
                app.setUserName(info.getUserName());
                app.setDeptName(info.getDeptName());

            }
        });

        return apps;
    }

    /**
     * 신청 승인/거부 처리
     */
    @Transactional
    public String decideApplication(ApprovalPatchReq req) {
        // 1. application 상태 업데이트
        int updated = approvalMapper.updateApplicationStatus(req.getAppId(), req.getStatus());
        if (updated != 1) {
            throw new RuntimeException("신청서 상태 업데이트 실패");
        }

        // 2. 승인일 때 user-service 학적/재직 상태 변경
        if ("승인".equals(req.getStatus())) {
            String newStatus = switch (req.getScheduleType()) {
                case "휴학신청" -> "휴학";
                case "복학신청" -> "재학";
                case "휴직신청" -> "휴직";
                case "복직신청" -> "재직";
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