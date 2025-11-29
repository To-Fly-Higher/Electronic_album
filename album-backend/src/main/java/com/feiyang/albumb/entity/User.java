package com.feiyang.albumb.entity;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String username;
    private String password;    // 存加密后的密码
    private String nickname;
    private String avatar;
    private Integer role;       // 0: 普通用户, 1: 管理员
}
