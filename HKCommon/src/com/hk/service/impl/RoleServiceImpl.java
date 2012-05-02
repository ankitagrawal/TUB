package com.hk.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.constants.core.EnumRole;
import com.hk.dao.impl.RoleDao;
import com.hk.domain.user.Role;
import com.hk.service.RoleService;

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

    @Override
    public List<Role> listAllBasicRoles() {
        return getRoleDao().listAllBasicRoles();
    }

    @Override
    public List<Role> listAllExceptAdmins() {
        return getRoleDao().listAllExceptAdmins();
    }

    public RoleDao getRoleDao() {
        return roleDao;
    }

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

}
