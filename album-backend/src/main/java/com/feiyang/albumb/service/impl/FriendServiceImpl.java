package com.feiyang.albumb.service.impl;

import com.feiyang.albumb.entity.FriendRequest;
import com.feiyang.albumb.entity.User;
import com.feiyang.albumb.mapper.FriendMapper;
import com.feiyang.albumb.mapper.UserMapper;
import com.feiyang.albumb.service.FriendService;
import com.feiyang.albumb.vo.FriendVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {

    private final FriendMapper friendMapper;
    private final UserMapper userMapper;

    @Autowired
    public FriendServiceImpl(FriendMapper friendMapper, UserMapper userMapper) {
        this.friendMapper = friendMapper;
        this.userMapper = userMapper;
    }


    @Override
    public List<FriendVO> getFriendList(Integer userId) {
        // 已好友
        List<User> friends = friendMapper.getFriends(userId);
        // 发出的待处理请求
        List<FriendRequest> sent = friendMapper.getSentRequests(userId);
        // 收到的待处理请求
        List<FriendRequest> received = friendMapper.getReceivedRequests(userId);

        List<FriendVO> result = new ArrayList<>();

        friends.forEach(u -> result.add(
                new FriendVO(u.getId(), u.getNickname(), u.getAvatar(), 1, "friend")
        ));

        sent.forEach(r -> {
            User toUser = userMapper.getById(r.getToUserId());
            result.add(new FriendVO(
                    toUser.getId(),
                    toUser.getNickname(),
                    toUser.getAvatar(),
                    r.getStatus(),
                    "sent"
            ));
        });

        received.forEach(r -> {
            User fromUser = userMapper.getById(r.getFromUserId());
            result.add(new FriendVO(
                    fromUser.getId(),
                    fromUser.getNickname(),
                    fromUser.getAvatar(),
                    r.getStatus(),
                    "received"
            ));
        });

        return result;
    }
}