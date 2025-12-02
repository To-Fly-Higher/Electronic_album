package com.feiyang.albumb.mapper;

import com.feiyang.albumb.entity.FriendRequest;
import com.feiyang.albumb.entity.User;
import org.apache.ibatis.annotations.*;

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


    /* 处理好友请求 + 建立好友关系 */

    // 按 ID 查请求记录
    @Select("SELECT id, from_user_id, to_user_id, status " +
            "FROM friend_request WHERE id = #{id}")
    FriendRequest getRequestById(@Param("id") Integer id);


    // 更新请求状态
    @Update("UPDATE friend_request SET status = #{status} WHERE id = #{id}")
    int updateRequestStatus(@Param("id") Integer id,
                            @Param("status") Integer status);

    // 插入一条好友关系（单向）
    @Insert("INSERT INTO friend(user_id, friend_id) VALUES(#{userId}, #{friendId})")
    int insertFriend(@Param("userId") Integer userId,
                     @Param("friendId") Integer friendId);

    // 判断是否已经是好友 避免重复插入
    @Select("SELECT COUNT(*) FROM friend " +
            "WHERE user_id = #{userId} AND friend_id = #{friendId}")
    int countFriendship(@Param("userId") Integer userId,
                        @Param("friendId") Integer friendId);

}