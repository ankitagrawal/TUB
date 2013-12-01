package com.hk.web.action.core.payment;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.stripes.action.BasePaymentGatewaySendReceiveAction;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.payment.CodPaymentGatewayWrapper;
import com.hk.manager.payment.PaymentManager;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: kani Time: 11 Feb, 2010 6:19:53 PM
 */
@Component
public class CodGatewaySendReceiveAction extends BasePaymentGatewaySendReceiveAction<CodPaymentGatewayWrapper> {

	@Autowired
	PaymentManager paymentManager;

	protected CodPaymentGatewayWrapper getPaymentGatewayWrapperFromTransactionData(BasePaymentGatewayWrapper.TransactionData data) {
		CodPaymentGatewayWrapper codPaymentGatewayWrapper = new CodPaymentGatewayWrapper();
		String amountStr = BasePaymentGatewayWrapper.TransactionData.decimalFormat.format(data.getAmount());

		codPaymentGatewayWrapper.addParameter(CodPaymentGatewayWrapper.param_amount, amountStr);
		codPaymentGatewayWrapper.addParameter(CodPaymentGatewayWrapper.param_orderId, data.getGatewayOrderId());
		codPaymentGatewayWrapper.addParameter(CodPaymentGatewayWrapper.param_redirectUrl, getRedirectUrl());
		String gatewayChecksum = CodPaymentGatewayWrapper.getRequestChecksum(data.getGatewayOrderId(), amountStr, getRedirectUrl());
		codPaymentGatewayWrapper.addParameter(CodPaymentGatewayWrapper.param_merchantChecksum, data.getChecksum());
		codPaymentGatewayWrapper.addParameter(CodPaymentGatewayWrapper.param_checksum, gatewayChecksum);

		return codPaymentGatewayWrapper;
	}

	@DefaultHandler
	public Resolution callback() {

		String gatewayOrderId = getContext().getRequest().getParameter(CodPaymentGatewayWrapper.param_orderId);
		String amountStr = getContext().getRequest().getParameter(CodPaymentGatewayWrapper.param_amount);
		Double amount = NumberUtils.toDouble(amountStr);
		String authDesc = getContext().getRequest().getParameter(CodPaymentGatewayWrapper.param_auth);
		String checksum = getContext().getRequest().getParameter(CodPaymentGatewayWrapper.param_checksum);
		String merchantChecksum = getContext().getRequest().getParameter(CodPaymentGatewayWrapper.param_merchantChecksum);
		String codContactName = getContext().getRequest().getParameter(CodPaymentGatewayWrapper.param_codContactName);
		String codContactPhone = getContext().getRequest().getParameter(CodPaymentGatewayWrapper.param_codContactPhone);
		String codCharges = getContext().getRequest().getParameter(CodPaymentGatewayWrapper.param_codCharges);
		String codTax = getContext().getRequest().getParameter(CodPaymentGatewayWrapper.param_codTax);

		Resolution resolution = null;
		try {
			CodPaymentGatewayWrapper.verifyChecksum(gatewayOrderId, amountStr, codCharges, codTax, checksum);

			paymentManager.verifyPayment(gatewayOrderId, amount, merchantChecksum);

			if (StringUtils.isBlank(codContactName) || StringUtils.isBlank(codContactPhone)) {
				throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.COD_CONTACT_INFO_INVALID);
			}
			if (CodPaymentGatewayWrapper.auth_Success.equals(authDesc)) {
				// paymentManager.successCod(gatewayOrderId, null, NumberUtils.toDouble(codCharges),
				// NumberUtils.toDouble(codTax));
				resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", gatewayOrderId);
			} else if (CodPaymentGatewayWrapper.auth_Fail.equals(authDesc)) {
				paymentManager.fail(gatewayOrderId);
				resolution = new RedirectResolution(PaymentFailAction.class).addParameter("gatewayOrderId", gatewayOrderId);
			} else {
				throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.INVALID_RESPONSE);
			}
		} catch (HealthkartPaymentGatewayException e) {
			paymentManager.error(gatewayOrderId, e);
			resolution = e.getRedirectResolution().addParameter("gatewayOrderId", gatewayOrderId);
		}

		return resolution;
	}

}
