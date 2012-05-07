package com.hk.pact.dao.coupon;

import com.hk.domain.coupon.FbcouponCoupon;
import com.hk.domain.marketing.FbcouponCampaign;
import com.hk.pact.dao.BaseDao;

public interface FbcouponCouponDao extends BaseDao {

    public FbcouponCoupon save(FbcouponCoupon coupon);

    public FbcouponCoupon findByCode(String code);

    public FbcouponCoupon findFreshCoupon(FbcouponCampaign fbcouponCampaign);

}
