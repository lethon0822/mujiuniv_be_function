package com.green.muziuniv_be_notuser.app.professor.coursemanage;



import com.green.muziuniv_be_notuser.app.professor.coursemanage.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProfessorMapper {

    int saveCourse(ProfessorPostReq req);

    // 이번 년도 내 강의 보기
    List<ProfessorGetRes> findByUserId(ProfessorGetReq req);

    // 강의 계획서 수정
    int modify(ProfessorPutReq req);

    List<CourseStudentGetRes> findStudentsByUserId(int courseId);


    //학과조회
    String findDeptNameByUserId(int userId);

    //강의평가 조회
    List<ProfessorSurveyCheckRes> findSurveyByCourseId(int courseId);
    //자기강의만 삭제
    int deleteCourse(@Param("courseId") int courseId, @Param("userId") Long userId);

    //강의평가 학생용
    //int updateReview(LecturesEvaluationDto dto);

    //수강 상태 변화
    //int updateEnrollStatus(EnrollStatusReq req);
}
