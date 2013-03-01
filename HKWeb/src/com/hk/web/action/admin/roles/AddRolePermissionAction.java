package com.hk.web.action.admin.roles;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.user.Permission;
import com.hk.domain.user.Role;
import com.hk.pact.dao.RoleDao;
import com.hk.pact.service.RoleService;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

@Secure( hasAnyRoles = {RoleConstants.ADMIN},  authActionBean = AdminPermissionAction.class)
@Component
public class AddRolePermissionAction extends BaseAction{

    private Role role;
    private Permission permission;
    private String roleName;
    private String permissionName;
    private static final String FORM = "/pages/admin/roles/addRolePermission.jsp";
    private static Logger logger = Logger.getLogger(AddRolePermissionAction.class);

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private RoleService roleService;

    @DefaultHandler
    public Resolution pre(){
        return new ForwardResolution(FORM);
    }

    public Resolution saveRoleAndPermission(){
        if(role!=null){
            if(getRoleService().listAllRoles().contains(role)){
                addRedirectAlertMessage(new SimpleMessage("This role already exists"));
            }else{
                roleName = role.getName();
                logger.debug("role Name : " + roleName);
                getRoleDao().save(role);
                addRedirectAlertMessage(new SimpleMessage("Role Added Successfully"));
            }
        }
        if(permission != null){
            if(!getRoleService().listAllPermissions().contains(permission)){
                permissionName = permission.getName();
                logger.debug("Permission Name : " + roleName);
                getRoleDao().save(permission);
                addRedirectAlertMessage(new SimpleMessage("Permission Added Successfully"));
            }else{
                addRedirectAlertMessage(new SimpleMessage("This permission already exists"));
            }
        }
        return new RedirectResolution(AddRolePermissionAction.class);
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

}

