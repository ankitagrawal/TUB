package com.hk.web.action.core.b2b;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.EmailTypeConverter;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.EnumRole;
import com.hk.domain.user.User;
import com.hk.dto.user.UserLoginDto;
import com.hk.exception.HealthkartLoginException;
import com.hk.exception.HealthkartSignupException;
import com.hk.manager.LinkManager;
import com.hk.manager.UserManager;
import com.hk.pact.service.RoleService;
import com.hk.pact.service.UserService;
import com.hk.web.action.HomeAction;

@UrlBinding("/b2b")
@Component
public class B2BAction extends BaseAction {
    @Validate(required = true, on = "signup")
    private String        name;

    @Validate(required = true, converter = EmailTypeConverter.class)
    private String        email;

    @Validate(required = true)
    private String        password;

    @Validate(required = true, expression = "this eq password", on = "signup")
    private String        passwordConfirm;

    private String        websiteName;
    private boolean       agreeToTerms;
    boolean               rememberMe;

    private static Logger logger = Logger.getLogger(B2BAction.class);
    @Autowired
    private LinkManager   linkManager;
    @Autowired
    private UserManager   userManager;
    @Autowired
    private RoleService   roleService;
    @Autowired
    private UserService   userService;

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/b2b/b2b.jsp");
    }

    @ValidationMethod(on = "signup")
    public void signupValidation() {
        if (!agreeToTerms) {
            getContext().getValidationErrors().add("terms", new LocalizableError("/Signup.action.terms.notAgree", getLinkManager().getTermsAndConditionsUrl()));
        }
    }

    @ValidationMethod(on = "signup")
    public void conformPassword() {
        if (!password.equals(passwordConfirm)) {
            getContext().getValidationErrors().add("passwordConfirm", new LocalizableError("/Signup.action.PasswordDontMatch"));
        }
    }

    @DontValidate
    public Resolution goToSignUp() {
        return new ForwardResolution("/pages/b2b/b2bSignup.jsp");
    }

    public Resolution signup() throws Exception {
        try {
            User signedupUser = userManager.signup(email, name, password, null);
            //signedupUser.getRoles().add(getRoleService().getRoleByName(EnumRole.B2B_USER));
            //getUserService().save(signedupUser);
        } catch (HealthkartSignupException e) {
            getContext().getValidationErrors().add("already exists", new LocalizableError("/Signup.action.email.id.already.exists"));
            return new ForwardResolution(getContext().getSourcePage());
        }
        addRedirectAlertMessage(new SimpleMessage("Welcome!!..Your account has not been verfied yet."));
        return new ForwardResolution(HomeAction.class);
    }

    public Resolution login() throws Exception {
        UserLoginDto user;
        try {
            user = getUserManager().login(email, password, rememberMe);
            if (user != null) {
                if (user.getLoggedUser().getRoles().contains(getRoleService().getRoleByName(EnumRole.B2B_USER.toString()))) {
                    logger.debug("b2b user Id : " + user.getLoggedUser());
                    return new ForwardResolution(HomeAction.class);
                } else {
                    logger.debug("b2b user is null.");
                    getSubject().logout();
                    addRedirectAlertMessage(new SimpleMessage("You can't log in as a b2b user. Your account will be verified."));
                    return new RedirectResolution(B2BAction.class);
                }
            }
        } catch (HealthkartLoginException e) {
            // Note: if the login fails, existing subject is still retained
            addValidationError("e1", new LocalizableError("/Login.action.user.notFound"));
            return getContext().getSourcePageResolution();
        }
        return new ForwardResolution(HomeAction.class);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public boolean isAgreeToTerms() {
        return agreeToTerms;
    }

    public void setAgreeToTerms(boolean agreeToTerms) {
        this.agreeToTerms = agreeToTerms;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public LinkManager getLinkManager() {
        return linkManager;
    }

    public void setLinkManager(LinkManager linkManager) {
        this.linkManager = linkManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public RoleService getRoleService() {
        return roleService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}