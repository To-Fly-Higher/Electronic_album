package com.feiyang.albumb.controller;

import com.feiyang.albumb.common.Result;
import com.feiyang.albumb.entity.User;
import com.feiyang.albumb.service.FriendRequestService;
import com.feiyang.albumb.service.FriendService;
import com.feiyang.albumb.service.PhotoLikeService;
import com.feiyang.albumb.service.UserService;
import com.feiyang.albumb.vo.FriendAlbumVO;
import com.feiyang.albumb.vo.FriendInf;
import com.feiyang.albumb.vo.FriendVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/friend")
public class FriendController {


    private final FriendService friendService;
    private final UserService userService;
    @Autowired
    private FriendRequestService friendRequestService;
    // 构造器注入（推荐）
    public FriendController(FriendService friendService,UserService userService,PhotoLikeService photoLikeService) {
        this.friendService = friendService;
        this.userService = userService;

    }


    @GetMapping("/list")
    public Result<List<FriendVO>> getFriendList(@RequestParam("user_id") Integer userId) {
        List<FriendVO> list = friendService.getFriendList(userId);
        return Result.success(list);
    }

    @GetMapping("/search")
    public Map<String, Object> searchFriend(@RequestParam String name) {
        Map<String, Object> result = new HashMap<>();
        User user = userService.findByNickname(name);

        if (user != null) {
            result.put("code", 200);
            result.put("msg", "success");
            result.put("data", user);
        } else {
            result.put("code", 200);
            result.put("msg", "success");
            result.put("data", null);
        }

        return result;
    }

    // 处理好友请求
    @PostMapping("/handle-request")
    public Result<Void> handleRequest(@RequestBody Map<String, Integer> body) {
        Integer userId   = body.get("user_id");   // 当前用户（收到请求的人）
        Integer friendId = body.get("request_id"); // 对方用户（发起请求的人）
        Integer action   = body.get("action");    // 1=同意, 2=拒绝

        friendService.handleFriendRequest(userId, friendId, action);
        return Result.success(null);
    }


    // 拉取好友公开相册
    @GetMapping("/{friendId}/albums")
    public Result<List<FriendAlbumVO>> getFriendAlbums(@PathVariable("friendId") Integer friendId) {
        List<FriendAlbumVO> albums = friendService.listFriendPublicAlbums(friendId);
        return Result.success(albums);
    }
    @GetMapping("/{friendId}")
    public Result<FriendInf> getFriendInformation(@PathVariable Integer friendId) {
        FriendInf friendInf = friendService.getFriendInf(friendId);
        return Result.success(friendInf);
    }


    @PostMapping("/request")
    public Map<String, Object> sendFriendRequest(@RequestBody Map<String, Object> paramMap) {
        Map<String, Object> result = new HashMap<>(2);
        try {
//            logger.info("=== 好友请求接口开始处理，前端参数：{} ===", paramMap);
            friendRequestService.sendFriendRequest(paramMap);
            result.put("code", 200);
            result.put("msg", "好友请求已发送");
//            logger.info("=== 好友请求接口处理成功，响应：{} ===", result);
        } catch (RuntimeException e) {
            // 业务异常：打印消息+堆栈
//            logger.error("=== 好友请求业务异常：{} ===", e.getMessage(), e);
            result.put("code", 400);
            result.put("msg", e.getMessage());
        }
        return result;
    }

}