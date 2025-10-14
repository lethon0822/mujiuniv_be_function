package com.green.muziuniv_be_notuser.app.notice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NoticePostReq {
    String noticeContent;
    String noticeTitle;
}
