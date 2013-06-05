/**
 * 
 */
package com.hk.web.action.core.loyaltypg;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.hk.constants.core.RoleConstants;
import com.hk.loyaltypg.service.LoyaltyProgramService;

/**
 * @author Ankit Chhabra
 *
 */
@Component
@Secure(hasAnyRoles = {RoleConstants.HK_LOYALTY_USER}, authActionBean=JoinLoyaltyProgramAction.class)
public class LoyaltyProductAction extends AbstractLoyaltyAction {
	@Autowired
	private LoyaltyProgramService loyaltyProgramService;

	@DefaultHandler
	public Resolution pre() {
		
		return new ForwardResolution("/pages/loyalty/productDescription.jsp");
	}

}
