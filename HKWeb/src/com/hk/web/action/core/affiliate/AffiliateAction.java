package com.hk.web.action.core.affiliate;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
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
import com.hk.constants.core.RoleConstants;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.user.User;
import com.hk.dto.user.UserLoginDto;
import com.hk.exception.HealthkartLoginException;
import com.hk.exception.HealthkartSignupException;
import com.hk.manager.AffiliateManager;
import com.hk.manager.LinkManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.RoleDao;

@UrlBinding("/affiliate")
@Component
public class AffiliateAction extends BaseAction {
	@Validate(required = true, on = "signup")
	private String name;

	@Validate(required = true, converter = EmailTypeConverter.class)
	private String email;

	@Validate(required = true)
	private String password;

	@Validate(required = true, expression = "this eq password", on = "signup")
	private String passwordConfirm;

	private String websiteName;
	private boolean agreeToTerms;
	boolean rememberMe;

	private static Logger logger = Logger.getLogger(AffiliateAction.class);
	@Autowired
	LinkManager linkManager;
	@Autowired
	UserManager userManager;
	@Autowired
	AffiliateManager affiliateManager;
	@Autowired
	RoleDao roleDao;
	Long affiliateType;
	Long affiliateMode;

	@DefaultHandler
	@DontValidate
	public Resolution pre() {
		return new ForwardResolution("/pages/affiliate/affiliate.jsp");
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
		return new ForwardResolution("/pages/affiliate/affiliateSignup.jsp");
	}

	public Resolution signup() throws Exception {
		try {
			affiliateManager.signup(name, email, password, websiteName, affiliateType, affiliateMode);
		} catch (HealthkartSignupException e) {
			getContext().getValidationErrors().add("already exists", new LocalizableError("/Signup.action.email.id.already.exists"));
			return new ForwardResolution(getContext().getSourcePage());
		}
		addRedirectAlertMessage(new SimpleMessage(
				"Welcome!!..Your account has not been verified yet. You will be informed as soon as it is verified. Mean while, why don't you fill up the requisted information"));
		return new ForwardResolution(AffiliateAccountAction.class);
	}

	public Resolution login() throws Exception {
		Affiliate affiliate;
		try {
			affiliate = affiliateManager.login(email, password, rememberMe);
			if (affiliate != null) {
				logger.debug("Affiliate Id : " + affiliate.getId());
			} else {
				logger.debug("affiliate is null.");
				getSubject().logout();
				addRedirectAlertMessage(new SimpleMessage("You can't log in as an affiliate"));
				return new ForwardResolution("/pages/affiliate/affiliate.jsp");
			}
		} catch (HealthkartLoginException e) {
			// Note: if the login fails, existing subject is still retained
			addValidationError("e1", new LocalizableError("/Login.action.user.notFound"));
			return getContext().getSourcePageResolution();
		}
		if (affiliate.getUser().getRoles().contains(getRoleService().getRoleByName(RoleConstants.HK_AFFILIATE_UNVERIFIED))) {
			return new ForwardResolution(AffiliateAccountAction.class);
		} else {
			return new ForwardResolution(AffiliateAccountAction.class, "affiliateAccount");
		}
	}

	@DontValidate
	public Resolution goToLoginExisting() {
		return new ForwardResolution("/pages/affiliate/userToAffiliate.jsp");
	}

	public Resolution loginExisting() throws Exception {
		try {
			UserLoginDto userLoginDto = userManager.login(email, password, rememberMe);
			User user = userLoginDto.getLoggedUser();
			affiliateManager.userToAffiliate(user, websiteName, affiliateMode, affiliateType);
		} catch (HealthkartLoginException e) {
			addValidationError("e1", new LocalizableError("/Login.action.user.notFound"));
			return new ForwardResolution("/pages/affiliate/affiliateSignup.jsp");
		} catch (HealthkartSignupException e) {
			addValidationError("e1", new LocalizableError("/Affiliate.action.userid.already.exists"));
			return new ForwardResolution("/pages/affiliate/affiliateSignup.jsp");
		}
		addRedirectAlertMessage(new SimpleMessage(
				"Welcome!!..Your account has not been verfied yet.You will be informed as soon as it is verified. Mean while, why dont you fill up the requisted information, so that we can contact you"));
		return new ForwardResolution(AffiliateAccountAction.class);
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

	public Long getAffiliateMode() {
		return affiliateMode;
	}

	public void setAffiliateMode(Long affiliateMode) {
		this.affiliateMode = affiliateMode;
	}

	public Long getAffiliateType() {
		return affiliateType;
	}

	public void setAffiliateType(Long affiliateType) {
		this.affiliateType = affiliateType;
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

}
