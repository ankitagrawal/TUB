package com.hk.web.action.core.payment;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumGateway;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Gateway;
import com.hk.domain.payment.Issuer;
import com.hk.domain.payment.Payment;
import com.hk.domain.user.BillingAddress;
import com.hk.manager.OrderManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.core.AddressDao;
import com.hk.web.action.core.auth.LoginAction;
import com.hk.web.action.core.payment.gateway.hkpay.HKPaySendReceiveAction;
import net.sourceforge.stripes.action.HttpCache;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

/*
 * User: Pratham
 * Date: 12/1/13  Time: 9:27 PM
*/

@Component
@Secure(hasAnyRoles = {RoleConstants.HK_UNVERIFIED, RoleConstants.HK_USER}, authUrl = "/core/auth/Login.action?source=" + LoginAction.SOURCE_CHECKOUT, disallowRememberMe = true)
@HttpCache(allow = false)
public class MakePaymentAction extends BaseAction {

    private static Logger logger = LoggerFactory.getLogger(PaymentAction.class);
    Long billingAddressId;
    @Validate(required = true)
    Issuer issuer;
    @Autowired
    PaymentManager paymentManager;
    @Autowired
    OrderManager orderManager;
    @Autowired
    AddressDao addressDao;
    @Validate(required = true, encrypted = true)
    private Order order;

    String issuerCode;

    @SuppressWarnings("unchecked")
    public Resolution proceed() {
        if (order.getOrderStatus().getId().equals(EnumOrderStatus.InCart.getId())) {
            order = orderManager.recalAndUpdateAmount(order);
            Gateway gateway = EnumGateway.HKPay.asGateway();
            PaymentMode paymentMode = EnumPaymentMode.ONLINE_PAYMENT.asPaymenMode();
            BillingAddress billingAddress = null;
            if (billingAddressId != null) {
                billingAddress = addressDao.getBillingAddressById(billingAddressId);
            }

            if (issuer != null) {
                // first create a payment row, this will also contain the payment checksum
                Payment payment = paymentManager.createNewPayment(order, paymentMode, BaseUtils.getRemoteIpAddrForUser(getContext()), gateway, issuer, billingAddress);
                Object tdp = BasePaymentGatewayWrapper.encodeTransactionDataParam(order.getAmount(), payment.getGatewayOrderId(), order.getId(), payment.getPaymentChecksum(), issuerCode, billingAddressId);
                return new RedirectResolution(HKPaySendReceiveAction.class, "proceed").addParameter(BasePaymentGatewayWrapper.TRANSACTION_DATA_PARAM, tdp);
            }
        }
        addRedirectAlertMessage(new SimpleMessage("Payment for the order is already made."));
        return new RedirectResolution(PaymentModeAction.class).addParameter("order", order);
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Issuer getIssuer() {
        return issuer;
    }

    public void setIssuer(Issuer issuer) {
        this.issuer = issuer;
    }

    public Long getBillingAddressId() {
        return billingAddressId;
    }

    public void setBillingAddressId(Long billingAddressId) {
        this.billingAddressId = billingAddressId;
    }

    public String getIssuerCode() {
        return issuerCode;
    }

    public void setIssuerCode(String issuerCode) {
        this.issuerCode = issuerCode;
    }
}
