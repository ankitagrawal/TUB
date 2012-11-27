package com.hk.web.action.core.payment;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.comparator.MapValueComparator;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumIssuerType;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Gateway;
import com.hk.domain.payment.GatewayIssuerMapping;
import com.hk.domain.payment.Issuer;
import com.hk.domain.payment.Payment;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.payment.PaymentModeDao;
import com.hk.pact.service.payment.GatewayIssuerMappingService;
import com.hk.web.action.core.auth.LoginAction;
import com.hk.web.factory.PaymentModeActionFactory;
import com.hk.web.filter.WebContext;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.util.CryptoUtil;
import net.sourceforge.stripes.validation.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 8/27/12
 * Time: 6:05 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
@Secure(hasAnyRoles = {RoleConstants.HK_UNVERIFIED, RoleConstants.HK_USER}, authUrl = "/core/auth/Login.action?source=" + LoginAction.SOURCE_CHECKOUT, disallowRememberMe = true)
public class RegisterOnlinePaymentAction extends BaseAction {

    private PaymentMode paymentMode;

    private boolean isCodConversion;

    @Validate(required = true, encrypted = true)
    private Order order;

    Issuer issuer;

    @Autowired
    PaymentManager paymentManager;
    @Autowired
    PaymentModeDao paymentModeDao;

    List<Issuer> bankIssuers;
    List<Issuer> cardIssuers;

    @Autowired
    GatewayIssuerMappingService gatewayIssuerMappingService;

    @DefaultHandler
    public Resolution pre() {
        //currently i can safely assume, that most people whom we give conversion benefit will have 0 cod charges only, no order amount is pretty much their online payment amount
        //verify if pricing engine will return the right amount or not, i would prefer using the previous payment amount as the base parameter
        bankIssuers = gatewayIssuerMappingService.getIssuerByType(EnumIssuerType.Bank.getId(), true);
        cardIssuers = gatewayIssuerMappingService.getIssuerByType(EnumIssuerType.Card.getId(), true);
        if (order != null && order.isCOD() && !order.getOrderStatus().getId().equals(EnumOrderStatus.InCart.getId())) {
            HttpServletResponse httpResponse = WebContext.getResponse();
            Cookie wantedCODCookie = new Cookie(HealthkartConstants.Cookie.codConverterID, CryptoUtil.encrypt(order.getId().toString()));
            wantedCODCookie.setPath("/");
            wantedCODCookie.setMaxAge(600);
            httpResponse.addCookie(wantedCODCookie);
        }
        return new ForwardResolution("/pages/prePayment.jsp");
    }

    @SuppressWarnings("unchecked")
    public Resolution prepay() {
        if (!order.getOrderStatus().getId().equals(EnumOrderStatus.InCart.getId())) {

            GatewayIssuerMapping preferredGatewayIssuerMapping = null;
            Gateway preferredGateway = null;

            if (issuer != null) {
                List<GatewayIssuerMapping> gatewayIssuerMappings = gatewayIssuerMappingService.searchGatewayIssuerMapping(issuer, null, true);
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
                        preferredGateway = preferredGatewayIssuerMapping.getGateway();
                        break;
                    }
                    oldValue = gatewayLongEntry.getValue();
                }
            }

            RedirectResolution redirectResolution;
            EnumPaymentMode enumPaymentMode = EnumPaymentMode.getPaymentModeFromId(paymentMode != null ? paymentMode.getId() : null);

            // first create a payment row, this will also contain the payment checksum
            Payment payment = paymentManager.createNewPayment(order, paymentMode, BaseUtils.getRemoteIpAddrForUser(getContext()), preferredGateway, issuer);

            if (preferredGatewayIssuerMapping != null) {
                Class actionClass = PaymentModeActionFactory.getActionClassForPayment(enumPaymentMode, preferredGateway, issuer.getIssuerType());
                redirectResolution = new RedirectResolution(actionClass, "proceed");
                return redirectResolution.addParameter(BasePaymentGatewayWrapper.TRANSACTION_DATA_PARAM, BasePaymentGatewayWrapper.encodeTransactionDataParam(order.getAmount(),
                        payment.getGatewayOrderId(), order.getId(), payment.getPaymentChecksum(), preferredGatewayIssuerMapping.getIssuerCode()));
            }
        }
        addRedirectAlertMessage(new SimpleMessage("Some Error Occurred, Unable to process your request"));
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

    public List<Issuer> getBankIssuers() {
        return bankIssuers;
    }

    public void setBankIssuers(List<Issuer> bankIssuers) {
        this.bankIssuers = bankIssuers;
    }

    public List<Issuer> getCardIssuers() {
        return cardIssuers;
    }

    public void setCardIssuers(List<Issuer> cardIssuers) {
        this.cardIssuers = cardIssuers;
    }

    public Issuer getIssuer() {
        return issuer;
    }

    public void setIssuer(Issuer issuer) {
        this.issuer = issuer;
    }

    public boolean isCodConversion() {
        return isCodConversion;
    }

    public void setCodConversion(boolean codConversion) {
        isCodConversion = codConversion;
    }
}

