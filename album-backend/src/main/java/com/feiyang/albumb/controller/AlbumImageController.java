package com.feiyang.albumb.controller;

import com.feiyang.albumb.common.Result;
import com.feiyang.albumb.entity.Photo;
import com.feiyang.albumb.service.PhotoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/album")
public class AlbumImageController {

    private final PhotoService photoService;

    public AlbumImageController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @PostMapping("/{albumId}/image")
    public Result<Void> uploadImage(@PathVariable Integer albumId,
                                    @RequestParam Integer userId,
                                    @RequestParam String name,
                                    @RequestParam("imageFile") MultipartFile imageFile) {
        // 基本校验
        if (imageFile == null || imageFile.isEmpty()) {
            return Result.fail(400, "上传文件为空");
        }

        try {
            // 1. 确定保存路径
            String projectPath = System.getProperty("user.dir");
            String uploadDir = projectPath + "/uploads/photo/";
            File dir = new File(uploadDir);
            if (!dir.exists() && !dir.mkdirs()) {
                return Result.fail(500, "创建上传目录失败");
            }

            // 2. 生成文件名并保存到本地
            String originalFilename = imageFile.getOriginalFilename();
            String safeName = (originalFilename == null || originalFilename.isEmpty())
                    ? "image"
                    : originalFilename;
            String fileName = System.currentTimeMillis() + "_" + safeName;
            File dest = new File(dir, fileName);
            imageFile.transferTo(dest);

            // 3. 构造给前端用的 URL
            String url = "/uploads/photo/" + fileName;

            // 4. 组装 Photo 对象，交给业务层去插入 photo 和 album_photo
            Photo photo = new Photo();
            photo.setUserId(userId);
            photo.setName(name);
            photo.setUrl(url);

            photoService.savePhotoToAlbum(albumId, photo);

            // 5. 返回统一 Result
            return Result.success("图片上传成功", null);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail(500, "文件保存失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器内部错误");
        }
    }
}
