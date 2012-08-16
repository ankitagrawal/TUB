package com.hk.web.action.admin.address;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.order.Order;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.web.action.admin.AdminHomeAction;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.UPDATE_ORDER }, authActionBean = AdminPermissionAction.class)
@Component
public class AdminSelectAddressAction extends BaseAction {

    /*private static Logger logger = LoggerFactory.getLogger(AdminSelectAddressAction.class);*/

    @Validate(required = true)
    private Order         order;

    @Validate(on = "selectAddress", required = true)
    private Address       selectedAddress;

    private List<Address> addresses;

    @Autowired
    AddressDao            addressDao;
    @Autowired
    OrderDao              orderDao;

    @DefaultHandler
    public Resolution pre() {
        User user = order.getUser();
        addresses = addressDao.getVisibleAddresses(user);
        selectedAddress = order.getAddress();
        if (selectedAddress == null) {
            // get the last order address? for not selecting just first non deleted one.
            if (addresses != null && addresses.size() > 0) {
                selectedAddress = addresses.get(0);
            }
        }
        return new ForwardResolution("/pages/admin/addressBook.jsp");
    }

    public Resolution selectAddress() {
        order.setAddress(selectedAddress);
        orderDao.save(order);

        addRedirectAlertMessage(new SimpleMessage("address has been updated for order " + order.getGatewayOrderId()));
        return new RedirectResolution(AdminHomeAction.class);
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public Address getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(Address selectedAddress) {
        this.selectedAddress = selectedAddress;
    }
}
