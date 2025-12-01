package com.feiyang.albumb.entity;


import lombok.Data;

@Data
public class Album {
    private Integer id;          // 相册ID
    private Integer userId;      // 用户ID
    private Integer categoryId;  // 类别ID（可为空）
    private String name;         // 相册名称
    private String coverUrl;     // 封面地址（可为空）
    private String remark;       // 描述（可为空）
    private Integer isPublic;  // 权限：0公开 1私有
}