package com.hk.impl.dao.core;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.akube.framework.shiro.Principal;
import com.akube.framework.shiro.manager.UserSecurityManager;
import com.hk.cache.RoleCache;
import com.hk.cache.vo.PermissionVO;
import com.hk.cache.vo.RoleVO;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.pact.dao.RoleDao;
import com.hk.pact.dao.user.UserDao;
import com.shiro.PrincipalImpl;

/**
 * Author: Kani Date: Sep 7, 2008
 */
@Repository
public class UserSecurityManagerImpl implements UserSecurityManager {

    private UserDao userDao;
    private RoleDao roleDao;

    @Autowired
    public UserSecurityManagerImpl(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    /*
     * Implementation of UserSecurityManager for Shiro Realm
     */

    public String getPasswordForUser(String userName) {

        User user = getUserDao().findByLogin(userName);
        if (user == null)
            return null;
        return user.getPasswordChecksum();

    }

    public Principal getPrincipal(String loginName) {

        User user = getUserDao().findByLogin(loginName);
        if (user == null)
            return null;
        return new PrincipalImpl(user);

    }

    public Set<String> getRoleNamesForUser(Principal principal) {
        /*
         * Set<String> roleNames = new LinkedHashSet<String>(); User user =
         * getUserDao().getUserById(principal.getId()); if (user == null) return roleNames; Set<Role> roles =
         * user.getRoles(); if (roles == null || roles.isEmpty()) return roleNames; for (Role role : roles) {
         * roleNames.add(role.getName()); } return roleNames;
         */

        Set<String> roleNames = new LinkedHashSet<String>();
        // UserVO userVO = UserCache.getInstance().getUserById(principal.getId());
        User user = getUserDao().getUserById(principal.getId());
        if (user == null)
            return roleNames;

        Set<Role> roles = user.getRoles();
        if (roles == null || roles.isEmpty())
            return roleNames;

        for (Role role : roles) {
            roleNames.add(role.getName());
        }
        return roleNames;
    }

    /**
     * Implementing permission is not very necessary. usually most aplications can do with user and roles. Permissions
     * is the next level of granularity wherein each role can have one or more permissions
     * 
     * @param principal
     * @param roles
     * @return
     */
    public Set<String> getPermissions(Principal principal, Set<String> roles) {
        /*
         * Set<String> permissions = new HashSet<String>(); for (String roleStr : roles) { Role role =
         * getRoleDao().getRoleByName(roleStr); for (Permission permission : role.getPermissions()) {
         * permissions.add(permission.getName()); } } return permissions;
         */

        Set<String> permissions = new HashSet<String>();
        for (String roleStr : roles) {
            // Role role = getRoleDao().getRoleByName(roleStr);
            RoleVO roleVO = RoleCache.getInstance().getRoleByName(roleStr);
            for (PermissionVO permissionVO : roleVO.getPermissions()) {
                permissions.add(permissionVO.getName());
            }
        }
        return permissions;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public RoleDao getRoleDao() {
        return roleDao;
    }

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

}
