package com.hk.web.action.admin.newsletter;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = {PermissionConstants.SEND_MARKETING_MAILS}, authActionBean = AdminPermissionAction.class)
@Component
public class EmailNewsletterAdmin extends BaseAction {

  public Resolution pre() {
    return new ForwardResolution("/pages/admin/newsletter/newsletterAdmin.jsp");
  }
  
}
