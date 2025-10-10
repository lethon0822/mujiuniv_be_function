package com.green.muziuniv_be_notuser.openfeign.user;


import com.green.muziuniv_be_notuser.configuration.FeignConfiguration;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.openfeign.user.model.UserInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "${constants.open-feign.user}", contextId = "userClient", configuration = FeignConfiguration.class, path = "/api/user")
public interface UserClient {

    // 여러유저 정보 가져옴
    @PostMapping("/list")
    ResultResponse<Map<Long, UserInfoDto>> getUserInfo(@RequestBody Map<String, List<Long>> request);

    // 단일 유저 정보 조회 (학적 상태 확인용)
    @GetMapping("/{userId}")
    ResultResponse<UserInfoDto> getUserById(@PathVariable("userId") Long userId);

    // 교수 학과 가져옴
    @GetMapping("/dept/code")
    String getProDeptCode(@RequestParam("user_id")Long userId);

    // 학적/재직 상태 변경
    @PutMapping("/status")
    void updateStatus(@RequestParam("userId") Long userId,
                      @RequestParam("status") Integer status);

}
