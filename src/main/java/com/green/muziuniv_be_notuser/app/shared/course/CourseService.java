package com.green.muziuniv_be_notuser.app.shared.course;


import com.green.muziuniv_be_notuser.app.shared.course.model.*;
import com.green.muziuniv_be_notuser.app.staff.approval.model.CoursePendingRes;
import com.green.muziuniv_be_notuser.app.student.enrollment.model.EnrollmentFilterRes;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.entity.course.Course;
import com.green.muziuniv_be_notuser.openfeign.department.DepartmentClient;
import com.green.muziuniv_be_notuser.openfeign.department.model.DepartmentHeadNameRes;

import com.green.muziuniv_be_notuser.openfeign.user.UserClient;
import com.green.muziuniv_be_notuser.openfeign.user.model.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseMapper courseMapper;
    private final CourseRepository courseRepository;
    private final UserClient userClient;
    private final DepartmentClient departmentClient;

    // 서치필터바에 따른 강의 리스트 조회
    public List<CourseFilterRes> findCoursesByFilter(CourseFilterReq req) {

        // req에 학과 조건이 있는 경우
        if (req.getDeptId() != null) {
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

        // req에 따른 courseList 조회
        List<CourseFilterRes> courseList = courseMapper.findCoursesByFilter(req);
        // courseList가 비었다면 유저 서버 호출 하지 않고 빈 리스트 반환
        if (courseList.isEmpty()) {
            return Collections.emptyList();
        }


        // 강의 리스트에서 교수 Id (userId)만 추출
        List<Long> professorIds = courseList.stream()
                .map(course -> course.getUserId())
                .distinct()
                .collect(Collectors.toList()); // stream을 다시 list로 바꿈.

        // 요청용 Map 생성
        Map<String, List<Long>> request = new HashMap<>();
        request.put("userId", professorIds);

        // 유저 서버 호출
        ResultResponse<Map<Long, UserInfoDto>> proInfo = userClient.getUserInfo(request);
        Map<Long, UserInfoDto> proGetResMap = proInfo.getResult();

        // 기존의 강의 데이터에 교수, 학과 정보 주입
        for (CourseFilterRes course : courseList) {
            UserInfoDto userInfoDto = proGetResMap.get(course.getUserId());
            if (userInfoDto != null) {
                course.setProfessorName(userInfoDto.getUserName());

                if (course.getGrade() == 0 || course.getType().contains("교양")) {  // 학년이 0이거나 타입에 교양이 포함되면 학과를 교양학부로
                    course.setDeptName("교양학부");
                } else {
                    course.setDeptName(userInfoDto.getDeptName());
                }

            }
        }

        return courseList;
    }


    //강의계획서 조회
    public CourseDetailRes findCourseDetail(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "courseId가 존재하지 않습니다."));

        List<Long> list = Arrays.asList(course.getUserId().getUserId());
        Map<String, List<Long>> request = new HashMap<>();
        request.put("userId", list);

        ResultResponse<Map<Long, UserInfoDto>> proInfo = userClient.getUserInfo(request);
        Map<Long, UserInfoDto> proMap = proInfo.getResult();

        CourseDetailRes result = CourseDetailRes.builder()
                .courseId(course.getCourseId())
                .classroom(course.getClassroom())
                .type(course.getType())
                .semester(course.getSemesterId().getSemester())
                .time(course.getTime())
                .title(course.getTitle())
                .credit(course.getCredit())
                .weekPlan(course.getWeekPlan())
                .textBook(course.getTextBook())
                .goal(course.getGoal())
                .maxStd(course.getMaxStd())
                .userName(proMap.get(course.getUserId().getUserId()).getUserName())
                .deptName(proMap.get(course.getUserId().getUserId()).getDeptName())
                .grade(course.getGrade())
                .build();
        return result;
    }

    // 오늘의 강의 조회(학생용)
    public List<TodayCourseStuRes> todayCourseStu(TodayCourseReq req) {
        List<TodayCourseStuRes> courseList = courseMapper.findTodayCourse(req);
        if (courseList.isEmpty()) {
            return null;
        }
        Set<Long> userList = courseList.stream()
                .map(c -> c.getUserId())
                .collect(Collectors.toSet());

        // map으로 변환 변환시 List로 바꿔서 넣음
        Map<String, List<Long>> request = new HashMap<>();
        request.put("userId", new ArrayList<>(userList));

        //통신
        ResultResponse<Map<Long, UserInfoDto>> proInfo = userClient.getUserInfo(request);
        Map<Long, UserInfoDto> proGetResMap = proInfo.getResult();


        for (TodayCourseStuRes course : courseList) {
            UserInfoDto userInfoDto = proGetResMap.get(course.getUserId());
            if (userInfoDto != null) {
                course.setUserName(userInfoDto.getUserName());
            }
        }
        return courseList;
    }

}




