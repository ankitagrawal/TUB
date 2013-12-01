package com.hk.web.action.core;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import com.hk.web.action.core.menu.SitemapAction;

@UrlBinding("/ror.xml")
public class RorAction extends SitemapAction {

  public Resolution pre() {
    super.pre();
    return new ForwardResolution("/ror.jsp");
  }

}
