package com.feiyang.albumb.mapper;

import com.feiyang.albumb.entity.FriendRequest;
import com.feiyang.albumb.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FriendMapper {

    @Select("SELECT u.id, u.nickname, u.avatar " +
            "FROM friend f " +
            "JOIN users u ON f.friend_id = u.id " +   // user 是保留字，需要加引号！
            "WHERE f.user_id = #{userId}")
    List<User> getFriends(@Param("userId") Integer userId);

    // 发出的请求
    @Select("SELECT fr.id, fr.from_user_id, fr.to_user_id, fr.status " +
            "FROM friend_request fr WHERE fr.from_user_id = #{userId} AND fr.status = 0")
    List<FriendRequest> getSentRequests(Integer userId);

    // 收到的请求
    @Select("SELECT fr.id, fr.from_user_id, fr.to_user_id, fr.status " +
            "FROM friend_request fr WHERE fr.to_user_id = #{userId} AND fr.status = 0")
    List<FriendRequest> getReceivedRequests(Integer userId);

}