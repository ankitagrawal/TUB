package com.hk.web.action.core;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import com.akube.framework.stripes.action.BaseAction;

public class GeneralErrorAction extends BaseAction {

  public Resolution pre() {
    return new ForwardResolution("/pages/generalError.jsp");
  }

}
