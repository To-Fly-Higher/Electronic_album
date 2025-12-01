package com.feiyang.albumb.controller;

import com.feiyang.albumb.common.Result;
import com.feiyang.albumb.entity.User;
import com.feiyang.albumb.service.FriendService;
import com.feiyang.albumb.service.UserService;
import com.feiyang.albumb.vo.FriendVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}