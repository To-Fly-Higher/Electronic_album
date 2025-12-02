package com.feiyang.albumb.service.impl;

import com.feiyang.albumb.entity.AlbumPhoto;
import com.feiyang.albumb.entity.Comment;
import com.feiyang.albumb.entity.Photo;
import com.feiyang.albumb.mapper.AlbumPhotoMapper;
import com.feiyang.albumb.mapper.CommentMapper;
import com.feiyang.albumb.mapper.PhotoLikeMapper;
import com.feiyang.albumb.mapper.PhotoMapper;
import com.feiyang.albumb.service.PhotoService;
import com.feiyang.albumb.vo.PhotoVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PhotoServiceImpl implements PhotoService {

    private final PhotoMapper photoMapper;
    private final AlbumPhotoMapper albumPhotoMapper;
    private final PhotoLikeMapper likeMapper;
    private final CommentMapper commentMapper;
    public PhotoServiceImpl(PhotoMapper photoMapper, AlbumPhotoMapper albumPhotoMapper
     ,PhotoLikeMapper likeMapper, CommentMapper commentMapper) {
        this.photoMapper = photoMapper;
        this.albumPhotoMapper = albumPhotoMapper;
        this.likeMapper = likeMapper;
        this.commentMapper = commentMapper;
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

    @Override
    public List<PhotoVO> getPhotosByAlbumId(Integer albumId) {

        List<Photo> list = photoMapper.getPhotosByAlbumId(albumId);
        List<PhotoVO> result = new ArrayList<>();

        for (Photo p : list) {
            PhotoVO vo = new PhotoVO();
            vo.setId(p.getId());
            vo.setName(p.getName());
            vo.setUrl(p.getUrl());
            vo.setLikes(likeMapper.getLikeByPhotoId(p.getId()));
            vo.setComments(commentMapper.getCommentsByPhotoId(p.getId()));
            result.add(vo);
        }

        return result;
    }

    //发表评论
    @Override
    public void addComment(Integer photoId, Integer userId, String comment) {
        Comment comment1 = new Comment();
        comment1.setPhotoId(photoId);
        comment1.setUserId(userId);
        comment1.setContent(comment);
        commentMapper.insertComment(comment1);
    }

    @Override
    @Transactional
    public boolean deleteComment(Integer commentId) {
        return commentMapper.deleteById(commentId) > 0;
    }
}
