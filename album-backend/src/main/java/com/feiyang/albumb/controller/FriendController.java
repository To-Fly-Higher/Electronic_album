package com.feiyang.albumb.controller;

import com.feiyang.albumb.common.Result;
import com.feiyang.albumb.entity.User;
import com.feiyang.albumb.service.FriendService;
import com.feiyang.albumb.service.UserService;
import com.feiyang.albumb.vo.FriendAlbumVO;
import com.feiyang.albumb.vo.FriendVO;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/friend")
public class FriendController {


    private final FriendService friendService;
    private final UserService userService;

    // 构造器注入（推荐）
    public FriendController(FriendService friendService,UserService userService) {
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

    /**
     * 处理好友请求
     *
     * @param body 请求体
     * @return 处理执行结果
     */
    @PostMapping("/handle-request")
    public Result<Void> handleRequest(@RequestBody Map<String, Integer> body) {
        Integer userId = body.get("user_id");
        Integer requestId = body.get("request_id");
        Integer action = body.get("action");

        // 校验 & 处理
        friendService.handleFriendRequest(userId, requestId, action);

        // done
        return Result.success(null);
    }

    @GetMapping("/{friendId}/albums")
    public Result<List<FriendAlbumVO>> getFriendAlbums(@PathVariable("friendId") Integer friendId) {
        List<FriendAlbumVO> albums = friendService.listFriendPublicAlbums(friendId);
        return Result.success(albums);
    }
}