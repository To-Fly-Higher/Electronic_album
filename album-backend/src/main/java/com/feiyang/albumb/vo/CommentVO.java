package com.feiyang.albumb.vo;

import com.feiyang.albumb.entity.User;
import lombok.Data;

@Data
public class CommentVO {
    private Integer id;
    private String content;
    private CUserVO user;
}