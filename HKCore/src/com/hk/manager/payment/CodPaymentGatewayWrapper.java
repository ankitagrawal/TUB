package com.hk.manager.payment;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.service.PaymentGatewayWrapper;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.LinkManager;
import com.hk.service.ServiceLocatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.zip.Adler32;

/**
 * User: kani Time: 11 Feb, 2010 6:20:49 PM
 */
@Component
public class CodPaymentGatewayWrapper extends BasePaymentGatewayWrapper<CodPaymentGatewayWrapper> implements PaymentGatewayWrapper {

	@Autowired
	private LinkManager linkManager;

	@SuppressWarnings("unused")
    private String url;

	private static final String merchantId = "lasjf234092sZFsafdlk";
	private static final String workingKey = "lasjkd9234092";

	public String getGatewayUrl() {
		linkManager = (LinkManager) ServiceLocatorFactory.getService("LinkManager");
		return linkManager.getCodGatewayUrl();
	}

	public void setGatewayUrl(String url) {
		this.url = url;
	}

	public static String param_amount = "amount";
	public static String param_orderId = "orderId";
	public static String param_redirectUrl = "redirectUrl";
	public static String param_codContactName = "codContactName";
	public static String param_codContactPhone = "codContactPhone";
	public static String param_checksum = "checksum";
	public static String param_codCharges = "codCharges";
	public static String param_codTax = "codTax";
	public static String param_merchantChecksum = "merchantChecksum";
	public static String param_auth = "auth";

	public static final String auth_Success = "Y";
	public static final String auth_Fail = "N";

	/*
		 * Gateway specific utility methods. usually for security/encryption/decryption etc
		 */

	public static void verifyChecksum(String OrderId, String Amount, String codCharges, String codTax, String CheckSum) throws HealthkartPaymentGatewayException {
		String newChecksum = getResponseChecksum(OrderId, Amount, codCharges, codTax);
		if (!newChecksum.equals(CheckSum)) {
			throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.CHECKSUM_MISMATCH);
		}
	}

	public static String getResponseChecksum(String OrderId, String Amount, String codCharges, String codTax) {
		String str = merchantId + "|" + OrderId + "|" + Amount + "|" + codCharges + "|" + codTax + "|" + workingKey;
		Adler32 adl = new Adler32();
		adl.update(str.getBytes());
		return String.valueOf(adl.getValue());
	}

	public static String getRequestChecksum(String OrderId, String Amount, String redirectUrl) {
		String str = merchantId + "|" + OrderId + "|" + Amount + "|" + redirectUrl + "|" + workingKey;
		Adler32 adl = new Adler32();
		adl.update(str.getBytes());
		return String.valueOf(adl.getValue());
	}

	public LinkManager getLinkManager() {
		return linkManager;
	}

	public void setLinkManager(LinkManager linkManager) {
		this.linkManager = linkManager;
	}

}
