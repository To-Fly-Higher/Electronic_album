package com.feiyang.albumb.service;

import com.feiyang.albumb.entity.Album;
import java.util.List;

public interface AlbumService {
    List<Album> getAlbumsByUserId(Integer userId);

    void createAlbum(Album album);
    void updateAlbum(Album album);
    Album getAlbumById(Integer id);

    boolean deleteAlbum(Integer id);
    Album getAlbumInf(Integer id);
}
