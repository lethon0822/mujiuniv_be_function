package com.green.muziuniv_be_notuser.app.staff.approval;

import com.green.muziuniv_be_notuser.app.staff.approval.model.ApprovalAppGetReq;
import com.green.muziuniv_be_notuser.app.staff.approval.model.ApprovalAppGetRes;
import com.green.muziuniv_be_notuser.app.staff.approval.model.ApprovalPatchReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApprovalMapper {
    List<ApprovalAppGetRes> applicationList (ApprovalAppGetReq req);
    int modifyStatus(ApprovalPatchReq req);
}
