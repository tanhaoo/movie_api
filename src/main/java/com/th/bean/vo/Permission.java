package com.th.bean.vo;

import lombok.Data;

/**
 * @author TanHaooo
 * @date 2021/3/18 16:30
 */
@Data
public class Permission {

    private String PermissionId;

    public Permission(String permissionId) {
        PermissionId = permissionId;
    }

}
