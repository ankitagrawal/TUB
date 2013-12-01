package com.hk.web.action.facebook.app.coupon;

import java.util.List;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.manager.FanCouponManager;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.marketing.FbcouponCampaign;

@Secure(hasAnyRoles = { RoleConstants.MARKETING })
@Component
public class FanCouponAdminAction extends BaseAction {

    @Autowired
    FanCouponManager               fanCouponManager;

    private List<FbcouponCampaign> campaignList;

    public Resolution pre() {
        campaignList = fanCouponManager.getCampaignList();
        return new ForwardResolution("/pages/facebook/app/coupon/fanCouponAdmin.jsp");
    }

    public List<FbcouponCampaign> getCampaignList() {
        return campaignList;
    }
}
