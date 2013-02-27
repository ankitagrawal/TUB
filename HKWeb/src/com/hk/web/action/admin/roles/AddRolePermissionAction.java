package com.hk.web.action.admin.roles;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.user.Role;
import com.hk.pact.dao.RoleDao;
import com.hk.pact.service.RoleService;
import net.sourceforge.stripes.action.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

@Secure( hasAnyRoles = {RoleConstants.ADMIN})
@Component
public class AddRolePermissionAction extends BaseAction{

    private Role role;
    private static final String FORM = "/pages/admin/roles/addRolePermission.jsp";

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private RoleService roleService;

    @DefaultHandler
    public Resolution pre(){
        return new ForwardResolution(FORM);
    }

    public Resolution save(){
        if(role.getName() != ""){
            roleDao.save(role);
            addRedirectAlertMessage(new SimpleMessage("Role Added Successfully"));
        }
        return new RedirectResolution(AddRolePermissionAction.class);
    }


}

