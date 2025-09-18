package com.green.muziuniv_be_notuser.app.student.graduation;

import com.green.muziuniv_be_notuser.app.student.graduation.model.GraduationCheckDTO;
import com.green.muziuniv_be_notuser.app.student.graduation.model.GraduationCheckRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GraduationService {

    private final GraduationRepository graduationRepository;

    public GraduationCheckRes getMyGraduationDiagnosis(Long userId){
        List<GraduationCheckDTO> creditList =graduationRepository.getMyGraduationDiagnosis(userId);

        GraduationCheckRes res = new GraduationCheckRes();

        int majorRequired = 0;
        int majorElective = 0;
        int generalRequired = 0;
        int generalElective = 0;

        // dto 결과 (각 이수 타입별 현재 취득 현황) res에 세팅
        for(GraduationCheckDTO dto: creditList){
            switch(dto.getType()){
                case "전공필수" -> majorRequired = dto.getTotalCredits().intValue();
                case "전공선택" -> majorElective = dto.getTotalCredits().intValue();
                case "교양필수" -> generalRequired = dto.getTotalCredits().intValue();
                case "교양선택" -> generalElective = dto.getTotalCredits().intValue();
            }
        }

        res.setMajorRequiredCredits(majorRequired);
        res.setMajorElectiveCredits(majorElective);
        res.setGeneralRequiredCredits(generalRequired);
        res.setGeneralElectiveCredits(generalElective);

        // 졸업 요건 기준
        int reqMajorRequired = 36; // 전공 필수
        int reqMajorElective = 34; // 전공 선택
        int reqGeneralRequired = 15; // 교양 필수
        int reqGeneralElective = 15; // 교양 선택
        // + reqFree = 30; 나머지 일반선택 30학점인데 결국 위 네 가지 항목에 포함.
        int reqTotal = 130; // 총 학점 130

        // 각 항목별 부족 학점 계산 ( 부족 학점이 음수 (= 졸업 요건보다 강의를 더 들은 경우)일 땐 0 세팅 )
        int shortageMajorRequired = Math.max(0, reqMajorRequired - majorRequired);
        int shortageMajorElective = Math.max(0,reqMajorElective - majorElective);
        int shortageGeneralRequired = Math.max(0,reqGeneralRequired - generalRequired);
        int shortageGeneralElective = Math.max(0,reqGeneralElective - generalElective);

        res.setShortageMajorRequiredCredits(shortageMajorRequired);
        res.setShortageMajorElectiveCredits(shortageMajorElective);
        res.setShortageGeneralRequiredCredits(shortageGeneralRequired);
        res.setShortageGeneralElectiveCredits(shortageGeneralElective);

        // 총 취득 학점
        int completedTotal = majorRequired + majorElective + generalRequired + generalElective;

        // 총 부족 학점
        int shortageTotal = Math.max(0, reqTotal - completedTotal);

        res.setTotalCompletedCredits(completedTotal);
        res.setTotalShortageCredits(shortageTotal);

        // 졸업 가능 여부
        boolean graduationPossible = completedTotal >= reqTotal
                && shortageMajorRequired == 0
                && shortageMajorElective == 0
                && shortageGeneralRequired == 0
                && shortageGeneralElective == 0
                && shortageTotal == 0;

        String graduationResult = graduationPossible ? "졸업 요건 달성" : "졸업 요건 미달";
        res.setGraduationResult(graduationResult);

        // 상세 메세지
        List<String> messages = new ArrayList<>();

        // 필수 요건 부족 안내
        if(shortageMajorRequired > 0){
            messages.add("전공필수 " + shortageMajorRequired + "학점");
        }
        if (shortageMajorElective > 0){
            messages.add("전공선택 " + shortageMajorElective + "학점");
        }
        if (shortageGeneralRequired > 0){
            messages.add("교양필수 " + shortageGeneralRequired + "학점");
        }
        if (shortageGeneralElective > 0){
            messages.add("교양선택 " + shortageGeneralElective + "학점");
        }

        // 나머지 일반 선택 30 처리
        if (shortageTotal > 0) {
            // 필수 요건이 다 충족된 경우, 일반선택 부족
            if (shortageMajorRequired == 0 && shortageMajorElective == 0
                    && shortageGeneralRequired == 0 && shortageGeneralElective == 0) {
                messages.add("일반선택 " + shortageTotal + "학점");
            }
            // 필수 요건도 부족한 경우,총 학점 기준에서 추가로 필요한 학점 안내
            else {
                int extraNeeded = shortageTotal
                        - (shortageMajorRequired + shortageMajorElective
                        + shortageGeneralRequired + shortageGeneralElective);

                if (extraNeeded > 0) {
                    messages.add("일반선택 " + extraNeeded + "학점");
                }
            }
        }

        String details;
        if (messages.isEmpty()) {
            details = "축하합니다. 졸업 요건을 모두 충족하였습니다.";
        } else {
            details = String.join(", ", messages) + " 부족";
        }

        res.setDetails(details);

        return res;
    }
}
