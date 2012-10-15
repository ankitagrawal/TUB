package com.hk.impl.service.discount;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.coupon.CouponType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.util.DateUtils;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.offer.Offer;
import com.hk.domain.user.User;
import com.hk.exception.HealthKartCouponException;
import com.hk.pact.dao.coupon.CouponDao;
import com.hk.pact.service.discount.CouponService;
import com.hk.util.TokenUtils;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponDao couponDao;
    
    @Override
    @Transactional
    public Coupon createCoupon(String couponCode, Date endDate, Long allowedTimes, Long alreadyUsed, Offer offer, User referrerUser, Boolean repetitiveUsage, CouponType couponType) {
        Coupon coupon = new Coupon();
        coupon.setCode(couponCode);
        coupon.setEndDate(DateUtils.getEndOfDay(endDate));
        coupon.setAllowedTimes(allowedTimes);
        coupon.setAlreadyUsed(alreadyUsed);
        coupon.setOffer(offer);
        coupon.setReferrerUser(referrerUser);
        coupon.setRepetitiveUsage(repetitiveUsage);
	    coupon.setCouponType(couponType);
        return save(coupon);
    }

    @Override
    public Coupon findByCode(String couponCode) {
        return getCouponDao().findByCode(couponCode);
    }

    @Override
    public List<Coupon> generateCoupons(String endPart, String couponCode, Long numberOfCoupons, Boolean repetitiveUsage, Date endDate, Long allowedTimes, Long alreadyUsed,
                                        Offer offer, CouponType couponType, User referredUser) throws HealthKartCouponException {
        List<Coupon> coupons = new ArrayList<Coupon>(1000);
        if (endPart == null)
            endPart = "";

        if (couponCode.length() + endPart.length() > 14) {
            throw new HealthKartCouponException("coupon length limit exceeded");
        }

        // generate coupons
        for (int i = 1; i <= numberOfCoupons; i++) {
            String code = TokenUtils.generateCouponCode(couponCode, endPart);
            if (findByCode(code) == null) {
                if (repetitiveUsage == null)
                    repetitiveUsage = false;
                Coupon coupon = createCoupon(code, endDate, allowedTimes, alreadyUsed, offer, referredUser, repetitiveUsage, couponType);
                coupons.add(coupon);
            } else {
                i--;
            }
        }
        return coupons;
    }

	@Override
	public List<Coupon> getAffiliateUnusedCoupons(Affiliate affiliate) {
		return couponDao.affiliateCoupon(affiliate);
	}

	@Override
    @Transactional
    public Coupon save(Coupon coupon) {
        return getCouponDao().save(coupon);
    }

    public CouponDao getCouponDao() {
        return couponDao;
    }

    public void setCouponDao(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    
    
}
