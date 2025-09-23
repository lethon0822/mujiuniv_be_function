package com.green.muziuniv_be_notuser.openfeign.user.model;

import lombok.Getter;

@Getter
public class UserInfoDto {
    private Long userId;
    private String userName;
    private String deptName;
}
