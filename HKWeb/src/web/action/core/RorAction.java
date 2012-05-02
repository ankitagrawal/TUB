package web.action.core;

import web.action.core.menu.SitemapAction;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/ror.xml")
public class RorAction extends SitemapAction {

  public Resolution pre() {
    super.pre();
    return new ForwardResolution("/ror.jsp");
  }

}
