package web.action.admin;

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
        RoleConstants.REPORT_ADMIN
    }
)
@UrlBinding("/admin")
@Component
public class AdminHomeAction extends BaseAction {

  public Resolution pre() {
    return new ForwardResolution("/pages/admin/adminHome.jsp");
  }

}