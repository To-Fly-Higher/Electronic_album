package com.feiyang.albumb.service.impl;

import com.feiyang.albumb.entity.Album;
import com.feiyang.albumb.entity.FriendRequest;
import com.feiyang.albumb.entity.User;
import com.feiyang.albumb.mapper.AlbumMapper;
import com.feiyang.albumb.mapper.FriendMapper;
import com.feiyang.albumb.mapper.UserMapper;
import com.feiyang.albumb.service.FriendService;
import com.feiyang.albumb.vo.FriendVO;
import com.feiyang.albumb.vo.FriendAlbumVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {

    private final FriendMapper friendMapper;
    private final UserMapper userMapper;
    private final AlbumMapper albumMapper;

    @Autowired
    public FriendServiceImpl(FriendMapper friendMapper,
                             UserMapper userMapper,
                             AlbumMapper albumMapper) {
        this.friendMapper = friendMapper;
        this.userMapper = userMapper;
        this.albumMapper = albumMapper;
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

    @Override
    @Transactional
    public void handleFriendRequest(Integer userId, Integer requestId, Integer action) {
        if (userId == null || requestId == null || action == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        // 查请求
        FriendRequest request = friendMapper.getRequestById(requestId);
        if (request == null) {
            throw new RuntimeException("好友请求不存在");
        }

        // 校验当前用户是否有权限处理，必须是 toUserId
        if (!userId.equals(request.getToUserId())) {
            throw new RuntimeException("无权处理该好友请求");
        }

        // 只能处理 待处理 的请求
        if (request.getStatus() != 0) {
            throw new RuntimeException("该好友请求已处理");
        }

        // 根据 action 处理
        if (action == 1) {
            // 同意：更新请求状态 + 建立双向好友关系
            friendMapper.updateRequestStatus(requestId, 1);

            Integer fromUserId = request.getFromUserId();
            Integer toUserId = request.getToUserId(); // 也就是 userId

            // 正向反向插 2 条记录
            // 先查 count 避免重复插入
            if (friendMapper.countFriendship(toUserId, fromUserId) == 0) {
                friendMapper.insertFriend(toUserId, fromUserId);
            }
            if (friendMapper.countFriendship(fromUserId, toUserId) == 0) {
                friendMapper.insertFriend(fromUserId, toUserId);
            }

        } else if (action == 2) {
            // 拒绝：只更新状态
            friendMapper.updateRequestStatus(requestId, 2);
        } else {
            // invalid action code
            throw new IllegalArgumentException("非法的操作类型");
        }
    }

    @Override
    public List<FriendAlbumVO> listFriendPublicAlbums(Integer friendId) {
        if (friendId == null) {
            return Collections.emptyList();
        }

        // 查询好友的公开相册
        List<Album> albums = albumMapper.getPublicAlbumsByUserId(friendId);
        if (albums == null || albums.isEmpty()) {
            return Collections.emptyList();
        }

        // 查询好友昵称，用于 ownerName
        User friend = userMapper.getById(friendId);
        String ownerName = friend != null ? friend.getNickname() : null;

        // Album -> FriendAlbumVO
        List<FriendAlbumVO> result = new ArrayList<>();
        for (Album album : albums) {
            FriendAlbumVO vo = new FriendAlbumVO();
            vo.setId(album.getId());
            vo.setName(album.getName());
            vo.setCover(album.getCoverUrl());   // 注意字段名：Album 是 coverUrl，VO 是 cover
            vo.setOwnerName(ownerName);

            result.add(vo);
        }

        return result;
    }
}