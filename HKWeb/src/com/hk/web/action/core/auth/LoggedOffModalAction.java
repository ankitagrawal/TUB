package com.hk.web.action.core.auth;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import com.akube.framework.stripes.action.BaseAction;

public class LoggedOffModalAction extends BaseAction {

  public Resolution pre() {
    return new ForwardResolution("/pages/modal/loggedOff.jsp");
  }

}
