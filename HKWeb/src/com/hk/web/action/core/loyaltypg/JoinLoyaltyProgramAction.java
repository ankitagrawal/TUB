package com.hk.web.action.core.loyaltypg;


import java.util.Date;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.user.User;
import com.hk.pact.service.RoleService;
import com.hk.pact.service.UserService;
import com.hk.web.action.core.auth.LoginAction;

@Component
@Secure(hasAnyRoles = {RoleConstants.HK_USER}, authActionBean=LoginAction.class)
public class JoinLoyaltyProgramAction extends BaseAction {
	
	String gender;
	Date dob;
	
	@Autowired UserService userService;
	@Autowired RoleService roleService;
	
	@DefaultHandler
	@DontValidate
	public Resolution pre() {
		return new ForwardResolution("/pages/loyalty/join.jsp");
	}

	public Resolution continueProgram() {
		User user = userService.getLoggedInUser();
		if(gender != null && gender.length() > 0) {
			user.setGender(gender);
		}
		
		if(dob != null) {
			user.setBirthDate(dob);
		}
		
		user.getRoles().add(roleService.getRoleByName(RoleConstants.HK_LOYALTY_USER));
		userService.save(user);
		return new RedirectResolution(LoyaltyCatalogAction.class);
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}

}
