package com.green.muziuniv_be_notuser.app.professor.score;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.muziuniv_be_notuser.app.professor.score.model.ScorePostReq;
import com.green.muziuniv_be_notuser.app.professor.score.model.ScorePutReq;
import com.green.muziuniv_be_notuser.app.professor.score.model.ScoreRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/professor/course/grade")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;

    /* -------------------------------
      성적 기입 (POST) - 단일 or 복수 모두 지원
   -------------------------------- */
    @PostMapping
    public ResponseEntity<?> saveScore(@RequestBody Object body) {
        if (body instanceof java.util.List<?>) {
            // 배열(JSON Array) → 여러 개 저장
            List<?> list = (List<?>) body;
            List<ScoreRes> result = list.stream()
                    .filter(item -> item instanceof Map) // JSON object 체크
                    .map(item -> {
                        Map<String, Object> map = (Map<String, Object>) item;
                        ScorePostReq req = new ScorePostReq(
                                Long.valueOf(map.get("enrollmentId").toString()),
                                (int) map.get("midScore"),
                                (int) map.get("finScore"),
                                (int) map.get("attendanceScore"),
                                (int) map.get("otherScore")
                        );
                        return scoreService.saveOrUpdateScore(req);
                    })
                    .toList();
            return ResponseEntity.ok(result);
        } else {
            // 단일 객체 → 한 명 저장
            ScorePostReq req = new ObjectMapper().convertValue(body, ScorePostReq.class);
            return ResponseEntity.ok(scoreService.saveOrUpdateScore(req));
        }
    }

    /* -------------------------------
       성적 수정 (PUT)
    -------------------------------- */
    @PutMapping
    public ResponseEntity<?> updateScore(@RequestBody Object body) {
        if (body instanceof java.util.List<?>) {
            List<?> list = (List<?>) body;
            List<ScoreRes> result = list.stream()
                    .filter(item -> item instanceof Map)
                    .map(item -> {
                        Map<String, Object> map = (Map<String, Object>) item;
                        ScorePutReq req = new ScorePutReq(
                                Long.valueOf(map.get("enrollmentId").toString()),
                                (int) map.get("midScore"),
                                (int) map.get("finScore"),
                                (int) map.get("attendanceScore"),
                                (int) map.get("otherScore")
                        );
                        return scoreService.updateScore(req);
                    })
                    .toList();
            return ResponseEntity.ok(result);
        } else {
            ScorePutReq req = new ObjectMapper().convertValue(body, ScorePutReq.class);
            return ResponseEntity.ok(scoreService.updateScore(req));
        }
    }

    /* -------------------------------
     성적 조회 (GET)
     /professor/course/{courseId}/grade
  -------------------------------- */
    @GetMapping("/{courseId}")
    public ResponseEntity<List<ScoreRes>> getScoresByCourse(@PathVariable Long courseId) {
        List<ScoreRes> result = scoreService.getScoresByCourse(courseId);
        return ResponseEntity.ok(result);
    }
}
