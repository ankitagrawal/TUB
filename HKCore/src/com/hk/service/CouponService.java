package com.hk.service;

import java.util.Date;
import java.util.List;

import com.hk.domain.coupon.Coupon;
import com.hk.domain.offer.Offer;
import com.hk.domain.user.User;
import com.hk.exception.HealthKartCouponException;

public interface CouponService {

    public Coupon save(Coupon coupon);

    public Coupon createCoupon(String couponCode, Date endDate, Long allowedTimes, Long alreadyUsed, Offer offer, User referrerUser, Boolean repetitiveUsage);

    public Coupon findByCode(String couponCode);

    public List<Coupon> generateCoupons(String endPart, String couponCode, Long numberOfCoupons, Boolean repetitiveUsage, Date endDate, Long allowedTimes, Long alreadyUsed,
            Offer offer) throws HealthKartCouponException;
}
