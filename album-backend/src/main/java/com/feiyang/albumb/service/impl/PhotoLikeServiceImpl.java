package com.feiyang.albumb.service.impl;

import com.feiyang.albumb.entity.User;
import com.feiyang.albumb.mapper.PhotoLikeMapper;
import com.feiyang.albumb.service.PhotoLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class PhotoLikeServiceImpl implements PhotoLikeService {

    @Autowired
    private PhotoLikeMapper photoLikeMapper;

    @Override
    @Transactional
    public void toggleLike(Integer photoId, Map<String, Object> paramMap) {
        // 解析参数（user_id 和 liked）
        Integer userId = parseIntegerParam(paramMap, "user_id");
        Boolean liked = parseBooleanParam(paramMap, "liked");

        // 参数校验
        if (photoId == null) {
            throw new RuntimeException("图片ID不能为空");
        }

        if (liked) {
            // 点赞：校验是否已点赞
            int count = photoLikeMapper.checkLiked(photoId, userId);
            if (count > 0) {
                throw new RuntimeException("已点赞，无需重复操作");
            }
            int rows = photoLikeMapper.likePhoto(photoId, userId);
            if (rows == 0) {
                throw new RuntimeException("点赞失败");
            }
        } else {
            // 取消点赞：校验是否已点赞
            int count = photoLikeMapper.checkLiked(photoId, userId);
            if (count == 0) {
                throw new RuntimeException("未点赞，无法取消");
            }
            int rows = photoLikeMapper.cancelLikePhoto(photoId, userId);
            if (rows == 0) {
                throw new RuntimeException("取消点赞失败");
            }
        }
    }

    @Override
    public List<User> getLikesByPhotoId(Integer photoId) {
        if (photoId == null) {
            throw new RuntimeException("图片ID不能为空");
        }
        // 调用Mapper查询点赞用户列表
        return photoLikeMapper.getLikesByPhotoId(photoId);
    }

    /**
     * 工具方法：解析Integer参数
     */
    private Integer parseIntegerParam(Map<String, Object> paramMap, String key) {
        Object value = paramMap.get(key);
        if (value == null) {
            throw new RuntimeException(key + "不能为空");
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            throw new RuntimeException(key + "格式错误，需为整数");
        }
    }

    /**
     * 工具方法：解析Boolean参数
     */
    private Boolean parseBooleanParam(Map<String, Object> paramMap, String key) {
        Object value = paramMap.get(key);
        if (value == null) {
            throw new RuntimeException(key + "不能为空");
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof String) {
            return "true".equalsIgnoreCase(value.toString()) || "1".equals(value.toString());
        } else {
            throw new RuntimeException(key + "格式错误，需为布尔值");
        }
    }
}