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
import com.hk.domain.loyaltypg.UserBadgeInfo;
import com.hk.domain.loyaltypg.UserOrderKarmaProfile;
import com.hk.domain.loyaltypg.UserOrderKarmaProfile.KarmaPointStatus;
import com.hk.domain.loyaltypg.UserOrderKarmaProfile.TransactionType;
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
	private UserBadgeInfo badgeInfo;
	private List<UserOrderKarmaProfile> karmaList;
	private Page pointsPage;
	
	@Autowired  
	private LoyaltyProgramService loyaltyProgramService;

	private double pointsDebited;
	private double pointsCredited;
	
	@DefaultHandler
	public Resolution pre() {
		if (this.getPrincipal() != null) {
			this.user = this.getUserService().getUserById(this.getPrincipal().getId());
			this.pointsPage = this.loyaltyProgramService.getProfileHistory(this.user, this.getPageNo(), this.getPerPage());
			this.karmaList = this.pointsPage.getList();
			this.badgeInfo = this.loyaltyProgramService.getUserBadgeInfo(this.user.getId());
			this.pointsCredited=0;
			this.pointsDebited=0;
			for(UserOrderKarmaProfile profile : this.karmaList) {
				
				if (TransactionType.DEBIT.equals(profile.getTransactionType())
						&& KarmaPointStatus.APPROVED.equals(profile.getStatus())) {
					this.pointsDebited += profile.getKarmaPoints();
				} else if (TransactionType.CREDIT.equals(profile.getTransactionType())
						&& KarmaPointStatus.APPROVED.equals(profile.getStatus())) {
					this.pointsCredited += profile.getKarmaPoints();
				}
			}
			
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

	/**
	 * @return the pointsDebited
	 */
	public double getPointsDebited() {
		return this.pointsDebited;
	}

	/**
	 * @param pointsDebited the pointsDebited to set
	 */
	public void setPointsDebited(double pointsDebited) {
		this.pointsDebited = pointsDebited;
	}

	/**
	 * @return the pointsCredited
	 */
	public double getPointsCredited() {
		return this.pointsCredited;
	}

	/**
	 * @param pointsCredited the pointsCredited to set
	 */
	public void setPointsCredited(double pointsCredited) {
		this.pointsCredited = pointsCredited;
	}

	/**
	 * @return the badgeInfo
	 */
	public UserBadgeInfo getBadgeInfo() {
		return this.badgeInfo;
	}

	/**
	 * @param badgeInfo the badgeInfo to set
	 */
	public void setBadgeInfo(UserBadgeInfo badgeInfo) {
		this.badgeInfo = badgeInfo;
	}

}