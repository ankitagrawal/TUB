package com.hk.web.action.core.payment;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.payment.EnumIssuerType;
import com.hk.domain.payment.GatewayIssuerMapping;
import com.hk.domain.payment.Issuer;
import com.hk.pact.dao.payment.GatewayIssuerMappingDao;
import com.hk.pact.service.payment.GatewayIssuerMappingService;
import com.hk.web.action.core.auth.LoginAction;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import java.util.List;

@Secure(hasAnyRoles = {RoleConstants.HK_UNVERIFIED, RoleConstants.HK_USER}, authUrl = "/core/auth/Login.action?source=" + LoginAction.SOURCE_CHECKOUT, disallowRememberMe = true)
public class PaymentModeAction extends BaseAction {

    List<Issuer> bankIssuers;
    List<Issuer> cardIssuers;

//    @Autowired
//    GatewayIssuerMappingService gatewayIssuerMappingService;

    @Autowired
    GatewayIssuerMappingDao gatewayIssuerMappingDao;

    public Resolution pre() {
        bankIssuers = gatewayIssuerMappingDao.getIssuerByType(EnumIssuerType.Bank.getId(),true);
        cardIssuers = gatewayIssuerMappingDao.getIssuerByType(EnumIssuerType.Card.getId(),true);
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
