package com.hk.web.action.core.affiliate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.constants.affiliate.EnumAffiliateStatus;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.affiliate.AffiliateStatus;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.manager.AffiliateManager;
import com.hk.pact.dao.affiliate.AffiliateDao;
import com.hk.pact.service.RoleService;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = {PermissionConstants.VERIFY_AFFILIATES}, authActionBean = AdminPermissionAction.class)
@Component
public class VerifyRejectAffiliateAction extends BasePaginatedAction {
	@Autowired
	private RoleService roleService;
	@Autowired
	AffiliateDao affiliateDao;
	@Autowired
	AffiliateManager affiliateManager;

	Page affiliatePage;
	List<User> unverifiedAffiliateUsers;
	List<Affiliate> unverifiedAffiliates = new ArrayList<Affiliate>();
	List<User> affiliateUsersToVerifyOrReject;
	String customMessage;
	String name;
	String email;
	String websiteName;
	String code;
	Long affiliateMode;
	Long affiliateType;
	Role role;
	AffiliateStatus affiliateStatus;

	@SuppressWarnings("unchecked")
    @DefaultHandler
	public Resolution pre() {
		List<Long> affiliateStatusIds = Arrays.asList(EnumAffiliateStatus.Unverified.getId());
		affiliatePage = affiliateDao.searchAffiliates(affiliateStatusIds, name, email, websiteName, code, affiliateMode, affiliateType,null, getPerPage(), getPageNo());
		if (affiliatePage != null) {
			unverifiedAffiliates = affiliatePage.getList();
		}
		return new ForwardResolution("/pages/affiliate/verifyAffiliate.jsp");
	}

	@SuppressWarnings("unchecked")
    public Resolution search() {
		List<Long> affiliateStatusIds = new ArrayList<Long>();
		if(affiliateStatus != null){
			affiliateStatusIds.add(affiliateStatus.getId());
		}
		affiliatePage = affiliateDao.searchAffiliates(affiliateStatusIds, name, email, websiteName, code, affiliateMode, affiliateType, role, getPerPage(), pageNo);
		unverifiedAffiliates = affiliatePage.getList();
		return new ForwardResolution("/pages/affiliate/verifyAffiliate.jsp");
	}

	public Resolution verifyAffiliates() {
		for (User user : affiliateUsersToVerifyOrReject) {
			affiliateManager.verifyAffiliate(user, customMessage);
		}
		addRedirectAlertMessage(new SimpleMessage("Selected Affiliates verified."));
		return new RedirectResolution(VerifyRejectAffiliateAction.class);
	}

	public Resolution rejectAffiliates() {
		for (User user : affiliateUsersToVerifyOrReject) {
			affiliateManager.rejectAffiliate(user);
		}
		addRedirectAlertMessage(new SimpleMessage("Selected Affiliates rejected."));
		return new RedirectResolution(VerifyRejectAffiliateAction.class);
	}

	public List<User> getUnverifiedAffiliateUsers() {
		return unverifiedAffiliateUsers;
	}

	public void setUnverifiedAffiliateUsers(List<User> unverifiedAffiliateUsers) {
		this.unverifiedAffiliateUsers = unverifiedAffiliateUsers;
	}

	public List<Affiliate> getUnverifiedAffiliates() {
		return unverifiedAffiliates;
	}

	public void setUnverifiedAffiliates(List<Affiliate> unverifiedAffiliates) {
		this.unverifiedAffiliates = unverifiedAffiliates;
	}

	public List<User> getAffiliateUsersToVerifyOrReject() {
		return affiliateUsersToVerifyOrReject;
	}

	public void setAffiliateUsersToVerifyOrReject(List<User> affiliateUsersToVerifyOrReject) {
		this.affiliateUsersToVerifyOrReject = affiliateUsersToVerifyOrReject;
	}

	public String getCustomMessage() {
		return customMessage;
	}

	public void setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
	}


	public Set<String> getParamSet() {
		HashSet<String> params = new HashSet<String>();
		params.add("name");
		params.add("email");
		return params;
	}

	public int getPerPageDefault() {
		return 20;
	}

	public int getPageCount() {
		return affiliatePage == null ? 0 : affiliatePage.getTotalPages();
	}

	public int getResultCount() {
		return affiliatePage == null ? 0 : affiliatePage.getTotalResults();
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
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

	public Page getAffiliatePage() {
		return affiliatePage;
	}

	public void setAffiliatePage(Page affiliatePage) {
		this.affiliatePage = affiliatePage;
	}

	public String getWebsiteName() {
		return websiteName;
	}

	public void setWebsiteName(String websiteName) {
		this.websiteName = websiteName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public AffiliateStatus getAffiliateStatus() {
		return affiliateStatus;
	}

	public void setAffiliateStatus(AffiliateStatus affiliateStatus) {
		this.affiliateStatus = affiliateStatus;
	}


}
