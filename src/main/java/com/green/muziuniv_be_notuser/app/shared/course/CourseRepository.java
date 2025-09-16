package com.green.muziuniv_be_notuser.app.shared.course;

import com.green.muziuniv_be_notuser.entity.course.Course;
import com.green.muziuniv_be_notuser.app.student.enrollment.model.EnrollmentRes;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CourseRepository extends JpaRepository<Course,Long> {
    EnrollmentRes findCourseByCourseId(Long courseId);
}
