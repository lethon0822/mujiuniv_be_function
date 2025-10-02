package com.green.muziuniv_be_notuser.app.student.gpa;

import com.green.muziuniv_be_notuser.app.student.gpa.model.GpaRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GpaService {
    private final GpaMapper gpaMapper;

    public List<GpaRes> getMyGpa(Long userId){
        return gpaMapper.getMyGpa(userId);
    }
}
