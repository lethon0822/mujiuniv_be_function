package com.green.muziuniv_be_notuser.app.professor.course;

import com.green.muziuniv_be_notuser.openfeign.course.CourseUserClient;
import com.green.muziuniv_be_notuser.app.professor.course.model.CourseStudentRes;
import com.green.muziuniv_be_notuser.app.professor.course.model.UserResDto;
import com.green.muziuniv_be_notuser.entity.enrollment.Enrollment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessorCourseService {

    private final ProfessorCourseRepository professorCourseRepository;
    private final CourseUserClient courseUserClient;

    public List<CourseStudentRes> getCourseStudents(Long courseId) {
        // 1. 수강신청 엔티티 조회
        List<Enrollment> enrollments = professorCourseRepository.findByCourse_CourseId(courseId);

        // 2. userId 추출
        List<Long> userIds = enrollments.stream()
                .map(Enrollment::getUserId)
                .collect(Collectors.toList());

        // 3. user-service 호출 → UserResDto 리스트
        List<UserResDto> users = courseUserClient.getUsersByIds(userIds);

        // 4. userId → UserResDto 맵핑
        Map<Long, UserResDto> userMap = users.stream()
                .collect(Collectors.toMap(UserResDto::getUserId, u -> u));

        // 5. 응답 조합
        return enrollments.stream()
                .map(e -> {
                    UserResDto user = userMap.get(e.getUserId());
                    return new CourseStudentRes(
                            e.getEnrollmentId(),   // 수강신청 PK
                            e.getUserId(),         // 학생 PK
                            user.getLoginId(),     // 로그인 ID
                            user.getUserName(),    // 이름
                            user.getGradeYear(),   // 학년
                            user.getDeptName()     // 학과명
                    );
                })
                .collect(Collectors.toList());

    }
}