package com.hk.web.action.core.payment;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumGateway;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Gateway;
import com.hk.domain.payment.GatewayIssuerMapping;
import com.hk.domain.payment.Issuer;
import com.hk.domain.payment.Payment;
import com.hk.domain.user.BillingAddress;
import com.hk.manager.OrderManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.service.payment.GatewayIssuerMappingService;
import com.hk.web.action.core.auth.LoginAction;
import com.hk.web.action.core.cart.CartAction;
import com.hk.web.action.core.order.OrderSummaryAction;
import com.hk.web.factory.PaymentModeActionFactory;
import net.sourceforge.stripes.action.ForwardResolution;
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

import java.util.*;

/**
 * Author: Pratham
 */
@Component
@Secure(hasAnyRoles = {RoleConstants.HK_UNVERIFIED, RoleConstants.HK_USER}, authUrl = "/core/auth/Login.action?source=" + LoginAction.SOURCE_CHECKOUT, disallowRememberMe = true)
@HttpCache(allow = false)
public class PaymentAction extends BaseAction {

    private PaymentMode paymentMode;
    private Gateway gateway;
    Long billingAddressId;
    private static Logger logger = LoggerFactory.getLogger(PaymentAction.class);
//    private Set<CartLineItem> trimCartLineItems = new HashSet<CartLineItem>();
//    private Integer               sizeOfCLI;

    @Validate(required = true)
    Issuer issuer;

    @Validate(required = true, encrypted = true)
    private Order order;

    @Autowired
    PaymentManager paymentManager;

    @Autowired
    OrderManager orderManager;

    @Autowired
    AddressDao addressDao;

    @Autowired
    GatewayIssuerMappingService gatewayIssuerMappingService;
    /*
   algorithm to route multiple gateways, first let the customer choose the issuer now based on the issuer, get all the damn gateways that serve it, alongwith the priority assigned by admin
   then you iterate over your faddu logic, to decide which gateway won then call the action corresponding to that gateway along with the issuer if needed
    */
    @SuppressWarnings("unchecked")
    public Resolution proceed() {
        if (order.getOrderStatus().getId().equals(EnumOrderStatus.InCart.getId())) {
            // recalculate the pricing before creating a payment.
            order = orderManager.recalAndUpdateAmount(order);
//            trimCartLineItems = orderManager.trimEmptyLineItems(order);
//            sizeOfCLI = trimCartLineItems.size();
//            if(trimCartLineItems!=null && trimCartLineItems.size()>0){
//                if(order.getCartLineItems()==null || order.getCartLineItems().size()==0){
//                    return new RedirectResolution(CartAction.class);
//                }
//                return new ForwardResolution(OrderSummaryAction.class).addParameter("trim",true).addParameter("sizeOfCLI",sizeOfCLI);
//            }

            //  todo ERP  Ankit --Call to make entries in SkuItemCLI --for temp booked method

            BillingAddress billingAddress = null;
            if(billingAddressId != null){
                billingAddress = addressDao.getBillingAddressById(billingAddressId);
            }

            String issuerCode = null;
            if (issuer != null) {
                try {
                    List<GatewayIssuerMapping> gatewayIssuerMappings = gatewayIssuerMappingService.searchGatewayIssuerMapping(issuer, null, true);
                    Long total = 0L;

                    for (GatewayIssuerMapping gatewayIssuerMapping : gatewayIssuerMappings) {
                        total += gatewayIssuerMapping.getPriority();
                    }

                    Integer random = (new Random()).nextInt(total.intValue());
                    long oldValue = 0L;
                    long priority = 0L;
                    long gatewayRangeValue = 0L;

                    for (GatewayIssuerMapping gatewayIssuerMapping : gatewayIssuerMappings) {
                        priority = gatewayIssuerMapping.getPriority();
                        gatewayRangeValue = oldValue + priority;
                        if (random < gatewayRangeValue) {
                            gateway = gatewayIssuerMapping.getGateway();
                            issuerCode = gatewayIssuerMapping.getIssuerCode();
                            break;
                        }
                        oldValue += priority;
                    }
                } catch (Exception e) {
                    //todo pratham, remove this piece of code
                    //this is a very crude away, although this code should not fail, but as a worse case scenario, redirecting customer to icici no matter what since it gives max option
                    logger.error("Routing Multiple gateways failed due to some exception" + e);
                    gateway = EnumGateway.ICICI.asGateway();
                }
            }

            RedirectResolution redirectResolution;
            paymentMode = EnumPaymentMode.ONLINE_PAYMENT.asPaymenMode();

            // first create a payment row, this will also contain the payment checksum
            Payment payment = paymentManager.createNewPayment(order, paymentMode, BaseUtils.getRemoteIpAddrForUser(getContext()), gateway, issuer, billingAddress);

            if (gateway != null) {
                Class actionClass = PaymentModeActionFactory.getActionClassForPayment(gateway, issuer.getIssuerType());
                redirectResolution = new RedirectResolution(actionClass, "proceed");
                return redirectResolution.addParameter(BasePaymentGatewayWrapper.TRANSACTION_DATA_PARAM, BasePaymentGatewayWrapper.encodeTransactionDataParam(order.getAmount(),
                        payment.getGatewayOrderId(), order.getId(), payment.getPaymentChecksum(), issuerCode, billingAddressId));
            }
        }
        addRedirectAlertMessage(new SimpleMessage("Payment for the order is already made."));
        return new RedirectResolution(PaymentModeAction.class).addParameter("order", order);
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
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

    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }

    public Long getBillingAddressId() {
        return billingAddressId;
    }

    public void setBillingAddressId(Long billingAddressId) {
        this.billingAddressId = billingAddressId;
    }
}