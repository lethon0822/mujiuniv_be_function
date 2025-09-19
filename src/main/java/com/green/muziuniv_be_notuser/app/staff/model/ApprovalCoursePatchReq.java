package com.green.muziuniv_be_notuser.app.staff.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 강의 개설 승인/거부 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalCoursePatchReq {
    private Long courseId;   // 강의 PK
    private String status;   // 승인 상태 ("승인", "거부")
}
