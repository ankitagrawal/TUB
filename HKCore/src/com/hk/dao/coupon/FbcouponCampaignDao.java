package com.hk.dao.coupon;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.util.BaseUtils;
import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.marketing.FbcouponCampaign;



@Repository
public class FbcouponCampaignDao extends BaseDaoImpl {

   

  @Transactional
  public FbcouponCampaign save(FbcouponCampaign campaign) {
    if (campaign != null) {
      if (campaign.getCreateDate() == null) campaign.setCreateDate(BaseUtils.getCurrentTimestamp());
    }

    return (FbcouponCampaign) super.save(campaign);
  }
  

}

