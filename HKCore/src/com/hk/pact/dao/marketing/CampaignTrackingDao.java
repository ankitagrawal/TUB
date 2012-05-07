package com.hk.pact.dao.marketing;

import com.hk.domain.marketing.CampaignTracking;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;

public interface CampaignTrackingDao extends BaseDao {

    public CampaignTracking saveRequest(String referralUrl, String targetUrl, String utmsource, String utmmedium, String utmcampaign, User user);

}
