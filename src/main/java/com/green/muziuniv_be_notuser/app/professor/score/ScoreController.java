package com.green.muziuniv_be_notuser.app.professor.score;

import com.green.muziuniv_be_notuser.app.professor.score.model.ScorePostReq;
import com.green.muziuniv_be_notuser.app.professor.score.model.ScorePutReq;
import com.green.muziuniv_be_notuser.app.professor.score.model.ScoreRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/professor/course/grade")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;

    /* 성적 기입 */
    @PostMapping
    public ResponseEntity<ScoreRes> saveScore(@RequestBody ScorePostReq req) {
        return ResponseEntity.ok(scoreService.saveScore(req));
    }
    /* 성적 수정 */
    @PutMapping
    public ResponseEntity<ScoreRes> updateScore(@RequestBody ScorePutReq req) {
        return ResponseEntity.ok(scoreService.updateScore(req));
    }
}


