package com.green.muziuniv_be_notuser.shared.semester;

import com.green.muziuniv_be_notuser.entity.semester.Semester;
import com.green.muziuniv_be_notuser.shared.semester.model.SemesterCreateReq;
import com.green.muziuniv_be_notuser.shared.semester.model.SemesterRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SemesterService {
    private final SemesterRepository semesterRepository;
    private final SemesterMapper mapper;

    public SemesterRes create(SemesterCreateReq req) {
        if (semesterRepository.existsByYearAndSemester(req.getYear(), req.getSemester())) {
            throw new IllegalArgumentException("이미 존재하는 학기입니다.");
        }
        Semester entity = Semester.builder()
                .year(req.getYear())
                .semester(req.getSemester())
                .build();
        return mapper.toRes(semesterRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<SemesterRes> listAll() {
        return semesterRepository.findAllByOrderByYearDescSemesterDesc()
                .stream().map(mapper::toRes).toList();
    }

    @Transactional(readOnly = true)
    public SemesterRes getByYearAndSemester(Integer year, Integer semester) {
        Semester s = semesterRepository.findByYearAndSemester(year, semester)
                .orElseThrow(() -> new IllegalArgumentException("해당 학기가 없습니다."));
        return mapper.toRes(s);
    }

    @Transactional(readOnly = true)
    public SemesterRes getById(Integer semesterId) {
        Semester s = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new IllegalArgumentException("해당 학기가 없습니다."));
        return mapper.toRes(s);
    }

    @Transactional(readOnly = true)
    public SemesterRes getLatest() {
        Semester s = semesterRepository.findFirstByOrderByYearDescSemesterDesc()
                .orElseThrow(() -> new IllegalArgumentException("학기 데이터가 없습니다."));
        return mapper.toRes(s);
    }
}