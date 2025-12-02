package com.feiyang.albumb.mapper;

import com.feiyang.albumb.entity.Album;
import com.feiyang.albumb.entity.User;
import com.feiyang.albumb.vo.CUserVO;
import com.feiyang.albumb.vo.UserVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT id, username, password, nickname, avatar, role " +
            "FROM users WHERE username = #{username}")
    User findByUsername(String username);

    @Insert("INSERT INTO users(username, password, nickname, avatar, role) " +
            "VALUES(#{username}, #{password}, #{nickname}, #{avatar}, #{role})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertUser(User user);

    // 根据 ID 查询用户
    @Select("SELECT id, username, password, nickname, avatar, role " +
            "FROM users WHERE id = #{id}")
    User getById(@Param("id") Integer id);


    @Select("SELECT id, username, nickname, avatar, role " +
            "FROM users " +
            "WHERE nickname = #{nickname} " +
            "LIMIT 1")
    User findByNickname(@Param("nickname") String nickname);

    // 查询所有有公开相册的用户及其相册（嵌套映射）
    @Select("SELECT u.id AS user_id, u.username, u.nickname, u.avatar " +
            "FROM users u " +
            "WHERE EXISTS (SELECT 1 FROM album a WHERE a.user_id = u.id AND a.is_public = 1)")
    @Results(id = "userWithAlbumsMap", value = {
            @Result(column = "user_id", property = "id", id = true),
            @Result(column = "username", property = "username"),
            @Result(column = "nickname", property = "nickname"),
            @Result(column = "avatar", property = "avatar"),
            @Result(property = "albums", column = "user_id",
                    many = @Many(select = "com.feiyang.albumb.mapper.UserMapper.selectPublicAlbumsByUserId"))
    })
    List<UserVO> selectUsersWithPublicAlbums();

    // 根据用户ID查询该用户的公开相册
    @Select("SELECT id, user_id, category_id, name, cover_url, remark, is_public " +
            "FROM album " +
            "WHERE user_id = #{userId} AND is_public = 1 " +
            "ORDER BY id")
    List<Album> selectPublicAlbumsByUserId(@Param("userId") Integer userId);


    @Select("SELECT id, nickname, avatar FROM users WHERE id = #{id}")
    CUserVO getCUserVOById(Integer id);
}
