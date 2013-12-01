package com.hk.cache.vo;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.hk.constants.core.EnumPermission;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;

/**
 * @author vaibhav.adlakha
 */
public class UserVO {

    private Long        id;

    private String      login;

    private String      email;

    private String      name;

    private String      passwordChecksum;

    private Date        birthDate;

    private String      gender;

    private Date        lastLoginDate;

    private Set<RoleVO> roles = new HashSet<RoleVO>();

    private String      userHash;

    /*
     * private Set<Warehouse> warehouses = new HashSet<Warehouse>(0); private Store store; private KarmaProfile
     * karmaProfile;
     */

    public UserVO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.email = user.getEmail();
        this.name = user.getName();
        this.passwordChecksum = user.getPasswordChecksum();
        this.birthDate = user.getBirthDate();
        this.gender = user.getGender();
        this.lastLoginDate = user.getLastLoginDate();
        this.userHash = user.getUserHash();

        for (Role role : user.getRoles()) {
            this.roles.add(new RoleVO(role));
        }
    }

    public User getUser() {
        User user = new User();
        user.setId(this.id);
        user.setName(this.name);
        user.setLogin(this.login);
        user.setEmail(this.email);
        user.setPasswordChecksum(this.passwordChecksum);
        user.setBirthDate(this.birthDate);
        user.setGender(this.gender);
        user.setLastLoginDate(this.lastLoginDate);
        user.setUserHash(this.userHash);

        return user;
    }

    public boolean hasPermission(EnumPermission enumPermission) {
        if (roles == null || roles.isEmpty()) {
            return false;
        }
        PermissionVO permissionVO = new PermissionVO();
        permissionVO.setName(enumPermission.getPermissionName());
        for (RoleVO roleVO : roles) {
            if (roleVO.getPermissions().contains(permissionVO)) {
                return true;
            }
        }
        return false;
    }

    public Set<String> getRoleNames() {
        Set<String> roleNames = new LinkedHashSet<String>();

        for (RoleVO roleVO : roles) {
            roleNames.add(roleVO.getName());
        }
        return roleNames;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswordChecksum() {
        return passwordChecksum;
    }

    public void setPasswordChecksum(String passwordChecksum) {
        this.passwordChecksum = passwordChecksum;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Set<RoleVO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleVO> roles) {
        this.roles = roles;
    }

    public String getUserHash() {
        return userHash;
    }

    public void setUserHash(String userHash) {
        this.userHash = userHash;
    }

}
