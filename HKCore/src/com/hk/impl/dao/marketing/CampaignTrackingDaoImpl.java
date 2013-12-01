package com.hk.impl.dao.marketing;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.hk.domain.marketing.CampaignTracking;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.marketing.CampaignTrackingDao;

@Repository
public class CampaignTrackingDaoImpl extends BaseDaoImpl implements CampaignTrackingDao {

    public CampaignTracking saveRequest(String referralUrl, String targetUrl, String utmsource, String utmmedium, String utmcampaign, User user) {
        CampaignTracking campaignTracking = new CampaignTracking();
        campaignTracking.setReferralUrl(referralUrl);
        campaignTracking.setTargetUrl(targetUrl);
        campaignTracking.setUtmSource(utmsource);
        campaignTracking.setUtmCampaign(utmcampaign);
        campaignTracking.setUtmMedium(utmmedium);
        campaignTracking.setTimeStamp(new Date());
        campaignTracking.setUserId(user != null ? user.getId() : null);
        return (CampaignTracking) save(campaignTracking);
    }

}
