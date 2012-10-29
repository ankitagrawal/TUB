package com.hk.manager.payment;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.service.PaymentGatewayWrapper;
import com.hk.exception.HealthkartPaymentGatewayException;

import java.util.zip.Adler32;


/**
 * Author: Kani
 * Date: Jan 14, 2009
 */
public class CCAvenueDummyPaymentGatewayWrapper extends BasePaymentGatewayWrapper<CCAvenueDummyPaymentGatewayWrapper> implements PaymentGatewayWrapper {
	@SuppressWarnings("unused")
    private String url;

	public String getGatewayUrl() {
		return "/ccavenueDummyGateway.jsp";
	}

	public void setGatewayUrl(String url) {
		this.url = url;
	}

	public static final String workingKey = "u3lupy38i2p7nmou32nb2gv0aq9d41g0";
	public static final String merchantId = "M_reevsate_4337";

	public static final String param_MerchantId = "Merchant_Id";
	public static final String param_Amount = "Amount";
	public static final String param_OrderId = "Order_Id";
	public static final String param_RedirectUrl = "Redirect_Url";
	public static final String param_BillingCustName = "billing_cust_name";
	public static final String param_BillingCustAddress = "billing_cust_address";
	public static final String param_BillingCustCountry = "billing_cust_country";
	public static final String param_BillingCustTel = "billing_cust_tel";
	public static final String param_BillingCustEmail = "billing_cust_email";
	public static final String param_DeliveryCustName = "delivery_cust_name";
	public static final String param_DeliveryCustAddress = "delivery_cust_address";
	public static final String param_DeliveryCustTel = "delivery_cust_tel";
	public static final String param_DeliveryCustNotes = "delivery_cust_notes";
	public static final String param_MerchantParam = "Merchant_Param";
	public static final String param_Notes = "Notes";
	public static final String param_Checksum = "Checksum";

	public static final String param_AuthDesc = "AuthDesc";

	public static final String AuthDesc_Success = "Y";
	public static final String AuthDesc_PendingApproval = "B";
	public static final String AuthDesc_Fail = "N";

	/*
	   * Gateway specific utility methods. usually for security/encryption/decryption etc
	   */

	public static void verifyChecksum(String MerchantId, String OrderId, String Amount, String AuthDesc, String WorkingKey, String CheckSum) throws HealthkartPaymentGatewayException {
		String newChecksum = getResponseChecksum(MerchantId, OrderId, Amount, AuthDesc, WorkingKey);
		if (!newChecksum.equals(CheckSum)) {
			throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.CHECKSUM_MISMATCH);
		}
	}

	public static String getResponseChecksum(String MerchantId, String OrderId, String Amount, String AuthDesc, String WorkingKey) {
		String str = MerchantId + "|" + OrderId + "|" + Amount + "|" + AuthDesc + "|" + WorkingKey;
		Adler32 adl = new Adler32();
		adl.update(str.getBytes());
		return String.valueOf(adl.getValue());
	}

	public static String getRequestChecksum(String MerchantId, String OrderId, String Amount, String redirectUrl, String WorkingKey) {
		String str = MerchantId + "|" + OrderId + "|" + Amount + "|" + redirectUrl + "|" + WorkingKey;
		Adler32 adl = new Adler32();
		adl.update(str.getBytes());
		return String.valueOf(adl.getValue());
	}
}
