package com.hk.db.seed.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.core.EnumPermission;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.user.Permission;

@Component
public class PermissionSeedData extends BaseSeedData {

    public void insert(java.lang.String name) {
        Permission permission = new Permission();
        permission.setName(name);
        save(permission);
    }

    public void invokeInsert() {
        List<String> pkList = new ArrayList<String>();

        for (EnumPermission permission : EnumPermission.values()) {

            if (pkList.contains(permission.getPermissionName()))
                throw new RuntimeException("Duplicate key " + permission.getPermissionName());
            else
                pkList.add(permission.getPermissionName());

            insert(permission.getPermissionName());
        }
    }

}
