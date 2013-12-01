package com.hk.pact.dao;

import com.hk.constants.core.EnumRole;
import com.hk.domain.user.Permission;
import com.hk.domain.user.Role;

import java.util.List;

public interface RoleDao extends BaseDao {

    public Role getRoleByName(EnumRole enumRole);

    public Role getRoleByName(String roleName);

    public List<Role> listAllExceptAdmins();

    public List<Role> listAllBasicRoles();

    public List<Role> listAllCustomerSupportRoles();

    public List<Role> listAllOperationsRoles();

    public List<Role> listAllHRRoles();

    public Permission getPermissionByName(String permissionName);
}
