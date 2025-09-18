package com.green.muziuniv_be_notuser.app.professor.score;

import com.green.muziuniv_be_notuser.app.professor.score.model.ScorePostReq;
import com.green.muziuniv_be_notuser.app.professor.score.model.ScorePutReq;
import com.green.muziuniv_be_notuser.app.professor.score.model.ScoreRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/professor/course/grade")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;

    /* -------------------------------
       성적 기입 (POST) - 중복 방지 방식
       → 이미 있으면 에러 발생
    -------------------------------- */
    @PostMapping
    public ResponseEntity<ScoreRes> saveScore(@RequestBody ScorePostReq req) {
        return ResponseEntity.ok(scoreService.saveScore(req));
    }

    /* -------------------------------
       성적 기입 (POST) - 자동 수정 방식
       → 이미 있으면 수정, 없으면 등록
       (엔드포인트 분리: /save-or-update)
    -------------------------------- */
    @PostMapping("/save-or-update")
    public ResponseEntity<ScoreRes> saveOrUpdateScore(@RequestBody ScorePostReq req) {
        return ResponseEntity.ok(scoreService.saveOrUpdateScore(req));
    }

    /* -------------------------------
       성적 수정 (PUT)
       → 반드시 기존 데이터가 있어야 수정 가능
    -------------------------------- */
    @PutMapping
    public ResponseEntity<ScoreRes> updateScore(@RequestBody ScorePutReq req) {
        return ResponseEntity.ok(scoreService.updateScore(req));
    }
}
