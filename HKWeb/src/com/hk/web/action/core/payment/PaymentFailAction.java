package com.hk.web.action.core.payment;

import com.akube.framework.stripes.action.BaseAction;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.Ssl;
import org.springframework.stereotype.Component;

@Component
@Ssl
public class PaymentFailAction extends BaseAction {

	public Resolution pre() {
		return new ForwardResolution("/pages/payment/paymentFail.jsp");
	}

}
