package com.hk.pact.dao;

import java.util.List;

import com.hk.constants.core.EnumRole;
import com.hk.domain.user.Role;

public interface RoleDao extends BaseDao {

    public Role getRoleByName(EnumRole enumRole);

    public Role getRoleByName(String roleName);

    public List<Role> listAllExceptAdmins();

    public List<Role> listAllBasicRoles();

    public List<Role> listAllCustomerSupportRoles();

    public List<Role> listAllOperationsRoles();

}
