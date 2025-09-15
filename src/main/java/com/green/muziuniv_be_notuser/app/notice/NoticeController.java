package com.green.muziuniv_be_notuser.app.notice;

import com.green.muziuniv_be_notuser.app.notice.model.NoticeGetRes;
import com.green.muziuniv_be_notuser.app.notice.model.NoticePostReq;
import com.green.muziuniv_be_notuser.app.notice.model.NoticePutReq;
import com.green.muziuniv_be_notuser.entity.notice.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping
    public ResponseEntity<?> postNotice (@RequestBody NoticePostReq req) {
        int result = noticeService.insertNoticeByStaff(req);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping
    public ResponseEntity<?> searchNotice(@RequestParam(value = "search_text", required = false) String searchText) {
        List<NoticeGetRes> result = noticeService.selectNoticeByTitleORContent(searchText);
        return ResponseEntity.ok(result);
    }

    // 제목으로만 검색
    @GetMapping("/title")
    public ResponseEntity<?> searchNoticeByTitle(@RequestParam("search_text") String searchText) {
        List<NoticeGetRes> result = noticeService.selectNoticeByTitle(searchText);
        return ResponseEntity.ok(result);
    }

    // 내용으로만 검색
    @GetMapping("/content")
    public ResponseEntity<?> searchNoticeByContent(@RequestParam("search_text") String searchText) {
        List<NoticeGetRes> result = noticeService.selectNoticeByContent(searchText);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{notice_id}")
    public ResponseEntity<?> getNotice(@PathVariable (name = "notice_id") int noticeId) {
        NoticeGetRes result = noticeService.selectNoticeById(noticeId);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/{notice_id}")
    public ResponseEntity<?> putNotice(@RequestBody NoticePutReq req) {
        int result = noticeService.updateNoticeByNoticeId(req);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteNotice(@RequestParam (name = "notice_id") int noticeId) {
        int result = noticeService.deleteNoticeByNoticeId(noticeId);
        return ResponseEntity.ok().body(result);
    }
}
