package com.feiyang.albumb.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FriendVO {
    private Long id;
    private String name;
    private String avatar;
    private Integer status;
    private String type; // sent / received
}
