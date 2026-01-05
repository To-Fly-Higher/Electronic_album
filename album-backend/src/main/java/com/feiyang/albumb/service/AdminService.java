package com.feiyang.albumb.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AdminService {
    void addAdmin(String username, String password, MultipartFile avatarFile)  throws IOException;
}
