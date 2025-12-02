package com.feiyang.albumb.service;

import com.feiyang.albumb.entity.AlbumCategory;

import java.util.List;

public interface AlbumCategoryService {
    List<AlbumCategory> getAllCategories();

    void addCategory(String name);

    void updateCategory(Integer id, String name);

    void deleteCategory(Integer id);
}
