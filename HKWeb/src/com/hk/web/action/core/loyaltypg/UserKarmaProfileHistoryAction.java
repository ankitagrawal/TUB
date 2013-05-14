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
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.loyaltypg.UserBadgeInfo;
import com.hk.domain.loyaltypg.UserOrderKarmaProfile;
import com.hk.domain.user.User;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.taglibs.Functions;
/**
 * @author Ankit Chhabra
 *
 */


@Component
@Secure(hasAnyRoles = {RoleConstants.HK_LOYALTY_USER}, authActionBean=JoinLoyaltyProgramAction.class)
public class UserKarmaProfileHistoryAction extends BasePaginatedAction {


	private User user;
	private UserBadgeInfo badgeInfo;
	private List<UserOrderKarmaProfile> karmaList;
	private Page pointsPage;
	
	
	@Autowired  
	private LoyaltyProgramService loyaltyProgramService;
	
	private double validPoints;
	private String upgradeString;
	private double pointsConverted;
	
	@DefaultHandler
	public Resolution pre() {
		if (this.getPrincipal() != null) {
			this.user = this.getUserService().getUserById(this.getPrincipal().getId());
			this.pointsPage = this.loyaltyProgramService.getUserLoyaltyProfileHistory(this.user, this.getPageNo(), this.getPerPage());
			this.karmaList = this.pointsPage.getList();
			this.validPoints = this.loyaltyProgramService.calculateLoyaltyPoints(this.user);
			this.badgeInfo = this.loyaltyProgramService.getUserBadgeInfo(this.user);
			double upgradeAmount =  this.loyaltyProgramService.fetchNextLevelInfo(this.user).getSpendRequired();
			Calendar cal = Calendar.getInstance();
			if (this.badgeInfo.getUpdationTime() != null) {
				cal.setTime(this.badgeInfo.getUpdationTime());
			} else {
				cal.setTime(this.badgeInfo.getCreationTime());
			}
			cal.add(Calendar.YEAR, 1);
			SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd,yyyy");
			if (upgradeAmount != 0) {
				this.upgradeString = "To move up a level, you need to spend :  Rs. " + Functions.roundNumberForDisplay(upgradeAmount) + " before " + dateFormat.format(cal.getTime());
				} else {
					this.upgradeString = "Hi! You are our Mogul. Make most of its use by shopping more and retaining this privileged position.";
				}
			
		}

		return new ForwardResolution("/pages/loyalty/userKarmaPointsHistory.jsp");
	}
	
	public Resolution convertPoints() {
		if (this.getPrincipal() != null) {
			this.user = this.getUserService().getUserById(this.getPrincipal().getId());
			this.pointsConverted = this.loyaltyProgramService.convertLoyaltyToRewardPoints(this.user);
		}

		return new RedirectResolution(UserKarmaProfileHistoryAction.class).addParameter("pointsConverted", this.pointsConverted);
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

	/**
	 * @return the pointsConverted
	 */
	public double getPointsConverted() {
		return this.pointsConverted;
	}

	/**
	 * @param pointsConverted the pointsConverted to set
	 */
	public void setPointsConverted(double pointsConverted) {
		this.pointsConverted = pointsConverted;
	}

}