package com.green.muziuniv_be_notuser.app.staff.approval;

import com.green.muziuniv_be_notuser.app.shared.course.CourseRepository;
import com.green.muziuniv_be_notuser.app.shared.course.model.CourseFilterRes;
import com.green.muziuniv_be_notuser.app.staff.approval.model.*;
import com.green.muziuniv_be_notuser.app.staff.approval.model.ApprovalCoursePatchReq;
import com.green.muziuniv_be_notuser.app.staff.approval.model.ApprovalCoursePatchRes;
import com.green.muziuniv_be_notuser.app.staff.approval.model.CoursePendingRes;
import com.green.muziuniv_be_notuser.entity.course.Course;
import com.green.muziuniv_be_notuser.openfeign.department.DepartmentClient;
import com.green.muziuniv_be_notuser.openfeign.department.model.DepartmentHeadNameRes;
import com.green.muziuniv_be_notuser.openfeign.user.UserClient;
import com.green.muziuniv_be_notuser.openfeign.user.model.UserInfoDto;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
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
    private final DepartmentClient departmentClient;

    /**
     * 신청 목록 조회
     */
    @Transactional(readOnly = true)
    public List<ApprovalAppGetRes> getApplications(ApprovalAppGetReq req) {
        // 1) notuser DB에서 application 목록 가져오기
        List<ApprovalAppGetRes> apps = approvalMapper.selectApplications(req);
        if (apps.isEmpty()) {
            return apps;
        }

        // 2) userId 리스트 뽑아서 user-service 호출
        List<Long> userIds = apps.stream()
                .map(ApprovalAppGetRes::getUserId)
                .distinct()
                .toList();

        // ✅ key 이름 "userId" 로 맞추기
        Map<String, List<Long>> request = Map.of("userId", userIds);
        ResultResponse<Map<Long, UserInfoDto>> response = userClient.getUserInfo(request);

        // ✅ null-safe
        Map<Long, UserInfoDto> userInfoMap =
                response != null && response.getResult() != null
                        ? response.getResult()
                        : Map.of();

        // 3) userName, deptName 매핑
        apps.forEach(app -> {
            UserInfoDto info = userInfoMap.get(app.getUserId());
            if (info != null) {
                app.setUserName(info.getUserName());
                app.setDeptName(info.getDeptName());

            }
        });

        return apps;
    }

    /**
     * 신청 승인/거부 처리
     */
    @Transactional
    public String decideApplication(ApprovalPatchReq req) {
        // 1. application 상태 업데이트
        int updated = approvalMapper.updateApplicationStatus(req.getAppId(), req.getStatus());
        if (updated != 1) {
            throw new RuntimeException("신청서 상태 업데이트 실패");
        }

        // 2. 승인일 때 user-service 학적/재직 상태 변경
        if ("승인".equals(req.getStatus())) {
            Integer newStatus = switch (req.getScheduleType()) {
                case "휴학신청","휴직신청" -> 0;
                case "복학신청","복직신청" -> 1;

                default -> throw new RuntimeException("알 수 없는 scheduleType");
            };

            userClient.updateStatus(req.getUserId(), newStatus);
            return "승인 완료";
        }

        // 3. 거부 처리
        if ("거부".equals(req.getStatus())) {
            return "거부 처리 완료";
        }

        return "알 수 없는 처리 상태";
    }

    // 강의 신청 처리 관련------------------------

    // 처리중 강의만 DTO 변환해서 반환
    public List<CoursePendingRes> getPendingCourses(CoursePendingReq req) {

        if (req.getDeptId() != null) { // 수연아 고마워
            // 1. 유저 서비스에 호출해서 해당 학과의 교수 리스트 가져오기
            ResultResponse<List<DepartmentHeadNameRes>> deptProfessors = departmentClient.findDeptHeadList(req.getDeptId());
            // 2. 교수 userId 리스트만 추출
            List<Long> professorIds = deptProfessors.getResult().stream()
                    .map(d -> d.getUserId())
                    .collect(Collectors.toList());
            // 학과 아이디로 교수 리스트를 가져왔는데 그 교수들이 개설한 강의가 없는 경우 -> 매퍼에서 if 조건문이 빠지면서 조회 결과가
            // 이상해지므로 빈 리스트 반환
            if (professorIds.isEmpty()) {
                return Collections.emptyList();
            }
            // 3. 요청 객체에 교수 리스트 세팅 (courseMapper에서 in 조건으로 사용)
            req.setProfessorIds(professorIds);
        }

        List<CoursePendingRes> courseList = approvalMapper.findCoursesByStatus(req);
        if(courseList.isEmpty()){
            return null;
        }
         //중복 제거
        Set<Long> userList = courseList.stream()
                .map(c -> c.getUserId())
                .collect(Collectors.toSet());

         // map으로 변환 변환시 List로 바꿔서 넣음
        Map<String, List<Long>> request = new HashMap<>();
        request.put("userId", new ArrayList<>(userList));

         //통신
        ResultResponse<Map<Long, UserInfoDto>> proInfo = userClient.getUserInfo(request);
        Map<Long, UserInfoDto> proGetResMap = proInfo.getResult();

        for (CoursePendingRes course : courseList) {
            UserInfoDto userInfoDto = proGetResMap.get(course.getUserId());
            if (userInfoDto != null) {
                course.setProfessorName(userInfoDto.getUserName());

                if (course.getGrade() == 0 || course.getType().contains("교양") ) {  // 학년이 0이거나 타입에 교양이 포함되면 학과를 교양학부로
                    course.setDeptName("교양학부");
                } else {
                    course.setDeptName(userInfoDto.getDeptName());
                }

            }
        }
        return courseList;
    }

    /**
     * 강의 승인/거부 처리
     */
    public ApprovalCoursePatchRes updateCourseStatus(ApprovalCoursePatchReq req) {
        Course course = courseRepository.findById(req.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("해당 강의가 존재하지 않습니다."));

        // 상태 업데이트 (승인/거부)
        course.setStatus(req.getStatus());
        courseRepository.save(course);

        return new ApprovalCoursePatchRes(course.getCourseId(), course.getStatus());
    }

    // 처리중인 신청 건(강의개설, 휴복직, 휴복학)카운트
    public CountApproval countAppWork(int sid){
        int course = approvalMapper.countCourse(sid);
        int app = approvalMapper.countApproval(sid);
        CountApproval result = new CountApproval(course, app);
        return result;
    }
}