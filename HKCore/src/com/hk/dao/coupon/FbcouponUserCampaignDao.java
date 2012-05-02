package com.hk.dao.coupon;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.util.BaseUtils;
import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.coupon.FbcouponUserCampaign;
import com.hk.domain.pk.FbcouponUserCampaignPk;

@Repository
public class FbcouponUserCampaignDao extends BaseDaoImpl {

    public FbcouponUserCampaign findById(FbcouponUserCampaignPk fbcouponUserCampaignPk) {
        return get(FbcouponUserCampaign.class, fbcouponUserCampaignPk);
    }

    @Transactional
    public FbcouponUserCampaign save(FbcouponUserCampaign userCampaign) {
        // set defaults
        if (userCampaign != null) {
            if (userCampaign.getCreateDate() == null)
                userCampaign.setCreateDate(BaseUtils.getCurrentTimestamp());
        }

        return (FbcouponUserCampaign) super.save(userCampaign);
    }

}
