package com.green.muziuniv_be_notuser.app.staff.approval.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApprovalAppGetReq {
    private Integer year;
    private Integer semester;
    private String scheduleType;
}
