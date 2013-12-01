package com.hk.web.action.core.affiliate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.hk.pact.dao.affiliate.AffiliateTxnDao;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.akube.framework.util.DateUtils;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.affiliate.AffiliateTxn;
import com.hk.domain.user.User;
import com.hk.pact.dao.affiliate.AffiliateDao;
import com.hk.pact.dao.user.UserDao;

/**
 * Created by IntelliJ IDEA. User: Pratham Date: Sep 13, 2011 Time: 6:05:35 PM To change this template use File |
 * Settings | File Templates.
 */
@Secure(hasAnyRoles = { RoleConstants.HK_AFFILIATE, RoleConstants.ADMIN })
@Component
public class AffiliateInsightsAction extends BasePaginatedAction {
    /*private static Logger      logger = Logger.getLogger(AffiliateInsightsAction.class);*/
    Affiliate                  affiliate;
    private Date               startDate;
    private Date               endDate;
    private Long               affiliateReferredOrdersCount;
    private List<AffiliateTxn> affiliateTxnOrdersList;
    private Page               affiliateTxnPage;
    @Autowired
    AffiliateDao               affiliateDao;
    @Autowired
    UserDao                    userDao;
    @Autowired
    AffiliateTxnDao affiliateTxnDao;

    public Resolution getReferredOrderDetails() {
        if (getPrincipal() != null) {
            User user = getUserService().getUserById(getPrincipal().getId());
            affiliate = affiliateDao.getAffilateByUser(user);
            Calendar cal = Calendar.getInstance();
            if (endDate == null) {
                endDate = cal.getTime();
            }
            cal.add(Calendar.MONTH, -1);
            if (startDate == null) {
                startDate = cal.getTime();
            }
            affiliateReferredOrdersCount = affiliateTxnDao.getReferredOrdersCountByAffiliate(affiliate, startDate, DateUtils.getEndOfDay(endDate));
            if (affiliateReferredOrdersCount > 0) {
                affiliateTxnPage = affiliateTxnDao.getReferredOrderListByAffiliate(affiliate, startDate, DateUtils.getEndOfDay(endDate), getPageNo(), getPerPage());
                affiliateTxnOrdersList = affiliateTxnPage.getList();
            }
        }
        return new ForwardResolution("/pages/affiliate/affiliateReferredOrders.jsp");
    }

    public int getPerPageDefault() {
        return 30;
    }

    public int getPageCount() {
        return affiliateTxnPage != null ? affiliateTxnPage.getTotalPages() : 0;
    }

    public int getResultCount() {
        return affiliateTxnPage != null ? affiliateTxnPage.getTotalResults() : 0;
    }

    public Set<String> getParamSet() {
        return null;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getAffiliateReferredOrdersCount() {
        return affiliateReferredOrdersCount;
    }

    public void setAffiliateReferredOrdersCount(Long affiliateReferredOrdersCount) {
        this.affiliateReferredOrdersCount = affiliateReferredOrdersCount;
    }

    public List<AffiliateTxn> getAffiliateTxnOrdersList() {
        return affiliateTxnOrdersList;
    }

    public void setAffiliateTxnOrdersList(List<AffiliateTxn> affiliateTxnOrdersList) {
        this.affiliateTxnOrdersList = affiliateTxnOrdersList;
    }

    public Page getAffiliateTxnPage() {
        return affiliateTxnPage;
    }

    public void setAffiliateTxnPage(Page affiliateTxnPage) {
        this.affiliateTxnPage = affiliateTxnPage;
    }

    public Affiliate getAffiliate() {
        return affiliate;
    }

    public void setAffiliate(Affiliate affiliate) {
        this.affiliate = affiliate;
    }

}
