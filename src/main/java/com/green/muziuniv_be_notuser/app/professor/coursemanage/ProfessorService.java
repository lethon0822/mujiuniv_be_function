package com.green.muziuniv_be_notuser.app.professor.coursemanage;



import com.green.muziuniv_be_notuser.app.professor.coursemanage.model.*;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.openfeign.user.UserClient;
import com.green.muziuniv_be_notuser.openfeign.user.model.ProGetRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfessorService {
    private final ProfessorMapper professorMapper;
    private final UserClient userClient;

    public int saveCourse(ProfessorPostReq req) {
        //Random random = new Random();
        ResultResponse<String> response = userClient.getProDeptCode(req.getUserId());
        String courseCode = response.getResult();
//        for(int i = 0; i<3; i++){
//            courseCode += (char)(random.nextInt(26)+65);
//        }
        for(int i = 0; i <4; i++){
            courseCode += (int)(Math.random()*10);
        }
        req.setCourseCode(courseCode);

        return professorMapper.saveCourse(req);
    }

    // 내 강의 목록 조회
    public List<ProfessorGetRes> findMyCourse(ProfessorGetReq req) {
        // 1. notuser DB에서 강의 목록 가져오기
        List<ProfessorGetRes> courses = professorMapper.findByUserId(req);

        if (courses.isEmpty()) return courses;

        // 2. 교수 학과 가져오기 (단일)
        ResultResponse<String> deptRes = userClient.getProDeptCode(req.getUserId());
        String deptName = deptRes.getResult();

        // 3. 각 강의 DTO에 학과명 채워넣기
        for (ProfessorGetRes course : courses) {
            course.setDeptName(deptName);
        }

        return courses;
    }

    // 강의 계획서 수정
    public int modify(ProfessorPutReq req) {
        return professorMapper.modify(req);
    }

    // 강의별 학생 리스트 조회
    public List<CourseStudentGetRes> courseStudentList(int courseId) {
        // 1. notuser DB에서 enrollment(userId, enrollmentId만) 가져오기
        List<CourseStudentGetRes> enrollments = professorMapper.findStudentsByUserId(courseId);

        if (enrollments.isEmpty()) return List.of();

        // 2. user 서비스에 userId 리스트 전달해서 이름, 학과 정보 조회
        Map<String, List<Long>> req = Map.of(
                "userId", enrollments.stream()
                        .map(e -> (long) e.getUserId())
                        .toList()
        );

        ResultResponse<List<ProGetRes>> response = userClient.getProInfo(req);
        List<ProGetRes> users = response.getResult();

        // 3. userClient에서 받아온 데이터 merge
        for (CourseStudentGetRes e : enrollments) {
            ProGetRes user = users.stream()
                    .filter(u -> u.getUserId().equals((long) e.getUserId()))
                    .findFirst()
                    .orElse(null);

            if (user != null) {
                e.setUserName(user.getUserName());
                e.setDeptName(user.getDeptName());
            }
        }

        return enrollments;
    }
    //학과 조회
    public String deptName(int userId){
        return professorMapper.findDeptNameByUserId(userId);
    }

    //강의평가 조회
    public List<ProfessorSurveyCheckRes> checkSurvey(int courseId){
        return professorMapper.findSurveyByCourseId(courseId);
    }
    // 자기강의만 삭제
    public int deleteCourse(int courseId, Long userId) {
        int result = professorMapper.deleteCourse(courseId, userId);
        if (result == 0) {
            throw new RuntimeException("삭제할 권한이 없거나 강의가 존재하지 않습니다.");
        }
        return result;
    }

}