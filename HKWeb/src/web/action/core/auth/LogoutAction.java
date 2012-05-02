package web.action.core.auth;

import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import com.akube.framework.stripes.action.BaseAction;

/**
 * User: kani
 * Time: 7 Oct, 2009 2:06:17 PM
 */
public class LogoutAction extends BaseAction {

  public Resolution pre() {
    getSubject().logout();
    return new RedirectResolution(LoginAction.class);
  }

}
