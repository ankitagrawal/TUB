package com.hk.web.action.core.auth;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import com.akube.framework.stripes.action.BaseAction;

public class AuthRequiredAction  extends BaseAction {

  public Resolution pre() {
    return new ForwardResolution("");
  }

}
