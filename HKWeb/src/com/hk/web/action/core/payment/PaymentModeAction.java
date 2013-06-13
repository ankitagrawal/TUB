package com.hk.web.action.core.payment;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.order.AdminOrderService;
import com.hk.constants.core.EnumRole;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.payment.EnumIssuerType;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Issuer;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.dto.pricing.PricingDto;
import com.hk.manager.OrderManager;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.payment.GatewayIssuerMappingService;
import com.hk.pricing.PricingEngine;
import com.hk.web.action.core.auth.LoginAction;
import com.hk.web.action.core.cart.CartAction;
import com.hk.web.action.core.user.SelectAddressAction;
import net.sourceforge.stripes.action.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Secure(hasAnyRoles = {RoleConstants.HK_UNVERIFIED, RoleConstants.HK_USER}, authUrl = "/core/auth/Login.action?source=" + LoginAction.SOURCE_CHECKOUT, disallowRememberMe = true)
@HttpCache(allow = false)
public class PaymentModeAction extends BaseAction {

    List<Issuer> bankIssuers;
    List<Issuer> cardIssuers;
    Map<String, String> codFailureMap = new HashMap<String, String>();
    private PricingDto pricingDto;
    private boolean showFailureMessage = false;
    private String paymentFailureGatewayOrderId;

    @Autowired
    GatewayIssuerMappingService gatewayIssuerMappingService;

    @Autowired
    OrderManager orderManager;
    @Autowired
    private AdminOrderService adminOrderService;
    @Autowired
    PricingEngine pricingEngine;
    @Autowired
	private OrderService orderService;

    Order order;

    public Resolution pre() {
        User user = getUserService().getUserById(getPrincipal().getId());
        order = orderManager.getOrCreateOrder(user);
        Set<Role> userRoles = user.getRoles();
        if(userRoles!=null && userRoles.size()>0 && userRoles.contains(EnumRole.B2B_USER.toRole())){
        	order.setB2bOrder(Boolean.TRUE);
        	orderService.save(order);
        }
        if (order.getCartLineItems() == null || order.getCartLineItems().isEmpty()) {
            addRedirectAlertMessage(new SimpleMessage("There are no items in your cart, Please select at least 1 item"));
            return new RedirectResolution(CartAction.class);
        }
        if (order.getAddress() == null) {
            addRedirectAlertMessage(new SimpleMessage("You have not selected the shipping address"));
            return new RedirectResolution(SelectAddressAction.class);
        }
        pricingDto = new PricingDto(pricingEngine.calculatePricing(order.getCartLineItems(), order.getOfferInstance(), order.getAddress(), order.getRewardPointsUsed()), order.getAddress());
        codFailureMap = adminOrderService.isCODAllowed(order, pricingDto.getGrandTotalPayable());
        bankIssuers = gatewayIssuerMappingService.getIssuerByType(EnumIssuerType.Bank.getId(), true);
        cardIssuers = gatewayIssuerMappingService.getIssuerByType(EnumIssuerType.Card.getId(), true);
        return new ForwardResolution("/pages/paymentMode.jsp");
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


    public Map<String, String> getCodFailureMap() {
        return codFailureMap;
    }

    public void setCodFailureMap(Map<String, String> codFailureMap) {
        this.codFailureMap = codFailureMap;
    }

    public boolean isShowFailureMessage() {
        return showFailureMessage;
    }

    public void setShowFailureMessage(boolean showFailureMessage) {
        this.showFailureMessage = showFailureMessage;
    }

    public String getPaymentFailureGatewayOrderId() {
        return paymentFailureGatewayOrderId;
    }

    public void setPaymentFailureGatewayOrderId(String paymentFailureGatewayOrderId) {
        this.paymentFailureGatewayOrderId = paymentFailureGatewayOrderId;
    }
}
