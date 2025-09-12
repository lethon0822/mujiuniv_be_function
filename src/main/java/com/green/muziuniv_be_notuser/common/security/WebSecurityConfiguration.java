package com.green.muziuniv_be_notuser.common.security;

import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // REST API니까 CSRF 끔
                .authorizeHttpRequests(auth -> auth
                        // 일단 테스트용. 임시로 permitAll() 적용
                        .requestMatchers("/api/course/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/professor/course/check/**").permitAll()
                        .requestMatchers("/api/semester/**").permitAll()
                        .requestMatchers("/api/department").permitAll()

                        .anyRequest().authenticated() // 그 외 요청은 인증 필요
                );

        return http.build();
    }
}
