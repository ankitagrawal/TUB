package com.hk.web.action.admin.user;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.user.User;
import com.hk.web.action.error.AdminPermissionAction;


@Secure(hasAnyPermissions = {PermissionConstants.SEARCH_USERS}, authActionBean = AdminPermissionAction.class)
@Component
public class RewardPointStatementAction extends BaseAction{

  @Validate(required = true)
  private User user;

  public Resolution pre() {
    return new ForwardResolution("/pages/admin/rewardPointStatement.jsp");
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
