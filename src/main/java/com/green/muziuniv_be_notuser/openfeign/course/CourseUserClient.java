package com.green.muziuniv_be_notuser.openfeign.course;

import com.green.muziuniv_be_notuser.app.professor.course.model.UserResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "RAINBOW-ACADEMIC-USER", path = "/api/user")
public interface CourseUserClient {
    // user-service에서 userId 리스트로 학생 정보 한 번에 조회
    @PostMapping("/batch")
    List<UserResDto> getUsersByIds(@RequestBody List<Long> userIds);
}
