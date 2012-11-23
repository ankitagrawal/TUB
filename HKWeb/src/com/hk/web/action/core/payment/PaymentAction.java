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
import com.hk.domain.payment.*;
import com.hk.domain.user.User;
import com.hk.manager.OrderManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.RoleDao;
import com.hk.pact.dao.offer.OfferInstanceDao;
import com.hk.pact.dao.payment.GatewayIssuerMappingDao;
import com.hk.pact.dao.payment.PaymentModeDao;
import com.hk.pact.dao.user.UserDao;
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
 * Author: Kani Date: Dec 29, 2008
 */
@Component
@Secure(hasAnyRoles = { RoleConstants.HK_UNVERIFIED, RoleConstants.HK_USER }, authUrl = "/core/auth/Login.action?source=" + LoginAction.SOURCE_CHECKOUT, disallowRememberMe = true)
public class PaymentAction extends BaseAction {

	@Validate(required = true)
	private PaymentMode paymentMode;

	Long bankId;
    PreferredBankGateway bank;

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

            if(issuer != null){

                List<GatewayIssuerMapping> gatewayIssuerMappings = new ArrayList<GatewayIssuerMapping>();
                
                gatewayIssuerMappings = gatewayIssuerMappingDao.searchGatewayByIssuer(issuer,true,true);

                Long total = 0L;

                Map<Gateway, Long> gatewayPriorityMap = new HashMap<Gateway, Long>();
                
                for (GatewayIssuerMapping gatewayIssuerMapping : gatewayIssuerMappings) {
                    
                    gatewayPriorityMap.put(gatewayIssuerMapping.getGateway(),gatewayIssuerMapping.getPriority());
                    
                    total += gatewayIssuerMapping.getPriority();
                    
                }

//             Map<Gateway, Double> gatewayHitRatioMap = gatewayIssuerMappingService.getGatewayHitRatio(issuer,true,true);

                //hacky solution, fetching base total from null

//                Double baseTotal = gatewayHitRatioMap.get(null);
                
                int trueCounter = 0;
                
                for(int i =0;i<= total;i++){

                if((new Random()).nextBoolean()){
                  
                    trueCounter++;
                    
                }



                    MapValueComparator mapValueComparator = new MapValueComparator(gatewayPriorityMap);
                    TreeMap<Gateway, Long> sortedGatewayPriorityMap = new TreeMap(mapValueComparator);
                    sortedGatewayPriorityMap.putAll(gatewayPriorityMap);

                    for (Map.Entry<Gateway, Long> gatewayLongEntry : sortedGatewayPriorityMap.entrySet()) {
                        Long oldValue = gatewayLongEntry.getValue();



                    }
                }


























             Integer random = (new Random()).nextInt(100);



            }

			RedirectResolution redirectResolution;

            // first create a payment row, this will also contain the payment checksum
            Payment payment = paymentManager.createNewPayment(order, paymentMode, BaseUtils.getRemoteIpAddrForUser(getContext()), issuer.getName());

			if (gateway != null) {
				Class actionClass = PaymentModeActionFactory.getActionClassForPaymentMode(EnumPaymentMode.ICICI);
				redirectResolution = new RedirectResolution(actionClass, "proceed");
				return redirectResolution.addParameter(BasePaymentGatewayWrapper.TRANSACTION_DATA_PARAM, BasePaymentGatewayWrapper.encodeTransactionDataParam(order.getAmount(),
						payment.getGatewayOrderId(), order.getId(), payment.getPaymentChecksum(), bankCode));
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

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}
}
