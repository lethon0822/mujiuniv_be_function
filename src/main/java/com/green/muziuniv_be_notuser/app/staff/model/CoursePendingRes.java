// 조회용

package com.green.muziuniv_be_notuser.app.staff.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CoursePendingRes {
    private Long courseId; // 강의 ID
    private String title; // 강의명
    private String classroom; // 강의실
    private String status; // 상태 (처리중/승인)
}
