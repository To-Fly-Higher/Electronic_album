package com.feiyang.albumb.entity;

import lombok.Data;

@Data
public class FriendRequest {
    private Integer id;
    private Integer fromUserId;
    private Integer toUserId;
    private Integer status; // 0=待处理, 1=已同意, 2=已拒绝
}