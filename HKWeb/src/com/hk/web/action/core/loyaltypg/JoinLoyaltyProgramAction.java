package com.hk.web.action.core.loyaltypg;


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
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.pact.service.RoleService;
import com.hk.pact.service.UserService;
import com.hk.web.action.core.auth.LoginAction;

@Component
@Secure(hasAnyRoles = {RoleConstants.HK_USER, RoleConstants.HK_UNVERIFIED}, authActionBean=LoginAction.class)
public class JoinLoyaltyProgramAction extends BaseAction {
	
	private String gender;
	private String name;
	
	@Autowired UserService userService;
	@Autowired RoleService roleService;
	@Autowired LoyaltyProgramService loyaltyProgramService;
	
	@DefaultHandler
	@DontValidate
	public Resolution pre() {
		return new ForwardResolution("/pages/loyalty/join.jsp");
	}

	public Resolution continueProgram() {
		User user = this.userService.getLoggedInUser();
		if(this.gender != null && this.gender.length() > 0) {
			user.setGender(this.gender);
		}
		
		if(this.name != null) {
			user.setName(this.name);
		}
		
		user.getRoles().add(this.roleService.getRoleByName(RoleConstants.HK_LOYALTY_USER));
		this.userService.save(user);
		this.loyaltyProgramService.updateUserBadgeInfo(user);
		return new RedirectResolution(LoyaltyCatalogAction.class);
	}
	
	public String getGender() {
		return this.gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
