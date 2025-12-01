package com.feiyang.albumb.controller;

import com.feiyang.albumb.common.Result;
import com.feiyang.albumb.dto.LoginRequest;
import com.feiyang.albumb.dto.RegisterRequest;
import com.feiyang.albumb.entity.User;
import com.feiyang.albumb.service.UserService;
import com.feiyang.albumb.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 注册
    @PostMapping("/register")
    public Result<Void> register(@RequestBody RegisterRequest req) {
        try {
            userService.register(req.getUsername(), req.getPassword());
            // 200 成功
            return Result.success("注册成功", null);
        } catch (IllegalArgumentException e) {
            // 400 用户名或密码不能为空
            return Result.fail(400, e.getMessage());
        } catch (IllegalStateException e) {
            // 400 用户名已存在
            return Result.fail(400, e.getMessage());
        } catch (Exception e) {
            // 500 服务器内部错误
            e.printStackTrace();
            return Result.fail(500, "服务器内部错误");
        }
    }

    // 登录
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginRequest req) {
        try {
            User user = userService.login(req.getUsername(), req.getPassword());

            // data：
            // {
            //   "id": 5,
            //   "username": "user001",
            //   "nickname": "测试用户",
            //   "avatar": "dummy-url",
            //   "role": 0,
            //   "token": "user-token-987654"
            // }
            Map<String, Object> data = new HashMap<>();
            data.put("id", user.getId());
            data.put("username", user.getUsername());
            data.put("nickname", user.getNickname());
            data.put("avatar", user.getAvatar());
            data.put("role", user.getRole());

            // for now, a random string
//            String token = "user-token-" + UUID.randomUUID();
//            data.put("token", token);

            // 200 登录成功
            return Result.success("登录成功", data);

        } catch (IllegalArgumentException e) {
            // 400, 参数错误
            return Result.fail(400, e.getMessage());
        } catch (NoSuchElementException e) {
            // 404 用户不存在
            return Result.fail(404, e.getMessage());
        } catch (IllegalStateException e) {
            // 401 密码错误
            return Result.fail(401, e.getMessage());
        } catch (Exception e) {
            // 500 服务器内部错误
            return Result.fail(500, "服务器内部错误");
        }
    }


}

