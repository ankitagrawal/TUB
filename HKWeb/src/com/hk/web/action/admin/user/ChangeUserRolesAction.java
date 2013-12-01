package com.hk.web.action.admin.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.Breadcrumb;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.dao.RoleDao;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * User: rahul Time: 1 Feb, 2010 1:31:38 PM
 */
@Secure(hasAnyPermissions = { PermissionConstants.CHANGE_USER_ROLES }, authActionBean = AdminPermissionAction.class)
@Breadcrumb(level = 3, name = "Roles for user: {user.login}", context = HealthkartConstants.BreadcrumbContext.admin)
@Component
public class ChangeUserRolesAction extends BaseAction {

    private static Logger auditLogger = LoggerFactory.getLogger("adminAuditLogger");
    @Autowired
    UserDao               userDao;
    @Autowired
    RoleDao               roleDao;

    @Validate(required = true)
    private User          user;

    private List<Role>    userRoles;

    private Set<Role>     roleSet;

    @DefaultHandler
    public Resolution pre() {
        userRoles = new ArrayList<Role>();

        List<Role> roleList;

        if (getSubject().hasRole(RoleConstants.GOD) || getSubject().hasRole(RoleConstants.ROCKSTAR)) {
            roleList = roleDao.getAll(Role.class);
        } else if (getSubject().hasRole(RoleConstants.CUSTOMER_SUPPORT_L3)) {
            roleList = roleDao.listAllCustomerSupportRoles();
        } else if (getSubject().hasRole(RoleConstants.OPS_MANAGER_L2)) {
            roleList = roleDao.listAllOperationsRoles();
        } else if (getSubject().hasRole(RoleConstants.HR)) {
            roleList = roleDao.listAllHRRoles();
        } else {
            roleList = roleDao.listAllBasicRoles();
        }

        for (Role role : roleList) {
            if (user.getRoles().contains(role)) {
                role.setSelected(true);
            }
            userRoles.add(role);
        }
        return new ForwardResolution("/pages/admin/changeUserRoles.jsp");
    }

    @ValidationMethod(on = "change")
    public void validate() {
        if (userRoles == null || userRoles.size() == 0) {
            addValidationError("userRoles", new LocalizableError("/ChangeUserRoles.action.no.role.selected"));
        } else {
            roleSet = new HashSet<Role>();
            userDao.refresh(user);
            Set<Role> userRolesDb = user.getRoles();
              for(Role role : userRoles){
                 if(userRolesDb.contains(role) && role.isSelected()){
                   roleSet.add(role);
                 }
                else if(!(userRolesDb.contains(role)) && (role.isSelected())){
                   roleSet.add(role);
                 }
              }
          for(Role role : userRolesDb){
            if(!userRoles.contains(role)){
              roleSet.add(role);
            }
          }
        }

    }

    public Resolution change() {
        user.setRoles(roleSet);
        getUserService().save(user);
        auditLogger.info("Admin [" + getPrincipal().getId() + "] - " + getPrincipal().getEmail() + " has updated roles for user [" + user.getId() + "] - " + user.getLogin());
        addRedirectAlertMessage(new LocalizableMessage("/ChangeUserRoles.action.role.changed.successfully"));
        return new RedirectResolution(EditUserAction.class).addParameter("user", user.getId());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
                                                                   
    public List<Role> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<Role> userRoles) {
        this.userRoles = userRoles;
    }
}
