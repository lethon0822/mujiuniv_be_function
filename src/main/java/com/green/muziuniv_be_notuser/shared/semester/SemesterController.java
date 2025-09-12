package com.green.muziuniv_be_notuser.shared.semester;

import com.green.muziuniv_be_notuser.shared.application.ApplicationMapper;
import com.green.muziuniv_be_notuser.shared.semester.model.SemesterCreateReq;
import com.green.muziuniv_be_notuser.shared.semester.model.SemesterRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/semester")
@RequiredArgsConstructor
public class SemesterController {
    private final SemesterService semesterService;
    private final ApplicationMapper applicationMapper;

    @PostMapping
    public SemesterRes create(@Valid @RequestBody SemesterCreateReq req) {
        return semesterService.create(req);
    }

    // 전체 목록 (최신순)
    @GetMapping
    public List<SemesterRes> list() {
        return semesterService.listAll();
    }

    // (year, semester)로 단건 조회
    @GetMapping("/find")
    public SemesterRes find(@RequestParam Integer year,
                            @RequestParam Integer semester) {
        return semesterService.getByYearAndSemester(year, semester);
    }

    // ID로 단건 조회
    @GetMapping("/{semesterId}")
    public SemesterRes getOne(@PathVariable Integer semesterId) {
        return semesterService.getById(semesterId);
    }

    // 가장 최근 학기
    @GetMapping("/latest")
    public SemesterRes latest() {
        return semesterService.getLatest();
    }

    @GetMapping("/{currentSemesterId}/next")
    public Map<String, Integer> next(@PathVariable Integer currentSemesterId) {
        Integer next = applicationMapper.selectNextSemesterId(currentSemesterId);
        if (next == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "다음 학기 없음");
        return Map.of("semesterId", next);
    }

}
