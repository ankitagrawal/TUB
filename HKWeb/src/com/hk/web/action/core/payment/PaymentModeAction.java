package com.hk.web.action.core.payment;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.payment.PreferredBankGateway;
import com.hk.domain.user.BillingAddress;
import com.hk.domain.user.User;
import com.hk.web.action.core.auth.LoginAction;
import com.hk.pact.dao.core.AddressDao;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.stripesstuff.plugin.security.Secure;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Secure(hasAnyRoles = { RoleConstants.HK_UNVERIFIED, RoleConstants.HK_USER }, authUrl = "/core/auth/Login.action?source=" + LoginAction.SOURCE_CHECKOUT, disallowRememberMe = true)
public class PaymentModeAction extends BaseAction {

//    private BillingAddress billingAddress;
    @Autowired
    AddressDao addressDao;

	List<PreferredBankGateway> bankList;

	public Resolution pre() {
		bankList = getBaseDao().getAll(PreferredBankGateway.class);
//        User user = getUserService().getUserById(getPrincipal().getId());
//        billingAddress =  addressDao.searchBillingAddress(user);
		return new ForwardResolution("/pages/paymentMode.jsp");
	}

	public List<PreferredBankGateway> getBankList() {
		return bankList;
	}

	public void setBankList(List<PreferredBankGateway> bankList) {
		this.bankList = bankList;
	}

//     public BillingAddress getBillingAddress() {
//        return billingAddress;
//    }
//
//    public void setBillingAddress(BillingAddress billingAddress) {
//        this.billingAddress = billingAddress;
//    }
    
}
