package com.green.muziuniv_be_notuser.app.professor.coursemanage;

import com.green.muziuniv_be_notuser.app.professor.coursemanage.model.ProfessorPostReq;
import com.green.muziuniv_be_notuser.entity.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Course,Long> {
}
