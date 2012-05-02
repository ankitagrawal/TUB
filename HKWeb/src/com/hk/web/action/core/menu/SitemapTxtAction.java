package com.hk.web.action.core.menu;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/sitemap.txt")
public class SitemapTxtAction extends SitemapAction {

  public Resolution pre() {
    super.pre();
    return new ForwardResolution("/urls.jsp");
  }

}
