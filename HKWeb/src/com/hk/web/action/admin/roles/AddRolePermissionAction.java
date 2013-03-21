package com.hk.web.action.admin.roles;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.user.Permission;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.pact.dao.RoleDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.RoleService;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.*;

@Secure( hasAnyRoles = {RoleConstants.ADMIN},  authActionBean = AdminPermissionAction.class)
@Component
public class AddRolePermissionAction extends BaseAction{

    private Role role;
    private Permission permission;
    private User user;
    private String roleName;
    private String permissionName;
    private static Logger logger = Logger.getLogger(AddRolePermissionAction.class);

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleService roleService;

    private String userPermissions;
    private String deletePermissions;
    private String userRoles;
    Set<Permission> permissionList = new HashSet<Permission>();
    private static final String LinkRoles = "/pages/admin/roles/linkRoles.jsp";

    @DefaultHandler
    public Resolution pre(){
        return new ForwardResolution("/pages/admin/roles/addRolePermission.jsp");
    }

    public Resolution linkRolePermission(){
        return new ForwardResolution(LinkRoles).addParameter("role",role).addParameter("permission",permission);
    }

    @SuppressWarnings("unchecked")
    public Resolution saveRoles(){
        if(user!=null && userRoles != null){
            user = userDao.getUserById(user.getId());
            List<String> userRoleList =new ArrayList<String>(Arrays.asList(userRoles.split(",")));
            Set<Role> roleList = user.getRoles();
            for(Role role1: roleList){
                if (!userRoleList.contains(role1.getName())){
                    userRoleList.add(role1.getName());
                }
            }
            Set<Role> roles = new HashSet<Role>();
            for(String roleName : userRoleList){
                roles.add(roleDao.getRoleByName(roleName));
            }
            user.setRoles(roles);
            userDao.save(user);
        }

        if(userRoles != null ){
            addRedirectAlertMessage(new SimpleMessage("Changes Saved Successfully"));
        }
        return new RedirectResolution(LinkRoles);
    }

    @SuppressWarnings("unchecked")
    public Resolution savePermissions(){
        if(role!=null && userPermissions != null){
            role = roleDao.getRoleByName(role.getName());
            List<String> userPermissionList = new ArrayList<String> (Arrays.asList(userPermissions.split(",")));
            Set<Permission> permissions = new HashSet<Permission>();
            for(String permissionName : userPermissionList){
                permissions.add(roleDao.getPermissionByName(permissionName));
            }
            role.setPermissions(permissions);
            roleDao.save(role);
        }

        if(userPermissions != null ){
            addRedirectAlertMessage(new SimpleMessage("Changes Saved Successfully"));
        }
        return new RedirectResolution(LinkRoles);
    }

    @SuppressWarnings("unchecked")
    public Resolution saveRoleAndPermission(){
        if(role!=null){
            roleName = role.getName();
            logger.debug("role Name : " + roleName);
            if( getRoleService().getRoleByName(roleName) != null){
                addRedirectAlertMessage(new SimpleMessage("This role already exists"));
            }
            else{
                getRoleDao().save(role);
                addRedirectAlertMessage(new SimpleMessage("Role Added Successfully"));
            }
        }
        if(permission != null){
            permissionName = permission.getName();
            logger.debug("Permission Name : " + permissionName);
            if(getRoleDao().getPermissionByName(permissionName) == null){
                getRoleDao().save(permission);
                addRedirectAlertMessage(new SimpleMessage("Permission Added Successfully"));
            }else{
                addRedirectAlertMessage(new SimpleMessage("This permission already exists"));
            }
        }
        return new RedirectResolution(AddRolePermissionAction.class);
    }

    public Resolution getPermissions(){
        HealthkartResponse HKResponse = null;
        Map dataMap = new HashMap();
        if(roleName != null ){
            Role role = roleDao.getRoleByName(roleName);
            permissionList = role.getPermissions();
            List<String> permissions  = new ArrayList<String>();
            if(permissionList.size() != 0 ){
                for(Permission permission1 : permissionList){
                    permissions.add(permission1.getName());
                }
                dataMap.put("permission",permissions);
                HKResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK,"Valid Permissions",dataMap);
            }else{
                HKResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR,"No permissions for this Role");
            }
        }else{
            HKResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR,"Invalid Role");
        }
        return new JsonResolution(HKResponse);
    }

    public RoleDao getRoleDao(){
        return roleDao;
    }

    public void setRoleDao(RoleDao roleDao1){
        this.roleDao = roleDao1;
    }

    public Role getRole(){
        return role;
    }

    public void setRole(Role role1){
        this.role = role1;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user1){
        this.user = user1;
    }

    public Permission getPermission(){
        return permission;
    }

    public void setPermission(Permission permission1){
        this.permission = permission1;
    }

    public RoleService getRoleService(){
        return roleService;
    }

    public void setRoleService(RoleService roleService1){
        this.roleService = roleService1;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getUserPermissions() {
        return userPermissions;
    }

    public void setUserPermissions(String userPermissions) {
        this.userPermissions = userPermissions;
    }

    public String getDeletePermissions() {
        return deletePermissions;
    }

    public void setDeletePermissions(String deletePermissions1) {
        this.deletePermissions = deletePermissions1;
    }

    public String getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(String userRoles) {
        this.userRoles = userRoles;
    }
}

