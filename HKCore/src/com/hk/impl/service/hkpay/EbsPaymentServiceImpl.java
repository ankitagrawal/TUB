package com.hk.impl.service.hkpay;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.payment.GatewayResponseKeys;
import com.hk.domain.payment.Payment;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.EmailManager;
import com.hk.manager.payment.EbsPaymentGatewayWrapper;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.service.UserService;
import com.hk.pact.service.payment.HkPaymentService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.web.AppConstants;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.math.NumberUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Shakti Singh
 * Date: 5/23/13
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
@Service("EbsService")
public class EbsPaymentServiceImpl implements HkPaymentService {

    private static Logger logger = LoggerFactory.getLogger(EbsPaymentServiceImpl.class);

    private static final String EBS_LIVE_PROPERTIES = "/ebs.live.properties";

    @Autowired
    private PaymentManager paymentManager;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private EmailManager emailManager;
    @Autowired
    private UserService userService;

    @Override
    public Map<String, Object> seekHkPaymentResponse(String gatewayOrderId) {

        Map<String, Object> paymentResultMap = new HashMap<String, Object>();

        try {
            // hit payment gateway and get a response
            Element element = callPaymentGateway(gatewayOrderId);
            // create a HK response object
            paymentResultMap = createHKPaymentResponseObject(paymentResultMap, element);
            // filter and mail if status has been changed
            //filterAndSendMail(gatewayOrderId, paymentResultMap);

        } catch (IOException e) {
            logger.debug(" Exception  while sending the request to Ebs Gateway " + gatewayOrderId, e);
        } catch (Exception e) {
            logger.debug(" Exception  while trying to search for payment details for EBS  payment " + gatewayOrderId, e);
        }

        return paymentResultMap;
    }

