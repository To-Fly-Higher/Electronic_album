package com.feiyang.albumb.mapper;

import com.feiyang.albumb.entity.Comment;
import com.feiyang.albumb.vo.CommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface CommentMapper {

    @Select("SELECT c.id AS id, c.content AS content, " +
            "u.id AS user_id, u.nickname AS user_nickname, u.avatar AS user_avatar " +
            "FROM comment c " +
            "JOIN users u ON c.user_id = u.id " +
            "WHERE c.photo_id = #{photoId}")
    List<CommentVO> getCommentsByPhotoId(@Param("photoId") Integer photoId);
}
