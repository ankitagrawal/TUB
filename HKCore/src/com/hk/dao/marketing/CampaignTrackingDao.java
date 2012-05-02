package com.hk.dao.marketing;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.marketing.CampaignTracking;
import com.hk.domain.user.User;

@Repository
public class CampaignTrackingDao extends BaseDaoImpl {

  /*public CampaignTrackingDao() {
    super(CampaignTracking.class);
  }*/

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

