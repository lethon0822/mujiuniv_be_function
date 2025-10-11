package com.green.muziuniv_be_notuser.app.notice;

import com.green.muziuniv_be_notuser.app.notice.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    //공지사항 페이지에서 공지를 등록 (됨)
    @PostMapping
    public ResponseEntity<?> postNotice (@RequestBody NoticePostReq req) {
        noticeService.insertNoticeByStaff(req);
        return ResponseEntity.ok().body(null);
    }

    //공지사항검색 제목+내용, 키워드 필터링(됨)
    @GetMapping
    public ResponseEntity<?> selectNotice(@ModelAttribute NoticeGetReq req) {
        String keyword = req.getKeyword();
        // keyword가 아예 없으면 전체 조회
        if (keyword == null || keyword.isBlank()) {
            List<NoticeGetRes> result = noticeService.selectNotice();
            return ResponseEntity.ok(result);
        }
        // 키워드 검색
        List<NoticeGetRes> result = noticeService.searchNoticeByKeyword(req);
        return ResponseEntity.ok(result);
    }

    //공지사항검색 제목만 (됨)
    @GetMapping("/search")
    public ResponseEntity<?> searchNoticeTitleAndContent(@PathVariable NoticeGetReq req) {
        List<NoticeGetRes> result = noticeService.searchNoticeByKeyword(req);
        return ResponseEntity.ok(result);
    }

    // 공지사항 자세히 보기(됨)
    @GetMapping("/{notice_id}")
     public ResponseEntity<?> searchSearch(@PathVariable("notice_id") Long noticeId) {
            NoticeGetRes notice = noticeService.searchSearch(noticeId);
            if (notice != null) {
                return ResponseEntity.ok(notice);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("공지 없음");
            }
    }


    //공지사항수정 (됨)
    @PutMapping("/{notice_id}")
    public ResponseEntity<?> updateNotice(@PathVariable("notice_id") Long id,   // ✅ 경로 변수 이름 일치
                                          @RequestBody NoticePutReq req) {
        req.setNoticeId(id);
        int result = noticeService.updateNotice(req);

        if (result > 0) {
            return ResponseEntity.ok("공지사항이 수정되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("수정 실패");
        }
    }


    //공지삭제 (됨)

    @DeleteMapping("/{notice_id}")
    public ResponseEntity<?> deleteNotice(@PathVariable("notice_id") String noticeIdStr) {
        Long noticeId;
        try {
            noticeId = Long.parseLong(noticeIdStr);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("올바른 notice_id를 입력해주세요.");
        }

        int deleted = noticeService.deleteNotice(noticeId);
        if (deleted > 0) {
            return ResponseEntity.ok("공지사항이 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 공지를 찾을 수 없습니다.");
        }
    }
}
