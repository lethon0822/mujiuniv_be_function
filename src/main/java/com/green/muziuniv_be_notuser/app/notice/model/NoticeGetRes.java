package com.green.muziuniv_be_notuser.app.notice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeGetRes {
    int noticeId;
    String createdAt;
    String updateAt;
    String noticeContent;
    String noticeTitle;
}
