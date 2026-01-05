package com.feiyang.albumb.vo;

import lombok.Data;

@Data
public class LikeVO {
    private Integer id;      // 点赞记录ID，可选
    private Integer userId;  // 点赞用户ID（关键）
    private String avatar;   // 用户头像
}