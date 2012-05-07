package com.hk.web.action.core.affiliate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.user.User;
import com.hk.manager.AffiliateManager;
import com.hk.pact.dao.affiliate.AffiliateDao;
import com.hk.pact.service.RoleService;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.VERIFY_AFFILIATES }, authActionBean = AdminPermissionAction.class)
@Component
public class VerifyAffiliateAction extends BasePaginatedAction {
    @Autowired
    private RoleService roleService;
    @Autowired
    AffiliateDao        affiliateDao;
    @Autowired
    AffiliateManager    affiliateManager;

    Page          userPage;
    List<User>          unverifiedAffiliateUsers;
    List<Affiliate>     unverifiedAffiliates = new ArrayList<Affiliate>();
    List<User>          affiliateUsersToVerify;
    String              customMessage;

    @DefaultHandler
    public Resolution pre() {
        userPage = getUserService().findByRole(getRoleService().getRoleByName(RoleConstants.HK_AFFILIATE_UNVERIFIED), getPageNo(), getPerPage());
        unverifiedAffiliateUsers = userPage.getList();
        for (User user : unverifiedAffiliateUsers) {
            unverifiedAffiliates.add(affiliateDao.getAffilateByUser(user));
        }
        return new ForwardResolution("/pages/affiliate/verifyAffiliate.jsp");
    }

    public Resolution verifyAffiliates() {
        for (User user : affiliateUsersToVerify) {
            affiliateManager.verifyAffiliate(user, customMessage);
        }
        addRedirectAlertMessage(new SimpleMessage("Affiliates are now verified."));
        return new ForwardResolution("/pages/admin/adminHome.jsp");
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

    public List<User> getAffiliateUsersToVerify() {
        return affiliateUsersToVerify;
    }

    public void setAffiliateUsersToVerify(List<User> affiliateUsersToVerify) {
        this.affiliateUsersToVerify = affiliateUsersToVerify;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }

    public Set<String> getParamSet() {
        return null;
    }

    public int getPerPageDefault() {
        return 20;
    }

    public int getPageCount() {
        return userPage == null ? 0 : userPage.getTotalPages();
    }

    public int getResultCount() {
        return userPage == null ? 0 : userPage.getTotalResults();
    }

    public RoleService getRoleService() {
        return roleService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

}
