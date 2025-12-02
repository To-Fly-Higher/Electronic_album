package com.feiyang.albumb.vo;

import com.feiyang.albumb.entity.Album;
import lombok.Data;

import java.util.List;

@Data
public class UserVO {
    private Integer id;
    private String username;
    private String nickname;
    private String avatar;
    private List<Album> albums; // 用户的相册列表
}