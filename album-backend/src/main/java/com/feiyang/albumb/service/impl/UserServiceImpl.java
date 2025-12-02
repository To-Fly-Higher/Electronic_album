package com.feiyang.albumb.service.impl;

import com.feiyang.albumb.entity.User;
import com.feiyang.albumb.mapper.UserMapper;
import com.feiyang.albumb.service.UserService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    // ❌ 不再进行加密，因此这个方法可以删掉或保留但不再使用
    // private String encodePassword(String rawPassword) { ... }

    /**
     * 注册（明文密码）
     */
    public void register(String username, String password) {
        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {
            throw new IllegalArgumentException("用户名或密码不能为空");
        }

        User existing = userMapper.findByUsername(username);
        if (existing != null) {
            throw new IllegalStateException("用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // ⭐ 明文存储
        user.setNickname(username);
        user.setAvatar(null);
        user.setRole(0);

        userMapper.insertUser(user);
    }

    /**
     * 登录（明文对比）
     */
    public User login(String username, String password) {
        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {
            throw new IllegalArgumentException("参数错误");
        }

        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new NoSuchElementException("用户不存在");
        }

        // ⭐ 明文对比，不再加密
        if (!password.equals(user.getPassword())) {
            throw new IllegalStateException("密码错误");
        }

        return user;
    }

    @Override
    public User findByNickname(String nickname) {
        return userMapper.findByNickname(nickname);
    }
}
