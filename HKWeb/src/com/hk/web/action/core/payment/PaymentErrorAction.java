package com.hk.web.action.core.payment;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.payment.Payment;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.pact.dao.payment.PaymentDao;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Author: Kani
 * Date: Jan 6, 2009
 */
@Component
public class PaymentErrorAction extends BaseAction {
	@Validate(required = true, encrypted = true)
	private String gatewayOrderId;

	private Payment payment;

	private int errorCode;
	private String errorMessage;

	@Autowired
	PaymentDao paymentDao;

	public Resolution pre() {
		payment = paymentDao.findByGatewayOrderId(gatewayOrderId);
		errorMessage = HealthkartPaymentGatewayException.Error.getErrorFromCode(errorCode).getMessage();
        return new ForwardResolution(PaymentModeAction.class).addParameter("showFailureMessage", true);
//		return new ForwardResolution("/pages/payment/paymentError.jsp");
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

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
