package web.action.core.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.EmailTypeConverter;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.dao.core.TempTokenDao;
import com.hk.dao.user.UserDao;
import com.hk.domain.TempToken;
import com.hk.domain.user.User;
import com.hk.manager.EmailManager;
import com.hk.manager.LinkManager;
import com.hk.web.HealthkartResponse;

@Component
public class ForgotPasswordAction extends BaseAction implements ValidationErrorHandler {

  @Validate(required = true, converter = EmailTypeConverter.class)
  private String email;

   UserDao userDao;
   TempTokenDao tempTokenDao;
   LinkManager linkManager;
   EmailManager emailManager;
  public static final int EXPIRY_DAYS = 10;

  @DontValidate
  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/modal/forgotPassword.jsp");
  }

  public Resolution handleValidationErrors(ValidationErrors validationErrors) throws Exception {
    return new JsonResolution(validationErrors, getContext().getLocale());
  }

  //commenting, captcha not getting generated
/*  @ValidationMethod
  public void captchaValidation() {
    String challengeField = getContext().getRequest().getParameter("recaptcha_challenge_field");
    String responseField = getContext().getRequest().getParameter("recaptcha_response_field");
    if (StringUtils.isBlank(responseField)) responseField = "null";

    ReCaptcha captcha = ReCaptchaFactory.newReCaptcha(HealthkartConstants.recaptchaPublicKey, HealthkartConstants.recaptchaPrivateKey, false);
    ReCaptchaResponse response = captcha.checkAnswer(getContext().getRequest().getRemoteAddr(), challengeField, responseField);
    if (!response.isValid()) {
      getContext().getValidationErrors().add("Captcha", new LocalizableError("/Signup.action.captcha.invalid"));
    }
  }*/

  /*public Resolution forgotPassword() {

    User user = userDao.findByLogin(email);
    if(user == null) {
      addValidationError("e1", new LocalizableError("/ForgotPassword.action.invalid.user"));
      return getContext().getSourcePageResolution();
    }

    TempToken tempToken = tempTokenDao.createNew(user, EXPIRY_DAYS);
    String resetPasswordLink = linkManager.getResetPasswordLink(tempToken);
    System.out.println("Reset Password Link: "+ resetPasswordLink);
    emailManager.sendResetPasswordEmail(user, resetPasswordLink);

    addRedirectAlertMessage(new LocalizableMessage("/ForgotPassword.action.email.sent", email));
    return getContext().getSourcePageResolution();
  }*/

  @JsonHandler
  public Resolution forgotPassword() {

    User user = userDao.findByLogin(email);
    if (user == null) {
      getContext().getValidationErrors().add("e2", new LocalizableError("/ForgotPassword.action.invalid.user"));
      return new JsonResolution(getContext().getValidationErrors(), getContext().getLocale());
    }

    TempToken tempToken = tempTokenDao.createNew(user, EXPIRY_DAYS);
    String resetPasswordLink = linkManager.getResetPasswordLink(tempToken);
    //System.out.println("Reset Password Link: " + resetPasswordLink);
    emailManager.sendResetPasswordEmail(user, resetPasswordLink);

    Map dataMap = new HashMap();
    HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Password reset mail has been sent to your email id. Please check your Inbox/Spam/Junk folders.", dataMap);

    noCache();
    return new JsonResolution(healthkartResponse);
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

}