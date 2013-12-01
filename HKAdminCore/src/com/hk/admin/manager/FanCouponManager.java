package com.hk.admin.manager;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hk.domain.coupon.FbcouponCoupon;
import com.hk.domain.coupon.FbcouponUser;
import com.hk.domain.coupon.FbcouponUserCampaign;
import com.hk.domain.marketing.FbcouponCampaign;
import com.hk.domain.pk.FbcouponUserCampaignPk;
import com.hk.pact.dao.coupon.FbcouponCampaignDao;
import com.hk.pact.dao.coupon.FbcouponCouponDao;
import com.hk.pact.dao.coupon.FbcouponUserCampaignDao;
import com.hk.pact.dao.coupon.FbcouponUserDao;

@Component
public class FanCouponManager {

    @Autowired
    FbcouponCampaignDao     fbcouponCampaignDao;

    @Autowired
    FbcouponUserCampaignDao fbcouponUserCampaignDao;

    @Autowired
    FbcouponUserDao         fbcouponUserDao;

    @Autowired
    FbcouponCouponDao       fbcouponCouponDao;

    public List<FbcouponCampaign> getCampaignList() {
        return getFbcouponCampaignDao().getAll(FbcouponCampaign.class);
    }

    public void createCampaign(String name, String code, String url, List<String> couponCodes, List<String> bonusCouponCodes) {
        createCampaign(name, code, url, couponCodes, bonusCouponCodes, true);
    }

    public void createCampaign(String name, String code, String url, List<String> couponCodes, List<String> bonusCouponCodes, boolean checkDuplicate) {
        FbcouponCampaign fbcouponCampaign = new FbcouponCampaign();
        fbcouponCampaign.setCode(code);
        fbcouponCampaign.setName(name);
        fbcouponCampaign.setShareUrl(url);
        fbcouponCampaign = getFbcouponCampaignDao().save(fbcouponCampaign);

        // insert coupons now
        int ctr = 0;
        for (String couponCode : couponCodes) {
            FbcouponCoupon couponDb = null;
            if (checkDuplicate) {
                couponDb = getFbcouponCouponDao().findByCode(couponCode);
            }
            if (couponDb == null) {
                FbcouponCoupon coupon = new FbcouponCoupon();
                coupon.setCoupon(couponCode);
                coupon.setFbcouponCampaign(fbcouponCampaign);
                coupon.setCouponUseCount(0L);
                coupon.setBonusCouponUseCount(0L);
                if (bonusCouponCodes.size() > ctr) {
                    coupon.setBonusCoupon(bonusCouponCodes.get(ctr));
                    ctr++;
                }
                getFbcouponCouponDao().save(coupon);
            }
        }

    }

    public FbcouponUser getOrCreateFbUser(String fbuid, String appId, String accessToken, String email, String gender, Date birthdayAsDate) {
        FbcouponUser fbcouponUser = getFbcouponUserDao().findByFbuidAndAppId(fbuid, appId);
        if (fbcouponUser == null) {
            fbcouponUser = new FbcouponUser();
            fbcouponUser.setFbuid(fbuid);
            fbcouponUser.setAccessToken(accessToken);
            fbcouponUser.setAppId(appId);
            fbcouponUser.setEmail(email);
            fbcouponUser.setGender(gender);
            fbcouponUser.setDob(birthdayAsDate);
            fbcouponUser = getFbcouponUserDao().save(fbcouponUser);
        }
        return fbcouponUser;
    }

    @Transactional
    public FbcouponUserCampaign getCouponForUser(FbcouponUser fbcouponUser, FbcouponCampaign fbcouponCampaign) {
        FbcouponCoupon coupon;

        FbcouponUserCampaignPk id = new FbcouponUserCampaignPk(fbcouponUser.getId(), fbcouponCampaign.getCode());
        FbcouponUserCampaign userCampaign = (FbcouponUserCampaign) getFbcouponUserCampaignDao().findById(id);
        if (userCampaign == null) {
            // user had not taken part in this campaign yet. give him a coupon (if available)
            coupon = getFbcouponCouponDao().findFreshCoupon(fbcouponCampaign);

            userCampaign = new FbcouponUserCampaign();
            userCampaign.setId(id);
            userCampaign.setFbcouponCoupon(coupon);
            userCampaign.setShared(false);

            userCampaign = getFbcouponUserCampaignDao().save(userCampaign);

            // now update the coupon use count
            coupon.setCouponUseCount(coupon.getCouponUseCount() + 1);
            coupon = getFbcouponCouponDao().save(coupon);
        }

        return userCampaign;
    }

    public String getBonusCouponForUser(FbcouponUser fbcouponUser, FbcouponCampaign fbcouponCampaign) {
        FbcouponUserCampaign userCampaign = getCouponForUser(fbcouponUser, fbcouponCampaign);
        FbcouponCoupon coupon = userCampaign.getFbcouponCoupon();

        if (!userCampaign.getShared()) {
            userCampaign.setShared(true);
            userCampaign = getFbcouponUserCampaignDao().save(userCampaign);

            coupon.setBonusCouponUseCount(coupon.getCouponUseCount() + 1);
            coupon = getFbcouponCouponDao().save(coupon);
        }

        return coupon.getBonusCoupon();
    }

    public FbcouponCampaignDao getFbcouponCampaignDao() {
        return fbcouponCampaignDao;
    }

    public void setFbcouponCampaignDao(FbcouponCampaignDao fbcouponCampaignDao) {
        this.fbcouponCampaignDao = fbcouponCampaignDao;
    }

    public FbcouponUserCampaignDao getFbcouponUserCampaignDao() {
        return fbcouponUserCampaignDao;
    }

    public void setFbcouponUserCampaignDao(FbcouponUserCampaignDao fbcouponUserCampaignDao) {
        this.fbcouponUserCampaignDao = fbcouponUserCampaignDao;
    }

    public FbcouponUserDao getFbcouponUserDao() {
        return fbcouponUserDao;
    }

    public void setFbcouponUserDao(FbcouponUserDao fbcouponUserDao) {
        this.fbcouponUserDao = fbcouponUserDao;
    }

    public FbcouponCouponDao getFbcouponCouponDao() {
        return fbcouponCouponDao;
    }

    public void setFbcouponCouponDao(FbcouponCouponDao fbcouponCouponDao) {
        this.fbcouponCouponDao = fbcouponCouponDao;
    }

}
