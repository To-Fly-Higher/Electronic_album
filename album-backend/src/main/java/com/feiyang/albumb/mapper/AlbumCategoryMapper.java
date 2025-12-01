package com.feiyang.albumb.mapper;

import com.feiyang.albumb.entity.AlbumCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AlbumCategoryMapper {

    @Select("SELECT id, name FROM album_category ORDER BY id ASC")
    List<AlbumCategory> getAllCategories();
}