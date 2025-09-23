package com.green.muziuniv_be_notuser.openfeign.user;


import com.green.muziuniv_be_notuser.configuration.FeignConfiguration;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.openfeign.user.model.UserInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "${constants.open-feign.user}", contextId = "userClient", configuration = FeignConfiguration.class)
public interface UserClient {

    // 교수 정보 가져옴
    @PostMapping("/api/user/list")
    ResultResponse<Map<Long, UserInfoDto>> getUserInfo(@RequestBody Map<String, List<Long>> request);

    // 교수 학과 가져옴
    @GetMapping("/api/user/dept")
    ResultResponse<String> getProDeptCode(@RequestParam("user_id")Long userId);
}
