package com.hk.impl.dao.coupon;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.util.BaseUtils;
import com.hk.domain.marketing.FbcouponCampaign;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.coupon.FbcouponCampaignDao;

@Repository
public class FbcouponCampaignDaoImpl extends BaseDaoImpl implements FbcouponCampaignDao {

    @Transactional
    public FbcouponCampaign save(FbcouponCampaign campaign) {
        if (campaign != null) {
            if (campaign.getCreateDate() == null)
                campaign.setCreateDate(BaseUtils.getCurrentTimestamp());
        }

        return (FbcouponCampaign) super.save(campaign);
    }

}
