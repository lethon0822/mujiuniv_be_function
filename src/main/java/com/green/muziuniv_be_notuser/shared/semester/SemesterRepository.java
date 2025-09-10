package com.green.muziuniv_be_notuser.shared.semester;

import com.green.muziuniv_be_notuser.entity.semester.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SemesterRepository extends JpaRepository<Semester, Integer> {

    // (year, semester) 한 쌍으로 조회/중복체크
    Optional<Semester> findByYearAndSemester(Integer year, Integer semester);
    boolean existsByYearAndSemester(Integer year, Integer semester);

    // 최신 학기 정렬/조회
    List<Semester> findAllByOrderByYearDescSemesterDesc();
    Optional<Semester> findFirstByOrderByYearDescSemesterDesc();
}