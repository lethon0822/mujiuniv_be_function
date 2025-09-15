package com.green.muziuniv_be_notuser.shared.course;

import com.green.muziuniv_be_notuser.entity.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course,Long> {

    // 일단 보류
    //List<Course> findByStatus(String status); // 승인된 강의만
}
