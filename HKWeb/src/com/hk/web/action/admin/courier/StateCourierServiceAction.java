package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.courier.StateList;
import com.hk.domain.courier.StateCourierService;
import com.hk.pact.dao.courier.StateCourierServiceDao;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.ArrayList;
import java.util.List;

@Secure(hasAnyPermissions = {PermissionConstants.VIEW_COURIER_INFO}, authActionBean = AdminPermissionAction.class)
@Component
public class StateCourierServiceAction extends BaseAction {
  @Autowired
  StateCourierServiceDao stateCourierServiceDao;


  private List<StateCourierService> stateCourierServiceList = new ArrayList<StateCourierService>();
     private String state;

  private List<String> stateList=new ArrayList<String>();
//
//
//  private  List<Courier> courierList=new ArrayList<Courier>();


  

  @DefaultHandler
  @DontValidate
  public Resolution pre() {
    stateCourierServiceList = getBaseDao().getAll(StateCourierService.class);
    stateList=StateList.stateList;
    return new ForwardResolution("/pages/admin/stateCourierService.jsp");
  }

  public Resolution save() {
    for (StateCourierService stateCourierService : stateCourierServiceList) {
      getBaseDao().save(stateCourierService);
    }
    addRedirectAlertMessage(new SimpleMessage("Changes saved"));
    return new RedirectResolution(StateCourierServiceAction.class);
  }

  public Resolution search() {
   List<StateCourierService> StateCourierServiceList  =stateCourierServiceDao.getAllStateCourierServiceByState(state);
    setStateCourierServiceList(StateCourierServiceList);
   return new ForwardResolution(StateCourierServiceAction.class);
  }



  public List<StateCourierService> getStateCourierServiceList() {
    return stateCourierServiceList;
  }

  public void setStateCourierServiceList(List<StateCourierService> stateCourierServiceList) {
    this.stateCourierServiceList = stateCourierServiceList;
  }
  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public List<String> getStateList() {
    return stateList;
  }

//   public List<Courier> getCourierList() {
//    return courierList;
//  }
  
}