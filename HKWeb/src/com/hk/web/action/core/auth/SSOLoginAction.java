package com.hk.web.action.core.auth;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.akube.framework.util.BaseUtils;
import com.hk.cache.HkApiUserCache;
import com.hk.domain.TempToken;
import com.hk.domain.api.HkApiUser;
import com.hk.domain.user.User;
import com.hk.dto.user.UserLoginDto;
import com.hk.exception.HealthkartLoginException;
import com.hk.exception.HealthkartSignupException;
import com.hk.manager.EmailManager;
import com.hk.manager.LinkManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.core.TempTokenDao;
import com.hk.pact.service.UserService;
import com.hk.security.HkAuthService;
import com.hk.security.exception.HkInvalidApiKeyException;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.HomeAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 11/16/12
 * Time: 4:25 PM
 */
@Component
public class SSOLoginAction extends BaseAction{
    private static Logger logger = LoggerFactory.getLogger(SSOLoginAction.class);

    private String                     apiKey;

    private String                     redirectUrl;

    @Validate(required = true, on = "signup")
    String userName;

    @Validate(required = true, on={"signup","login","sendResetLink"})
    String userLogin;

    @Validate(required = true,on={"signup","login"})
    String password;

    @Validate(required = true, on="signup")
    String repeatPassword;

    String error;

    private HkApiUser hkApiUser;

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
        if(getSubject().isAuthenticated()){
            //check if the user is already authenticated in Healthkart, we are not allowing remembered users here
            if(!StringUtils.isEmpty(redirectUrl) && !StringUtils.isEmpty(apiKey)){
                try{
                    hkAuthService.isValidAppKey(apiKey);
                }catch (HkInvalidApiKeyException ex){
                    logger.info(ex.getMessage()+" attempted from "+getRemoteHostAddr());
                    return new RedirectResolution(redirectUrl);
                }
                redirectUrl=redirectUrl.concat("?uaToken=").concat(hkAuthService.generateUserAccessToken(getPrincipal().getEmail(),apiKey));
                return new RedirectResolution(redirectUrl, false);
            }else {
                return new RedirectResolution(HomeAction.class);
            }
        }
        hkApiUser = HkApiUserCache.getInstance().getHkApiUser(apiKey);
        return new ForwardResolution("/pages/sso/singleLogin.jsp");
    }

    @HandlesEvent("login")
    @JsonHandler
    public Resolution login(){
        HealthkartResponse healthkartResponse;
        UserLoginDto userLoginDto = null;
        try{
            if(!StringUtils.isEmpty(apiKey)){
                hkAuthService.isValidAppKey(apiKey);
            }
            userLoginDto = userManager.login(userLogin, password, true);
        } catch (HkInvalidApiKeyException ex){
            logger.info(ex.getMessage()+" attempted from "+getRemoteHostAddr());
            healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_REDIRECT, redirectUrl);
            return new  JsonResolution(healthkartResponse);
        } catch (HealthkartLoginException e) {
            // Note: if the login fails, existing subject is still retained
            addValidationError("e1", new LocalizableError("/Login.action.user.notFound"));
            healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Login is not valid!");
            return new  JsonResolution(healthkartResponse);
        }
        if(!StringUtils.isEmpty(redirectUrl)&&!StringUtils.isEmpty(apiKey)){
            redirectUrl=redirectUrl.concat("?uaToken=").concat(hkAuthService.generateUserAccessToken(userLogin,apiKey));
            healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_REDIRECT, redirectUrl);
            return new  JsonResolution(healthkartResponse);
        } else{
            healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_REDIRECT, getContext().getRequest().getContextPath());
            return new  JsonResolution(healthkartResponse);
        }
    }

    @HandlesEvent("signup")
    @JsonHandler
    public Resolution signup(){
        HealthkartResponse healthkartResponse;
        try{
            if(!StringUtils.isEmpty(apiKey)){
                hkAuthService.isValidAppKey(apiKey);
            }
        } catch (HkInvalidApiKeyException ex){
            logger.info(ex.getMessage()+" attempted from "+getRemoteHostAddr());
            healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_REDIRECT, redirectUrl);
            return new  JsonResolution(healthkartResponse);
        }
        try {
            userManager.signup(userLogin, userName, password, null);
        } catch (HealthkartSignupException e) {
            healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "A user already exists with this email");
            return new  JsonResolution(healthkartResponse);
        }

        if(!StringUtils.isEmpty(redirectUrl)&& !StringUtils.isEmpty(apiKey)){
            redirectUrl=redirectUrl.concat("?uaToken=").concat(hkAuthService.generateUserAccessToken(userLogin,apiKey));
            healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_REDIRECT, redirectUrl);
            return new  JsonResolution(healthkartResponse);
        }else{
            healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_REDIRECT, getContext().getRequest().getContextPath());
            return new  JsonResolution(healthkartResponse);
        }
    }

    @JsonHandler
    public Resolution sendResetLink(){
        HealthkartResponse healthkartResponse;
        User user = userService.findByLogin(userLogin);
        if (user == null) {
            healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "There seems to be no such user in our system");
            return new  JsonResolution(healthkartResponse);
        }

        TempToken tempToken = tempTokenDao.createNew(user, EXPIRY_DAYS);
        String resetPasswordLink = linkManager.getSSOResetPasswordLink(tempToken);
        emailManager.sendResetPasswordEmail(user, resetPasswordLink);
        healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Please check your inbox");
        return new  JsonResolution(healthkartResponse);
    }

    @ValidationMethod(on = {"signup","sendResetLink"})
    public void isValidEmail() {
        if (!BaseUtils.isValidEmail(userLogin)) {
            getContext().getValidationErrors().add("invalidEmail", new LocalizableError("/Signup.action.InvalidEmail"));
        }
    }

    @ValidationMethod(on = {"signup"})
    public void conformPassword() {
        if (!password.equals(repeatPassword)) {
            getContext().getValidationErrors().add("passwordConfirm", new LocalizableError("/Signup.action.PasswordDontMatch"));
        }
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HkApiUser getHkApiUser() {
        return hkApiUser;
    }

    public void setHkApiUser(HkApiUser hkApiUser) {
        this.hkApiUser = hkApiUser;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}
