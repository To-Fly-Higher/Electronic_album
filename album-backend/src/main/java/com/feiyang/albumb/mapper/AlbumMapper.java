package com.feiyang.albumb.mapper;

import com.feiyang.albumb.entity.Album;
import com.feiyang.albumb.entity.AlbumCategory;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AlbumMapper {

    @Select("SELECT id, user_id, category_id, name, cover_url, remark, is_public " +
            "FROM album " +
            "WHERE user_id = #{userId} " +
            "ORDER BY id ASC")
    List<Album> getAlbumsByUserId(@Param("userId") Integer userId);




    // 新建相册
    @Insert("INSERT INTO album(user_id, category_id, name, cover_url, remark, is_public) " +
            "VALUES(#{userId}, #{categoryId}, #{name}, #{coverUrl}, #{remark}, #{isPublic})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertAlbum(Album album);

    // 修改相册
    @Update("UPDATE album SET category_id=#{categoryId}, name=#{name}, cover_url=#{coverUrl}, " +
            "remark=#{remark}, is_public=#{isPublic} WHERE id=#{id}")
    void updateAlbum(Album album);

    // 根据ID查询相册（修改时使用）
    @Select("SELECT * FROM album WHERE id=#{id}")
    Album findById(@Param("id") Integer id);

    @Select("SELECT * FROM album WHERE id = #{id}")
    Album getById(Integer id);

    @Delete("DELETE FROM album WHERE id = #{id}")
    int deleteById(Integer id);


    /* 好友相册 */

    // 查用户公开相册
    @Select("SELECT id, user_id, category_id, name, cover_url, remark, is_public " +
            "FROM album " +
            "WHERE user_id = #{userId} AND is_public = 1 " +
            "ORDER BY id ASC")
    List<Album> getPublicAlbumsByUserId(@Param("userId") Integer userId);

    //获取相册信息
    @Select("select * from album where id = #{id}")
    public Album getAlbumInf(Integer id);
}
