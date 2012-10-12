package com.hk.pact.service.discount;

import java.util.Date;
import java.util.List;

import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.coupon.CouponType;
import com.hk.domain.offer.Offer;
import com.hk.domain.user.User;
import com.hk.exception.HealthKartCouponException;

public interface CouponService {

    public Coupon save(Coupon coupon);

    public Coupon createCoupon(String couponCode, Date endDate, Long allowedTimes, Long alreadyUsed, Offer offer, User referrerUser, Boolean repetitiveUsage, CouponType couponType);

    public Coupon findByCode(String couponCode);

    public List<Coupon> generateCoupons(String endPart, String couponCode, Long numberOfCoupons, Boolean repetitiveUsage, Date endDate, Long allowedTimes, Long alreadyUsed,
                                        Offer offer, CouponType couponType, User referredUser) throws HealthKartCouponException;

	public List<Coupon> getAffiliateUnusedCoupons(Affiliate affiliate);
}
