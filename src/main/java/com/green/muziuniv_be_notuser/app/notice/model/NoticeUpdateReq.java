package com.green.muziuniv_be_notuser.app.notice.model;

import lombok.Getter;
import lombok.Setter;


    @Getter
    @Setter
    public class NoticeUpdateReq {
        Long noticeId;
        String createdAt;
        String noticeContent;
        String noticeTitle;
    }

