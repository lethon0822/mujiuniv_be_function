package com.green.muziuniv_be_notuser.openfeign.user;


import com.green.muziuniv_be_notuser.configuration.FeignConfiguration;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.openfeign.user.model.ProGetRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "${constants.open-feign.user}", contextId = "userClient", configuration = FeignConfiguration.class)
public interface UserClient {

    // 교수 정보 가져옴
    @PostMapping("api/user/professor")
    ResultResponse<List<ProGetRes>> getProInfo(@RequestBody Map<String, List<Long>> request);
}
