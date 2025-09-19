package com.green.muziuniv_be_notuser.app.staff;

import com.green.muziuniv_be_notuser.entity.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffCourseRepository extends JpaRepository<Course,Long> {
    List<Course> findByStatus(String status);
}
