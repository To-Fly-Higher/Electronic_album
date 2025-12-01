package com.feiyang.albumb.service.impl;

import com.feiyang.albumb.entity.AlbumPhoto;
import com.feiyang.albumb.entity.Photo;
import com.feiyang.albumb.mapper.AlbumPhotoMapper;
import com.feiyang.albumb.mapper.PhotoMapper;
import com.feiyang.albumb.service.PhotoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PhotoServiceImpl implements PhotoService {

    private final PhotoMapper photoMapper;
    private final AlbumPhotoMapper albumPhotoMapper;

    public PhotoServiceImpl(PhotoMapper photoMapper, AlbumPhotoMapper albumPhotoMapper) {
        this.photoMapper = photoMapper;
        this.albumPhotoMapper = albumPhotoMapper;
    }

    @Override
    public List<Photo> listPhotosByAlbum(Integer albumId) {
        return photoMapper.findByAlbumId(albumId);
    }

    @Override
    @Transactional
    public void savePhotoToAlbum(Integer albumId, Photo photo) {
        // 插入 photo 表（生成 id）
        photoMapper.insert(photo);

        // 插入 album_photo 关联
        AlbumPhoto albumPhoto = new AlbumPhoto();
        albumPhoto.setAlbumId(albumId);
        albumPhoto.setPhotoId(photo.getId());
        albumPhotoMapper.insert(albumPhoto);
    }

    @Override
    @Transactional
    public boolean deletePhoto(Integer albumId, Integer photoId) {
        // 删掉 album_photo 关联
        albumPhotoMapper.deleteByAlbumAndPhoto(albumId, photoId);

        // 再删 photo 本身
        int rows = photoMapper.deleteById(photoId);

        return rows > 0;
    }
}
