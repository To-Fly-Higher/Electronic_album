package com.feiyang.albumb.service;

import com.feiyang.albumb.entity.User;
import com.feiyang.albumb.vo.UserVO;

import java.util.List;

public interface UserService {

    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     */
    void register(String username, String password);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的用户对象
     */
    User login(String username, String password);

    User findByNickname(String nickname);

    List<UserVO> getUsersWithPublicAlbums();

}
