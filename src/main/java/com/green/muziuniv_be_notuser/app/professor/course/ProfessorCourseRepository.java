package com.green.muziuniv_be_notuser.app.professor.course;

import com.green.muziuniv_be_notuser.entity.enrollment.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessorCourseRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByCourse_CourseId(Long courseId);
}
