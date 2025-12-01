package com.feiyang.albumb.mapper;

import com.feiyang.albumb.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PhotoLikeMapper {

    @Select("SELECT u.id AS id, u.avatar AS avatar " +
            "FROM photo_like pl " +
            "JOIN users u ON pl.user_id = u.id " +
            "WHERE pl.photo_id = #{photoId}")
    List<User> getLikesByPhotoId(@Param("photoId") Integer photoId);
}
