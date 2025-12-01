package com.feiyang.albumb.mapper;

import com.feiyang.albumb.entity.AlbumPhoto;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AlbumPhotoMapper {

    // 建立相册-图片关联
    @Insert("INSERT INTO album_photo(album_id, photo_id) " +
            "VALUES(#{albumId}, #{photoId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(AlbumPhoto albumPhoto);

    // 删除某个相册下的一张图片的关联关系
    @Delete("DELETE FROM album_photo WHERE album_id = #{albumId} AND photo_id = #{photoId}")
    int deleteByAlbumAndPhoto(@Param("albumId") Integer albumId,
                              @Param("photoId") Integer photoId);

    // 根据图片ID删除所有关联（比如彻底删图片时用）
    @Delete("DELETE FROM album_photo WHERE photo_id = #{photoId}")
    int deleteByPhotoId(@Param("photoId") Integer photoId);
}
