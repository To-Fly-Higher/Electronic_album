package com.feiyang.albumb.vo;

import com.feiyang.albumb.entity.User;
import lombok.Data;

@Data
public class CommentVO {
    private Integer id;
    private String content;
    private User user;  // 评论者
}
