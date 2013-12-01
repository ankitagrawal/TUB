package com.hk.db.seed.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.core.EnumRole;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.user.Role;

@Component
public class RoleSeedData extends BaseSeedData {
    public void insert(java.lang.String name) {
        Role role = new Role();
        role.setName(name);
        save(role);
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
