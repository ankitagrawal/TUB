package com.hk.web.action.facebook.app.promo;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import com.akube.framework.stripes.action.BaseAction;

public class FanPromoAction extends BaseAction {

  public Resolution pre() {
    return new ForwardResolution("/pages/facebook/app/promo/fanPromo.jsp");
  }
  
}
