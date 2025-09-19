package com.green.muziuniv_be_notuser.app.staff.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 강의 개설 승인/거부 응답 DTO
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalCoursePatchRes {
    private Long courseId; // 강의 PK
    private String status; // 승인 결과
}
