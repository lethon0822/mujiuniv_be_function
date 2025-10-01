package com.green.muziuniv_be_notuser.app.notice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeGetReq {
    Long noticeId;
    String createdAt;
    String updateAt;
    String noticeContent;
    String noticeTitle;
}
