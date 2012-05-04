package com.hk.web.action.admin.courier;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;


import com.akube.framework.gson.JsonUtils;
import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.constants.core.PermissionConstants;
import com.hk.dao.core.AddressDao;
import com.hk.domain.courier.Courier;
import com.hk.domain.user.Address;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = {PermissionConstants.SEARCH_ORDERS}, authActionBean = AdminPermissionAction.class)
@Component
public class SetDefaultCourierAction extends BaseAction {
    @Autowired
   AddressDao addressDao;

  @Validate(required = true)
  private Address address;

  @Validate(required = true)
  private Courier courier;

  @JsonHandler
  public Resolution pre() {
    if(address == null || courier == null) {
      HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "courier not set");
      return new JsonResolution(healthkartResponse);
    }
    address.setCourier(courier);
    addressDao.save(address);
    Map<String, Object> data = new HashMap<String, Object>(1);
    data.put("courier", JsonUtils.hydrateHibernateObject(courier));
    HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "courier set", data);
    return new JsonResolution(healthkartResponse);
  }                                                
  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public Courier getCourier() {
    return courier;
  }

  public void setCourier(Courier courier) {
    this.courier = courier;
  }


}
