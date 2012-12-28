package com.hk.rest.mobile.service.action;

/**
 * Created by IntelliJ IDEA.
 * User: Satish
 * Date: Oct 9, 2012
 * Time: 7:27:50 PM
 * To change this template use File | Settings | File Templates.
 */
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.util.CryptoUtil;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.session.Session;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.core.HealthkartConstants;
import com.hk.domain.user.User;
import com.hk.exception.HealthkartSignupException;
import com.hk.manager.LinkManager;
import com.hk.manager.UserManager;
import com.hk.pact.service.UserService;
import com.hk.rest.mobile.service.utils.MHKConstants;
import com.hk.web.HealthkartResponse;

@Path("/mSignup")
@Component
public class MSignUpAction extends MBaseAction {

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

	// private String redirectUrl;
	// private String source;

	@Session(key = HealthkartConstants.Session.signupDate)
	private String signupDate;

	@Autowired
	private UserManager userManager;
	@Autowired
	private LinkManager linkManager;
	@Autowired
	private UserService userService;

	/*
	 * @DefaultHandler
	 * 
	 * @DontValidate public Resolution pre() { return new
	 * ForwardResolution("/pages/signup.jsp"); }
	 */

	/*
	 * @ValidationMethod() public void isValidEmail() { if
	 * (!BaseUtils.isValidEmail(email)) {
	 * getContext().getValidationErrors().add("invalidEmail", new
	 * LocalizableError("/Signup.action.InvalidEmail")); } }
	 * 
	 * @ValidationMethod public void conformPassword() { if
	 * (!password.equals(passwordConfirm)) {
	 * getContext().getValidationErrors().add("passwordConfirm", new
	 * LocalizableError("/Signup.action.PasswordDontMatch")); } }
	 */

	/*
	 * @ValidationMethod public void signupValidation() { if (!agreeToTerms) {
	 * getContext().getValidationErrors().add("terms", new
	 * LocalizableError("/Signup.action.terms.notAgree",
	 * getLinkManager().getTermsAndConditionsUrl())); } }
	 */

	@DefaultHandler
	@POST
	@Path("/signup/")
	@Produces("application/json")
	public String signup(@FormParam("email") String email,
			@FormParam("name") String name,
			@FormParam("password") String password,
			@Context HttpServletRequest request) {
		User referredBy = null;
		HealthkartResponse healthkartResponse;
		String jsonBuilder = "";
		String message = MHKConstants.STATUS_DONE;
		String status = MHKConstants.STATUS_OK;
		Cookie referredByCookie = BaseUtils.getCookie(request,
				HealthkartConstants.Cookie.referred_by);
		if (referredByCookie != null) {
			referredBy = getUserService().findByUserHash(
					CryptoUtil.decrypt(referredByCookie.getValue()));
		}
		try {
			userManager.signup(email, name, password, referredBy);
		} catch (HealthkartSignupException e) {
			message = MHKConstants.EMAIL_ID_ALRDY_EXIST;
			status = MHKConstants.STATUS_ERROR;
		}

		Map<String,Object> map = new HashMap<String, Object>();
		map.put("email", email);
		map.put("name", name);

		/*
		 * Date currentDate = Calendar.getInstance().getTime(); signupDate =
		 * GAUtil.formatDate(currentDate); MUserLoginJSONResponse jsonResponse =
		 * new MUserLoginJSONResponse();
		 * if(null!=userManager.getUserService()&&null
		 * !=userManager.getUserService
		 * ().getLoggedInUser()&&null!=userManager.getUserService
		 * ().getLoggedInUser().getAddresses()){ User user =
		 * userManager.getUserService().getLoggedInUser(); for(Address
		 * address:userManager
		 * .getUserService().getLoggedInUser().getAddresses()){
		 * jsonResponse.setCity(address.getCity());
		 * jsonResponse.setState(address.getState());
		 * jsonResponse.setEmail(user.getEmail());
		 * jsonResponse.setId(user.getId());
		 * jsonResponse.setLine1(address.getLine1());
		 * jsonResponse.setLine2(address.getLine2());
		 * jsonResponse.setLogin(user.getLogin());
		 * jsonResponse.setName(user.getName());
		 * jsonResponse.setPhone(address.getPhone());
		 * jsonResponse.setPin(address.getPin()); } }
		 */
		/*
		 * if (!StringUtils.isBlank(redirectUrl)) { return new
		 * RedirectResolution(redirectUrl, false); }
		 */
		/*
		 * 
		 * if (!StringUtils.isBlank(source) &&
		 * source.equals(LoginAction.SOURCE_CHECKOUT)) { //return new
		 * RedirectResolution(SelectAddressAction.class); }
		 */

		healthkartResponse = new HealthkartResponse(status, message, map);
		jsonBuilder = com.akube.framework.gson.JsonUtils.getGsonDefault()
				.toJson(healthkartResponse);
		return jsonBuilder;
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

	/*
	 * public String getRedirectUrl() { return redirectUrl; }
	 * 
	 * public void setRedirectUrl(String redirectUrl) { this.redirectUrl =
	 * redirectUrl; }
	 * 
	 * public String getSource() { return source; }
	 * 
	 * public void setSource(String source) { this.source = source; }
	 */

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
