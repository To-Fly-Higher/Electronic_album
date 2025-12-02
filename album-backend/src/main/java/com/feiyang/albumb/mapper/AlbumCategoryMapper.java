package com.feiyang.albumb.mapper;

import com.feiyang.albumb.entity.AlbumCategory;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AlbumCategoryMapper {

    // 获取全部类别
    @Select("SELECT id, name FROM album_category ORDER BY id ASC")
    List<AlbumCategory> getAllCategories();

    // 添加类别
    @Insert("INSERT INTO album_category(name) VALUES(#{name})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertCategory(AlbumCategory category);

    // 更新类别
    @Update("UPDATE album_category SET name = #{name} WHERE id = #{id}")
    int updateCategory(AlbumCategory category);

    // 删除类别
    @Delete("DELETE FROM album_category WHERE id = #{id}")
    int deleteCategory(@Param("id") Integer id);
}