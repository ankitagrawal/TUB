package com.hk.web.action.core.payment.gateway.hkpay;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.stripes.action.BasePaymentGatewaySendReceiveAction;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.payment.EnumGateway;
import com.hk.domain.payment.Gateway;
import com.hk.domain.payment.Payment;
import com.hk.domain.user.Address;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.HKPayPaymentGatewayWrapper;
import com.hk.manager.LinkManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.service.payment.PaymentService;
import com.hk.web.AppConstants;
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
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

/*
 * User: Pratham
 * Date: 12/1/13  Time: 9:24 PM
*/

@Component
public class HKPaySendReceiveAction extends BasePaymentGatewaySendReceiveAction<HKPayPaymentGatewayWrapper> {

    private static Logger logger = LoggerFactory.getLogger(HKPaySendReceiveAction.class);
    @Autowired
    PaymentManager paymentManager;

    @Autowired
    PaymentService paymentService;

    @Autowired
    LinkManager linkManager;

//    public static String country = "IND";
//    public static String description = "Live transaction";
//    public static String merchantTransactionId = "1";

//    public static String accountId = "10258";
//    public static String secretKey = "10703078";

    protected HKPayPaymentGatewayWrapper getPaymentGatewayWrapperFromTransactionData(BasePaymentGatewayWrapper.TransactionData data) {
        Properties properties = BaseUtils.getPropertyFile(AppConstants.getAppClasspathRootPath() + "/hkPay.live.properties");
        String accountId = properties.getProperty("accountId");
        String secretKey = properties.getProperty("secret_key");
        String description = properties.getProperty("description");
        String country = properties.getProperty("country");

        HKPayPaymentGatewayWrapper hkPaymentGatewayWrapper = new HKPayPaymentGatewayWrapper();
        String amountStr = BasePaymentGatewayWrapper.TransactionData.decimalFormat.format(data.getAmount());

        String orderId = String.valueOf(data.getOrderId());

        String server_secure_hash = getRequestChecksum(secretKey, accountId, amountStr, data.getGatewayOrderId(), orderId);
        String return_url = linkManager.getPaymentGatewayReturnUrl();

        Payment payment = paymentService.findByGatewayOrderId(data.getGatewayOrderId());
        Address address = payment.getOrder().getAddress();


        hkPaymentGatewayWrapper.addParameter(HKPayPaymentGatewayWrapper.address, address.getLine1());
        hkPaymentGatewayWrapper.addParameter(HKPayPaymentGatewayWrapper.city, address.getCity());
        hkPaymentGatewayWrapper.addParameter(HKPayPaymentGatewayWrapper.state, address.getState());
        hkPaymentGatewayWrapper.addParameter(HKPayPaymentGatewayWrapper.phone, address.getPhone());
        hkPaymentGatewayWrapper.addParameter(HKPayPaymentGatewayWrapper.postal_code, address.getPincode().getPincode());
        hkPaymentGatewayWrapper.addParameter(HKPayPaymentGatewayWrapper.name, address.getName());
        hkPaymentGatewayWrapper.addParameter(HKPayPaymentGatewayWrapper.email, address.getUser().getEmail());
        hkPaymentGatewayWrapper.addParameter(HKPayPaymentGatewayWrapper.return_url, return_url);
        hkPaymentGatewayWrapper.addParameter(HKPayPaymentGatewayWrapper.account_id, accountId);
        hkPaymentGatewayWrapper.addParameter(HKPayPaymentGatewayWrapper.merchantTransactionId, data.getOrderId());
        hkPaymentGatewayWrapper.addParameter(HKPayPaymentGatewayWrapper.reference_no, data.getGatewayOrderId());
        hkPaymentGatewayWrapper.addParameter(HKPayPaymentGatewayWrapper.description, description);
        hkPaymentGatewayWrapper.addParameter(HKPayPaymentGatewayWrapper.secure_hash, server_secure_hash);
        hkPaymentGatewayWrapper.addParameter(HKPayPaymentGatewayWrapper.amount, amountStr);
        hkPaymentGatewayWrapper.addParameter(HKPayPaymentGatewayWrapper.country, country);
        String issuerCode = data.getPaymentMethod();
        if (issuerCode != null && StringUtils.isNotBlank(issuerCode)) {
            hkPaymentGatewayWrapper.addParameter(HKPayPaymentGatewayWrapper.payment_option, issuerCode);
        }

        String url = properties.getProperty("secureHKPay");
//        String url = "http://stag.securepay.healthkart.com/gateway/request";


        hkPaymentGatewayWrapper.setGatewayUrl(url);
        logger.info("Hitting payment gateway at url" + url);
        logger.info("Payment request parameters are: " + hkPaymentGatewayWrapper.toString());

        return hkPaymentGatewayWrapper;
    }

