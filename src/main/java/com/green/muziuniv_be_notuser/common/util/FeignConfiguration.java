package com.green.muziuniv_be_notuser.common.util;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {
    @Bean
    Logger.Level feignLoggerLevel(){ return Logger.Level.FULL;}
}
