package com.hk.web.action.facebook.app.promo2;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import com.akube.framework.stripes.action.BaseAction;

public class FanPromo2Action extends BaseAction {

  public Resolution pre() {
    return new ForwardResolution("/pages/facebook/app/promo2/fanPromo2.jsp");
  }

}
