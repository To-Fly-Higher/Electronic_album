package com.feiyang.albumb.service;

import com.feiyang.albumb.entity.Photo;
import com.feiyang.albumb.vo.PhotoVO;

import java.util.List;

public interface PhotoService {

    // 相册里的图片列表
    List<Photo> listPhotosByAlbum(Integer albumId);

    // 保存图片到相册（包括 photo 和 album_photo）
    void savePhotoToAlbum(Integer albumId, Photo photo);

    // 从相册中删除一张图片
    // 处理关联 & 图片本身
    boolean deletePhoto(Integer albumId, Integer photoId);

    List<PhotoVO> getPhotosByAlbumId(Integer albumId);

}
