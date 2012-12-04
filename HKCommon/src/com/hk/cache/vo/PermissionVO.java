package com.hk.cache.vo;

import java.io.Serializable;

import com.hk.domain.user.Permission;

/**
 * @author vaibhav.adlakha
 */
@SuppressWarnings("serial")
public class PermissionVO implements Serializable {

    private String name;

    
    
    public PermissionVO() {
    
    }

    public PermissionVO(Permission permission) {
        this.name = permission.getName();
    }

    public Permission getPermission() {
        Permission permission = new Permission();
        permission.setName(name);
        return permission;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
