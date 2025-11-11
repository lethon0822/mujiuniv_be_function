package com.green.muziuniv_be_notuser.app.student.gpa;

import com.green.muziuniv_be_notuser.app.student.gpa.model.GpaRes;
import com.green.muziuniv_be_notuser.app.student.gpa.model.GpaRowDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GpaService {
    private final GpaMapper gpaMapper;

    public List<GpaRes> getMyGpa(Long userId, Long semesterId) {

        // 1) DB에서 해당하는 성적 SELECT
        List<GpaRowDTO> rows = gpaMapper.findAllGradesForGpa(userId, semesterId);

        // 2) 학기별 그룹핑
        Map<Long, List<GpaRowDTO>> grouped = rows.stream().collect(Collectors.groupingBy(GpaRowDTO::getSemesterId));

        // 3) GPA 계산
        return grouped.entrySet().stream()
                .map(entry -> {
                    Long semId = entry.getKey();
                    List<GpaRowDTO> list = entry.getValue();

                    double totalScore = 0;
                    double totalCredit = 0;
                    double majorScore = 0;
                    double majorCredit = 0;

                    for (GpaRowDTO row : list) {
                        double score = convertRank(row.getRank());
                        int credit = row.getCredit();

                        totalScore += score * credit;
                        totalCredit += credit;

                        if ("전공필수".equals(row.getType()) || "전공선택".equals(row.getType())) {
                            majorScore += score * credit;
                            majorCredit += credit;
                        }
                    }

                    double gpa = round(totalScore / totalCredit);
                    double majorGpa = (majorCredit == 0) ? 0.0 : round(majorScore / majorCredit);

                    return GpaRes.builder()
                            .semesterId(semId)
                            .year(list.get(0).getYear())
                            .semester(list.get(0).getSemester())
                            .totalCredit((long) totalCredit)
                            .gpa(gpa)
                            .majorGpa(majorGpa)
                            .build();
                })
                .sorted((a, b) -> {
                    int cmp = Integer.compare(a.getYear(), b.getYear());
                    if (cmp == 0) {
                        return Integer.compare(a.getSemester(), b.getSemester());
                    }
                    return cmp;
                })
                .collect(Collectors.toList());

    }
    private double convertRank(String rank) {
        return switch (rank) {
            case "A+" -> 4.5;
            case "A" -> 4.0;
            case "B+" -> 3.5;
            case "B" -> 3.0;
            case "C+" -> 2.5;
            case "C" -> 2.0;
            case "D+" -> 1.5;
            case "D" -> 1.0;
            default -> 0.0;
        };
    }

    private double round(double value) {
        return Math.round(value * 100) / 100.0;
    }

}
