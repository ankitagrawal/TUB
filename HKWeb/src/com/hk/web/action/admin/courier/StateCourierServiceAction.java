package com.hk.web.action.admin.courier;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.courier.StateList;
import com.hk.domain.core.State;
import com.hk.domain.courier.StateCourierService;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.pact.dao.courier.StateCourierServiceDao;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = {PermissionConstants.VIEW_COURIER_INFO}, authActionBean = AdminPermissionAction.class)
@Component
public class StateCourierServiceAction extends BaseAction {
  @Autowired
  StateCourierServiceDao stateCourierServiceDao;
  @Autowired
  PincodeDao pincodeDao;

 private StateCourierService stateCourierService;

  private List<StateCourierService> stateCourierServiceList = null;
  private State state;

  private List<String> stateList = new ArrayList<String>();
   private boolean displayAddNewRow;



  @DefaultHandler
  @DontValidate
  public Resolution pre() {
    displayAddNewRow=false;
    return new ForwardResolution("/pages/admin/stateCourierService.jsp");
  }


  public Resolution search() {
 stateCourierServiceList = stateCourierServiceDao.getAllStateCourierServiceByState(state);
    if (stateCourierServiceList != null && stateCourierServiceList.size() > 0) {
      setStateCourierServiceList(stateCourierServiceList);
    }
     return new ForwardResolution("/pages/admin/stateCourierService.jsp");
  }

    public Resolution save() {
         stateCourierServiceDao.save(stateCourierService);
       addRedirectAlertMessage(new SimpleMessage("state courier info saved"));
             return search();

  }

  public Resolution  addNewRow(){
    setStateList(StateList.stateList);
    if(state == null){
     addRedirectAlertMessage(new SimpleMessage("Select State"));
    return new ForwardResolution("/pages/admin/stateCourierService.jsp");
    }
   setDisplayAddNewRow(true);
     return search(); 
  }

  public List<StateCourierService> getStateCourierServiceList() {
    return stateCourierServiceList;
  }

  public void setStateCourierServiceList(List<StateCourierService> stateCourierServiceList) {
    this.stateCourierServiceList = stateCourierServiceList;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public void setStateList(List<String> stateList) {
    this.stateList = stateList;
  }

  public List<String> getStateList() {
    return stateList;
  }

  public StateCourierService getStateCourierService() {
    return stateCourierService;
  }

  public void setStateCourierService(StateCourierService stateCourierService) {
    this.stateCourierService = stateCourierService;
  }

  public boolean isDisplayAddNewRow() {
    return displayAddNewRow;
  }

  public void setDisplayAddNewRow(boolean displayAddNewRow) {
    this.displayAddNewRow = displayAddNewRow;
  }
}