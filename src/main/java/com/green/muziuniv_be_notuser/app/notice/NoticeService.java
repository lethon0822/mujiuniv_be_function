package com.green.muziuniv_be_notuser.app.notice;

import com.green.muziuniv_be_notuser.app.notice.model.*;
import com.green.muziuniv_be_notuser.entity.notice.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeMapper noticeMapper;
    private final NoticeRepository noticeRepository;
    public void insertNoticeByStaff (NoticePostReq req){
       Notice notice = Notice.builder()
               .noticeTitle(req.getNoticeTitle())
               .noticeContent(req.getNoticeContent())
               .build();
       noticeRepository.save(notice);
    }

    //공지사항검색 제목+내용, 키워드 필터링
    public List<NoticeSearchGetRes> searchNotice() {
        return noticeMapper.searchNotice();
    }

    public List<NoticeSearchGetRes> searchNoticeByKeyword(String keyword) {
        return noticeMapper.searchNoticeByKeyword(keyword);
    }

    //공지사항검색 제목만 (됨)
    public List<NoticeGetRes> searchNoticeTitle(){
        return noticeMapper.searchNoticeTitle();
    }
    // 공지사항검색 내용만 (됨)
//    public List<NoticeGetRes> searchNoticeContent(){
//        return noticeMapper.searchNoticeContent();
//    }


    //자세히보기

    public NoticeGetRes searchSearch(Long noticeId) {
        return noticeMapper.searchSearch(noticeId);
    }

    //수정하기


    public boolean updateNotice(NoticeUpdateReq req) {
        return noticeMapper.updateNotice(req);
    }

   //공지삭제
   public int deleteNotice(Long noticeId) {
       return noticeMapper.deleteNotice(noticeId);
   }





}