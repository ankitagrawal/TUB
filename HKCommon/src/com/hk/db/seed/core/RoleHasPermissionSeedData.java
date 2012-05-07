package db.seed.master;

import com.google.inject.Inject;
import app.domain.Role;
import app.domain.Permission;
import app.dao.RoleDao;
import app.dao.PermissionDao;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;

import mhc.common.constants.EnumRoleHasPermission;
import mhc.common.constants.EnumPermission;

/**
 * User: rahul
 * Time: 5 Jan, 2010 3:09:49 PM
 */
public class RoleHasPermissionSeedData {

  @Inject RoleDao roleDao;
  @Inject PermissionDao permissionDao;

  public void insert(java.lang.String name, List<EnumPermission> enumPermissions) {
    Role role = roleDao.find(name);
    if (role == null) {
      throw new RuntimeException("Role "+name+" not found while inserting RoleHasPermission");
    }
    for (EnumPermission enumPermission : enumPermissions) {
      Permission permission = permissionDao.find(enumPermission.getPermissionName());
      if (permission == null) {
        throw new RuntimeException("Error while inserting permission "+enumPermission.getPermissionName()+" for role "+name);
      }
      role.getPermissions().add(permission);
    }
    roleDao.save(role);
  }

  public void invokeInsert() {

    // removing any existing role permission mappings
    for (Role role : roleDao.listAll()) {
      role.setPermissions(new HashSet<Permission>(0));
      roleDao.save(role);
    }


    List<String> pkList = new ArrayList<String>();

    for (EnumRoleHasPermission roleHasPermission : EnumRoleHasPermission.values()) {

      if (pkList.contains(roleHasPermission.getRoleName()))
        throw new RuntimeException("Duplicate key " + roleHasPermission.getRoleName());
      else pkList.add(roleHasPermission.getRoleName());

      insert(roleHasPermission.getRoleName(), roleHasPermission.getEnumPermissions());
    }
  }

}
