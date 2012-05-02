package web.action.core;

import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.web.HealthkartResponse;

public class HeartbeatAction extends BaseAction {

  public Resolution beat() {
    HealthkartResponse healthkartResponse;
    if (getSecurityManager().getSubject().isAuthenticated()) {
      healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "authenticated");
    } else {
      healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "un-authenticated");
    }
    return new JsonResolution(healthkartResponse);
  }

}
