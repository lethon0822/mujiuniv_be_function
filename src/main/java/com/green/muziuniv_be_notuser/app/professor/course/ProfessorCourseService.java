package com.green.muziuniv_be_notuser.app.professor.course;

import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.openfeign.course.CourseUserClient;
import com.green.muziuniv_be_notuser.app.professor.course.model.CourseStudentRes;
import com.green.muziuniv_be_notuser.openfeign.course.model.UserResDto;
import com.green.muziuniv_be_notuser.entity.enrollment.Enrollment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessorCourseService {

    private final ProfessorCourseRepository professorCourseRepository;
    private final CourseUserClient courseUserClient;

    // 강의별 학생 리스트 조회
    public List<CourseStudentRes> getCourseStudents(Long courseId) {
        // 1. Enrollment 에서 학생 userId 추출
        List<Enrollment> enrollments = professorCourseRepository.findByCourse_CourseId(courseId);
        List<Long> userIds = enrollments.stream()
                .map(Enrollment::getUserId)
                .toList();

        if (userIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. User-service 호출
        Map<String, List<Long>> request = new HashMap<>();
        request.put("userId", userIds);

        ResultResponse<List<UserResDto>> response = courseUserClient.getUsersByIds(request);
        List<UserResDto> users = response.getResult();

        // 3. userId -> UserResDto 매핑
        Map<Long, UserResDto> userMap = users.stream()
                .collect(Collectors.toMap(UserResDto::getUserId, u -> u));

        // 4. 응답 조합
        return enrollments.stream()
                .map(e -> {
                    UserResDto user = userMap.get(e.getUserId());
                    return new CourseStudentRes(
                            e.getEnrollmentId(),
                            e.getUserId(),
                            user != null ? user.getLoginId() : null,
                            user != null ? user.getUserName() : null,
                            user != null ? user.getGradeYear() : 0,
                            user != null ? user.getDeptName() : null
                    );
                })
                .toList();
    }
}