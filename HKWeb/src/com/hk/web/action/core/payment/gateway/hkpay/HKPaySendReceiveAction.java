package com.hk.web.action.core.payment.gateway.hkpay;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.stripes.action.BasePaymentGatewaySendReceiveAction;
import com.hk.constants.core.Keys;
import com.hk.domain.payment.Payment;
import com.hk.domain.user.Address;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.HKPayPaymentGatewayWrapper;
import com.hk.manager.LinkManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.service.payment.PaymentService;
import com.hk.web.action.core.payment.PaymentFailAction;
import com.hk.web.action.core.payment.PaymentSuccessAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * User: Pratham
 * Date: 12/1/13  Time: 9:24 PM
*/

@Component
public class HKPaySendReceiveAction extends BasePaymentGatewaySendReceiveAction<HKPayPaymentGatewayWrapper> {

    private static Logger logger = LoggerFactory.getLogger(HKPaySendReceiveAction.class);

    @Value("#{hkEnvProps['" + Keys.Env.secureHKPay + "']}")
    private String secureHKPay;

    @Autowired
    PaymentManager paymentManager;

    @Autowired
    PaymentService paymentService;

    @Autowired
    LinkManager linkManager;

    public static String country = "IND";
    public static String accountId = "10258";
    public static String secretKey = "10703078";
    public static String description = "Live transaction";

    protected HKPayPaymentGatewayWrapper getPaymentGatewayWrapperFromTransactionData(BasePaymentGatewayWrapper.TransactionData data) {
        HKPayPaymentGatewayWrapper hkPaymentGatewayWrapper = new HKPayPaymentGatewayWrapper();
        String amountStr = BasePaymentGatewayWrapper.TransactionData.decimalFormat.format(data.getAmount());

        String orderId = String.valueOf(data.getOrderId());

        String server_secure_hash = getRequestChecksum(secretKey, accountId, amountStr, data.getGatewayOrderId(), orderId);
        String return_url = linkManager.getPaymentGatewayReturnUrl();

        Payment payment = paymentService.findByGatewayOrderId(data.getGatewayOrderId());
        Address address = payment.getOrder().getAddress();

        hkPaymentGatewayWrapper.addParameter("address", address.getLine1());
        hkPaymentGatewayWrapper.addParameter("city", address.getCity());
        hkPaymentGatewayWrapper.addParameter("state", address.getState());
        hkPaymentGatewayWrapper.addParameter("phone", address.getPhone());
        hkPaymentGatewayWrapper.addParameter("postal_code", address.getPincode().getPincode());
        hkPaymentGatewayWrapper.addParameter("name", address.getName());
        hkPaymentGatewayWrapper.addParameter("email", address.getUser().getEmail());
        hkPaymentGatewayWrapper.addParameter("return_url", return_url);
        hkPaymentGatewayWrapper.addParameter("account_id", accountId);
        hkPaymentGatewayWrapper.addParameter("reference_no", data.getGatewayOrderId());
        hkPaymentGatewayWrapper.addParameter("merchantTransactionId", data.getOrderId());
        hkPaymentGatewayWrapper.addParameter("reference_no", data.getGatewayOrderId());
        hkPaymentGatewayWrapper.addParameter("description", description);
        hkPaymentGatewayWrapper.addParameter("secure_hash", server_secure_hash);
        hkPaymentGatewayWrapper.addParameter("amount", amountStr);
        hkPaymentGatewayWrapper.addParameter("country", country);
        String issuerCode = data.getPaymentMethod();
        if (issuerCode != null && StringUtils.isNotBlank(issuerCode)) {
            hkPaymentGatewayWrapper.addParameter("payment_option", issuerCode);
        }

        hkPaymentGatewayWrapper.setGatewayUrl(secureHKPay);

        return hkPaymentGatewayWrapper;
    }

