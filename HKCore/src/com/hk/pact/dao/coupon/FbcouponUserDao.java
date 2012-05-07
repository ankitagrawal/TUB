package com.hk.pact.dao.coupon;

import com.hk.domain.coupon.FbcouponUser;
import com.hk.pact.dao.BaseDao;

public interface FbcouponUserDao extends BaseDao {

    public FbcouponUser save(FbcouponUser user);

    public FbcouponUser findByFbuidAndAppId(String fbuid, String appId);
}
