package com.hk.pact.dao.coupon;

import com.hk.domain.coupon.FbcouponUserCampaign;
import com.hk.domain.pk.FbcouponUserCampaignPk;
import com.hk.pact.dao.BaseDao;

public interface FbcouponUserCampaignDao extends BaseDao {

    public FbcouponUserCampaign findById(FbcouponUserCampaignPk fbcouponUserCampaignPk);

    public FbcouponUserCampaign save(FbcouponUserCampaign userCampaign);

}
