package com.green.muziuniv_be_notuser.openfeign.user.model;

import com.green.muziuniv_be_notuser.common.util.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "${constants.open-feign.function}", configuration = FeignConfiguration.class)
public interface UserClient {
}
