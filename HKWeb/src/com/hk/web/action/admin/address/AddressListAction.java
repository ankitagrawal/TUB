package com.hk.web.action.admin.address;

import java.util.List;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;


import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.order.Order;
import com.hk.domain.user.Address;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(
    hasAnyPermissions = {PermissionConstants.VIEW_ACTION_QUEUE, PermissionConstants.UPDATE_ACTION_QUEUE, PermissionConstants.SEARCH_ORDERS},
    authActionBean = AdminPermissionAction.class
)
@Component
public class AddressListAction extends BaseAction {

  @Validate(required = true)
  private Order order;

  List<Address> addressList;
  Address selectedAddress;
  
  public Resolution pre() {
    addressList = order.getUser().getAddresses();
    selectedAddress = order.getAddress();
    return new ForwardResolution("/pages/admin/userAddreses.jsp");
  }

  public Address getSelectedAddress() {
    return selectedAddress;
  }

  public void setSelectedAddress(Address selectedAddress) {
    this.selectedAddress = selectedAddress;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public List<Address> getAddressList() {
    return addressList;
  }

  public void setAddressList(List<Address> addressList) {
    this.addressList = addressList;
  }
}
