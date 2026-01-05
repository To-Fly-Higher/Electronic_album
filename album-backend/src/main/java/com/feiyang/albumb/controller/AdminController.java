package com.feiyang.albumb.controller;

import com.feiyang.albumb.common.Result;
import com.feiyang.albumb.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 添加管理员
     */
    @PostMapping("/add")
    public Result<Void> addAdmin(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam("avatar") MultipartFile avatarFile) {
        try {
            adminService.addAdmin(username, password, avatarFile);
            return Result.success("管理员添加成功", null);
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
}