package web.action.admin;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.constants.core.HealthkartConstants;
import com.hk.web.HealthkartResponse;
import com.hk.web.filter.WebContext;

@Secure
public class SetInSessionAction extends BaseAction {

  private String wantedCOD = "false";

  @DefaultHandler
  @JsonHandler
  public Resolution pre() {
    WebContext.getRequest().getSession().setAttribute(HealthkartConstants.Session.wantedCOD, wantedCOD);
    Map<String, Object> data = new HashMap<String, Object>(1);
    HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Value set in session", data);
    return new JsonResolution(healthkartResponse);
  }

  public void setWantedCOD(String wantedCOD) {
    this.wantedCOD = wantedCOD;
  }
}