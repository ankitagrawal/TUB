package com.hk.service;

import java.util.List;

import com.hk.constants.core.EnumRole;
import com.hk.domain.user.Role;

public interface RoleService {

    public Role getRoleByName(EnumRole enumRole);

    public Role getRoleByName(String roleName);

    public List<Role> listAllExceptAdmins();

    public List<Role> listAllBasicRoles();

}
