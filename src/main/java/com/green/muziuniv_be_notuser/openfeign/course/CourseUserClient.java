package com.green.muziuniv_be_notuser.openfeign.course;

import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.openfeign.course.model.UserResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "MUJIUNIV-USER", path = "/api/user")
public interface CourseUserClient {
    @PostMapping("/student")
    ResultResponse<List<UserResDto>> getUsersByInfo(@RequestBody Map<String, List<Long>> userIds);
}


