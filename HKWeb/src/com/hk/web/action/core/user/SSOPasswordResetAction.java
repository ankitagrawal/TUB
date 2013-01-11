package com.hk.web.action.core.user;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 1/6/13
 * Time: 10:23 AM
 */

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.domain.TempToken;
import com.hk.domain.user.User;
import com.hk.pact.dao.core.TempTokenDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.web.action.core.auth.LoginAction;
import com.hk.web.action.core.auth.SSOLoginAction;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

@Component
public class SSOPasswordResetAction extends BaseAction {

    @Validate(required = true)
    private String token;

    @Validate(required = true)
    private String password;

    @Validate(required = true, expression = "this eq password")
    private String passwordConfirm;
    @Autowired
    TempTokenDao tempTokenDao;
    @Autowired
    UserDao userDao;

    @DontValidate
    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/sso/ssoPasswordReset.jsp");
    }

    public Resolution reset() {
        TempToken tempToken = tempTokenDao.findByToken(token);
        if (tempToken == null) {
            addValidationError("e1", new LocalizableError("/ResetPassword.action.token.invalid"));
            return getContext().getSourcePageResolution();
        }

        if (!tempToken.isValid()) {
            tempTokenDao.expire(tempToken);
            addValidationError("e1", new LocalizableError("/ResetPassword.action.token.expired"));
            return getContext().getSourcePageResolution();
        }

        User user = tempToken.getUser();
        user.setPasswordChecksum(BaseUtils.passwordEncrypt(password));
        getUserService().save(user);

        tempTokenDao.expire(tempToken);

        addRedirectAlertMessage(new LocalizableMessage("/ResetPassword.action.password.changed"));
        return new RedirectResolution(SSOLoginAction.class);
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
