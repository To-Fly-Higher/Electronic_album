package com.feiyang.albumb.service.impl;

import com.feiyang.albumb.entity.User;
import com.feiyang.albumb.mapper.UserMapper;
import com.feiyang.albumb.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private UserMapper userMapper;

    public void addAdmin(String username, String password, MultipartFile avatarFile) throws IOException {
        // 校验用户名密码
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("用户名或密码不能为空");
        }

        // 检查用户名是否存在
        User existing = userMapper.findByUsername(username);
        if (existing != null) {
            throw new IllegalStateException("用户名已存在");
        }

        // 保存头像到本地
        String avatarUrl = null;
        if (avatarFile != null && !avatarFile.isEmpty()) {
            String projectPath = System.getProperty("user.dir");
            String uploadDir = projectPath + "/uploads/avatar/";
            File dir = new File(uploadDir);
            if (!dir.exists() && !dir.mkdirs()) {
                throw new IOException("创建上传目录失败");
            }

            String originalFilename = avatarFile.getOriginalFilename();
            String safeName = (originalFilename == null || originalFilename.isEmpty())
                    ? "avatar" : originalFilename;
            String fileName = System.currentTimeMillis() + "_" + safeName;
            File dest = new File(dir, fileName);
            avatarFile.transferTo(dest);

            avatarUrl = "/uploads/avatar/" + fileName; // 存数据库的 URL
        }

        // 构建管理员对象
        User admin = new User();
        admin.setUsername(username);
        admin.setPassword(password); // ⭐ 明文存储，生产环境要加密
        admin.setNickname(username);
        admin.setAvatar(avatarUrl);
        admin.setRole(1); // 1=管理员

        userMapper.insertUser(admin);
    }
}
