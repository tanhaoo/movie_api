package com.th.bean.vo;

import lombok.Data;

/**
 * @author TanHaooo
 * @date 2021/3/18 16:42
 */
@Data
public class UserInfo {

    private String name;
    private String avatar;
    private Permissions role;

    public UserInfo(String name, String avatar, Permissions role) {
        this.name = name;
        this.avatar = avatar;
        this.role = role;
    }

    public UserInfo(String name, Permissions role) {
        this.name = name;
        this.role = role;
    }
}
