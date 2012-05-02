package com.hk.web.action.admin.courier;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;


import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.courier.StateCourierService;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = {PermissionConstants.VIEW_COURIER_INFO}, authActionBean = AdminPermissionAction.class)
@Component
public class StateCourierServiceAction extends BaseAction {

  private List<StateCourierService> stateCourierServiceList = new ArrayList<StateCourierService>();

  

  @DefaultHandler
  @DontValidate
  public Resolution pre() {
    stateCourierServiceList = getBaseDao().getAll(StateCourierService.class);
    return new ForwardResolution("/pages/admin/stateCourierService.jsp");
  }

  public Resolution save() {
    for (StateCourierService stateCourierService : stateCourierServiceList) {
      getBaseDao().save(stateCourierService);
    }
    addRedirectAlertMessage(new SimpleMessage("Changes saved"));
    return new RedirectResolution(StateCourierServiceAction.class);
  }

  public List<StateCourierService> getStateCourierServiceList() {
    return stateCourierServiceList;
  }

  public void setStateCourierServiceList(List<StateCourierService> stateCourierServiceList) {
    this.stateCourierServiceList = stateCourierServiceList;
  }
}