    @DefaultHandler
    public Resolution callback() {
        Properties properties = BaseUtils.getPropertyFile(AppConstants.getAppClasspathRootPath() + "/hkPay.live.properties");
        String accountId = properties.getProperty("accountId");
        String secretKey = properties.getProperty("secret_key");
        String gatewayRefId = getContext().getRequest().getParameter("gatewayReferenceId");
        String hkpayRefId = getContext().getRequest().getParameter("hkpayReferenceId");
        String gatewayChecksum = getContext().getRequest().getParameter("checksum");
        String rrn = getContext().getRequest().getParameter("rootReferenceNumber");
        String authIdCode = getContext().getRequest().getParameter("authIdCode");
        String amountStr = getContext().getRequest().getParameter("amount");
        Double amount = NumberUtils.toDouble(amountStr);
        String authDesc = getContext().getRequest().getParameter("transactionStatus");
        String gatewayOrderId = getContext().getRequest().getParameter("gatewayOrderId");
        String orderId = getContext().getRequest().getParameter("orderId");
        String issuerId = getContext().getRequest().getParameter("issuerId");
        Gateway gateway = getGateway(getContext().getRequest().getParameter("gatewayId"));

        StringBuffer paramsSB = new StringBuffer();
        paramsSB.append("accountId: " + accountId).append("\n")
                .append("secretKey: " + secretKey).append("\n")
                .append("gatewayRefId: " + gatewayRefId).append("\n")
                .append("hkpayRefId: " + hkpayRefId).append("\n")
                .append("gatewayChecksum: " + gatewayChecksum).append("\n")
                .append("rrn: " + rrn).append("\n")
                .append("authIdCode: " + authIdCode).append("\n")
                .append("amountStr: " + amountStr).append("\n")
                .append("amount: " + amount).append("\n")
                .append("authDesc: " + authDesc).append("\n")
                .append("gatewayOrderId: " + gatewayOrderId).append("\n")
                .append("orderId: " + orderId).append("\n")
                .append("issuerId: " + issuerId).append("\n")
                .append("gateway: " + gateway).append("\n");
        logger.info("Response from the gateway " + gateway.getName() +" is: \n"+paramsSB.toString());

        Resolution resolution = null;
        try {
            // gateway specific validation
            verifyChecksum(secretKey, accountId, amountStr, gatewayOrderId, orderId, authDesc, gatewayChecksum);
            // our own validations
            paymentManager.verifyPayment(gatewayOrderId, amount, null);

            // payment callback has been verified. now see if it is successful or failed from the gateway response
            if ("Y".equals(authDesc)) {
                paymentManager.success(gatewayOrderId, hkpayRefId, gatewayRefId, rrn, null, authIdCode, gateway);
                resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", hkpayRefId);
            } else if ("AP".equals(authDesc)) {
                paymentManager.pendingApproval(gatewayOrderId, hkpayRefId, gatewayRefId, gateway);
                resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", hkpayRefId);
            } else if ("F".equals(authDesc)) {
                paymentManager.fail(gatewayOrderId, hkpayRefId, gatewayRefId, null, gateway);
                resolution = new RedirectResolution(PaymentFailAction.class).addParameter("gatewayOrderId", hkpayRefId);
            } else {
                throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.INVALID_RESPONSE);
            }
        } catch (HealthkartPaymentGatewayException e) {
            paymentManager.error(gatewayOrderId, e);
            resolution = e.getRedirectResolution().addParameter("gatewayOrderId", gatewayOrderId);
        }
        return resolution;
    }

    private Gateway getGateway(String gatewayId) {
        Gateway gateway = null;
        try {
            gateway = EnumGateway.getGatewayFromId(Long.parseLong(gatewayId)).asGateway();
        } catch (Exception e) {
            logger.info("Exception parsing gateway id in HKPaySendReceiveAction", e);
            gateway = null;
        }
        return gateway;
    }

    private static void verifyChecksum(String secretKey, String accountId, String Amount,
                                       String gatewayOrderId, String orderId, String authStatus, String checkSum) throws HealthkartPaymentGatewayException {
        String newChecksum = getResponseChecksum(secretKey, accountId, Amount, gatewayOrderId, orderId, authStatus);
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

    private static String getResponseChecksum(String secretKey, String accountId, String amount, String gatewayOrderId, String merchantTransactionId, String status) {
        String pass = secretKey + "|" + accountId + "|" + amount + "|" + gatewayOrderId + "|" + merchantTransactionId + "|" + status;
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

}
