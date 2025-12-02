package com.feiyang.albumb.service;

import com.feiyang.albumb.vo.FriendAlbumVO;
import com.feiyang.albumb.vo.FriendInf;
import com.feiyang.albumb.vo.FriendVO;

import java.util.List;

public interface FriendService {
    /**
     * 获取用户的好友列表和好友请求
     * @param userId 用户ID
     * @return 好友VO列表
     */
    List<FriendVO> getFriendList(Integer userId);

    /**
     * 处理好友请求（同意/拒绝）
     * @param userId   当前登录用户ID，对应 to_user_id
     * @param friendId 对方用户ID，对应 from_user_id
     * @param action   1=同意, 2=拒绝
     */
    void handleFriendRequest(Integer userId, Integer friendId, Integer action);


    /**
     * 拉取某个好友的公开相册列表
     *
     * @param friendId 好友 ID
     * @return 好友公开相册列表
     */
    List<FriendAlbumVO> listFriendPublicAlbums(Integer friendId);


    //获取好友信息
    FriendInf getFriendInf(Integer friendId);
}