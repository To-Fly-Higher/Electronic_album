package com.feiyang.albumb.service;

import com.feiyang.albumb.vo.FriendVO;

import java.util.List;

public interface FriendService {
    /**
     * 获取用户的好友列表和好友请求
     * @param userId 用户ID
     * @return 好友VO列表
     */
    List<FriendVO> getFriendList(Integer userId);
}