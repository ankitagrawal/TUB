package com.hk.pact.service;

import com.hk.constants.core.EnumRole;
import com.hk.domain.user.Permission;
import com.hk.domain.user.Role;

public interface RoleService {

    public Role getRoleByName(EnumRole enumRole);

    public Role getRoleByName(String roleName);

   /* public List<Role> listAllExceptAdmins();

    public List<Role> listAllBasicRoles();*/

    public Permission save(Permission permission);

}
