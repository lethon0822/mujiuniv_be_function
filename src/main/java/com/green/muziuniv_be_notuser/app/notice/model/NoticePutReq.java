package com.green.muziuniv_be_notuser.app.notice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NoticePutReq {
    Long noticeId;
    String createdAt;
    String updatedAt;
    String noticeContent;
    String noticeTitle;
}
