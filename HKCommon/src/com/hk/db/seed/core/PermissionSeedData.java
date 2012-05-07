package db.seed.master;

import com.google.inject.Inject;
import app.dao.PermissionDao;
import app.domain.Permission;

import java.util.List;
import java.util.ArrayList;

import mhc.common.constants.EnumPermission;

public class PermissionSeedData {

  @Inject PermissionDao permissionDao;

  public void insert(java.lang.String name) {
    Permission permission = new Permission();
    permission.setName(name);
    permissionDao.save(permission);
  }

  public void invokeInsert() {
    List<String> pkList = new ArrayList<String>();

    for (EnumPermission permission : EnumPermission.values()) {

      if (pkList.contains(permission.getPermissionName()))
        throw new RuntimeException("Duplicate key " + permission.getPermissionName());
      else pkList.add(permission.getPermissionName());

      insert(permission.getPermissionName());
    }
  }

}
