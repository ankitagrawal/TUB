package com.hk.web.action.core.payment;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.comparator.MapValueComparator;
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
import com.hk.pact.dao.payment.GatewayIssuerMappingDao;
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

import java.util.*;

/**
 * Author: Pratham
 */
@Component
@Secure(hasAnyRoles = {RoleConstants.HK_UNVERIFIED, RoleConstants.HK_USER}, authUrl = "/core/auth/Login.action?source=" + LoginAction.SOURCE_CHECKOUT, disallowRememberMe = true)
public class PaymentAction extends BaseAction {

    @Validate(required = true)
    private PaymentMode paymentMode;

    Issuer issuer;

    @Validate(required = true, encrypted = true)
    private Order order;

    @Autowired
    PaymentManager paymentManager;

    @Autowired
    OrderManager orderManager;

    @Autowired
    GatewayIssuerMappingService gatewayIssuerMappingService;
    @Autowired
    GatewayIssuerMappingDao gatewayIssuerMappingDao;

    @SuppressWarnings("unchecked")
    public Resolution proceed() {
        if (order.getOrderStatus().getId().equals(EnumOrderStatus.InCart.getId())) {
            // recalculate the pricing before creating a payment.
            order = orderManager.recalAndUpdateAmount(order);

            if (order.getAmount() == 0) {
                addRedirectAlertMessage(new LocalizableMessage("/CheckoutAction.action.checkout.not.allowed.on.empty.cart"));
                return new RedirectResolution(CartAction.class);
            }

            GatewayIssuerMapping preferredGatewayIssuerMapping = null;

            if (issuer != null) {
                List<GatewayIssuerMapping> gatewayIssuerMappings = gatewayIssuerMappingDao.searchGatewayIssuerMapping(issuer, null, true);
                Long total = 0L;

                Map<GatewayIssuerMapping, Long> gatewayIssuerMappingPriorityMap = new HashMap<GatewayIssuerMapping, Long>();
                for (GatewayIssuerMapping gatewayIssuerMapping : gatewayIssuerMappings) {
                    gatewayIssuerMappingPriorityMap.put(gatewayIssuerMapping, gatewayIssuerMapping.getPriority());
                    total += gatewayIssuerMapping.getPriority();
                }

                MapValueComparator mapValueComparator = new MapValueComparator(gatewayIssuerMappingPriorityMap);
                TreeMap<GatewayIssuerMapping, Long> sortedGatewayPriorityMap = new TreeMap(mapValueComparator);
                sortedGatewayPriorityMap.putAll(gatewayIssuerMappingPriorityMap);

                Integer random = (new Random()).nextInt(total.intValue());
                long oldValue = 0L;

                for (Map.Entry<GatewayIssuerMapping, Long> gatewayLongEntry : sortedGatewayPriorityMap.entrySet()) {
                    long gatewayRangeValue = oldValue + gatewayLongEntry.getValue();
                    if (random <= gatewayRangeValue) {
                        preferredGatewayIssuerMapping = gatewayLongEntry.getKey();
                        break;
                    }
                    oldValue = gatewayLongEntry.getValue();
                }
            }

            /*
            algorithm to route multiple gateways, first let the customer choose the issuer
            now based on the issuer, get all the damn gateways that serve it, alongwith the priority assigned by admin
            then you iterate over your faddu logic, to decide which gateway won
            then call the action corresponding to that gateway along with the issuer if needed
             */

            RedirectResolution redirectResolution;

            EnumPaymentMode dummyEnumPaymentMode = EnumPaymentMode.getPaymentModeFromId(preferredGatewayIssuerMapping != null ? preferredGatewayIssuerMapping.getGateway().getId() : null);
            paymentMode = dummyEnumPaymentMode != null ? dummyEnumPaymentMode.asPaymenMode() : paymentMode;
            EnumPaymentMode enumPaymentMode = EnumPaymentMode.getPaymentModeFromId(paymentMode != null ? paymentMode.getId() : null);
            // first create a payment row, this will also contain the payment checksum
            Payment payment = paymentManager.createNewPayment(order, paymentMode, BaseUtils.getRemoteIpAddrForUser(getContext()), issuer != null ? issuer.getName() : "");

            if (preferredGatewayIssuerMapping != null) {
                Class actionClass = PaymentModeActionFactory.getActionClassByGatewayIssuer(preferredGatewayIssuerMapping.getGateway(), preferredGatewayIssuerMapping.getIssuer().getIssuerType());
                redirectResolution = new RedirectResolution(actionClass, "proceed");
                return redirectResolution.addParameter(BasePaymentGatewayWrapper.TRANSACTION_DATA_PARAM, BasePaymentGatewayWrapper.encodeTransactionDataParam(order.getAmount(),
                        payment.getGatewayOrderId(), order.getId(), payment.getPaymentChecksum(), preferredGatewayIssuerMapping.getIssuerCode()));
            } else if (paymentMode != null) {
                Class actionClass = PaymentModeActionFactory.getActionClassForPaymentMode(enumPaymentMode);
                redirectResolution = new RedirectResolution(actionClass, "proceed");
                return redirectResolution.addParameter(BasePaymentGatewayWrapper.TRANSACTION_DATA_PARAM, BasePaymentGatewayWrapper.encodeTransactionDataParam(order.getAmount(),
                        payment.getGatewayOrderId(), order.getId(), payment.getPaymentChecksum(), null));
            } else {
                // ccavneue is the default gateway
                Class actionClass = PaymentModeActionFactory.getActionClassForPaymentMode(EnumPaymentMode.CCAVENUE_DUMMY);
                redirectResolution = new RedirectResolution(actionClass, "proceed");
            }
            return redirectResolution.addParameter(BasePaymentGatewayWrapper.TRANSACTION_DATA_PARAM, BasePaymentGatewayWrapper.encodeTransactionDataParam(order.getAmount(),
                    payment.getGatewayOrderId(), order.getId(), payment.getPaymentChecksum(), null));

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


}
