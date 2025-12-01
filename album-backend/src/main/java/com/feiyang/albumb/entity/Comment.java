package com.feiyang.albumb.entity;
import lombok.Data;

@Data
public class Comment {
    private Integer id;              // 评论ID
    private Integer photoId;         // 图片ID
    private Integer userId;          // 评论人ID
    private String content;          // 评论内容
}