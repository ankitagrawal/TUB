package com.hk.web.action.admin.address;

import java.util.List;

import com.hk.pact.service.core.AddressService;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.order.Order;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.user.Address;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(
        hasAnyPermissions = {PermissionConstants.VIEW_ACTION_QUEUE, PermissionConstants.UPDATE_ACTION_QUEUE, PermissionConstants.SEARCH_ORDERS},
        authActionBean = AdminPermissionAction.class
)
@Component
public class AdminAddressListAction extends BaseAction {

    @Validate(required = true, on="changeOrderAddress")
    private Order order;
    @Validate(required = true, on="changeSubscriptionAddress")
    private Subscription subscription;

    @Autowired
    AddressService addressDao;

    List<Address> addressList;
    Address selectedAddress;

    public Resolution changeOrderAddress() {
        addressList = addressDao.getVisibleAddresses(order.getUser());
        selectedAddress = order.getAddress();
        return new ForwardResolution("/pages/admin/userAddreses.jsp");
    }

    public Resolution changeSubscriptionAddress(){
        addressList = addressDao.getVisibleAddresses(subscription.getUser());
        selectedAddress = subscription.getAddress();
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

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
}
