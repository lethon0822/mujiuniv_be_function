package com.green.muziuniv_be_notuser.app.notice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeSearchGetRes {
private Long noticeId;

private String noticeContent;
private String noticeTitle;

}
