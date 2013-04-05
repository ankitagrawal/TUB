/**
 * 
 */
package com.hk.web.action.core.loyaltypg;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.domain.loyaltypg.UserOrderKarmaProfile;
import com.hk.domain.user.User;
import com.hk.loyaltypg.service.LoyaltyProgramService;
/**
 * @author Ankit Chhabra
 *
 */

@Secure
@Component
public class UserKarmaProfileHistoryAction extends BasePaginatedAction {


	private User user;

	private List<UserOrderKarmaProfile> karmaList;
	private Page pointsPage;
	
	@Autowired  
	private LoyaltyProgramService loyaltyProgramService;


	@DefaultHandler
	public Resolution pre() {
		if (this.getPrincipal() != null) {
			this.user = this.getUserService().getUserById(this.getPrincipal().getId());
			this.pointsPage = this.loyaltyProgramService.getProfileHistory(this.user, this.getPageNo(), this.getPerPage());
			this.karmaList = this.pointsPage.getList();
		}

		return new ForwardResolution("/pages/loyalty/userKarmaPointsHistory.jsp");
	}

	public User getUser() {
		return this.user;
	}


	@Override
	public int getPerPageDefault() {
		return 20;
	}

	@Override
	public int getPageCount() {
		return this.pointsPage != null ? this.pointsPage.getTotalPages() : 0;
	}

	@Override
	public int getResultCount() {
		return this.pointsPage != null ? this.pointsPage.getTotalResults() : 0;
	}

	@Override
	public Set<String> getParamSet() {
		return new HashSet<String>();
	}

	/**
	 * @return the loyaltyProgramService
	 */
	public LoyaltyProgramService getLoyaltyProgramService() {
		return this.loyaltyProgramService;
	}

	/**
	 * @param loyaltyProgramService the loyaltyProgramService to set
	 */
	public void setLoyaltyProgramService(LoyaltyProgramService loyaltyProgramService) {
		this.loyaltyProgramService = loyaltyProgramService;
	}

	/**
	 * @return the karmaList
	 */
	public List<UserOrderKarmaProfile> getKarmaList() {
		return this.karmaList;
	}

	/**
	 * @param karmaList the karmaList to set
	 */
	public void setKarmaList(List<UserOrderKarmaProfile> karmaList) {
		this.karmaList = karmaList;
	}

}