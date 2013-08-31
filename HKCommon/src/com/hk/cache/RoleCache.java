package com.hk.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hk.cache.vo.RoleVO;
import com.hk.constants.core.EnumRole;
import com.hk.domain.user.Role;
import com.hk.pact.service.RoleService;
import com.hk.pact.dao.BaseDao;
import com.hk.service.ServiceLocatorFactory;

/**
 * @author vaibhav.adlakha
 */
public class RoleCache {

  private static RoleCache _instance = new RoleCache();
  private RoleCache _transient;

  private Map<String, RoleVO> nameToRoleCache = new HashMap<String, RoleVO>();

  private RoleService roleService;
  private BaseDao baseDao;

  private RoleCache() {
  }

  public static RoleCache getInstance() {
    return _instance;
  }

  public void addRole(RoleVO roleVO) {
    if (StringUtils.isNotBlank(roleVO.getName())) {
      nameToRoleCache.put(roleVO.getName(), roleVO);
    }

  }

  public RoleVO getRoleByName(EnumRole enumRole) {
    return getRoleByName(enumRole.getRoleName());
  }

  public RoleVO getRoleByName(String roleName) {
    RoleVO roleVO = nameToRoleCache.get(roleName);

    /**
     * if role is not in cache try and attempt to find from db
     */
    if (roleVO == null) {
      Role role = getRoleService().getRoleByName(roleName);
      roleVO = new RoleVO(role);
    }
    return roleVO;
  }

  public void freeze() {
    _instance = this;
  }

  public void reset() {
    _transient = new RoleCache();
  }

  public RoleCache getTransientCache() {
    return _transient;
  }

  public RoleService getRoleService() {
    if (roleService == null) {
      roleService = (RoleService) ServiceLocatorFactory.getService(RoleService.class);
    }
    return roleService;
  }

  public BaseDao getBaseDao() {
    if(baseDao == null){
      baseDao = (BaseDao) ServiceLocatorFactory.getService(BaseDao.class);
    }
    return baseDao;
  }

  public void populateRoleCache() {
    RoleCache.getInstance().reset();
    RoleCache roleCache = RoleCache.getInstance().getTransientCache();
    List<Role> allRoles = getBaseDao().getAll(Role.class);

    for (Role role : allRoles) {
      roleCache.addRole(new RoleVO(role));
    }
    roleCache.freeze();

  }
}
