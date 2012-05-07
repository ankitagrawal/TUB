package com.hk.web.action.admin.user;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.Breadcrumb;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.user.User;
import com.hk.pact.dao.user.UserDao;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * User: rahul
 * Time: 1 Feb, 2010 12:54:03 PM
 */
@Secure(hasAnyPermissions = PermissionConstants.UPDATE_USER, authActionBean = AdminPermissionAction.class)
@Breadcrumb(level = 3, name = "change password for user: {user.login}", context = HealthkartConstants.BreadcrumbContext.admin)
@Component
public class ChangeLoginPasswordAction extends BaseAction {
    @Autowired
   UserDao userDao;

  @Validate(required = true)
  private User user;

  @Validate(required = true, on = "change")
  private String password;

  @Validate(required = true, expression = "this eq password", on = "change")
  private String passwordConfirm;

  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/changeLoginPassword.jsp");
  }

  public Resolution change() {
    user.setPasswordChecksum(BaseUtils.passwordEncrypt(password));
    getUserService().save(user);
    addRedirectAlertMessage(new LocalizableMessage("/ChangeLoginPassword.action.password.changed.successfully"));
    return new RedirectResolution(EditUserAction.class).addParameter("user", user.getId());
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPasswordConfirm() {
    return passwordConfirm;
  }

  public void setPasswordConfirm(String passwordConfirm) {
    this.passwordConfirm = passwordConfirm;
  }
}
