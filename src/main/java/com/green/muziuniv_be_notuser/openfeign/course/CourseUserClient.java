package com.green.muziuniv_be_notuser.openfeign.course;

import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.openfeign.course.model.UserResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "RAINBOW-ACADEMIC-USER", path = "/api/user")
public interface CourseUserClient {
    // user-service에서 userId 리스트로 학생 정보 한 번에 조회
    @PostMapping("/list")
    ResultResponse<List<UserResDto>> getUsersByIds(@RequestBody Map<String, List<Long>> userIds);

}


