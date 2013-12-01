package com.hk.pact.dao.affiliate;

import java.util.Date;

import com.akube.framework.dao.Page;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.affiliate.AffiliateTrafficDetails;
import com.hk.pact.dao.BaseDao;

public interface AffiliateTrafficDetailsDao extends BaseDao {

    public AffiliateTrafficDetails saveTrafficDetails(Affiliate affiliate, String targetUrl, String referralUrl);

    public Page getAffiliateTrafficDetails(Affiliate affiliate, Date startDate, Date endDate, int pageNo, int perPage);

    public Long getAffiliateTrafficCount(Affiliate affiliate, Date startDate, Date endDate);
}
