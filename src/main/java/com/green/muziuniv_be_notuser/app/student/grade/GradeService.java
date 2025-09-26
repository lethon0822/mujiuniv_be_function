package com.green.muziuniv_be_notuser.app.student.grade;

import com.green.muziuniv_be_notuser.app.shared.schedule.ScheduleMapper;
import com.green.muziuniv_be_notuser.app.shared.schedule.ScheduleValidator;
import com.green.muziuniv_be_notuser.app.student.grade.model.GetAllPermanentGradeReq;
import com.green.muziuniv_be_notuser.app.student.grade.model.GetAllPermanentGradeRes;
import com.green.muziuniv_be_notuser.app.student.grade.model.GetMyCurrentGradeRes;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.openfeign.user.UserClient;
import com.green.muziuniv_be_notuser.openfeign.user.model.UserInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GradeService {
    private final GradeMapper gradeMapper;
    private final UserClient userClient;
    private final ScheduleValidator scheduleValidator;

    // 등급으로 평점 set 하는 함수
    private double convertRankToPoint(String rank) {
        if (rank == null) return 0.0;

        return switch (rank) {
            case "A+" -> 4.5;
            case "A" -> 4.0;
            case "B+" -> 3.5;
            case "B" -> 3.0;
            case "C+" -> 2.5;
            case "C" -> 2.0;
            case "D+" -> 1.5;
            case "D" -> 1.0;
            case "F" -> 0.0;
            default -> 0.0;
        };
    }

    // 영구 성적 조회
    public ResponseEntity<?> getAllPermanentGrade(long userId, GetAllPermanentGradeReq req) {
        List<GetAllPermanentGradeRes> res = gradeMapper.getAllPermanentGrade(userId, req);

        for (GetAllPermanentGradeRes item : res) {
            // res의 등급으로부터 평점 set
            item.setPoint(convertRankToPoint(item.getRank()));
            // res의 교수명 세팅을 위해 유저 서버랑 통신
            // req 세팅
            Map<String, List<Long>> request = Map.of("userId", List.of(item.getUserId()));
            // 유저 서버 호출
            ResultResponse<Map<Long, UserInfoDto>> response = userClient.getUserInfo(request);
            Map<Long, UserInfoDto> proGetResMap = response.getResult();

            // 기존의 강의 데이터에 교수 정보 주입
            for (GetAllPermanentGradeRes grade : res) {
                UserInfoDto userInfoDto = proGetResMap.get(grade.getUserId());
                if (userInfoDto != null) {
                    grade.setProfessorName(userInfoDto.getUserName());
                }

            }
        }
        return ResponseEntity.ok(new ResultResponse<>("영구 성적 조회 성공", res));
    }

    // 금학기 성적 조회
    public ResponseEntity<?> getMyCurrentGrades(Long userId, Long semesterId) {
        // 0. 성적 조회 기간 체크
        scheduleValidator.validateOpen(semesterId, "성적조회");

        // 성적 조회 로직 실행
        List<GetMyCurrentGradeRes> res = gradeMapper.getMyCurrentGrade(userId, semesterId);

        // res의 교수명 세팅을 위해 유저 서버랑 통신
        // 1. 교수 id 리스트 뽑기
        List<Long> professorIds = res.stream()
                .map(GetMyCurrentGradeRes::getUserId)
                .distinct()
                .toList();
        if (professorIds.isEmpty()) {
            return ResponseEntity.ok(new ResultResponse<>("금학기 성적 없음", res));
        }

        // 2. 유저 서버 호출
        Map<String, List<Long>> request = Map.of("userId", professorIds);
        ResultResponse<Map<Long, UserInfoDto>> response = userClient.getUserInfo(request);
        Map<Long, UserInfoDto> proGetResMap = response.getResult();


        // 4. 각 성적 DTO에 교수명 세팅 + point 세팅
        for (GetMyCurrentGradeRes item : res) {
            UserInfoDto userInfoDto = proGetResMap.get(item.getUserId());
            if (userInfoDto != null) {
                item.setProfessorName(userInfoDto.getUserName());
            }

            item.setPoint(convertRankToPoint(item.getRank()));
        }
        return ResponseEntity.ok(new ResultResponse<>("금학기 성적 조회 성공", res));
    }
}




