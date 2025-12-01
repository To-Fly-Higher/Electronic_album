package com.feiyang.albumb.entity;

import lombok.Data;

@Data
public class Like {
    private Integer id;         // 点赞ID
    private Integer photoId;    // 图片ID
    private Integer userId;     // 点赞用户ID
}