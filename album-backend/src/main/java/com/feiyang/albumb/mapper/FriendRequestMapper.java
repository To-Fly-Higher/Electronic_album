package com.feiyang.albumb.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface FriendRequestMapper {
    // 修正：删除 created_time 字段和对应值 NOW()
    @Insert("INSERT INTO friend_request (from_user_id, to_user_id, status) " +
            "VALUES (#{fromUserId}, #{toUserId}, 0)")
    int insertFriendRequest(
            @Param("fromUserId") Integer fromUserId,
            @Param("toUserId") Integer toUserId,
            @Param("remark") String remark);

    // 其余方法不变（hasPendingRequest、isFriend、updateRequestStatus、getRequestById）
    @Select("SELECT COUNT(1) FROM friend_request " +
            "WHERE from_user_id = #{fromUserId} AND to_user_id = #{toUserId} AND status = 0")
    int hasPendingRequest(
            @Param("fromUserId") Integer fromUserId,
            @Param("toUserId") Integer toUserId);

    @Select("SELECT COUNT(1) FROM friend " +
            "WHERE (user_id = #{fromUserId} AND friend_id = #{toUserId}) " +
            "OR (user_id = #{toUserId} AND friend_id = #{fromUserId})")
    int isFriend(
            @Param("fromUserId") Integer fromUserId,
            @Param("toUserId") Integer toUserId);

    @Update("UPDATE friend_request SET status = #{status} WHERE id = #{requestId}")
    int updateRequestStatus(
            @Param("requestId") Integer requestId,
            @Param("status") Integer status);

    @Select("SELECT * FROM friend_request WHERE id = #{requestId}")
    Map<String, Object> getRequestById(@Param("requestId") Integer requestId);

    // 修正：删除 created_time 字段和对应值 NOW()
    @Insert("INSERT INTO friend (user_id, friend_id) VALUES (#{userId}, #{friendId})")
    int insertFriend(@Param("userId") Integer userId, @Param("friendId") Integer friendId);
}