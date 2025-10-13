package com.green.muziuniv_be_notuser.app.staff.approval;

import com.green.muziuniv_be_notuser.app.shared.course.CourseRepository;
import com.green.muziuniv_be_notuser.app.staff.approval.model.*;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.entity.course.Course;
import com.green.muziuniv_be_notuser.openfeign.user.UserClient;
import com.green.muziuniv_be_notuser.openfeign.user.model.UserInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovalService {

    private final ApprovalMapper approvalMapper;
    private final UserClient userClient;
    private final CourseRepository courseRepository;

    // ==========================================================
    // 📘 1. 학적·인사 신청 목록 조회
    // ==========================================================
    @Transactional(readOnly = true)
    public List<ApprovalAppGetRes> getApplications(ApprovalAppGetReq req) {
        List<ApprovalAppGetRes> apps = approvalMapper.selectApplications(req);
        if (apps.isEmpty()) return apps;

        // ✅ userId 목록 추출
        List<Long> userIds = apps.stream()
                .map(ApprovalAppGetRes::getUserId)
                .distinct()
                .toList();

        // ✅ 유저 정보 요청 (Feign)
        Map<String, List<Long>> request = Map.of("userId", userIds);
        ResultResponse<Map<Long, UserInfoDto>> response = userClient.getUserInfo(request);

        Map<Long, UserInfoDto> userInfoMap =
                response != null && response.getResult() != null
                        ? response.getResult() : Map.of();

        // ✅ 이름 / 학과 매핑
        apps.forEach(app -> {
            UserInfoDto info = userInfoMap.get(app.getUserId());
            if (info != null) {
                app.setUserName(info.getUserName());
                app.setDeptName(info.getDeptName());
            }
        });

        return apps;
    }

    // ==========================================================
    // 📘 2. 신청 승인 / 거부 처리
    // ==========================================================
    @Transactional
    public String decideApplication(ApprovalPatchReq req) {
        // 1️⃣ 신청 상태 업데이트
        int updated = approvalMapper.updateApplicationStatus(req.getAppId(), req.getStatus());
        if (updated != 1) {
            throw new RuntimeException("신청서 상태 업데이트 실패");
        }

        // 2️⃣ 승인 처리인 경우
        if ("승인".equals(req.getStatus())) {
            // 이제 logicalType은 없음 → scheduleType만 사용
            String type = req.getScheduleType();

            // 3️⃣ scheduleType으로 유저 상태 결정
            Integer newStatus = switch (type) {
                case "휴학신청", "휴직신청" -> 0; // 상태값 0 = 휴학/휴직 중
                case "복학신청", "복직신청" -> 1; // 상태값 1 = 재학/재직 중
                default -> throw new RuntimeException("알 수 없는 신청 유형: " + type);
            };

            log.info("[승인 처리] appId={}, userId={}, scheduleType={}, newStatus={}",
                    req.getAppId(), req.getUserId(), type, newStatus);

            try {
                userClient.updateStatus(req.getUserId(), newStatus);
            } catch (Exception e) {
                log.error("유저 상태 업데이트 실패 → userId={}, status={}", req.getUserId(), newStatus, e);
                throw new RuntimeException("유저 상태 업데이트 중 오류 발생", e);
            }

            return type + " 승인 완료";
        }

        // 3️⃣ 거부 처리인 경우
        if ("거부".equals(req.getStatus())) {
            log.info("[거부 처리] appId={}, userId={}, scheduleType={}", req.getAppId(), req.getUserId(), req.getScheduleType());
            return req.getScheduleType() + " 거부 완료";
        }

        // 4️⃣ 기타 잘못된 상태
        return "알 수 없는 처리 상태";
    }

    // ==========================================================
    // 📘 3. 강의 승인 관련 (교수 전용)
    // ==========================================================
    @Transactional(readOnly = true)
    public List<CoursePendingRes> getPendingCourses(CoursePendingReq req) {
        List<CoursePendingRes> courseList = approvalMapper.findCoursesByStatus(req);
        if (courseList.isEmpty()) return courseList;

        // 교수 정보 조회
        Set<Long> userIds = courseList.stream()
                .map(CoursePendingRes::getUserId)
                .collect(Collectors.toSet());

        Map<String, List<Long>> request = Map.of("userId", new ArrayList<>(userIds));
        ResultResponse<Map<Long, UserInfoDto>> response = userClient.getUserInfo(request);

        Map<Long, UserInfoDto> userMap =
                response != null && response.getResult() != null
                        ? response.getResult() : Map.of();

        // 교수명, 학과명 매핑
        courseList.forEach(course -> {
            UserInfoDto info = userMap.get(course.getUserId());
            if (info != null) {
                course.setProfessorName(info.getUserName());
                course.setDeptName(course.getGrade() == 0 ? "교양학부" : info.getDeptName());
            }
        });

        return courseList;
    }

    // ==========================================================
    // 📘 4. 강의 승인 상태 변경
    // ==========================================================
    @Transactional
    public ApprovalCoursePatchRes updateCourseStatus(ApprovalCoursePatchReq req) {
        Course course = courseRepository.findById(req.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("해당 강의가 존재하지 않습니다."));

        course.setStatus(req.getStatus());
        courseRepository.save(course);

        log.info("[강의 승인] courseId={}, status={}", course.getCourseId(), course.getStatus());
        return new ApprovalCoursePatchRes(course.getCourseId(), course.getStatus());
    }
}
