package com.feiyang.albumb.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendAlbumVO {
    private Integer id;         // album id
    private String name;        // literal
    private String cover;       // album cover url
    private String ownerName;   // xxx's public album
}
