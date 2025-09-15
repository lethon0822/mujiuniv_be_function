package com.green.muziuniv_be_notuser.app.notice;

import com.green.muziuniv_be_notuser.app.notice.model.NoticeGetRes;
import com.green.muziuniv_be_notuser.app.notice.model.NoticePostReq;
import com.green.muziuniv_be_notuser.app.notice.model.NoticePutReq;
import com.green.muziuniv_be_notuser.entity.notice.Notice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeMapper noticeMapper;

    public int insertNoticeByStaff (NoticePostReq req){
        return noticeMapper.insertNoticeByStaff(req);
    }

    public List<NoticeGetRes> selectNoticeByTitleORContent (String searchText){
        return noticeMapper.selectNoticeByTitleORContent(searchText);
    }

    public List<NoticeGetRes> selectNoticeByTitle(String searchText){
        return noticeMapper.selectNoticeByTitle(searchText);
    }

    public List<NoticeGetRes> selectNoticeByContent(String searchText){
        return noticeMapper.selectNoticeByContent(searchText);
    }

    public NoticeGetRes selectNoticeById(int noticeId){
        return noticeMapper.selectNoticeById(noticeId);
    }

    public int updateNoticeByNoticeId(NoticePutReq req){
        return noticeMapper.updateNoticeByNoticeId(req);
    }

    public int deleteNoticeByNoticeId(int noticeId){
        return noticeMapper.deleteNoticeByNoticeId(noticeId);
    }


}