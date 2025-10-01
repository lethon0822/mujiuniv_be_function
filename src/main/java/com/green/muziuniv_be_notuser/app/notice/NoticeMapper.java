package com.green.muziuniv_be_notuser.app.notice;


import com.green.muziuniv_be_notuser.app.notice.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeMapper {







    List<NoticeSearchGetRes> searchNoticeByKeyword(@Param("keyword") String keyword);

    List<NoticeSearchGetRes> searchNotice();

    List<NoticeGetRes> searchNoticeTitle();

    List<NoticeGetRes> searchNoticeContent();


    NoticeGetRes searchSearch(@Param("noticeId") Long noticeId);

    boolean updateNotice(NoticeUpdateReq req);

    int deleteNotice(Long noticeId);
}
