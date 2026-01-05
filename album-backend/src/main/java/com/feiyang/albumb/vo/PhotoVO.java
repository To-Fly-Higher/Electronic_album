package com.feiyang.albumb.vo;

import com.feiyang.albumb.entity.Comment;
import com.feiyang.albumb.entity.User;
import lombok.Data;
import java.util.List;

@Data
public class PhotoVO {
    private Integer id;
    private String name;
    private String url;
    private List<LikeVO> likes;     // 点赞用户列表
    private List<CommentVO> comments;  // 评论列表
    private Boolean liked;   // 👈 当前用户是否点赞
}