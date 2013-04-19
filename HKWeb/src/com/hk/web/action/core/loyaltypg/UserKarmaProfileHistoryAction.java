/**
 * 
 */
package com.hk.web.action.core.loyaltypg;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	
	private double validPoints;
	private String upgradeString;
	
	@DefaultHandler
	public Resolution pre() {
		if (this.getPrincipal() != null) {
			this.user = this.getUserService().getUserById(this.getPrincipal().getId());
			this.pointsPage = this.loyaltyProgramService.getProfileHistory(this.user, this.getPageNo(), this.getPerPage());
			this.karmaList = this.pointsPage.getList();
			this.validPoints = this.loyaltyProgramService.calculateValidPoints(this.user.getId());
			this.badgeInfo = this.loyaltyProgramService.getUserBadgeInfo(this.user.getId());
			double upgradeAmount =  this.loyaltyProgramService.calculateUpgradePoints(this.badgeInfo);
			Calendar cal = Calendar.getInstance();
			cal.setTime(this.badgeInfo.getUpdationTime());
			cal.add(Calendar.YEAR, 1);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			if (upgradeAmount != -1) {
				this.upgradeString = "To move up a level, you need to spend :  Rs. " + upgradeAmount + " before " + dateFormat.format(cal.getTime());
				} else {
					this.upgradeString = "Congratulations! You are at highest level till at least " +  dateFormat.format(cal.getTime());
				}
			
			/*for(UserOrderKarmaProfile profile : this.karmaList) {
				
				if (TransactionType.DEBIT.equals(profile.getTransactionType())
						&& KarmaPointStatus.APPROVED.equals(profile.getStatus())) {
					this.pointsDebited += profile.getKarmaPoints();
				} else if (TransactionType.CREDIT.equals(profile.getTransactionType())
						&& KarmaPointStatus.APPROVED.equals(profile.getStatus())) {
					this.pointsCredited += profile.getKarmaPoints();
				}
			}*/
			
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

	/**
	 * @return the upgradeString
	 */
	public String getUpgradeString() {
		return this.upgradeString;
	}

	/**
	 * @param upgradeString the upgradeString to set
	 */
	public void setUpgradeString(String upgradeString) {
		this.upgradeString = upgradeString;
	}

	/**
	 * @return the validPoints
	 */
	public double getValidPoints() {
		return this.validPoints;
	}

	/**
	 * @param validPoints the validPoints to set
	 */
	public void setValidPoints(double validPoints) {
		this.validPoints = validPoints;
	}

}