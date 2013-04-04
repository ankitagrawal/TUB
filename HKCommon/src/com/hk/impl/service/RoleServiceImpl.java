package com.hk.impl.service;

import com.hk.constants.core.EnumRole;
import com.hk.domain.user.Permission;
import com.hk.domain.user.Role;
import com.hk.pact.dao.RoleDao;
import com.hk.pact.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public Role getRoleByName(EnumRole enumRole) {
        return getRoleDao().getRoleByName(enumRole);
    }

    @Override
    public Role getRoleByName(String roleName) {
        return getRoleDao().getRoleByName(roleName);
    }

    /*
     * @Override public List<Role> listAllBasicRoles() { return getRoleDao().listAllBasicRoles(); }
     */

    /*
     * @Override public List<Role> listAllExceptAdmins() { return getRoleDao().listAllExceptAdmins(); }
     */

    public RoleDao getRoleDao() {
        return roleDao;
    }

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }
}
