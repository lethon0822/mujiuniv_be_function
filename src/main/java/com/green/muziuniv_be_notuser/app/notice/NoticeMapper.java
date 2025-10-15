package com.green.muziuniv_be_notuser.app.notice;


import com.green.muziuniv_be_notuser.app.notice.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeMapper {
//    List<NoticeSearchGetRes> searchNoticeByKeyword(@Param("keyword") String keyword);

    int insertNotice(NoticePostReq req);
    List<NoticeGetRes> selectNotice();
    List<NoticeGetRes> searchNoticeByKeyword(NoticeGetReq req);
    NoticeGetRes noticeDetail(Long id);
    int updateNotice(NoticePutReq req);
    int deleteNotice(Long noticeId);
    void updateViews(ViewDto dto);
}
