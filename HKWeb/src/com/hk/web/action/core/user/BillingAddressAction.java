package com.hk.web.action.core.user;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.validation.Validate;
import com.hk.domain.user.User;
import com.hk.domain.user.Address;
import com.hk.domain.user.BillingAddress;
import com.hk.domain.order.Order;
import com.hk.domain.core.PaymentMode;
//import com.hk.domain.order.OrderBillingAddress;
import com.hk.pact.service.core.AddressService;
import com.hk.pact.service.UserService;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.dao.core.AddressDao;
import com.hk.manager.OrderManager;
import com.hk.web.action.core.payment.PaymentModeAction;
import com.hk.web.action.core.payment.PaymentAction;
import com.akube.framework.stripes.action.BaseAction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Nov 8, 2012
 * Time: 1:31:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class BillingAddressAction extends BaseAction {

    private BillingAddress billingAddress;
    @Autowired
    AddressDao addressDao;
    Long bankId;

    @Validate(required = true)
    private PaymentMode paymentMode;

    Order order;

    @DefaultHandler
    public Resolution pre() {
        User user = getUserService().getUserById(getPrincipal().getId());
        billingAddress = addressDao.searchBillingAddress(user);
        return new RedirectResolution(PaymentModeAction.class);
    }


    public Resolution save() {
        User user = getUserService().getUserById(getPrincipal().getId());
        BillingAddress existingBillingAddress = addressDao.searchBillingAddress(user);
        if (existingBillingAddress != null) {
            existingBillingAddress.setPin(billingAddress.getPin());
            existingBillingAddress.setName(billingAddress.getName());
            existingBillingAddress.setLine1(billingAddress.getLine1());
            existingBillingAddress.setLine2(billingAddress.getLine2());
            existingBillingAddress.setPhone(billingAddress.getPhone());
            existingBillingAddress.setState(billingAddress.getState());
            existingBillingAddress.setCity(billingAddress.getCity());
            existingBillingAddress.setUser(user);
            existingBillingAddress.getOrders().add(order);
            addressDao.save(existingBillingAddress);

        } else {
            billingAddress.getOrders().add(order);
            billingAddress.setUser(user);
            addressDao.save(billingAddress);
        }
        return new RedirectResolution(PaymentAction.class, "proceed").addParameter("paymentMode", paymentMode).addParameter("order", order).addParameter("bankId", bankId);
    }


    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

    public AddressDao getAddressDao() {
        return addressDao;
    }

    public void setAddressDao(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    public void setBillingAddress(BillingAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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
}
