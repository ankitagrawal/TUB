package com.hk.web.action.facebook.app.coupon;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.admin.manager.FanCouponManager;
import com.hk.domain.coupon.FbcouponUser;
import com.hk.domain.coupon.FbcouponUserCampaign;
import com.hk.domain.marketing.FbcouponCampaign;
import com.hk.pact.dao.coupon.FbcouponUserCampaignDao;
import com.hk.pact.dao.coupon.FbcouponUserDao;
import com.hk.web.HealthkartResponse;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;

@Component
public class FanCouponClaimAction extends BaseAction implements ValidationErrorHandler {

  @Validate(required = true)
  private String fbuid;

  @Validate(required = true, on = "pre")
  private String accessToken;

  @Validate(required = true)
  private String appId;

  @Validate(required = true)
  private FbcouponCampaign fbcouponCampaign;

  private FbcouponUserCampaign fbcouponUserCampaign;

  @Autowired
   FanCouponManager fanCouponManager;

  @Autowired
   FbcouponUserCampaignDao fbcouponUserCampaignDao;

  @Autowired
   FbcouponUserDao fbcouponUserDao;

  public Resolution handleValidationErrors(ValidationErrors validationErrors) throws Exception {
    return new JsonResolution(validationErrors, getContext().getLocale());
  }

  @JsonHandler
  @DefaultHandler
  public Resolution pre() {
    FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
    com.restfb.types.User user = facebookClient.fetchObject("me", com.restfb.types.User.class);
    Connection<com.restfb.types.User> myFriends = facebookClient.fetchConnection("me/friends", com.restfb.types.User.class);

    String status = HealthkartResponse.STATUS_OK;
    String message = "";
    Map dataMap = new HashMap();

    if (myFriends.getData().size() < 25) {
      status = HealthkartResponse.STATUS_ERROR;
      message = "You should have more than 25 friends to use this app";
    } else {
      // check if user exists for fbuid and appid. save accesstoken and email also if creating new user.
      FbcouponUser fbcouponUser = fanCouponManager.getOrCreateFbUser(fbuid, appId, accessToken, user.getEmail(), user.getGender(), user.getBirthdayAsDate());

      fbcouponUserCampaign = fanCouponManager.getCouponForUser(fbcouponUser, fbcouponCampaign);

      dataMap.put("coupon", fbcouponUserCampaign.getFbcouponCoupon().getCoupon());
      dataMap.put("shared", fbcouponUserCampaign.getShared());
    }

    HealthkartResponse healthkartResponse = new HealthkartResponse(status, message, dataMap);
    return new JsonResolution(healthkartResponse);
  }

  @JsonHandler
  public Resolution share() {
    HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "");
    FbcouponUser fbcouponUser = fbcouponUserDao.findByFbuidAndAppId(fbuid, appId);
    fbcouponUserCampaign = fanCouponManager.getCouponForUser(fbcouponUser, fbcouponCampaign);
    fbcouponUserCampaign.setShared(true);
    fbcouponUserCampaignDao.save(fbcouponUserCampaign);

    return new JsonResolution(healthkartResponse);
  }

  public String getFbuid() {
    return fbuid;
  }

  public void setFbuid(String fbuid) {
    this.fbuid = fbuid;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public FanCouponManager getFanCouponManager() {
    return fanCouponManager;
  }

  public void setFanCouponManager(FanCouponManager fanCouponManager) {
    this.fanCouponManager = fanCouponManager;
  }

  public FbcouponCampaign getFbcouponCampaign() {
    return fbcouponCampaign;
  }

  public void setFbcouponCampaign(FbcouponCampaign fbcouponCampaign) {
    this.fbcouponCampaign = fbcouponCampaign;
  }

  public FbcouponUserCampaign getFbcouponUserCampaign() {
    return fbcouponUserCampaign;
  }

}
