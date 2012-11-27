package com.hk.web.action.core.payment;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Gateway;
import com.hk.domain.payment.GatewayIssuerMapping;
import com.hk.domain.payment.Issuer;
import com.hk.domain.payment.Payment;
import com.hk.manager.OrderManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.service.payment.GatewayIssuerMappingService;
import com.hk.web.action.core.auth.LoginAction;
import com.hk.web.action.core.cart.CartAction;
import com.hk.web.factory.PaymentModeActionFactory;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.List;
import java.util.Random;

/**
 * Author: Pratham
 */
@Component
@Secure(hasAnyRoles = {RoleConstants.HK_UNVERIFIED, RoleConstants.HK_USER}, authUrl = "/core/auth/Login.action?source=" + LoginAction.SOURCE_CHECKOUT, disallowRememberMe = true)
public class PaymentAction extends BaseAction {

    private PaymentMode paymentMode;
    private Gateway gateway;

    @Validate(required = true)
    Issuer issuer;

    @Validate(required = true, encrypted = true)
    private Order order;

    @Autowired
    PaymentManager paymentManager;

    @Autowired
    OrderManager orderManager;

    @Autowired
    GatewayIssuerMappingService gatewayIssuerMappingService;

    /*
   algorithm to route multiple gateways, first let the customer choose the issuer
   now based on the issuer, get all the damn gateways that serve it, alongwith the priority assigned by admin
   then you iterate over your faddu logic, to decide which gateway won
   then call the action corresponding to that gateway along with the issuer if needed
    */
    @SuppressWarnings("unchecked")
    public Resolution proceed() {
        if (order.getOrderStatus().getId().equals(EnumOrderStatus.InCart.getId())) {
            // recalculate the pricing before creating a payment.
            order = orderManager.recalAndUpdateAmount(order);

            if (order.getAmount() == 0) {       //todo rethink this
                addRedirectAlertMessage(new LocalizableMessage("/CheckoutAction.action.checkout.not.allowed.on.empty.cart"));
                return new RedirectResolution(CartAction.class);
            }

            GatewayIssuerMapping preferredGatewayIssuerMapping = null;
            String issuerCode = null;

            if (issuer != null) {
                List<GatewayIssuerMapping> gatewayIssuerMappings = gatewayIssuerMappingService.searchGatewayIssuerMapping(issuer, null, true);
                Long total = 0L;

//                Map<GatewayIssuerMapping, Long> gatewayIssuerMappingPriorityMap = new HashMap<GatewayIssuerMapping, Long>();
                for (GatewayIssuerMapping gatewayIssuerMapping : gatewayIssuerMappings) {
//                    gatewayIssuerMappingPriorityMap.put(gatewayIssuerMapping, gatewayIssuerMapping.getPriority());
                    total += gatewayIssuerMapping.getPriority();
                }

//                MapValueComparator mapValueComparator = new MapValueComparator(gatewayIssuerMappingPriorityMap);
//                TreeMap<GatewayIssuerMapping, Long> sortedGatewayPriorityMap = new TreeMap(mapValueComparator);
//                sortedGatewayPriorityMap.putAll(gatewayIssuerMappingPriorityMap);
//                for (Map.Entry<GatewayIssuerMapping, Long> gatewayLongEntry : sortedGatewayPriorityMap.entrySet()) {
//                    long gatewayRangeValue = oldValue + gatewayLongEntry.getValue();
//                    if (random <= gatewayRangeValue) {
//                        preferredGatewayIssuerMapping = gatewayLongEntry.getKey();
//                        preferredGateway = preferredGatewayIssuerMapping.getGateway();
//                        break;
//                    }
//                    oldValue = gatewayLongEntry.getValue();
//                }

                Integer random = (new Random()).nextInt(total.intValue());
                long oldValue = 0L;

                for (GatewayIssuerMapping gatewayIssuerMapping : gatewayIssuerMappings) {
                    long gatewayRangeValue = oldValue + gatewayIssuerMapping.getPriority();
                    if (random < gatewayRangeValue) {
                        gateway = gatewayIssuerMapping.getGateway();
                        issuerCode = gatewayIssuerMapping.getIssuerCode();
                        break;
                    }
                    oldValue = gatewayIssuerMapping.getPriority();
                }

            }

            RedirectResolution redirectResolution;
            paymentMode = EnumPaymentMode.ONLINE_PAYMENT.asPaymenMode();

            // first create a payment row, this will also contain the payment checksum
            Payment payment = paymentManager.createNewPayment(order, paymentMode, BaseUtils.getRemoteIpAddrForUser(getContext()), gateway, issuer);

            if (gateway != null) {
                Class actionClass = PaymentModeActionFactory.getActionClassForPayment(gateway, issuer.getIssuerType());
                redirectResolution = new RedirectResolution(actionClass, "proceed");
                return redirectResolution.addParameter(BasePaymentGatewayWrapper.TRANSACTION_DATA_PARAM, BasePaymentGatewayWrapper.encodeTransactionDataParam(order.getAmount(),
                        payment.getGatewayOrderId(), order.getId(), payment.getPaymentChecksum(), issuerCode));
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
}
