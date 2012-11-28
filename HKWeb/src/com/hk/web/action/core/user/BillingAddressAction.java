package com.hk.web.action.core.user;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.LocalizableError;
import com.hk.domain.user.User;
import com.hk.domain.user.Address;
import com.hk.domain.user.BillingAddress;
import com.hk.domain.user.Role;
import com.hk.domain.order.Order;
import com.hk.domain.core.PaymentMode;
import com.hk.pact.service.core.AddressService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.RoleService;
import com.hk.web.action.core.payment.PaymentAction;
import com.hk.constants.core.RoleConstants;
import com.akube.framework.stripes.action.BaseAction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Nov 8, 2012
 * Time: 1:31:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class BillingAddressAction extends BaseAction {

    private BillingAddress address;

    private Long billingAddressId;

    Long bankId;

    private PaymentMode paymentMode;

    @Autowired
    AddressService addressDao;

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    private List<BillingAddress> billingAddresses = new ArrayList<BillingAddress>(1);

    BillingAddress deleteAddress;

    private String email;

    private Order order;

    private BillingAddress selectedAddress;


    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        User user = getUserService().getUserById(getPrincipal().getId());
        email = user.getEmail();
        billingAddresses = addressDao.getVisibleBillingAddress(user);
        return new ForwardResolution("/pages/billingAddressBook.jsp");
    }

    public Resolution remove() {
        User user = getUserService().getUserById(getPrincipal().getId());
        deleteAddress = addressDao.getBillingAddressById(billingAddressId);
        deleteAddress.setUser(user);
        deleteAddress.setDeleted(true);
        addressDao.save(deleteAddress);
        return new RedirectResolution(BillingAddressAction.class);
    }


    public Resolution checkout() {
        Role tempUserRole = getRoleService().getRoleByName(RoleConstants.TEMP_USER);
        User user = getUserService().getUserById(getPrincipal().getId());
        if (user.getRoles().contains(tempUserRole)) {
            user.setEmail(email);
            user = getUserService().save(user);
        }
        selectedAddress = addressDao.getBillingAddressById(billingAddressId);
        selectedAddress.getOrders().add(order);
        selectedAddress.setUser(user);
        addressDao.save(selectedAddress);
        return new RedirectResolution(PaymentAction.class, "proceed").addParameter("paymentMode", paymentMode).addParameter("order", order).addParameter("bankId", bankId);
    }


    public Resolution create() {
        User user = getUserService().getUserById(getPrincipal().getId());
        address.getOrders().add(order);
        address.setUser(user);
        addressDao.save(address);
        return new RedirectResolution(PaymentAction.class, "proceed").addParameter("paymentMode", paymentMode).addParameter("order", order).addParameter("bankId", bankId);
    }


    public List<BillingAddress> getBillingAddresses() {
        return billingAddresses;
    }

    public void setBillingAddresses(List<BillingAddress> billingAddresses) {
        this.billingAddresses = billingAddresses;
    }

    public BillingAddress getDeleteAddress() {
        return deleteAddress;
    }

    public void setDeleteAddress(BillingAddress deleteAddress) {
        this.deleteAddress = deleteAddress;
    }

    public BillingAddress getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(BillingAddress selectedAddress) {
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

    public BillingAddress getAddress() {
        return address;
    }

    public void setAddress(BillingAddress address) {
        this.address = address;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public AddressService getAddressDao() {
        return addressDao;
    }

    public void setAddressDao(AddressService addressDao) {
        this.addressDao = addressDao;
    }

    public Long getBillingAddressId() {
        return billingAddressId;
    }

    public void setBillingAddressId(Long billingAddressId) {
        this.billingAddressId = billingAddressId;
    }
}

