package com.green.muziuniv_be_notuser.app.shared.course;


import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.entity.course.Course;
import com.green.muziuniv_be_notuser.openfeign.user.model.ProGetRes;
import com.green.muziuniv_be_notuser.openfeign.user.UserClient;
import com.green.muziuniv_be_notuser.app.shared.course.model.CourseDetailRes;
import com.green.muziuniv_be_notuser.app.shared.course.model.CourseFilterReq;
import com.green.muziuniv_be_notuser.app.shared.course.model.CourseFilterRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseMapper courseMapper;
    private final CourseRepository courseRepository;
    private final UserClient userClient;

    public List<CourseFilterRes> findCoursesByFilter(CourseFilterReq req){
        // 서리필터바에 따른 강의 리스트 조회
       List<CourseFilterRes> courseList = courseMapper.findCoursesByFilter(req);

       // 강의 리스트에서 교수 Id (userId)만 추출
        List<Long> professorIds = courseList.stream()
                .map(course -> course.getUserId())
                .distinct()
                .collect(Collectors.toList()); //stream을 다시 list로 바꿈.

        // 요청용 Map 생성
        Map<String, List<Long>> request = new HashMap<>();
        request.put("userId", professorIds);

        // 유저 서버 호출
        ResultResponse<List<ProGetRes>> response = userClient.getProInfo(request);
        List<ProGetRes> professorsInfos = response.getResult();

        Map<Long, ProGetRes> proGetResMap = professorsInfos.stream()
                                            .collect(Collectors.toMap(professor -> professor.getUserId(), professor -> professor));

        // 기존의 강의 데이터에 교수, 학과 정보 주입
        for (CourseFilterRes course : courseList) {
            ProGetRes proGetRes = proGetResMap.get(course.getUserId());
            if (proGetRes != null) {
                course.setProfessorName(proGetRes.getUserName());
                course.setDeptName(proGetRes.getDeptName());
            }

        }
        return courseList;
    }

    //강의계획서 조회
    public CourseDetailRes findCourseDetail(Long courseId){
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "courseId가 존재하지 않습니다."));

        List<Long> list = Arrays.asList(course.getUserId().getUserId());
        Map<String, List<Long>> request = new HashMap<>();
        request.put("userId", list);

        ResultResponse<List<ProGetRes>> proInfo = userClient.getProInfo(request);
        List<ProGetRes> professorsInfo = proInfo.getResult();

        Map<Long, ProGetRes> proGetResMap = professorsInfo.stream()
                .collect(Collectors.toMap(professor -> professor.getUserId(), professor -> professor));

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
                .userName(proGetResMap.get(course.getUserId().getUserId()).getUserName())
                .deptName(proGetResMap.get(course.getUserId().getUserId()).getDeptName())
                .grade(course.getGrade())
                .build();
        return result;
    }




}
