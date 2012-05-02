package com.hk.web.action.facebook.app.referral;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import com.akube.framework.stripes.action.BaseAction;

public class FbReferralAction extends BaseAction {

  public Resolution pre() {
    return new ForwardResolution("/pages/facebook/app/referral/fbReferral.jsp");
  }
  
}
