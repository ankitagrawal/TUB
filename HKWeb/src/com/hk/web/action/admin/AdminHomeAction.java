package com.hk.web.action.admin;

import com.hk.constants.core.EnumRole;
import com.hk.domain.user.User;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;

@Secure(
    hasAnyRoles = {
        RoleConstants.GOD,
        RoleConstants.ROCKSTAR,
        RoleConstants.ADMIN,
        RoleConstants.OPS_MANAGER,
        RoleConstants.TICKETADMIN,
        RoleConstants.CUSTOMER_SUPPORT,
        RoleConstants.CATEGORY_MANAGER,
        RoleConstants.FINANCE,
        RoleConstants.REPORT_ADMIN,
        RoleConstants.BASIC_ROLES
    }
)
@UrlBinding("/admin")
@Component
public class AdminHomeAction extends BaseAction {

  public Resolution pre() {
    User user = getPrincipalUser();
      if(user != null){
          if(user.getRoles().contains(EnumRole.ADMIN.toRole())){
              return new ForwardResolution("/pages/admin/adminHome.jsp");
          }else if(user.getRoles().contains(EnumRole.CATEGORY_MANAGER.toRole())){
              return new ForwardResolution("/pages/admin/categoryAdminHome.jsp");
          }else if(user.getRoles().contains(EnumRole.OPS_MANAGER.toRole())){
              return new ForwardResolution("/pages/admin/courier/courierAdminHome.jsp");
          }else if(user.getRoles().contains(EnumRole.CUSTOMER_SUPPORT.toRole())){
              return new ForwardResolution("/pages/admin/crm/customerSupportAdminHome.jsp");
          }
      }
    return new ForwardResolution("/pages/admin/adminHome.jsp");
  }

}