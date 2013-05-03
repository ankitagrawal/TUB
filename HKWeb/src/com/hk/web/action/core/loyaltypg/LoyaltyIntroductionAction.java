package com.hk.web.action.core.loyaltypg;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;

import com.hk.constants.core.RoleConstants;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;


public class LoyaltyIntroductionAction  extends AbstractLoyaltyAction {

	@Autowired private UserService userService;

	@DefaultHandler
	public Resolution pre() {

		User user = this.userService.getLoggedInUser();

		if (user!=null && user.getRoleStrings().contains(RoleConstants.HK_LOYALTY_USER)) {
			return new ForwardResolution(LoyaltyCatalogAction.class);
		}
		return new ForwardResolution("/pages/loyalty/info/stellarIntroduction.jsp");
	}


}
