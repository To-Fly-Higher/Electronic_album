package com.feiyang.albumb.service.impl;

import com.feiyang.albumb.entity.User;
import com.feiyang.albumb.mapper.UserMapper;
import com.feiyang.albumb.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    // 对 password 进行加密
    private String encodePassword(String rawPassword) {
        return DigestUtils.md5DigestAsHex(rawPassword.getBytes());
    }

    /**
     * 注册：
     * 1. 检查 username、password 是否为空
     * 2. 查询数据库判断 username 是否已存在
     * 3. 对 password 进行加密
     * 4. 插入 user 表
     * 5. return null
     */
    public void register(String username, String password) {
        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {
            // 400 用户名或密码不能为空
            throw new IllegalArgumentException("用户名或密码不能为空");
        }

        User existing = userMapper.findByUsername(username);
        if (existing != null) {
            // 400 用户名已存在
            throw new IllegalStateException("用户名已存在");
        }

        String encodedPassword = encodePassword(password);

        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setNickname(username);
        user.setAvatar(null);
        user.setRole(0);

        userMapper.insertUser(user);
    }

    /**
     * 登录：
     * 1. 校验参数（username + password）
     * 2. 查询数据库
     * 3. 不存在 → 抛 NoSuchElementException("用户不存在")
     * 4. 密码不正确 → 抛 IllegalStateException("密码错误")
     * 5. 正确 → 返回 User
     */
    public User login(String username, String password) {
        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {
            // 400 参数错误
            throw new IllegalArgumentException("参数错误");
        }

        User user = userMapper.findByUsername(username);
        if (user == null) {
            // 404 用户不存在
            throw new NoSuchElementException("用户不存在");
        }

        // 校验密码
        String encoded = encodePassword(password);
        if (!encoded.equals(user.getPassword())) {
            // 401, 密码错误
            throw new IllegalStateException("密码错误");
        }

        // 4. 登录成功
        return user;
    }

    @Override
    public User findByNickname(String nickname) {
        return userMapper.findByNickname(nickname);
    }
}
