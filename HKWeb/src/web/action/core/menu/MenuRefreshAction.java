package web.action.core.menu;

import web.action.HomeAction;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.helper.MenuHelper;

public class MenuRefreshAction extends BaseAction {

  
  MenuHelper menuHelper;

  public Resolution pre() {
    menuHelper.refresh();
    return new ForwardResolution(HomeAction.class);
  }
}
