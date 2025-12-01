package com.feiyang.albumb.entity;

import lombok.Data;

@Data
public class Photo {
    private Integer id;         // 图片ID
    private Integer userId;     // 上传用户ID
    private String url;         // 图片路径
    private String name;        // 图片名称

    // not in table
    private Integer albumId;    // 相册ID
}