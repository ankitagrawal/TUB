package com.hk.web.action.admin.address;

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
import com.hk.dao.core.AddressDao;
import com.hk.dao.order.OrderDao;
import com.hk.domain.order.Order;
import com.hk.domain.user.Address;
import com.hk.manager.AddressBookManager;
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
    AddressDao                  addressDao;
    @Autowired
    AddressMatchScoreCalculator addressMatchScoreCalculator;

    @Validate(required = true)
    private Order               order;

    @Validate(required = true, on = "replace")
    private Address             address;

    @ValidateNestedProperties( {
            @Validate(field = "name", maxlength = 80, required = true, on = { "save" }),
            @Validate(field = "line1", maxlength = 120, required = true, on = { "save" }),
            @Validate(field = "line2", maxlength = 120, on = { "save" }),
            @Validate(field = "city", maxlength = 60, required = true, on = { "save" }),
            @Validate(field = "state", maxlength = 50, required = true, on = { "save" }),
            @Validate(field = "pin", maxlength = 10, required = true, on = { "save" }),
            @Validate(field = "phone", maxlength = 25, required = true, on = { "save" }) })
    private Address             newAddress;

    private boolean             copyToUserAddressBook;

    OrderDao                    orderDao;

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
        newAddress = order.getAddress();
        return new ForwardResolution("/pages/admin/editAddress.jsp");
    }

    public Resolution save() {
        if (copyToUserAddressBook) {
            newAddress = addressBookManager.editAddress(order.getUser(), order.getAddress(), this.newAddress); // here
                                                                                                                // edited
                                                                                                                // address
                                                                                                                // is
                                                                                                                // the
                                                                                                                // new
                                                                                                                // address
                                                                                                                // which
                                                                                                                // is to
                                                                                                                // be
                                                                                                                // saved
        } else {
            if (address == null) {
                address = order.getAddress();
            }
            boolean isDuplicateAddress = addressMatchScoreCalculator.isDuplicateAddress(address);
            if (!isDuplicateAddress) {
                newAddress = addressDao.save(newAddress);
            }
        }
        if (newAddress != null) {
            order.setAddress(newAddress);
            orderDao.save(order);
            addRedirectAlertMessage(new LocalizableMessage("/ChangeOrderAddress.action.address.edited"));
        } else {
            addRedirectAlertMessage(new SimpleMessage("Duplicate address, it has been used in a different HK account too."));
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
}
