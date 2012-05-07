package com.hk.web.action.facebook.app.coupon;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.admin.manager.FanCouponManager;
import com.hk.domain.coupon.FbcouponUser;
import com.hk.domain.marketing.FbcouponCampaign;
import com.hk.web.HealthkartResponse;

@Component
public class FanCouponClaimBonusAction extends BaseAction {

    @Validate(required = true, encrypted = true)
    private FbcouponUser     fbcouponUser;

    @Validate(required = true)
    private FbcouponCampaign fbcouponCampaign;

    FanCouponManager         fanCouponManager;

    @SuppressWarnings("unchecked")
    @JsonHandler
    public Resolution bonus() {
        String bonusCoupon = fanCouponManager.getBonusCouponForUser(fbcouponUser, fbcouponCampaign);

        Map map = new HashMap();
        map.put("bonus", bonusCoupon);
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "good", map);
        return new JsonResolution(healthkartResponse);
    }

    public FbcouponUser getFbcouponUser() {
        return fbcouponUser;
    }

    public void setFbcouponUser(FbcouponUser fbcouponUser) {
        this.fbcouponUser = fbcouponUser;
    }

    public FbcouponCampaign getFbcouponCampaign() {
        return fbcouponCampaign;
    }

    public void setFbcouponCampaign(FbcouponCampaign fbcouponCampaign) {
        this.fbcouponCampaign = fbcouponCampaign;
    }
}
