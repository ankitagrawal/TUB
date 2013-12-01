package com.hk.db.seed.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.core.EnumPermission;
import com.hk.constants.core.EnumRoleHasPermission;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.user.Permission;
import com.hk.domain.user.Role;

/**
 * User: rahul Time: 5 Jan, 2010 3:09:49 PM
 */
@Component
public class RoleHasPermissionSeedData extends BaseSeedData {

    public void insert(java.lang.String name, List<EnumPermission> enumPermissions) {
        Role role = getBaseDao().get(Role.class, name);
        if (role == null) {
            throw new RuntimeException("Role " + name + " not found while inserting RoleHasPermission");
        }
        for (EnumPermission enumPermission : enumPermissions) {
            Permission permission = getBaseDao().get(Permission.class, enumPermission.getPermissionName());
            if (permission == null) {
                throw new RuntimeException("Error while inserting permission " + enumPermission.getPermissionName() + " for role " + name);
            }
            role.getPermissions().add(permission);
        }
        save(role);
    }

    public void invokeInsert() {

        // removing any existing role permission mappings
        for (Role role : getBaseDao().getAll(Role.class)) {
            role.setPermissions(new HashSet<Permission>(0));
            save(role);
        }

        List<String> pkList = new ArrayList<String>();

        for (EnumRoleHasPermission roleHasPermission : EnumRoleHasPermission.values()) {

            if (pkList.contains(roleHasPermission.getRoleName()))
                throw new RuntimeException("Duplicate key " + roleHasPermission.getRoleName());
            else
                pkList.add(roleHasPermission.getRoleName());

            insert(roleHasPermission.getRoleName(), roleHasPermission.getEnumPermissions());
        }
    }

}
