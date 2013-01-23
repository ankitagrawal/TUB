package com.hk.web.action.core.user;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.SimpleError;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.order.Order;
import com.hk.domain.user.Address;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.manager.OrderManager;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.service.RoleService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.AddressService;
import com.hk.web.action.core.auth.LoginAction;
import com.hk.web.action.core.order.OrderSummaryAction;

@Secure(hasAnyRoles = { RoleConstants.HK_UNVERIFIED, RoleConstants.HK_USER }, authUrl = "/core/auth/Login.action?source=" + LoginAction.SOURCE_CHECKOUT, disallowRememberMe = true)
@Component
@HttpCache(allow = false)
public class SelectAddressAction extends BaseAction {

    //private static Logger logger    = LoggerFactory.getLogger(SelectAddressAction.class);

    @Autowired
    AddressService       addressDao;
    @Autowired
    OrderManager          orderManager;
    @Autowired
    OrderDao              orderDao;
    @Autowired
    private UserService   userService;
    @Autowired
    private RoleService   roleService;

    private List<Address> addresses = new ArrayList<Address>(1);

    //@Validate(required = true, on = "remove")
    Address               deleteAddress;

    //@Validate(converter = EmailTypeConverter.class)
    private String        email;

	 // @Validate(required = true)
    private Order order;

    private boolean printAlert;

	  //@ValidationMethod(on = "checkout")
    public void validate() {
        Role tempUserRole = getRoleService().getRoleByName(RoleConstants.TEMP_USER);
        User user = getUserService().getUserById(getPrincipal().getId());
        if (user.getRoles().contains(tempUserRole)) {
            if (StringUtils.isBlank(email)) {
                addValidationError("email", new SimpleError("Email is required for Guest checkout."));
            }
        }
    }

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        User user = getUserService().getUserById(getPrincipal().getId());
        email = user.getEmail();
        addresses = addressDao.getVisibleAddresses(user);
        order = orderManager.getOrCreateOrder(user);
        selectedAddress = order.getAddress();
        if (selectedAddress == null) {
            // get the last order address? for not selecting just first non deleted one.
            if (addresses != null && addresses.size() > 0) {
                selectedAddress = addresses.get(0);
            }
        }

        return new ForwardResolution("/pages/addressBook.jsp").addParameter("printAlert",printAlert);

    }

    //@ValidationMethod(on = "remove")
    public void validateDelete() {
        User user = getUserService().getUserById(getPrincipal().getId());
        if (!user.getAddresses().contains(deleteAddress)) {
            getContext().getValidationErrors().add("invalid address id", new LocalizableError("/EditAddress.action.invalid.address"));
        }
    }

    public Resolution remove() {
        User user = getUserService().getUserById(getPrincipal().getId());
        order = orderManager.getOrCreateOrder(user);

        deleteAddress.setDeleted(true);
        addressDao.save(deleteAddress);

        order.setAddress(null);
        orderDao.save(order);

        // addRedirectAlertMessage(new LocalizableMessage("/SelectAddress.action.address.deleted"));

        return new RedirectResolution(SelectAddressAction.class);
    }

//    @Validate(on = "checkout", required = true)
    Address selectedAddress;

    public Resolution checkout() {
        Role tempUserRole = getRoleService().getRoleByName(RoleConstants.TEMP_USER);
        User user = getUserService().getUserById(getPrincipal().getId());
        if (user.getRoles().contains(tempUserRole)) {
            user.setEmail(email);
            user = getUserService().save(user);
        }

        order = orderManager.getOrCreateOrder(user);
        order.setAddress(selectedAddress);
        orderDao.save(order);

        return new RedirectResolution(OrderSummaryAction.class);
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public Address getDeleteAddress() {
        return deleteAddress;
    }

    public void setDeleteAddress(Address deleteAddress) {
        this.deleteAddress = deleteAddress;
    }

    public Address getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(Address selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public RoleService getRoleService() {
        return roleService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

  public boolean isPrintAlert() {
    return printAlert;
  }

  public boolean getPrint(){
    return printAlert;
  }

  public void setPrintAlert(boolean printAlert) {
    this.printAlert = printAlert;
  }
}