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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
// 注册（支持头像上传）
    @PostMapping("/register")
    public Result<Void> register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar
    ) {
        try {
            String avatarUrl = null;

            // 1. 如果上传了头像，先保存到本地
            if (avatar != null && !avatar.isEmpty()) {

                // 1.1 确定保存路径
                String projectPath = System.getProperty("user.dir");
                String uploadDir = projectPath + "/uploads/avatar/";
                File dir = new File(uploadDir);
                if (!dir.exists() && !dir.mkdirs()) {
                    return Result.fail(500, "创建头像目录失败");
                }

                // 1.2 生成安全文件名
                String originalFilename = avatar.getOriginalFilename();
                String safeName = (originalFilename == null || originalFilename.isEmpty())
                        ? "avatar.png"
                        : originalFilename;
                String fileName = System.currentTimeMillis() + "_" + safeName;

                // 1.3 保存文件
                File dest = new File(dir, fileName);
                avatar.transferTo(dest);

                // 1.4 生成给前端用的访问路径
                avatarUrl = "/uploads/avatar/" + fileName;
            }

            // 2. 调用业务层注册（把 avatarUrl 传进去）
            userService.register(username, password, avatarUrl);

            return Result.success("注册成功", null);

        } catch (IllegalArgumentException e) {
            return Result.fail(400, e.getMessage());
        } catch (IllegalStateException e) {
            return Result.fail(400, e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail(500, "头像保存失败");
        } catch (Exception e) {
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

