package com.feiyang.albumb.entity;

import lombok.Data;

@Data
public class Friend {
    private Integer id;           // 记录ID
    private Integer userId;       // 用户ID
    private Integer friendId;     // 好友ID
}