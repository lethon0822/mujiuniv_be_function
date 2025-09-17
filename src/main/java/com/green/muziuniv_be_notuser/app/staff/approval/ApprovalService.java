package com.green.muziuniv_be_notuser.app.staff.approval;

import com.green.muziuniv_be_notuser.app.staff.approval.model.ApprovalAppGetReq;
import com.green.muziuniv_be_notuser.app.staff.approval.model.ApprovalAppGetRes;
import com.green.muziuniv_be_notuser.app.staff.approval.model.ApprovalPatchReq;
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
        List<ProGetRes> users = userClient.getProInfo(request).getResult();

        Map<Long, ProGetRes> userMap = users.stream()
                .collect(Collectors.toMap(user -> user.getUserId(), user -> user));

        applications.forEach(app -> {
            ProGetRes user = userMap.get(app.getUserId());
            if (user != null) {
                app.setUserName(user.getUserName());
                app.setDeptName(user.getDeptName());
            }
        });

        return applications;
    }

    public String modifyStatus(ApprovalPatchReq req){
        int result =  approvalMapper.modifyStatus(req);
        if(result == 1 && req.getStatus() == "승인") {
            return "승인 완료";
        } else if (req.getStatus() == "거부") {
            return "승인 거부";
        }
        return "문제 발생 ";
        //TODO 예외처리
    }
}
