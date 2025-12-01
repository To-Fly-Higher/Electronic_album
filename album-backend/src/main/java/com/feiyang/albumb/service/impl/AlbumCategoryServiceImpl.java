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
}
