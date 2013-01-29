package com.hk.web.action.core.loyaltypg;


import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.session.Session;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.HealthkartConstants;
import com.hk.dto.user.UserLoginDto;
import com.hk.exception.HealthkartLoginException;
import com.hk.manager.UserManager;

@Component
public class SignInAction extends BaseAction {

	@Validate(required = true)
	String email;

	@Validate(required = true)
	String password;

	private String redirectUrl;
	private boolean rememberMe;

	@Session(key = HealthkartConstants.Session.userId)
	private String userId;

	@Autowired
	UserManager userManager;

	@DefaultHandler
	@DontValidate
	public Resolution pre() {
		return new ForwardResolution("/pages/loyalty/login.jsp");
	}

	public Resolution login() {
		UserLoginDto userLoginDto = null;
		try {
			userLoginDto = userManager.login(email, password, true);
		} catch (HealthkartLoginException e) {
			addValidationError("e1", new LocalizableError("/Login.action.user.notFound"));
			return getContext().getSourcePageResolution();
		}

		userId = userLoginDto.getLoggedUser().getId().toString();

		if (!StringUtils.isBlank(redirectUrl)) {
			return new RedirectResolution(redirectUrl, false);
		}

		return new RedirectResolution(LoyaltyCatalogAction.class);
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
}
