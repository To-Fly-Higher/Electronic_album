package com.feiyang.albumb.service.impl;

import com.feiyang.albumb.entity.Album;
import com.feiyang.albumb.entity.AlbumCategory;
import com.feiyang.albumb.mapper.AlbumMapper;
import com.feiyang.albumb.service.AlbumService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class AlbumServiceImpl implements AlbumService {

    private final AlbumMapper albumMapper;

    public AlbumServiceImpl(AlbumMapper albumMapper) {
        this.albumMapper = albumMapper;
    }

    @Override
    public List<Album> getAlbumsByUserId(Integer userId) {
        return albumMapper.getAlbumsByUserId(userId);
    }


    @Override
    public void createAlbum(Album album) {
        albumMapper.insertAlbum(album);
    }

    @Override
    public void updateAlbum(Album album) {
        albumMapper.updateAlbum(album);
    }

    @Override
    public Album getAlbumById(Integer id) {
        return albumMapper.findById(id);
    }
    @Override
    public boolean deleteAlbum(Integer id) {
        Album album = albumMapper.getById(id);
        if (album == null) return false;

        // 删除封面文件
        if (album.getCoverUrl() != null && !album.getCoverUrl().isEmpty()) {
            String projectPath = System.getProperty("user.dir");
            String filePath = projectPath + album.getCoverUrl(); // coverUrl 前面是 /uploads/...
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        }

        // 删除数据库记录
        int result = albumMapper.deleteById(id);
        return result > 0;
    }
    @Override
    public Album getAlbumInf(Integer albumid) {
        return albumMapper.getAlbumInf(albumid);
    }
}
