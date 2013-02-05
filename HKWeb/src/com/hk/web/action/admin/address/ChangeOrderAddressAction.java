package com.hk.web.action.admin.address;

import com.hk.domain.core.Country;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.order.Order;
import com.hk.domain.user.Address;
import com.hk.manager.AddressBookManager;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.service.core.AddressService;
import com.hk.util.AddressMatchScoreCalculator;

/**
 * User: rahul Time: 2 Feb, 2010 2:36:37 PM
 */
@Secure(hasAnyPermissions = { PermissionConstants.UPDATE_ORDER })
@Component
public class ChangeOrderAddressAction extends BaseAction {

    @Autowired
    AddressBookManager          addressBookManager;
    @Autowired
    AddressService              addressDao;
    @Autowired
    AddressMatchScoreCalculator addressMatchScoreCalculator;

    @Validate(required = true)
    private Order               order;


    private Address             newAddress;

    private Long countryId;

    @ValidateNestedProperties( {
            @Validate(required = true, on = "replace"),
            @Validate(field = "name", maxlength = 80, required = true, on = { "save" }),
            @Validate(field = "line1", maxlength = 120, required = true, on = { "save" }),
            @Validate(field = "line2", maxlength = 120, on = { "save" }),
            @Validate(field = "city", maxlength = 60, required = true, on = { "save" }),
            @Validate(field = "state", maxlength = 50, required = true, on = { "save" }),
            @Validate(field = "pincode", maxlength = 6, required = true, on = { "save" }),
            @Validate(field = "phone", maxlength = 25, required = true, on = { "save" }) })
    private Address             address;

    private boolean             copyToUserAddressBook;

    @Autowired
    private OrderDao                    orderDao;

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/changeOrderAddress.jsp");
    }

    public Resolution replace() {
        if (!order.getUser().getAddresses().contains(address)) {
            throw new RuntimeException("Address invalid. Selected address does not belong to the specified user");
        }
        order.setAddress(address);
        orderDao.save(order);
        addRedirectAlertMessage(new LocalizableMessage("/ChangeOrderAddress.action.address.replaced"));
        return new RedirectResolution(ChangeOrderAddressAction.class).addParameter("order", order.getId());
    }

    public Resolution edit() {
        address = order.getAddress();
        return new ForwardResolution("/pages/admin/editAddress.jsp");
    }

    public Resolution save() {
      if(address.getPincode()==null){
        addRedirectAlertMessage(new SimpleMessage("We don't service to this pincode!!"));
        return new RedirectResolution(ChangeOrderAddressAction.class).addParameter("order", order.getId());
      }
      Country country = addressDao.getCountry(countryId);
      address.setCountry(country);      
        if (copyToUserAddressBook) {
            newAddress = addressBookManager.editAddress(order.getUser(), order.getAddress(), address);
        }
        if (newAddress != null) {
            order.setAddress(newAddress);
            orderDao.save(order);
            addRedirectAlertMessage(new LocalizableMessage("/ChangeOrderAddress.action.address.edited"));
        } else {
          address.setUser(order.getUser());
          address = addressDao.save(address);
          order.setAddress(address);
          orderDao.save(order);
          addRedirectAlertMessage(new LocalizableMessage("/ChangeOrderAddress.action.address.edited"));
        }
        return new RedirectResolution(ChangeOrderAddressAction.class).addParameter("order", order.getId());
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Address getNewAddress() {
        return newAddress;
    }

    public void setNewAddress(Address newAddress) {
        this.newAddress = newAddress;
    }

    public boolean isCopyToUserAddressBook() {
        return copyToUserAddressBook;
    }

    public void setCopyToUserAddressBook(boolean copyToUserAddressBook) {
        this.copyToUserAddressBook = copyToUserAddressBook;
    }

  public Long getCountryId() {
    return countryId;
  }

  public void setCountryId(Long countryId) {
    this.countryId = countryId;
  }
}
