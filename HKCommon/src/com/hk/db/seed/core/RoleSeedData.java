package com.hk.db.seed.core;

import java.util.ArrayList;
import java.util.List;

import com.hk.constants.core.EnumRole;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.user.Role;
import com.hk.pact.dao.RoleDao;

public class RoleSeedData extends BaseSeedData {

    RoleDao roleDao;

    public void insert(java.lang.String name) {
        Role role = new Role();
        role.setName(name);
        roleDao.save(role);
    }

    public void invokeInsert() {
        List<String> pkList = new ArrayList<String>();

        for (EnumRole role : EnumRole.values()) {

            if (pkList.contains(role.getRoleName()))
                throw new RuntimeException("Duplicate key " + role.getRoleName());
            else
                pkList.add(role.getRoleName());

            insert(role.getRoleName());
        }
    }

}
