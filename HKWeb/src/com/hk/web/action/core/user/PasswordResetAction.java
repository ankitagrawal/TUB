package com.hk.web.action.core.user;

import org.springframework.stereotype.Component;


import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.dao.core.TempTokenDao;
import com.hk.dao.user.UserDao;
import com.hk.domain.TempToken;
import com.hk.domain.user.User;
import com.hk.web.action.core.auth.LoginAction;

@Component
public class PasswordResetAction extends BaseAction {

  @Validate(required = true)
  private String token;

  @Validate(required = true)
  private String password;

  @Validate(required = true, expression = "this eq password")
  private String passwordConfirm;

   TempTokenDao tempTokenDao;
   UserDao userDao;

  @DontValidate
  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/passwordReset.jsp");
  }

  public Resolution reset() {
    TempToken tempToken = tempTokenDao.findByToken(token);
    if(tempToken == null){
      addValidationError("e1", new LocalizableError("/ResetPassword.action.token.invalid"));
      return getContext().getSourcePageResolution();
    }

    if(!tempToken.isValid()) {
      tempTokenDao.expire(tempToken);
      addValidationError("e1", new LocalizableError("/ResetPassword.action.token.expired"));
      return getContext().getSourcePageResolution();
    }

    User user = tempToken.getUser();
    user.setPasswordChecksum(BaseUtils.passwordEncrypt(password));
    userDao.save(user);

    tempTokenDao.expire(tempToken);

    addRedirectAlertMessage(new LocalizableMessage("/ResetPassword.action.password.changed"));
    return new RedirectResolution(LoginAction.class);
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPassword() {
    return password;
  }

  public void setPasswordConfirm(String passwordConfirm) {
    this.passwordConfirm = passwordConfirm;
  }

  public String getPasswordConfirm() {
    return passwordConfirm;
  }

}