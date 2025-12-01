package com.feiyang.albumb.controller;


import com.feiyang.albumb.entity.Photo;
import com.feiyang.albumb.service.PhotoService;
import com.feiyang.albumb.vo.PhotoVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/album")
public class PhotoController {

    private final PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/{albumId}/images")
    public Map<String, Object> getPhotos(@PathVariable Integer albumId) {
        List<PhotoVO> photos = photoService.getPhotosByAlbumId(albumId);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "success");
        result.put("data", photos);
        return result;
    }

    @DeleteMapping("/{albumId}/image/{photoId}")
    public Map<String, Object> deletePhoto(
            @PathVariable Integer albumId,  // 从路径获取相册ID
            @PathVariable Integer photoId   // 从路径获取图片ID
    ) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 调用服务层执行删除（核心逻辑，需确保PhotoService有对应方法）
            photoService.deletePhoto(albumId, photoId);

            result.put("code", 200);
            result.put("msg", "照片删除成功");
            return result;
        } catch (RuntimeException e) {
            // 业务异常（照片不存在、无归属等）
            result.put("code", 400);
            result.put("msg", e.getMessage());
            return result;
        } catch (Exception e) {
            // 系统异常（文件删除失败、数据库错误等）
            result.put("code", 500);
            result.put("msg", "照片删除失败");
            return result;
        }
    }
}
