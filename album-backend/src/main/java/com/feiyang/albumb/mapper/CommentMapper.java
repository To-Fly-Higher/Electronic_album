package com.feiyang.albumb.mapper;


import com.feiyang.albumb.entity.Comment;
import com.feiyang.albumb.vo.CommentVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Select("SELECT id, content, user_id FROM comment WHERE photo_id = #{photoId}")
    @Results(id = "CommentVOMap", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "content", property = "content"),
            @Result(column = "user_id", property = "user",
                    one = @One(select = "com.feiyang.albumb.mapper.UserMapper.getCUserVOById"))
    })
    List<CommentVO> getCommentsByPhotoId(Integer photoId);

    //发表评论
    @Insert("INSERT INTO comment(photo_id,user_id,content) values (#{photoId},#{userId},#{content})")
    void insertComment(Comment comment);

    @Delete("DELETE FROM comment WHERE id = #{commentId}")
    int deleteById(Integer commentId);
}
