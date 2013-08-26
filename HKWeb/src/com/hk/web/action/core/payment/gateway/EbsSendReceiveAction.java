package com.hk.web.action.core.payment.gateway;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.stripes.action.BasePaymentGatewaySendReceiveAction;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.domain.payment.Payment;
import com.hk.domain.user.Address;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.LinkManager;
import com.hk.manager.payment.EbsPaymentGatewayWrapper;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.service.payment.PaymentService;
import com.hk.util.RC4;
import com.hk.web.AppConstants;
import com.hk.web.action.core.payment.PaymentFailAction;
import com.hk.web.action.core.payment.PaymentPendingApprovalAction;
import com.hk.web.action.core.payment.PaymentSuccessAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 6/18/12
 * Time: 2:45 PM
 * To change this template use File | Settings | File Templates.
 */

@Component
public class EbsSendReceiveAction extends BasePaymentGatewaySendReceiveAction<EbsPaymentGatewayWrapper> {

	private static Logger logger = LoggerFactory.getLogger(EbsSendReceiveAction.class);


	@Autowired
	PaymentManager paymentManager;

	@Autowired
	PaymentService paymentService;

	@Autowired
	LinkManager linkManager;

	public static String country = "IND";
	public static String description = "test transaction";

	protected EbsPaymentGatewayWrapper getPaymentGatewayWrapperFromTransactionData(BasePaymentGatewayWrapper.TransactionData data) {
		String propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + "/ebs.live.properties";
		Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);
		String secret_key = properties.getProperty(EbsPaymentGatewayWrapper.secret_key);
		String account_id = properties.getProperty(EbsPaymentGatewayWrapper.account_id);
		String mode = properties.getProperty(EbsPaymentGatewayWrapper.mode);

