package com.hk.web.action.core.payment;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.payment.Payment;
import com.hk.pact.dao.payment.PaymentDao;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: kani
 * Time: 14 Apr, 2010 6:01:55 PM
 */
@Component
public class FreeCheckoutSuccessAction extends BaseAction {

	@Validate(required = true)
	private String gatewayOrderId;

	private Payment payment;

	@Autowired
	PaymentDao paymentDao;

	public Resolution pre() {
		payment = paymentDao.findByGatewayOrderId(gatewayOrderId);
        if(isHybridRelease())
		    return new ForwardResolution("/pages/payment/freeCheckoutSuccessBeta.jsp");
        else
            return new ForwardResolution("/pages/payment/freeCheckoutSuccess.jsp");
    }

	public String getGatewayOrderId() {
		return gatewayOrderId;
	}

	public void setGatewayOrderId(String gatewayOrderId) {
		this.gatewayOrderId = gatewayOrderId;
	}

	public Payment getPayment() {
		return payment;
	}
}
