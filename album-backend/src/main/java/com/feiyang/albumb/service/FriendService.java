package com.feiyang.albumb.service;

import com.feiyang.albumb.vo.FriendAlbumVO;
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
     * 处理好友请求
     *
     * @param userId 接收方用户 ID
     * @param requestId 好友请求记录 ID
     * @param action 1=同意，2=拒绝
     */
    void handleFriendRequest(Integer userId, Integer requestId, Integer action);

    /**
     * 拉取某个好友的公开相册列表
     *
     * @param friendId 好友 ID
     * @return 好友公开相册列表
     */
    List<FriendAlbumVO> listFriendPublicAlbums(Integer friendId);
}