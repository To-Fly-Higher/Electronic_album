package com.feiyang.albumb.mapper;

import com.feiyang.albumb.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PhotoLikeMapper {

    @Select("SELECT u.id AS id, u.avatar AS avatar " +
            "FROM photo_like pl " +
            "JOIN users u ON pl.user_id = u.id " +
            "WHERE pl.photo_id = #{photoId}")
    List<User> getLikesByPhotoId(@Param("photoId") Integer photoId);

    /**
     * 取消点赞：删除点赞关系（表名改为 photo_like）
     */
    @Delete("DELETE FROM photo_like WHERE photo_id = #{photoId} AND user_id = #{userId}")
    int cancelLikePhoto(@Param("photoId") Integer photoId, @Param("userId") Integer userId);

    /**
     * 校验是否已点赞（表名改为 photo_like）
     */
    @Select("SELECT COUNT(1) FROM photo_like WHERE photo_id = #{photoId} AND user_id = #{userId}")
    int checkLiked(@Param("photoId") Integer photoId, @Param("userId") Integer userId);



    @Insert("INSERT INTO photo_like (photo_id, user_id) " +
            "VALUES (#{photoId}, #{userId})")
    int likePhoto(@Param("photoId") Integer photoId, @Param("userId") Integer userId);
}


