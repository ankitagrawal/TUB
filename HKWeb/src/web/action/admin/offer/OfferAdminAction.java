package web.action.admin.offer;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import web.action.error.AdminPermissionAction;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;

@Secure(hasAnyPermissions = {PermissionConstants.VIEW_OFFER}, authActionBean = AdminPermissionAction.class)
@Component
public class OfferAdminAction extends BaseAction {

  public Resolution pre() {
    return new ForwardResolution("/pages/admin/offer/offerAdmin.jsp");
  }

}
