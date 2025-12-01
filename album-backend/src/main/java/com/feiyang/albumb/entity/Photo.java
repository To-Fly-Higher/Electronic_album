package com.feiyang.albumb.entity;

import lombok.Data;

@Data
public class Photo {
    private Integer id;          // 图片ID
    private Integer albumId;     // 相册ID
    private Integer userId;      // 上传用户ID
    private String url;          // 图片路径
}