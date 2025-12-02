package com.feiyang.albumb.service;

import com.feiyang.albumb.entity.User;

import java.util.List;
import java.util.Map;

public interface PhotoLikeService {
    /**
     * 切换点赞状态（点赞/取消点赞）
     */
    void toggleLike(Integer photoId, Map<String, Object> paramMap);

    /**
     * 获取指定图片的点赞用户列表（你的查询需求）
     */
    List<User> getLikesByPhotoId(Integer photoId);
}