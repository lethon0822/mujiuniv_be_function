package com.green.muziuniv_be_notuser.app.staff.approval.model;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.beans.ConstructorProperties;

/**
 * 강의 개설 승인/거부 요청 DTO
 */
@Getter
@Setter
@AllArgsConstructor
public class ApprovalCoursePatchReq {
    private Long courseId;   // 강의 PK
    private String status;   // 승인 상태 ("승인", "거부")

//    @ConstructorProperties({"id", "status"})
//    public ApprovalCoursePatchReq(Long id, String status) {
//        courseId = id;
//        this.status = status;
//    }
}
