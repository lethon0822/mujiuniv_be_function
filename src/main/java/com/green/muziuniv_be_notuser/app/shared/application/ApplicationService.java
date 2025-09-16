package com.green.muziuniv_be_notuser.app.shared.application;


import com.green.muziuniv_be_notuser.app.shared.application.model.AppPostReq;
import com.green.muziuniv_be_notuser.app.shared.application.model.ApplicationListRow;
import com.green.muziuniv_be_notuser.app.shared.application.model.ApplyNextReq;
import com.green.muziuniv_be_notuser.app.shared.application.model.ScheduleWindow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationMapper applicationMapper;

    public int insertAppForReason (AppPostReq req) {
        return applicationMapper.insertAppForReason(req);
    }

    @Transactional
    public void applyForNextSemester(Long userId, ApplyNextReq req, boolean enforceWindow) {
        if (req.getCurrentSemesterId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "currentSemesterId가 없습니다.");
        }

        // 1) 다음 학기
        Integer nextSemId = applicationMapper.selectNextSemesterId(req.getCurrentSemesterId());
        if (nextSemId == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "다음 학기가 없습니다.");

        // 2) 타입 매핑(영→한) 또는 그대로 사용
        String scheduleType = normalizeType(req.getType());

        // 3) 해당 스케줄
        ScheduleWindow win = applicationMapper.selectScheduleForSemesterAndType(nextSemId, scheduleType);
        if (win == null || win.getScheduleId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 학기의 접수 일정이 등록되지 않았습니다.");
        }

        // 4) (선택) 접수 기간 체크
        if (enforceWindow) {
            Integer open = applicationMapper.isScheduleOpenNow(win.getScheduleId());
            if (open == null || open != 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "지금은 접수기간이 아닙니다.");
            }
        }

        // 5) 신청 생성 (status: '처리중')
        applicationMapper.insertApplication(userId, win.getScheduleId(), req.getReason());
    }

    @Transactional(readOnly = true)
    public List<ApplicationListRow> myApplications(Long userId, String status) {
        return applicationMapper.selectMyApplications(userId, status);
    }

    /** "LEAVE"/"RETURN" 또는 한글 입력을 모두 허용 */
    private String normalizeType(String t) {
        if (t == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "type이 없습니다.");
        return switch (t) {
            case "LEAVE", "휴학", "휴학신청"   -> "휴학신청";
            case "RETURN", "복학", "복학신청" -> "복학신청";
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "type은 LEAVE/RETURN 이어야 합니다.");
        };
    }
}
