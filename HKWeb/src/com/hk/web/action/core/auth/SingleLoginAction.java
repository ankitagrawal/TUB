package com.hk.web.action.core.auth;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.dto.user.UserLoginDto;
import com.hk.exception.HealthkartLoginException;
import com.hk.exception.HealthkartSignupException;
import com.hk.manager.UserManager;
import com.hk.security.HkAuthService;
import com.hk.security.exception.HkInvalidApiKeyException;
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
 * Date: 11/16/12
 * Time: 4:25 PM
 */
@Component
public class SingleLoginAction extends BaseAction{
    private static Logger logger = LoggerFactory.getLogger(SingleLoginAction.class);

    @Validate(required = true,on={"signup","login"})
    String                     apiKey;

    @Validate(required = true,on={"signup","login"})
    String                     redirectUrl;

    @Validate(required = true, on = "signup")
    String userName;

    @Validate(required = true, on={"signup","login"})
    String userLogin;

    @Validate(required = true,on={"signup","login"})
    String password;

    @Validate(required = true, on="signup")
    String repeatPassword;

    /*@Validate(required = true, on="login")
    String appToken;*/
    boolean logintabselected =true;

    String error;

    @Autowired
    HkAuthService hkAuthService;
    @Autowired
    UserManager userManager;


    @DefaultHandler
    @DontValidate
    public Resolution pre() {
            return new ForwardResolution("/pages/singleLogin.jsp");
    }

    @DontValidate
    @HandlesEvent("login")
    public Resolution login(){
        logintabselected =true;
        UserLoginDto userLoginDto = null;
        try{
            if(hkAuthService.isValidAppKey(apiKey)){
               //no logic right now
            }
            userLoginDto = userManager.login(userLogin, password, true);
        } catch (HkInvalidApiKeyException ex){
            logger.info(ex.getMessage()+" attempted from "+getRemoteHostAddr());
            return new RedirectResolution(redirectUrl);
        } catch (HealthkartLoginException e) {
            // Note: if the login fails, existing subject is still retained
            addValidationError("e1", new LocalizableError("/Login.action.user.notFound"));
            return getContext().getSourcePageResolution();
        }
        /* use the following logic incase appToken is sent instead of appKey
        try{
            if(hkAuthService.isValidAppToken(appToken)){

            }
            HkUsernamePasswordAuthenticationToken authRequest = new HkUsernamePasswordAuthenticationToken(userName, password, apiKey);
            HkAuthentication hkAuthentication = hkAuthService.authenticate(authRequest);
        }catch (HkInvalidApiKeyException ex){
            logger.info(ex.getMessage()+" attempted from "+getRemoteHostAddr());
            return new RedirectResolution(redirectUrl);
        }catch (HkInvalidAppTokenException ex){
            logger.info(ex.getMessage()+" attempted from "+getRemoteHostAddr());
            return new RedirectResolution(redirectUrl);
        }catch (HkTokenExpiredException ex){
            logger.info(ex.getMessage()+" attempted from "+getRemoteHostAddr());
            return new RedirectResolution(redirectUrl);
        }*/
        redirectUrl=redirectUrl.concat("?uaToken=").concat(hkAuthService.generateUserAccessToken(userLogin,apiKey));
        return new RedirectResolution(redirectUrl, false);
    }

    @DontValidate
    @HandlesEvent("signup")
    public Resolution signup(){
        logintabselected =false;
        try{
            if(hkAuthService.isValidAppKey(apiKey)){
                //no logic right now
            }
        } catch (HkInvalidApiKeyException ex){
            logger.info(ex.getMessage()+" attempted from "+getRemoteHostAddr());
            return new RedirectResolution(redirectUrl);
        }
        try {
            userManager.signup(userLogin, userName, password, null);
        } catch (HealthkartSignupException e) {
            addValidationError("e1", new LocalizableError("/Signup.action.email.id.already.exists"));
            return new ForwardResolution(getContext().getSourcePage()).addParameter("apiKey",apiKey).addParameter("redirectUrl",redirectUrl);
        }

        redirectUrl=redirectUrl.concat("?uaToken=").concat(hkAuthService.generateUserAccessToken(userLogin,apiKey));
        return new RedirectResolution(redirectUrl, false);
    }

    @ValidationMethod(on = {"signup"})
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

   /* public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }*/

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

    public boolean isLogintabselected() {
        return logintabselected;
    }

    public void setLogintabselected(boolean logintabselected) {
        this.logintabselected = logintabselected;
    }
}
