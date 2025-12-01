package com.feiyang.albumb.mapper;

import com.feiyang.albumb.entity.User;
import org.apache.ibatis.annotations.*;

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
}
