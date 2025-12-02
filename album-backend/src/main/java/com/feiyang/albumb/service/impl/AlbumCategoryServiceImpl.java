package com.feiyang.albumb.service.impl;

import com.feiyang.albumb.entity.AlbumCategory;
import com.feiyang.albumb.mapper.AlbumCategoryMapper;
import com.feiyang.albumb.service.AlbumCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumCategoryServiceImpl implements AlbumCategoryService {

    private final AlbumCategoryMapper categoryMapper;

    public AlbumCategoryServiceImpl(AlbumCategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<AlbumCategory> getAllCategories() {
        return categoryMapper.getAllCategories();
    }

    @Override
    public void addCategory(String name) {
        AlbumCategory category = new AlbumCategory();
        category.setName(name);
        categoryMapper.insertCategory(category);
    }

    @Override
    public void updateCategory(Integer id, String name) {
        AlbumCategory category = new AlbumCategory();
        category.setId(id);
        category.setName(name);
        categoryMapper.updateCategory(category);
    }

    @Override
    public void deleteCategory(Integer id) {
        categoryMapper.deleteCategory(id);
    }
}
