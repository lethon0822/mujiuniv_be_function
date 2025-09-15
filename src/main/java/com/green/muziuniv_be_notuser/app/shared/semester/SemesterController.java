package com.green.muziuniv_be_notuser.app.shared.semester;

import com.green.muziuniv_be_notuser.app.shared.semester.model.SemesterCreateReq;
import com.green.muziuniv_be_notuser.app.shared.semester.model.SemesterRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/semester")
@RequiredArgsConstructor
@Slf4j
public class SemesterController {
    private final SemesterService semesterService;

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
}
