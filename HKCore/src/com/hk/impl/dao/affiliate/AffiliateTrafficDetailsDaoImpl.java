package com.hk.impl.dao.affiliate;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.affiliate.AffiliateTrafficDetails;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.affiliate.AffiliateTrafficDetailsDao;

/**
 * Created by IntelliJ IDEA. User: Pratham Date: Aug 16, 2011 Time: 11:04:04 PM To change this template use File |
 * Settings | File Templates.
 */

@Repository
public class AffiliateTrafficDetailsDaoImpl extends BaseDaoImpl implements AffiliateTrafficDetailsDao {

    public AffiliateTrafficDetails saveTrafficDetails(Affiliate affiliate, String targetUrl, String referralUrl) {
        AffiliateTrafficDetails affiliateTrafficDetails = new AffiliateTrafficDetails();
        affiliateTrafficDetails.setAffiliate(affiliate);
        affiliateTrafficDetails.setTargetUrl(targetUrl);
        affiliateTrafficDetails.setReferralUrl(referralUrl);
        affiliateTrafficDetails.setUserTimestamp(new Date());
        return (AffiliateTrafficDetails) save(affiliateTrafficDetails);
    }

    @SuppressWarnings("unchecked")
    public Page getAffiliateTrafficDetails(Affiliate affiliate, Date startDate, Date endDate, int pageNo, int perPage) {
        String hqlQuery = "select id from AffiliateTrafficDetails atd where atd.affiliate=:affiliate and atd.userTimestamp >= :startDate and atd.userTimestamp <= :endDate";
        List<Long> affiliateTrafficStats = null;

        affiliateTrafficStats = findByNamedParams(hqlQuery, new String[] { "affiliate", "startDate", "endDate" }, new Object[] { affiliate, startDate, endDate });

        DetachedCriteria criteria = DetachedCriteria.forClass(AffiliateTrafficDetails.class);
        criteria.add(Restrictions.in("id", affiliateTrafficStats));

        return list(criteria, pageNo, perPage);
    }

    public Long getAffiliateTrafficCount(Affiliate affiliate, Date startDate, Date endDate) {
        String countQuery = "select count(atd.id) from AffiliateTrafficDetails atd where atd.affiliate=? and atd.userTimestamp >= ? and atd.userTimestamp <= ?";
        return count(countQuery, new Object[] { affiliate, startDate, endDate });
    }
}
