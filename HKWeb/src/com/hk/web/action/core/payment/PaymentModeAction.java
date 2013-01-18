package com.hk.web.action.core.payment;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.payment.EnumIssuerType;
import com.hk.domain.order.Order;
import com.hk.domain.payment.GatewayIssuerMapping;
import com.hk.domain.payment.Issuer;
import com.hk.domain.user.User;
import com.hk.manager.OrderManager;
import com.hk.pact.dao.payment.GatewayIssuerMappingDao;
import com.hk.pact.service.payment.GatewayIssuerMappingService;
import com.hk.web.action.core.auth.LoginAction;
import com.hk.web.action.core.cart.CartAction;
import com.hk.web.action.core.user.SelectAddressAction;
import net.sourceforge.stripes.action.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import java.util.List;

@Secure(hasAnyRoles = {RoleConstants.HK_UNVERIFIED, RoleConstants.HK_USER}, authUrl = "/core/auth/Login.action?source=" + LoginAction.SOURCE_CHECKOUT, disallowRememberMe = true)
@HttpCache(allow = false)
public class PaymentModeAction extends BaseAction {

    List<Issuer> bankIssuers;
    List<Issuer> cardIssuers;

    @Autowired
    GatewayIssuerMappingService gatewayIssuerMappingService;

    @Autowired
    OrderManager orderManager;
    
    Order order;

    public Resolution pre() {
        User user = getUserService().getUserById(getPrincipal().getId());
        order = orderManager.getOrCreateOrder(user);
        if (order.getCartLineItems() == null || order.getCartLineItems().isEmpty()) {
            addRedirectAlertMessage(new SimpleMessage("There are no items in your cart, Please select at least 1 item"));
            return new RedirectResolution(CartAction.class);
        }
        if (order.getAddress() == null) {
            addRedirectAlertMessage(new SimpleMessage("You have not selected the shipping address"));
            return new RedirectResolution(SelectAddressAction.class);
        }
        bankIssuers = gatewayIssuerMappingService.getIssuerByType(EnumIssuerType.Bank.getId(),true);
        cardIssuers = gatewayIssuerMappingService.getIssuerByType(EnumIssuerType.Card.getId(),true);
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
}
