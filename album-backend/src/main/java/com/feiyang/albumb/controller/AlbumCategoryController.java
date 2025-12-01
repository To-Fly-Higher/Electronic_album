package com.feiyang.albumb.controller;


import com.feiyang.albumb.common.Result;
import com.feiyang.albumb.entity.AlbumCategory;
import com.feiyang.albumb.service.AlbumCategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/album")
public class AlbumCategoryController {

    private final AlbumCategoryService categoryService;

    public AlbumCategoryController(AlbumCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public Result<List<AlbumCategory>> getCategories() {
        List<AlbumCategory> categories = categoryService.getAllCategories();
        return Result.success("获取成功", categories);
    }
}
