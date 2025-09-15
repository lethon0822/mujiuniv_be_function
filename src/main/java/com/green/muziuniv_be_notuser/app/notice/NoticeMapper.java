package com.green.muziuniv_be_notuser.app.notice;


import com.green.muziuniv_be_notuser.app.notice.model.NoticeGetRes;
import com.green.muziuniv_be_notuser.app.notice.model.NoticePostReq;
import com.green.muziuniv_be_notuser.app.notice.model.NoticePutReq;
import com.green.muziuniv_be_notuser.entity.notice.Notice;
import org.apache.ibatis.annotations.Mapper;
import com.green.muziuniv_be_notuser.app.notice.model.NoticeGetReq;

import java.util.List;

@Mapper
public interface NoticeMapper {
   List<Notice> findAll();
   Notice findById(int noticeId);

    List<NoticeGetRes> selectNoticeByContent(String searchText);

    int insertNoticeByStaff(NoticePostReq req);

    List<NoticeGetRes> selectNoticeByTitleORContent(String searchText);

    List<NoticeGetRes> selectNoticeByTitle(String searchText);

    NoticeGetRes selectNoticeById(int noticeId);

    int updateNoticeByNoticeId(NoticePutReq req);

    int deleteNoticeByNoticeId(int noticeId);
}
