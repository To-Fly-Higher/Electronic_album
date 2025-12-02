package com.feiyang.albumb.service;

import java.util.Map;

public interface FriendRequestService {
    /**
     * 发送好友请求（接收前端 Map 参数，内部映射表字段）
     */
    void sendFriendRequest(Map<String, Object> paramMap);
}