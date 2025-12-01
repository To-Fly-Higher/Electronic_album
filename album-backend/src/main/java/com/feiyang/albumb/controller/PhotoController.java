package com.feiyang.albumb.controller;


import com.feiyang.albumb.entity.Photo;
import com.feiyang.albumb.service.PhotoService;
import com.feiyang.albumb.vo.PhotoVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/album")
public class PhotoController {

    private final PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/{albumId}/images")
    public Map<String, Object> getPhotos(@PathVariable Integer albumId) {
        List<PhotoVO> photos = photoService.getPhotosByAlbumId(albumId);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "success");
        result.put("data", photos);
        return result;
    }
}
