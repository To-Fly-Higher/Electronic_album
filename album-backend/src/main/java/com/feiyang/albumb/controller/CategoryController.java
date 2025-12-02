package com.feiyang.albumb.controller;

import com.feiyang.albumb.common.Result;
import com.feiyang.albumb.entity.AlbumCategory;
import com.feiyang.albumb.service.AlbumCategoryService;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryController {

    private final AlbumCategoryService categoryService;

    public CategoryController(AlbumCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/category/add")
    public Result<Void> addCategory(@RequestBody AlbumCategory category) {
        categoryService.addCategory(category.getName());
        return Result.success("成功", null);
    }

    @PutMapping("/category/update/{id}")
    public Result<Void> updateCategory(@PathVariable Integer id,
                                       @RequestBody AlbumCategory category) {
        categoryService.updateCategory(id, category.getName());
        return Result.success("成功", null);
    }

    @DeleteMapping("/category/delete/{id}")
    public Result<Void> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return Result.success("成功", null);
    }
}
