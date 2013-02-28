package com.hk.web.action.admin.roles;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
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
    private String roleName;
    private static final String FORM = "/pages/admin/roles/addRolePermission.jsp";
    private static Logger logger = Logger.getLogger(AddRolePermissionAction.class);

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private RoleService roleService;

    @DefaultHandler
    public Resolution pre(){
        return new RedirectResolution(FORM);
    }

    public Resolution save(){
        if(role!=null){
            roleName = role.getName();
            logger.debug("role Name : " + roleName);
            getRoleDao().save(role);
            addRedirectAlertMessage(new SimpleMessage("Role Added Successfully"));
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

    public String getName(){
        return roleName;
    }

    public void setName(String name){
        this.roleName = name;
    }

    public RoleService getRoleService(){
        return roleService;
    }

    public void setRoleService(RoleService roleService1){
        this.roleService = roleService1;
    }

}

