package com.feiyang.albumb.controller;

import com.feiyang.albumb.common.Result;
import com.feiyang.albumb.entity.Album;
import com.feiyang.albumb.service.AlbumService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/user/album")
public class AlbumController {

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/list")
    public Result<List<Album>> getAlbums(@RequestParam Integer userId) {
        List<Album> albums = albumService.getAlbumsByUserId(userId);
        return Result.success("获取成功", albums);
    }

    // 新建相册
    @PostMapping
    public Result<Void> createAlbum(@RequestParam Integer userId,
                                    @RequestParam Integer categoryId,
                                    @RequestParam String name,
                                    @RequestParam String remark,
                                    @RequestParam Integer isPublic,
                                    @RequestParam("coverFile") MultipartFile coverFile) {
        try {
            // 保存文件
            // 获取项目根目录
            String projectPath = System.getProperty("user.dir");
            String uploadDir = projectPath + "/uploads/album_cover/";
//            String uploadDir = "uploads/album_cover/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();
            String fileName = System.currentTimeMillis() + "_" + coverFile.getOriginalFilename();
            File dest = new File(dir, fileName);
            coverFile.transferTo(dest);

            // 构建 album
            Album album = new Album();
            album.setUserId(userId);
            album.setCategoryId(categoryId);
            album.setName(name);
            album.setRemark(remark);
            album.setIsPublic(isPublic);
            album.setCoverUrl("/uploads/album_cover/" + fileName);

            albumService.createAlbum(album);
            return Result.success("创建成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误");
        }
    }

    // 修改相册
    @PutMapping("/{id}")
    public Result<Void> updateAlbum(@PathVariable Integer id,
                                    @RequestParam Integer categoryId,
                                    @RequestParam String name,
                                    @RequestParam String remark,
                                    @RequestParam Integer isPublic,
                                    @RequestParam(value = "coverFile", required = false) MultipartFile coverFile) {
        try {
            Album album = albumService.getAlbumById(id);
            if (album == null) return Result.fail(404, "相册不存在");

            album.setCategoryId(categoryId);
            album.setName(name);
            album.setRemark(remark);
            album.setIsPublic(isPublic);

            if (coverFile != null) {
                // 获取项目根目录
                String projectPath = System.getProperty("user.dir");
                String uploadDir = projectPath + "/uploads/album_cover/";
//                String uploadDir = "D:/ProjectOfVscode/E/album-backend/uploads/album_cover/";

//                String uploadDir = "uploads/album_cover/";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();
                String fileName = System.currentTimeMillis() + "_" + coverFile.getOriginalFilename();
                File dest = new File(dir, fileName);
                coverFile.transferTo(dest);
                album.setCoverUrl("/uploads/album_cover/" + fileName);
            }

            albumService.updateAlbum(album);
            return Result.success("修改成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误");
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteAlbum(@PathVariable Integer id) {
        boolean success = albumService.deleteAlbum(id);
        if (success) {
            return Result.success("删除成功", null);
        } else {
            return Result.fail(404, "相册不存在或删除失败");
        }
    }
}
