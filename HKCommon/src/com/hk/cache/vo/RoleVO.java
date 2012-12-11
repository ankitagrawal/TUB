package com.hk.cache.vo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.hk.domain.user.Permission;
import com.hk.domain.user.Role;

/**
 * @author vaibhav.adlakha
 */
@SuppressWarnings("serial")
public class RoleVO implements Serializable {

    private String            name;

    private Set<PermissionVO> permissions = new HashSet<PermissionVO>();

    public RoleVO(Role role) {
        this.name = role.getName();

        for (Permission permission : role.getPermissions()) {
            this.permissions.add(new PermissionVO(permission));
        }
    }
    
    public Role getRole(){
        Role role = new Role();
        role.setName(this.name);
        
        return role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<PermissionVO> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionVO> permissions) {
        this.permissions = permissions;
    }

}