		EbsPaymentGatewayWrapper ebsPaymentGatewayWrapper = new EbsPaymentGatewayWrapper();
		String amountStr = BasePaymentGatewayWrapper.TransactionData.decimalFormat.format(data.getAmount());


		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			logger.debug("NoSuchAlgorithmException occurred while generating MD5 hash" + e);
		}
		String return_url = linkManager.getEbsPaymentGatewayReturnUrl() + "?" + EbsPaymentGatewayWrapper.reqResParameter + "={DR}";
		String pass = secret_key + "|" + account_id + "|" + amountStr + "|" + data.getGatewayOrderId() + "|" + return_url + "|" + mode;
		byte[] dataBytes = pass.getBytes();
		assert m != null;
		m.update(dataBytes, 0, dataBytes.length);
		BigInteger i = new BigInteger(1, m.digest());

		String secure_hash = String.format("%1$032X", i);

		Payment payment = paymentService.findByGatewayOrderId(data.getGatewayOrderId());
		Address address = payment.getOrder().getAddress();

		ebsPaymentGatewayWrapper.addParameter(EbsPaymentGatewayWrapper.address, address.getLine1());
		ebsPaymentGatewayWrapper.addParameter(EbsPaymentGatewayWrapper.city, address.getCity());
		ebsPaymentGatewayWrapper.addParameter(EbsPaymentGatewayWrapper.state, address.getState());
		ebsPaymentGatewayWrapper.addParameter(EbsPaymentGatewayWrapper.phone, address.getPhone());
		ebsPaymentGatewayWrapper.addParameter(EbsPaymentGatewayWrapper.postal_code, address.getPincode().getPincode());
		ebsPaymentGatewayWrapper.addParameter(EbsPaymentGatewayWrapper.name, address.getName());
		ebsPaymentGatewayWrapper.addParameter(EbsPaymentGatewayWrapper.email, address.getUser().getEmail());
		ebsPaymentGatewayWrapper.addParameter(EbsPaymentGatewayWrapper.return_url, return_url);
		ebsPaymentGatewayWrapper.addParameter(EbsPaymentGatewayWrapper.account_id, account_id);
		ebsPaymentGatewayWrapper.addParameter(EbsPaymentGatewayWrapper.mode, mode);
		ebsPaymentGatewayWrapper.addParameter(EbsPaymentGatewayWrapper.reference_no, data.getGatewayOrderId());
		ebsPaymentGatewayWrapper.addParameter(EbsPaymentGatewayWrapper.description, description);
		ebsPaymentGatewayWrapper.addParameter(EbsPaymentGatewayWrapper.secure_hash_decrypted, pass);
		ebsPaymentGatewayWrapper.addParameter(EbsPaymentGatewayWrapper.secure_hash, secure_hash);
		ebsPaymentGatewayWrapper.addParameter(EbsPaymentGatewayWrapper.amount, amountStr);
		ebsPaymentGatewayWrapper.addParameter(EbsPaymentGatewayWrapper.country, country);
        String issuerCode = data.getPaymentMethod();
        if(issuerCode != null && StringUtils.isNotBlank(issuerCode) ){
            ebsPaymentGatewayWrapper.addParameter(EbsPaymentGatewayWrapper.payment_option, data.getPaymentMethod());
        }
		return ebsPaymentGatewayWrapper;
	}                                                                              

	@DefaultHandler
	public Resolution callback() {
		String propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + "/ebs.live.properties";
		Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);
		String key = properties.getProperty(EbsPaymentGatewayWrapper.secret_key);

		StringBuilder data1 = new StringBuilder().append(getContext().getRequest().getParameter(EbsPaymentGatewayWrapper.reqResParameter));

		for (int i = 0; i < data1.length(); i++) {
			if (data1.charAt(i) == ' ')
				data1.setCharAt(i, '+');
		}
		Base64 base64 = new Base64();
		byte[] data = base64.decode(data1.toString());
		RC4 rc4 = new RC4(key);
		byte[] result = rc4.rc4(data);


		ByteArrayInputStream byteIn = new ByteArrayInputStream(result, 0, result.length);
		DataInputStream dataIn = new DataInputStream(byteIn);
		String recvString1 = "";
		String recvString = "";
		try {
			recvString1 = dataIn.readLine();
		} catch (IOException e) {
			logger.debug("IO Exception occurred during callback of ebs " + e);
		}
		int i = 0;
		while (recvString1 != null) {
			i++;
			if (i > 705) break;
			recvString += recvString1 + "\n";
			try {
				recvString1 = dataIn.readLine();
			} catch (IOException e) {
				System.out.println("Exception occurred " + e);
			}
		}

		recvString = recvString.replace("=&", "=--&");

		Map<String, String> paramMap = EbsPaymentGatewayWrapper.parseResponse(recvString);

		String gatewayOrderId = paramMap.get(EbsPaymentGatewayWrapper.MerchantRefNo);
		String amountStr = paramMap.get(EbsPaymentGatewayWrapper.Amount);
		Double amount = NumberUtils.toDouble(amountStr);
		String authStatus = paramMap.get(EbsPaymentGatewayWrapper.ResponseCode);
		String flag_status = paramMap.get(EbsPaymentGatewayWrapper.IsFlagged);
        String rrn = paramMap.get(EbsPaymentGatewayWrapper.TransactionID);
        String TxMsg = paramMap.get(EbsPaymentGatewayWrapper.ResponseMessage);
        String ePGTxnID = paramMap.get(EbsPaymentGatewayWrapper.PaymentID);
		String merchantParam = null;

		Resolution resolution = null;
		try {
			// our own validations
			Payment payment = paymentManager.verifyPayment(gatewayOrderId, amount, merchantParam);

			// payment callback has been verified. now see if it is successful or failed from the gateway response
			if (EbsPaymentGatewayWrapper.authStatus_Success.equals(authStatus) && EbsPaymentGatewayWrapper.is_Flagged_False.equalsIgnoreCase(flag_status)) {

                //currently putting a dummy check for payment status at Ebs only, essentially at this stage payment status will not be success, just double checking, and if yes do nothing
                if(!EnumPaymentStatus.SUCCESS.getId().equals(payment.getPaymentStatus().getId())){
                    paymentManager.success(gatewayOrderId, ePGTxnID, rrn, TxMsg, null);
                }

				resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", gatewayOrderId);
			} else if (EbsPaymentGatewayWrapper.is_Flagged_True.equalsIgnoreCase(flag_status)) {
				paymentManager.pendingApproval(gatewayOrderId,ePGTxnID);
				resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", gatewayOrderId);
			} else {
				 paymentManager.fail(gatewayOrderId, ePGTxnID,TxMsg);
				resolution = new RedirectResolution(PaymentFailAction.class).addParameter("gatewayOrderId", gatewayOrderId);
			}
		} catch (HealthkartPaymentGatewayException e) {
			 paymentManager.error(gatewayOrderId, ePGTxnID, e, TxMsg);
			resolution = e.getRedirectResolution().addParameter("gatewayOrderId", gatewayOrderId);
		}
		return resolution;
	}

}
