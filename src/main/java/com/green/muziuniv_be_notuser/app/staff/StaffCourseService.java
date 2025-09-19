package com.green.muziuniv_be_notuser.app.staff;

import com.green.muziuniv_be_notuser.app.staff.model.ApprovalCoursePatchReq;
import com.green.muziuniv_be_notuser.app.staff.model.ApprovalCoursePatchRes;
import com.green.muziuniv_be_notuser.app.staff.model.CoursePendingRes;
import com.green.muziuniv_be_notuser.entity.course.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffCourseService {
    private final StaffCourseRepository staffCourseRepository;

    // 처리중 강의만 DTO 변환해서 반환
    public List<CoursePendingRes> getPendingCourses() {
        return staffCourseRepository.findByStatus("처리중")
                .stream()
                .map(c -> new CoursePendingRes(
                        c.getCourseId(),
                        c.getTitle(),
                        c.getClassroom(),
                        c.getStatus()
                ))
                .collect(Collectors.toList());
    }

    /**
     * 강의 승인/거부 처리
     */
    public ApprovalCoursePatchRes updateCourseStatus(ApprovalCoursePatchReq req) {
        Course course = staffCourseRepository.findById(req.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("해당 강의가 존재하지 않습니다."));

        // 상태 업데이트 (승인/거부)
        course.setStatus(req.getStatus());
        staffCourseRepository.save(course);

        return new ApprovalCoursePatchRes(course.getCourseId(), course.getStatus());
    }
}

