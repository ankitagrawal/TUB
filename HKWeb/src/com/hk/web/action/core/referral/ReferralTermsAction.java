package com.hk.web.action.core.referral;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import com.akube.framework.stripes.action.BaseAction;

public class ReferralTermsAction extends BaseAction {

  public Resolution pre() {
    return new ForwardResolution("/pages/modal/referralTerms.jsp");
  }

}
