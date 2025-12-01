package com.feiyang.albumb.controller;

import com.feiyang.albumb.common.Result;
import com.feiyang.albumb.service.FriendService;
import com.feiyang.albumb.vo.FriendVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/friend")
public class FriendController {


    private final FriendService friendService;

    // 构造器注入（推荐）
    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }
    @GetMapping("/list")
    public Result<List<FriendVO>> getFriendList(@RequestParam("user_id") Integer userId) {
        List<FriendVO> list = friendService.getFriendList(userId);
        return Result.success(list);
    }
}