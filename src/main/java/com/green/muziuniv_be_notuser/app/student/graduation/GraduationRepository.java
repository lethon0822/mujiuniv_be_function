package com.green.muziuniv_be_notuser.app.student.graduation;

import com.green.muziuniv_be_notuser.app.student.graduation.model.GraduationCheckDTO;
import com.green.muziuniv_be_notuser.entity.enrollment.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface GraduationRepository extends JpaRepository<Enrollment, Long> {

    @Query("""
        SELECT new com.green.muziuniv_be_notuser.app.student.graduation.model.GraduationCheckDTO(c.type, SUM(c.credit))
        FROM Enrollment e
        JOIN e.course c
        WHERE e.userId= :userId
        AND e.status = '수강완료'
        GROUP BY c.type
    """)
    List<GraduationCheckDTO> getMyGraduationDiagnosis(Long userId);
}
