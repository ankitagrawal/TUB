package com.hk.web.action.core.affiliate;

import java.util.Calendar;
import java.util.Date;
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
import com.akube.framework.util.DateUtils;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.affiliate.AffiliateTrafficDetails;
import com.hk.domain.user.User;
import com.hk.pact.dao.affiliate.AffiliateDao;
import com.hk.pact.dao.affiliate.AffiliateTrafficDetailsDao;
import com.hk.pact.dao.user.UserDao;

/**
 * Created by IntelliJ IDEA. User: Pratham Date: Sep 13, 2011 Time: 6:13:38 PM To change this template use File |
 * Settings | File Templates.
 */
@Secure(hasAnyRoles = { RoleConstants.HK_AFFILIATE, RoleConstants.ADMIN })
@Component
public class AffiliateStatisticsAction extends BasePaginatedAction {
    /*private static Logger                 logger = Logger.getLogger(AffiliateStatisticsAction.class);*/
    Affiliate                             affiliate;
    private Date                          startDate;
    private Date                          endDate;
    private Long                          affiliateTrafficCount;
    private List<AffiliateTrafficDetails> affiliateTrafficDetails;
    private Page                          affiliateTrafficDetailsPage;
    @Autowired
    AffiliateDao                          affiliateDao;
    @Autowired
    UserDao                               userDao;
    @Autowired
    AffiliateTrafficDetailsDao            affiliateTrafficDetailsDao;

    @DefaultHandler
    public Resolution getTrafficCount() {
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
            affiliateTrafficCount = affiliateTrafficDetailsDao.getAffiliateTrafficCount(affiliate, startDate, DateUtils.getEndOfDay(endDate));
            if (affiliateTrafficCount > 0) {
                affiliateTrafficDetailsPage = affiliateTrafficDetailsDao.getAffiliateTrafficDetails(affiliate, startDate, DateUtils.getEndOfDay(endDate), getPageNo(), getPerPage());
                affiliateTrafficDetails = affiliateTrafficDetailsPage.getList();
            }
        }
        return new ForwardResolution("/pages/affiliate/affiliateStatistics.jsp");
    }

    public int getPerPageDefault() {
        return 30;
    }

    public int getPageCount() {
        return affiliateTrafficDetailsPage != null ? affiliateTrafficDetailsPage.getTotalPages() : 0;
    }

    public int getResultCount() {
        return affiliateTrafficDetailsPage != null ? affiliateTrafficDetailsPage.getTotalResults() : 0;
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

    public Long getAffiliateTrafficCount() {
        return affiliateTrafficCount;
    }

    public void setAffiliateTrafficCount(Long affiliateTrafficCount) {
        this.affiliateTrafficCount = affiliateTrafficCount;
    }

    public List<AffiliateTrafficDetails> getAffiliateTrafficDetails() {
        return affiliateTrafficDetails;
    }

    public void setAffiliateTrafficDetails(List<AffiliateTrafficDetails> affiliateTrafficDetails) {
        this.affiliateTrafficDetails = affiliateTrafficDetails;
    }

    public Page getAffiliateTrafficDetailsPage() {
        return affiliateTrafficDetailsPage;
    }

    public void setAffiliateTrafficDetailsPage(Page affiliateTrafficDetailsPage) {
        this.affiliateTrafficDetailsPage = affiliateTrafficDetailsPage;
    }

    public Affiliate getAffiliate() {
        return affiliate;
    }

    public void setAffiliate(Affiliate affiliate) {
        this.affiliate = affiliate;
    }
}
