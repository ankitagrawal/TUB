package com.hk.web.action.core.user;

import net.sourceforge.stripes.action.*;
import com.hk.taglibs.Functions;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.LocalizableError;
import com.hk.domain.user.User;
import com.hk.domain.user.Address;
import com.hk.domain.user.BillingAddress;
import com.hk.domain.user.Role;
import com.hk.domain.order.Order;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.Country;
import com.hk.pact.service.core.AddressService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.RoleService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.dao.order.OrderDao;
import com.hk.web.action.core.payment.PaymentAction;
import com.hk.web.action.core.payment.PaymentModeAction;
import com.hk.web.action.core.payment.PaymentSuccessAction;
import com.hk.web.action.core.cart.CartAction;
import com.hk.web.action.HomeAction;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.manager.OrderManager;
import com.akube.framework.stripes.action.BaseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Nov 8, 2012
 * Time: 1:31:09 PM
 * To change this template use File | Settings | File Templates.
 */
@Secure
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
    @Autowired
    private OrderService orderService;
    @Autowired
    OrderDao orderDao;
    @Autowired
    OrderManager orderManager;

    private List<BillingAddress> billingAddresses = new ArrayList<BillingAddress>(0);

    BillingAddress deleteAddress;

    private String email;

    private Order order;

    private BillingAddress selectedAddress;


    @DefaultHandler
    @DontValidate
    public Resolution pre() {
      User user = getUserService().getUserById(getPrincipal().getId());
      order = orderManager.getOrCreateOrder(user);
      if(order.getAddress()==null){
          addRedirectAlertMessage(new SimpleMessage("You have not selected the shipping address"));
           return  new RedirectResolution(SelectAddressAction.class);
      }
      billingAddresses = addressDao.getVisibleBillingAddress(user);
      return new ForwardResolution("/pages/billingAddressBook.jsp");
    }

    public Resolution remove() {
        User user = getUserService().getUserById(getPrincipal().getId());
        if(billingAddressId!=null){
        deleteAddress = addressDao.getBillingAddressById(billingAddressId);
        deleteAddress.setUser(user);
        deleteAddress.setDeleted(true);
        addressDao.save(deleteAddress);
        return new RedirectResolution(BillingAddressAction.class);
        }
        else{
            addRedirectAlertMessage(new SimpleMessage("No Billing Address Exist"));
            return new RedirectResolution(BillingAddressAction.class);
        }
    }


    public Resolution checkout() {
        User user = getUserService().getUserById(getPrincipal().getId());
        if (billingAddressId !=null) {
            order = orderManager.getOrCreateOrder(user);
            if(order.getCartLineItems().size()==0){
                return new RedirectResolution(CartAction.class);
            }
            selectedAddress = addressDao.getBillingAddressById(billingAddressId);
            selectedAddress.getOrders().add(order);
            selectedAddress.setUser(user);
            addressDao.save(selectedAddress);
            return new RedirectResolution(PaymentAction.class, "proceed").addParameter("paymentMode", paymentMode).addParameter("order", order).addParameter("bankId", bankId).addParameter("billingAddressId", selectedAddress.getId());
        } else {
             addRedirectAlertMessage(new SimpleMessage("No Billing Address Exist"));
            return new RedirectResolution(BillingAddressAction.class);
        }
    }


    public Resolution create() {
        User user = getUserService().getUserById(getPrincipal().getId());
            order = orderManager.getOrCreateOrder(user);
            if(order.getCartLineItems().size()==0){
                return new RedirectResolution(CartAction.class);
            }
            address.getOrders().add(order);
            address.setUser(user);
            address = addressDao.save(address);
            return new RedirectResolution(PaymentAction.class, "proceed").addParameter("paymentMode", paymentMode).addParameter("order", order).addParameter("bankId", bankId).addParameter("billingAddressId", address.getId());
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

    public OrderService getOrderService() {
        return orderService;
    }
}