    @DefaultHandler
    public Resolution callback() {
        /*
        *         hkPaymentGatewayWrapper.addParameter("address", address.getLine1());
        hkPaymentGatewayWrapper.addParameter("city", address.getCity());
        hkPaymentGatewayWrapper.addParameter("state", address.getState());
        hkPaymentGatewayWrapper.addParameter("phone", address.getPhone());
        hkPaymentGatewayWrapper.addParameter("postal_code", address.getPincode().getPincode());
        hkPaymentGatewayWrapper.addParameter("name", address.getName());
        hkPaymentGatewayWrapper.addParameter("email", address.getUser().getEmail());
        hkPaymentGatewayWrapper.addParameter("return_url", return_url);
        hkPaymentGatewayWrapper.addParameter("account_id", accountId);
        hkPaymentGatewayWrapper.addParameter("reference_no", data.getGatewayOrderId());
        hkPaymentGatewayWrapper.addParameter("merchantTransactionId", data.getOrderId());
        hkPaymentGatewayWrapper.addParameter("reference_no", data.getGatewayOrderId());
        hkPaymentGatewayWrapper.addParameter("description", description);
        hkPaymentGatewayWrapper.addParameter("secure_hash", server_secure_hash);
        hkPaymentGatewayWrapper.addParameter("amount", amountStr);
        hkPaymentGatewayWrapper.addParameter("country", country);
        String issuerCode = data.getPaymentMethod();
        if (issuerCode != null && StringUtils.isNotBlank(issuerCode)) {
            hkPaymentGatewayWrapper.addParameter("payment_option", issuerCode);
        }
*/
        String gatewayRefId = getContext().getRequest().getParameter();
        String hkpayRefId = getContext().getRequest().getParameter("gatewayOrderId");
        String gatewayOrderId = getContext().getRequest().getParameter("gatewayOrderId");

        String merchantId = getContext().getRequest().getParameter("accountId");
        String amountStr = getContext().getRequest().getParameter("amount");
        Double amount = NumberUtils.toDouble(amountStr);
        String authDesc = getContext().getRequest().getParameter("transactionStatus");
        String gatewayChecksum = getContext().getRequest().getParameter("checksum");

        Resolution resolution = null;
        try {
            // gateway specific validation
            verifyChecksum(secretKey, accountId, amountStr, gatewayOrderId, authDesc,  gatewayChecksum);
            // our own validations
            paymentManager.verifyPayment(gatewayOrderId, amount, null);

            // payment callback has been verified. now see if it is successful or failed from the gateway response
            if ("Y".equals(authDesc)) {
                // TODO: pass all params not just garteway order id
                paymentManager.success(gatewayOrderId);
                resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", gatewayOrderId);
            } else if ("AP".equals(authDesc)) {
                paymentManager.pendingApproval(gatewayOrderId);
                resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", gatewayOrderId);
            } else if ("F".equals(authDesc)) {
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

    private static void verifyChecksum(String secretKey, String accountId, String Amount, String gatewayOrderId, String authDesc, String checkSum) throws HealthkartPaymentGatewayException {
        String newChecksum = generateResponseCheckSum(secretKey, accountId, Amount, gatewayOrderId, gatewayOrderId, authDesc);
        if (!newChecksum.equals(checkSum)) {
            throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.CHECKSUM_MISMATCH);
        }
    }

    private static String getRequestChecksum(String secretKey, String accountId, String amount, String gatewayOrderId, String orderId) {
        String pass = secretKey + "|" + accountId + "|" + amount + "|" + gatewayOrderId + "|" + orderId;
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            //
        }
        byte[] dataBytes = pass.getBytes();
        assert m != null;
        m.update(dataBytes, 0, dataBytes.length);
        BigInteger i = new BigInteger(1, m.digest());
        return String.format("%1$032X", i);
    }

    public static String  generateResponseCheckSum(String secret_key, String account_id, String amountStr, String reference_no, String merchantTransactionId, String status){
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.debug("NoSuchAlgorithmException occurred while generating MD5 hash" + e);
        }
        //secretKey + "|" + accountId + "|" + amount + "|" + gatewayOrderId + "|" + merchantTransactionId + "|" + status;
        String pass = secret_key + "|" + account_id + "|" + amountStr + "|" + reference_no + "|" + merchantTransactionId + "|" + status;
        byte[] dataBytes = pass.getBytes();
        assert m != null;
        m.update(dataBytes, 0, dataBytes.length);
        BigInteger i = new BigInteger(1, m.digest());
        return String.format("%1$032X", i);
    }

}
