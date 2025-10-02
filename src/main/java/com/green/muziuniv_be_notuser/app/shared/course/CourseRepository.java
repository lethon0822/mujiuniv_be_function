package com.green.muziuniv_be_notuser.app.shared.course;

import com.green.muziuniv_be_notuser.entity.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {
    List<Course> findByStatus(String status);
}