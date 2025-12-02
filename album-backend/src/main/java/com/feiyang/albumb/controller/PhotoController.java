package com.feiyang.albumb.controller;


import com.feiyang.albumb.entity.Photo;
import com.feiyang.albumb.entity.User;
import com.feiyang.albumb.service.PhotoLikeService;
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

    private final PhotoLikeService photoLikeService;
    @Autowired
    public PhotoController(PhotoService photoService,PhotoLikeService photoLikeService) {
        this.photoService = photoService;
        this.photoLikeService = photoLikeService;
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
        }
    }

    /**
     * 切换点赞状态（适配前端原请求，仅修改路径前缀）
     * 接口路径：POST /api/photo/{photoId}/like
     * 请求体：{ "user_id": xxx, "liked": true/false }
     */
    @PostMapping("/{photoId}/like")
    public Map<String, Object> toggleLike(
            @PathVariable Integer photoId,
            @RequestBody Map<String, Object> paramMap
    ) {
        Map<String, Object> result = new HashMap<>();
        try {
            photoLikeService.toggleLike(photoId, paramMap);
            Boolean liked = (Boolean) paramMap.get("liked");
            String msg = liked != null && liked ? "点赞成功" : "取消点赞成功";
            result.put("code", 200);
            result.put("msg", msg);
            return result;
        } catch (RuntimeException e) {
            result.put("code", 400);
            result.put("msg", e.getMessage());
            return result;
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "操作失败");
            return result;
        }
    }

    /**
     * 查询指定图片的点赞用户列表（你的查询需求）
     * 接口路径：GET /api/photo/{photoId}/likes
     */
    @GetMapping("/{photoId}/likes")
    public Map<String, Object> getLikesByPhotoId(@PathVariable Integer photoId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<User> likeUsers = photoLikeService.getLikesByPhotoId(photoId);
            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", likeUsers); // 返回点赞用户列表
            return result;
        } catch (RuntimeException e) {
            result.put("code", 400);
            result.put("msg", e.getMessage());
            return result;
        }
    }
}
