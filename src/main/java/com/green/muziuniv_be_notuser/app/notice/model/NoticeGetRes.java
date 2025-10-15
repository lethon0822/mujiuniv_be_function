package com.green.muziuniv_be_notuser.app.notice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeGetRes {
    Long noticeId;
    String createdAt;
    String updatedAt;
    String noticeContent;
    String noticeTitle;
    Integer type;
    Integer view;
}


