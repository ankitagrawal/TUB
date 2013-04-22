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
import com.hk.domain.user.User;
import com.hk.dto.user.UserLoginDto;
import com.hk.exception.HealthkartLoginException;
import com.hk.loyaltypg.service.LoyaltyProgramService;
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

	@Autowired
	LoyaltyProgramService loyaltyProgramService; 

	@DefaultHandler
	@DontValidate
	public Resolution pre() {
		return new ForwardResolution("/pages/loyalty/login.jsp");
	}

	public Resolution login() {
		UserLoginDto userLoginDto = null;
		try {
			userLoginDto = this.userManager.login(this.email, this.password, true);
		} catch (HealthkartLoginException e) {
			this.addValidationError("e1", new LocalizableError("/Login.action.user.notFound"));
			return this.getContext().getSourcePageResolution();
		}

		User user = userLoginDto.getLoggedUser();
		this.userId = user.getId().toString();

		if (!StringUtils.isBlank(this.redirectUrl)) {
			return new RedirectResolution(this.redirectUrl, false);
		}

		return new RedirectResolution(LoyaltyCatalogAction.class);
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRedirectUrl() {
		return this.redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public boolean isRememberMe() {
		return this.rememberMe;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}
}
