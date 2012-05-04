package com.hk.web.action.core.menu;

import org.springframework.beans.factory.annotation.Autowired;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.helper.MenuHelper;
import com.hk.web.action.HomeAction;

public class MenuRefreshAction extends BaseAction {

    @Autowired
  MenuHelper menuHelper;

  public Resolution pre() {
    menuHelper.refresh();
    return new ForwardResolution(HomeAction.class);
  }
}
