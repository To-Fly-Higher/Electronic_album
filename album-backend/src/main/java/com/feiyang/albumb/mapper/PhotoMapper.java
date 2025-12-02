package com.feiyang.albumb.mapper;

import com.feiyang.albumb.entity.Photo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PhotoMapper {

    // 插入一张图片记录
    @Insert("INSERT INTO photo(user_id, url, name) " +
            "VALUES(#{userId}, #{url}, #{name})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Photo photo);

    // 根据相册ID查询相册下所有图片
    // 通过 album_photo 关联
    @Select("SELECT p.id, p.user_id, p.url, p.name " +
            "FROM photo p " +
            "JOIN album_photo ap ON ap.photo_id = p.id " +
            "WHERE ap.album_id = #{albumId}")
    List<Photo> findByAlbumId(@Param("albumId") Integer albumId);

    // 删除一张图片
    // 不删 album_photo 关联
    @Delete("DELETE FROM photo WHERE id = #{id}")
    int deleteById(@Param("id") Integer id);


    @Select("SELECT p.id, p.name, p.url " +
            "FROM photo p " +
            "JOIN album_photo ap ON p.id = ap.photo_id " +
            "WHERE ap.album_id = #{albumId}")
    List<Photo> getPhotosByAlbumId(@Param("albumId") Integer albumId);


//
//    @Select("SELECT id, name, url FROM photo WHERE album_id = #{albumId}")
//    List<Photo> getPhotoByAlbumId(Integer albumId);
}
