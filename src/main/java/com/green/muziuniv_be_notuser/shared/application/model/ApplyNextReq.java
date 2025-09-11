package com.green.muziuniv_be_notuser.shared.application.model;

import lombok.Data;

@Data
public class ApplyNextReq {
    private Integer currentSemesterId; // Pinia의 현재 semesterId
    private String type;               // "LEAVE" | "RETURN" 또는 "휴학신청" | "복학신청"
    private String reason;             // 사유(선택)
}