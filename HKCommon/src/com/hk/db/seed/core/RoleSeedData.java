package db.seed.master;

import com.google.inject.Inject;
import app.dao.RoleDao;
import app.domain.Role;

import java.util.List;
import java.util.ArrayList;

import mhc.common.constants.EnumRole;

public class RoleSeedData {

  @Inject
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
      else pkList.add(role.getRoleName());

      insert(role.getRoleName());
    }
  }

}
