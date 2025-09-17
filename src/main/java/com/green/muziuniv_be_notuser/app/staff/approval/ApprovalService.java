package com.green.muziuniv_be_notuser.app.staff.approval;

import com.green.muziuniv_be_notuser.app.staff.approval.model.ApprovalAppGetReq;
import com.green.muziuniv_be_notuser.app.staff.approval.model.ApprovalAppGetRes;
import com.green.muziuniv_be_notuser.app.staff.approval.model.ApprovalPatchReq;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.openfeign.user.UserClient;
import com.green.muziuniv_be_notuser.openfeign.user.model.ProGetRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApprovalService {
    public final ApprovalMapper approvalMapper;
    private final UserClient userClient;

    //신청서 리스트
    public List<ApprovalAppGetRes> applicationList(ApprovalAppGetReq req) {
        List<ApprovalAppGetRes> applications = approvalMapper.applicationList(req);

        if (applications.isEmpty()) return applications;

        List<Long> userIds = applications.stream()
                .map(app -> app.getUserId())
                .distinct()
                .toList();

        Map<String, List<Long>> request = Map.of("userIds", userIds);
        request.put("userId", userIds);

        ResultResponse<List<ProGetRes>> response = userClient.getProInfo(request);
        List<ProGetRes> userInfos = response.getResult();

        Map<Long, ProGetRes> userMap = userInfos.stream()
                                       .collect(Collectors.toMap(user -> user.getUserId(), user -> user));

        for (ApprovalAppGetRes app : applications) {
            ProGetRes proGetRes = userMap.get(app.getUserId());
            if (proGetRes != null) {
                app.setUserId(proGetRes.getUserId());
                app.setUserName(proGetRes.getUserName());
                app.setDeptName(proGetRes.getDeptName());
            }
        }

        return applications;
    }

    public String modifyStatus(ApprovalPatchReq req){
        int result =  approvalMapper.modifyStatus(req);
        if(result == 1 && req.getStatus().equals("승인")) {
            return "승인 완료";
        } else if (req.getStatus() == "거부") {
            return "승인 거부";
        }
        return "문제 발생 ";
        //TODO 예외처리
    }
}
