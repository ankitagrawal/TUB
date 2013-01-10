package com.hk.web.action.core.user;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.domain.TempToken;
import com.hk.domain.user.User;
import com.hk.manager.EmailManager;
import com.hk.manager.LinkManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.core.TempTokenDao;
import com.hk.pact.service.UserService;
import com.hk.security.HkAuthService;
import com.hk.web.action.core.auth.SSOLoginAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 */
@Component
public class SSOForgotPasswordAction extends BaseAction{
    private static Logger logger = LoggerFactory.getLogger(SSOLoginAction.class);

    @Validate(required = true, on = "reset")
    String email;

    String error;

    @Autowired
    HkAuthService hkAuthService;
    @Autowired
    UserManager userManager;
    @Autowired
    UserService userService;
    @Autowired
    EmailManager emailManager;
    @Autowired
    LinkManager linkManager;
    @Autowired
    TempTokenDao tempTokenDao;

    public  static final int EXPIRY_DAYS=10;
    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/sso/ssoForgetPassword.jsp");
    }

    @HandlesEvent("reset")
    public Resolution sendResetLink(){
        User user = userService.findByLogin(email);
        if (user == null) {
            addValidationError("e1", new LocalizableError("/ForgotPassword.action.invalid.user"));
            return getContext().getSourcePageResolution();
        }

        TempToken tempToken = tempTokenDao.createNew(user, EXPIRY_DAYS);
        String resetPasswordLink = linkManager.getSSOResetPasswordLink(tempToken);
        emailManager.sendResetPasswordEmail(user, resetPasswordLink);
        getContext().getMessages().add(new SimpleMessage("please check you inbox"));
        return getContext().getSourcePageResolution();
    }

    @ValidationMethod(on = {"signup"})
    public void isValidEmail() {
        if (!BaseUtils.isValidEmail(email)) {
            getContext().getValidationErrors().add("invalidEmail", new LocalizableError("/Signup.action.InvalidEmail"));
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
