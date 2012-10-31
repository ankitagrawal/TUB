package com.hk.web.action.core.payment.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.manager.EmailManager;
import com.hk.manager.LinkManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.manager.payment.PayPalPaymentGatewayWrapper;
import com.hk.pact.dao.payment.PaymentDao;
import com.hk.web.AppConstants;
import com.hk.domain.payment.Payment;
import com.hk.domain.order.Order;
import com.akube.framework.stripes.action.BasePaymentGatewaySendReceiveAction;
import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.util.BaseUtils;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.DefaultHandler;
import com.hk.domain.user.User;
import com.hk.domain.user.Address;

import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Oct 30, 2012
 * Time: 1:26:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class PayPalCreditDebitSendReceiveAction extends BasePaymentGatewaySendReceiveAction<PayPalPaymentGatewayWrapper> {
    private static Logger logger = LoggerFactory.getLogger(PayPalCreditDebitSendReceiveAction.class);

    @Autowired
    PaymentDao paymentDao;
    @Autowired
    PaymentManager paymentManager;
    @Autowired
    LinkManager linkManager;
    @Autowired
    EmailManager emailManager;


    protected PayPalPaymentGatewayWrapper getPaymentGatewayWrapperFromTransactionData(BasePaymentGatewayWrapper.TransactionData data) {
//      PayPalPaymentGatewayWrapper payPalPaymentGatewayWrapper = new PayPalPaymentGatewayWrapper(AppConstants.appBasePath);
        Payment payment = paymentDao.findByGatewayOrderId(data.getGatewayOrderId());
        Order order = payment.getOrder();
        User user = order.getUser();
        Address address = order.getAddress();
        String amountStr = BasePaymentGatewayWrapper.TransactionData.decimalFormat.format(data.getAmount());
        amountStr = "10.10";
        String propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + "/paypal.live.properties";
        Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);
        String userid = properties.getProperty(PayPalPaymentGatewayWrapper.USER);
        String pwd = properties.getProperty(PayPalPaymentGatewayWrapper.PWD);
        String mode = properties.getProperty(PayPalPaymentGatewayWrapper.MODE);
        String signature = properties.getProperty(PayPalPaymentGatewayWrapper.SIGNATURE);
        String Version = properties.getProperty(PayPalPaymentGatewayWrapper.VERSION);
        String paymentAction = properties.getProperty(PayPalPaymentGatewayWrapper.PAYMENTACTION);
         String method = properties.getProperty(PayPalPaymentGatewayWrapper.METHOD);

        PayPalPaymentGatewayWrapper payPalPaymentGatewayWrapper = new PayPalPaymentGatewayWrapper();

        payPalPaymentGatewayWrapper.addParameter(PayPalPaymentGatewayWrapper.SIGNATURE, signature);
        payPalPaymentGatewayWrapper.addParameter(PayPalPaymentGatewayWrapper.USER, userid);
        payPalPaymentGatewayWrapper.addParameter(PayPalPaymentGatewayWrapper.PWD, pwd);
        payPalPaymentGatewayWrapper.addParameter(PayPalPaymentGatewayWrapper.AMT, amountStr);
        payPalPaymentGatewayWrapper.addParameter(PayPalPaymentGatewayWrapper.VERSION, Version);
        payPalPaymentGatewayWrapper.addParameter(PayPalPaymentGatewayWrapper.RETURNURL, linkManager.getPayPalPaymentGatewayReturnUrl());
        payPalPaymentGatewayWrapper.addParameter(PayPalPaymentGatewayWrapper.CANCELURL, linkManager.getPayPalPaymentGatewayCancelUrl());
        payPalPaymentGatewayWrapper.addParameter(PayPalPaymentGatewayWrapper.PAYMENTACTION, paymentAction);
        payPalPaymentGatewayWrapper.addParameter(PayPalPaymentGatewayWrapper.METHOD, method);
        payPalPaymentGatewayWrapper.setGatewayUrl(properties.getProperty(payPalPaymentGatewayWrapper.merchantURLPart));

        return payPalPaymentGatewayWrapper;
    }


    @DefaultHandler
    public Resolution callback() {
        logger.info("in Paypal  callback -> " + getContext().getRequest().getParameterMap());
		String Token = getContext().getRequest().getParameter(PayPalPaymentGatewayWrapper.TOKEN);
        String Ack = getContext().getRequest().getParameter(PayPalPaymentGatewayWrapper.ACK);
        String Coorelationid = getContext().getRequest().getParameter(PayPalPaymentGatewayWrapper.CORRELATIONID);

        if (Ack.equals("Success")){
            
        }

//		String gatewayOrderId = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.TxId);
//		String TxStatus = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.TxStatus);
//		String pgRespCode = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.pgRespCode);
//		String amount = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.amount);

 	logger.info("in Paypal callback -> " + Token + "Acknowledge " + Ack + "Coorelation id  " + Coorelationid);

        return null;
    }
}
