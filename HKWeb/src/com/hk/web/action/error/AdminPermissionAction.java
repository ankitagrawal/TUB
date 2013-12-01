package com.hk.web.action.error;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import com.akube.framework.stripes.action.BaseAction;

public class AdminPermissionAction extends BaseAction {

  public Resolution pre() {
    return new ForwardResolution("/pages/error/adminPermission.jsp");
  }

}
