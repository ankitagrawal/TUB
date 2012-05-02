package web.action.core.auth;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import web.action.HomeAction;
import web.action.admin.AdminHomeAction;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
import com.hk.dao.impl.RoleDao;
import com.hk.dto.user.UserLoginDto;
import com.hk.exception.HealthkartLoginException;
import com.hk.manager.UserManager;

@Component
public class LoginAction extends BaseAction {

  private static Logger logger = LoggerFactory.getLogger(LoginAction.class);

  @Validate(required = true)
  String email;

  @Validate(required = true)
  String password;

  private String redirectUrl;
  private boolean rememberMe;
  private String source;

  public static final String SOURCE_CHECKOUT = "checkout";

  
  UserManager userManager;
  
  RoleDao roleDao;

  @DefaultHandler
  @DontValidate
  public Resolution pre() {
    return new ForwardResolution("/pages/login.jsp");
  }

  public Resolution login() {
    UserLoginDto userLoginDto = null;
    try {
      userLoginDto = userManager.login(email, password, true);
    } catch (HealthkartLoginException e) {
      // Note: if the login fails, existing subject is still retained
      addValidationError("e1", new LocalizableError("/Login.action.user.notFound"));
      return getContext().getSourcePageResolution();
    }

    // // check if this account is blocked, and show an account blocked page
    // if
    // (userLoginDto.getLoggedUser().getRoles().contains(roleDao.find(RoleConstants.ITV_BLOCKED)))
    // {
    // // this is a blocked user.
    // return new RedirectResolution(BlockedAccountAction.class);
    // }

    // if (userLoginDto.isTransferData()) {
    // return new RedirectResolution(MergeUsersAction.class)
    // .addParameter("redirectUrl", redirectUrl)
    // .addParameter("tempUser", userLoginDto.getTempUser().getId())
    // .addParameter("tempUserHash",
    // userLoginDto.getTempUser().getUserHash());
    // }

    // if(userLoginDto.getLoggedUser().getPrinter() != null) {
    // return new RedirectResolution(PrinterQueueAction.class);
    // }

    if (!StringUtils.isBlank(redirectUrl)) {
      return new RedirectResolution(redirectUrl, false);
    }

    if (
        userLoginDto.getLoggedUser().getRoles().contains(roleDao.find(RoleConstants.ADMIN)) ||
        userLoginDto.getLoggedUser().getRoles().contains(roleDao.find(RoleConstants.GOD)) ||
        userLoginDto.getLoggedUser().getRoles().contains(roleDao.find(RoleConstants.CATEGORY_MANAGER)) ||
        userLoginDto.getLoggedUser().getRoles().contains(roleDao.find(RoleConstants.OPS_MANAGER)) ||
        userLoginDto.getLoggedUser().getRoles().contains(roleDao.find(RoleConstants.WH_MANAGER)) ||
        userLoginDto.getLoggedUser().getRoles().contains(roleDao.find(RoleConstants.TICKETADMIN)) ||
        userLoginDto.getLoggedUser().getRoles().contains(roleDao.find(RoleConstants.CUSTOMER_SUPPORT))
        )
      return new RedirectResolution(AdminHomeAction.class);
    else
      return new RedirectResolution(HomeAction.class);
  }
  

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRedirectUrl() {
    return redirectUrl;
  }

  public void setRedirectUrl(String redirectUrl) {
    this.redirectUrl = redirectUrl;
  }

  public boolean isRememberMe() {
    return rememberMe;
  }

  public void setRememberMe(boolean rememberMe) {
    this.rememberMe = rememberMe;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }
}
