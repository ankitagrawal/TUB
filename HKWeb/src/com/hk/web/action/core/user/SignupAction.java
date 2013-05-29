package com.hk.web.action.core.user;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.core.HealthkartConstants;
import com.hk.domain.user.User;
import com.hk.exception.HealthkartSignupException;
import com.hk.manager.LinkManager;
import com.hk.manager.UserManager;
import com.hk.pact.service.UserService;
import com.hk.util.ga.GAUtil;
import com.hk.web.action.HomeAction;
import com.hk.web.action.core.auth.LoginAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.util.CryptoUtil;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.session.Session;

import javax.servlet.http.Cookie;
import java.util.Calendar;
import java.util.Date;

@Component
@Ssl
public class SignupAction extends BaseAction {

	@Validate(required = true)
	private String email;

	@Validate(required = true)
	private String name;

	@Validate(required = true)
	private String password;

	@Validate(required = true)
	private String passwordConfirm;

	@Validate(required = true)
	private boolean agreeToTerms;

	private String redirectUrl;
	private String source;

	@Session(key = HealthkartConstants.Session.signupDate)
	private String signupDate;

	@Autowired
	private UserManager userManager;
	@Autowired
	private LinkManager linkManager;
	@Autowired
	private UserService userService;

	@DefaultHandler
	@DontValidate
	public Resolution pre() {
		return new ForwardResolution("/pages/signup.jsp");
	}

	@ValidationMethod()
	public void isValidEmail() {
		if (!BaseUtils.isValidEmail(email)) {
			getContext().getValidationErrors().add("invalidEmail", new LocalizableError("/Signup.action.InvalidEmail"));
		}
	}

	@ValidationMethod
	public void conformPassword() {
		if (!password.equals(passwordConfirm)) {
			getContext().getValidationErrors().add("passwordConfirm", new LocalizableError("/Signup.action.PasswordDontMatch"));
		}
	}

/*    @ValidationMethod
    public void signupValidation() {
        if (!agreeToTerms) {
            getContext().getValidationErrors().add("terms", new LocalizableError("/Signup.action.terms.notAgree", getLinkManager().getTermsAndConditionsUrl()));
        }
    }*/

	public Resolution signup() {
		User referredBy = null;
		Cookie referredByCookie = BaseUtils.getCookie(getContext().getRequest(), HealthkartConstants.Cookie.referred_by);
		if (referredByCookie != null) {
			referredBy = getUserService().findByUserHash(CryptoUtil.decrypt(referredByCookie.getValue()));
		}
		try {
			userManager.signup(email, name, password, referredBy);
		} catch (HealthkartSignupException e) {
			addValidationError("e1", new LocalizableError("/Signup.action.email.id.already.exists"));
			return new ForwardResolution(getContext().getSourcePage());
		}

		Date currentDate = Calendar.getInstance().getTime();
		signupDate = GAUtil.formatDate(currentDate);

		if (!StringUtils.isBlank(redirectUrl)) {
			return new RedirectResolution(redirectUrl, false);
		}

		if (!StringUtils.isBlank(source) && source.equals(LoginAction.SOURCE_CHECKOUT)) {
			return new RedirectResolution(SelectAddressAction.class);
		}

		// return new RedirectResolution(WelcomeAction.class);
		return new RedirectResolution(HomeAction.class);
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public String getPassword() {
		return password;
	}

	public void setAgreeToTerms(boolean agreeToTerms) {
		this.agreeToTerms = agreeToTerms;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public LinkManager getLinkManager() {
		return linkManager;
	}

	public void setLinkManager(LinkManager linkManager) {
		this.linkManager = linkManager;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getSignupDate() {
		return signupDate;
	}

	public boolean isAgreeToTerms() {
		return agreeToTerms;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}


}