    @Override
    public Payment updatePayment(String gatewayOrderId) {
        // hit gateway and get updated HK Response Object
        Map<String, Object> hkrespObj = seekHkPaymentResponse(gatewayOrderId);
        if (hkrespObj != null && !hkrespObj.isEmpty()) {
            String amtStr = (String) hkrespObj.get(GatewayResponseKeys.HKConstants.Amount.getKey());
            Double amount = NumberUtils.toDouble(amtStr);
            String resp_code = (String) hkrespObj.get(GatewayResponseKeys.HKConstants.RESPONSE_CODE.getKey());
            String resp_msg = (String) hkrespObj.get(GatewayResponseKeys.HKConstants.RESPONSE_MSG.getKey());
            String rrn = (String) hkrespObj.get(GatewayResponseKeys.HKConstants.ROOT_REFER_NO.getKey());
            String authIdCode = (String) hkrespObj.get(GatewayResponseKeys.HKConstants.AUTH_ID_CODE.getKey());
            String gateway_refer_id = (String) hkrespObj.get(GatewayResponseKeys.HKConstants.GATEWAY_REFERENCE_ID.getKey());
            String merchantParam = null;
            try {

                // our own validations
                paymentManager.verifyPayment(gatewayOrderId, amount, merchantParam);

                // payment callback has been verified. now see if it is successful or failed from the gateway response
                if (resp_code.equals(GatewayResponseKeys.HKConstants.SUCCESS.getKey())) {
                    paymentManager.success(gatewayOrderId, gateway_refer_id, rrn, resp_msg, authIdCode);
                } else if (resp_code.equals(GatewayResponseKeys.HKConstants.AUTH_PEND.getKey())) {
                    paymentManager.pendingApproval(gatewayOrderId, gateway_refer_id);
                } else if (resp_code.equals(GatewayResponseKeys.HKConstants.FAILED.getKey())) {
                    paymentManager.fail(gatewayOrderId, gateway_refer_id, resp_msg);
                    //emailManager.sendPaymentFailMail(getPrincipalUser(), gatewayOrderId);

                } else {
                    throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.INVALID_RESPONSE);
                }
            } catch (HealthkartPaymentGatewayException e) {
                //emailManager.sendPaymentFailMail(getPrincipalUser(), gatewayOrderId);
                paymentManager.error(gatewayOrderId, e);

            }
        }
        return paymentService.findByGatewayOrderId(gatewayOrderId);
    }

    private Map<String, Object> createHKPaymentResponseObject(Map<String, Object> hkrespObject, Element ele) {

        if (ele != null) {

            String trnsid = ele.getAttributeValue(GatewayResponseKeys.EbsConstants.TXN_TRANSACTION_ID.getKey());
            String paymentId = ele.getAttributeValue(GatewayResponseKeys.EbsConstants.TXN_PAYMENT_ID.getKey());
            String amount = ele.getAttributeValue(GatewayResponseKeys.EbsConstants.AMOUNT.getKey());
            String status = ele.getAttributeValue(GatewayResponseKeys.EbsConstants.TXN_STATUS.getKey());
            String isFlagged = ele.getAttributeValue(GatewayResponseKeys.EbsConstants.IS_FLAGGED.getKey());
            String errorCode = ele.getAttributeValue(EbsPaymentGatewayWrapper.TXN_ERROR_CODE);
            String errorMessage = ele.getAttributeValue(EbsPaymentGatewayWrapper.TXN_ERROR_MSG);
            String gatewayOrderId = ele.getAttributeValue(EbsPaymentGatewayWrapper.TXN_REFERENCE_NO);

            if (paymentId != null) {
                hkrespObject.put(GatewayResponseKeys.HKConstants.Amount.getKey(), amount);
                hkrespObject.put(GatewayResponseKeys.HKConstants.GATEWAY_ORDER_ID.getKey(), gatewayOrderId);
                hkrespObject.put(GatewayResponseKeys.HKConstants.GATEWAY_REFERENCE_ID.getKey(), paymentId);
                hkrespObject.put(GatewayResponseKeys.HKConstants.ROOT_REFER_NO.getKey(), trnsid);
                hkrespObject.put(GatewayResponseKeys.HKConstants.AUTH_ID_CODE.getKey(), null); // no auth id code is returned from ebs
                hkrespObject.put(GatewayResponseKeys.HKConstants.RESPONSE_MSG.getKey(), status);
                if (isFlagged.equalsIgnoreCase(GatewayResponseKeys.EbsConstants.IS_FLAGGED_FALSE.getKey())) {
                    hkrespObject.put(GatewayResponseKeys.HKConstants.RESPONSE_CODE.getKey(), GatewayResponseKeys.HKConstants.SUCCESS.getKey());
                } else if (isFlagged.equalsIgnoreCase(GatewayResponseKeys.EbsConstants.IS_FLAGGED_TRUE.getKey())) {
                    hkrespObject.put(GatewayResponseKeys.HKConstants.RESPONSE_CODE.getKey(), GatewayResponseKeys.HKConstants.AUTH_PEND.getKey());
                } else {
                    hkrespObject.put(GatewayResponseKeys.HKConstants.RESPONSE_CODE.getKey(), GatewayResponseKeys.HKConstants.FAILED.getKey());
                }
            } else {
                hkrespObject.put(GatewayResponseKeys.HKConstants.RESPONSE_CODE.getKey(), GatewayResponseKeys.HKConstants.ERROR.getKey());
                hkrespObject.put(GatewayResponseKeys.HKConstants.ERROR_LOG.getKey(), errorMessage);
            }

        }
        return hkrespObject;
    }

    @SuppressWarnings("unchecked")
    private Element callPaymentGateway(String gatewayOrderId) throws Exception {

        Element ele = null;
        String propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + EBS_LIVE_PROPERTIES;
        Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);
        String action = EbsPaymentGatewayWrapper.TXN_ACTION_STATUS;
        //Instantiate an HttpClient
        HttpClient client = new HttpClient();
        String url = EbsPaymentGatewayWrapper.EBS_TXN_URL;

        //Instantiate a GET HTTP method
        PostMethod method = new PostMethod(url);
        method.setRequestHeader("Content-type", "text/xml; charset=ISO-8859-1");


        //Define name-value pairs to set into the QueryString
        NameValuePair nvp1 = new NameValuePair(EbsPaymentGatewayWrapper.TXN_SECRET_KEY, (String) properties.get(GatewayResponseKeys.EbsConstants.GATEWAY_SECRET_KEY.getKey()));
        NameValuePair nvp2 = new NameValuePair(EbsPaymentGatewayWrapper.TXN_ACTION, action);
        NameValuePair nvp4 = new NameValuePair(EbsPaymentGatewayWrapper.TXN_REF_NO, gatewayOrderId);
        NameValuePair nvp5 = new NameValuePair(EbsPaymentGatewayWrapper.PaymentID, null); //txnPaymentId
        NameValuePair nvp3 = new NameValuePair(EbsPaymentGatewayWrapper.Amount, null);  //txnAmount
        //   Account id should be the last parameter
        NameValuePair nvp6 = new NameValuePair(EbsPaymentGatewayWrapper.TXN_ACCOUNT_ID, (String) properties.get(GatewayResponseKeys.EbsConstants.ACCOUNT_ID.getKey()));


        String res = null;
        if (action.equals(EbsPaymentGatewayWrapper.TXN_ACTION_STATUS)) {
            method.setQueryString(new NameValuePair[]{nvp1, nvp2, nvp4, nvp6});
        } else if (action.equals(EbsPaymentGatewayWrapper.TXN_ACTION_REFUND) || action.equals(EbsPaymentGatewayWrapper.TXN_ACTION_CAPTURE) || action.equals(EbsPaymentGatewayWrapper.TXN_ACTION_CANCEL)) {
            method.setQueryString(new NameValuePair[]{nvp1, nvp2, nvp3, nvp5, nvp6});
        }

        client.executeMethod(method);
        res = method.getResponseBodyAsString();
        Document doc = new SAXBuilder().build(new StringReader(res));

        XPath xPath = XPath.newInstance("/output");
        List<Element> xmlElementList = xPath.selectNodes(doc);

        if (xmlElementList != null && !xmlElementList.isEmpty()) ele = xmlElementList.get(0);

        method.releaseConnection();

        return ele;
    }

   /* private void filterAndSendMail(String gatewayOrderId, Map<String, Object> respObject) {

        Payment payment = paymentService.findByGatewayOrderId(gatewayOrderId);

        if (respObject != null && !respObject.isEmpty()) {

            String resp_code = (String) respObject.get(GatewayResponseKeys.HKConstants.RESPONSE_CODE.getKey());
            if (!isPaymentStatusEqual(payment.getPaymentStatus().getName(), resp_code)) {
                emailManager.sendAdminPaymentStatusChangeEmail(userService.getLoggedInUser(), payment.getPaymentStatus().getName(), resp_code, gatewayOrderId);
            }
        }

    }

    private boolean isPaymentStatusEqual(String oldStatus, String newStatus) {
        return (oldStatus != null && oldStatus.equalsIgnoreCase(newStatus));
    }*/
}
