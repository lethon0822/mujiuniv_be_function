package com.green.muziuniv_be_notuser.app.staff.approval;



import com.green.muziuniv_be_notuser.app.staff.approval.model.ApprovalAppGetReq;
import com.green.muziuniv_be_notuser.app.staff.approval.model.ApprovalAppGetRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApprovalMapper {
    List<ApprovalAppGetRes> selectApplications(ApprovalAppGetReq req);

    int updateApplicationStatus(@Param("appId") Long appId,
                                @Param("status") String status);
}
