package com.hk.pact.dao.coupon;

import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.coupon.Coupon;
import com.hk.pact.dao.BaseDao;

import java.util.List;

public interface CouponDao extends BaseDao {

    public Coupon save(Coupon coupon);

    public Coupon findByCode(String couponCode);

	List<Coupon> affiliateCoupon(Affiliate affiliate);
}
