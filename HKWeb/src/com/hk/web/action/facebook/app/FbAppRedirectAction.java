package com.hk.web.action.facebook.app;

import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.FbConstants;
import com.hk.web.action.facebook.app.contest.FanContestAction;
import com.hk.web.action.facebook.app.coupon.FanCouponAction;
import com.hk.web.action.facebook.app.promo.FanPromoAction;
import com.hk.web.action.facebook.app.promo2.FanPromo2Action;
import com.hk.web.action.facebook.app.referral.FbReferralAction;

@UrlBinding("/facebook/app/{appName}")
public class FbAppRedirectAction extends BaseAction {

  private String appName;
  private static final String SIGNED_REQUEST = FbConstants.Param.signed_request;

  public Resolution sendToApp() {
    if ("coupon".equals(appName)) {
      return new RedirectResolution(FanCouponAction.class).addParameter(SIGNED_REQUEST, getContext().getRequest().getParameter(SIGNED_REQUEST));
    }
    if ("contest".equals(appName)) {
      return new RedirectResolution(FanContestAction.class).addParameter(SIGNED_REQUEST, getContext().getRequest().getParameter(SIGNED_REQUEST));
    }
    if ("promo".equals(appName)) {
      return new RedirectResolution(FanPromoAction.class).addParameter(SIGNED_REQUEST, getContext().getRequest().getParameter(SIGNED_REQUEST));
    }
    if ("promo2".equals(appName)) {
      return new RedirectResolution(FanPromo2Action.class).addParameter(SIGNED_REQUEST, getContext().getRequest().getParameter(SIGNED_REQUEST));
    }
    if ("referral".equals(appName)) {
      return new RedirectResolution(FbReferralAction.class).addParameter(SIGNED_REQUEST, getContext().getRequest().getParameter(SIGNED_REQUEST));
    }
    return null;
  }

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }
}
