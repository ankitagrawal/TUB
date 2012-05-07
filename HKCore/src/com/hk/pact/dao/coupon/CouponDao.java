package com.hk.pact.dao.coupon;

import com.hk.domain.coupon.Coupon;
import com.hk.pact.dao.BaseDao;

public interface CouponDao extends BaseDao {

    public Coupon save(Coupon coupon);

    public Coupon findByCode(String couponCode);

}
