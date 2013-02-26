package com.hk.web.action.admin.roles;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.user.Role;
import com.hk.pact.dao.RoleDao;
import com.hk.pact.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

@Secure( hasAnyRoles = {RoleConstants.ADMIN})
@Component
public class AddRolePermissionAction extends BaseAction{
    @Autowired
    private Role role;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RoleService roleService;







}

