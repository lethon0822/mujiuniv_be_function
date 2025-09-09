package com.green.muziuniv_be_notuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MuziunivBeNotuserApplication {

    public static void main(String[] args) {
        SpringApplication.run(MuziunivBeNotuserApplication.class, args);
    }

}
