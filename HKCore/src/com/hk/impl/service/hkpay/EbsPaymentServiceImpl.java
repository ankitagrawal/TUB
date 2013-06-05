package com.hk.impl.service.hkpay;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.payment.EnumGateway;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.payment.EnumPaymentTransactionType;
import com.hk.constants.payment.GatewayResponseKeys;
import com.hk.domain.payment.Payment;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.EmailManager;
import com.hk.manager.payment.EbsPaymentGatewayWrapper;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.payment.PaymentDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.payment.HkPaymentService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.pojo.HkPaymentResponse;
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
import java.util.*;

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
    @Autowired
    private PaymentDao paymentDao;

    @Override
    public Map<String, Object> seekHkPaymentResponse(String gatewayOrderId) {

        Map<String, Object> paymentResultMap = new HashMap<String, Object>();

        try {
            // hit payment gateway and get a response
            Element element = callPaymentGateway(gatewayOrderId,null, null, null, EbsPaymentGatewayWrapper.TXN_ACTION_STATUS);
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

    @Override
    public List<HkPaymentResponse> seekPaymentFromGateway(Payment basePayment) throws HealthkartPaymentGatewayException {
        String gatewayOrderId = basePayment.getGatewayOrderId();
        List<HkPaymentResponse> gatewayPaymentList = new ArrayList<HkPaymentResponse>();

        try{

            // call payment gateway and get the base payment object
            Element element = callPaymentGateway(gatewayOrderId,null, null, null, EbsPaymentGatewayWrapper.TXN_ACTION_STATUS);
            HkPaymentResponse baseGatewayPayment = verifyAndCreateHkResponsePayment(element, gatewayOrderId,EnumPaymentTransactionType.SALE.getName());
            List<Payment> paymentList = paymentDao.searchPayments(null, null, null, null, null, null,null,basePayment);

            gatewayPaymentList.add(baseGatewayPayment);

            // again call gateway and update all child payment list
            if(paymentList != null){
                for(Payment payment : paymentList){
                    String gatewayReferenceId = payment.getGatewayReferenceId();
                    String rrn = payment.getRrn();
                    if(gatewayReferenceId != null && rrn != null){
                        Element ele = callPaymentGateway(gatewayOrderId, payment.getGatewayReferenceId(),payment.getRrn(),null,EbsPaymentGatewayWrapper.TXN_ACTION_STATUS_PAYMENT_ID);
                        HkPaymentResponse childPayment = verifyAndCreateHkResponsePayment(ele, payment.getGatewayOrderId(),EnumPaymentTransactionType.REFUND.getName());
                        gatewayPaymentList.add(childPayment);
                    }

                }
            }

        } catch (Exception e){
            logger.debug("Ebs Payment gateway exception :",e);
            //throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.UNKNOWN);
        }

        return gatewayPaymentList;
    }

    private HkPaymentResponse verifyAndCreateHkResponsePayment(Element element, String gatewayOrderId, String transactionType) {
        HkPaymentResponse hkPaymentResponse = null;
        if(element != null){
            hkPaymentResponse = createPayment(gatewayOrderId,null,null, null,transactionType,null,null);     //EnumPaymentTransactionType.SALE.getName()

            String paymentId = element.getAttributeValue(GatewayResponseKeys.EbsConstants.TXN_PAYMENT_ID.getKey());
            String transactionId = element.getAttributeValue(GatewayResponseKeys.EbsConstants.TXN_TRANSACTION_ID.getKey());
            String amount = element.getAttributeValue(GatewayResponseKeys.EbsConstants.AMOUNT.getKey());
            String status = element.getAttributeValue(GatewayResponseKeys.EbsConstants.TXN_STATUS.getKey());
            String isFlagged = element.getAttributeValue(GatewayResponseKeys.EbsConstants.IS_FLAGGED.getKey());
            String errorCode = element.getAttributeValue(EbsPaymentGatewayWrapper.TXN_ERROR_CODE);
            String errorMessage = element.getAttributeValue(EbsPaymentGatewayWrapper.TXN_ERROR_MSG);
            //String gatewayOrderId = element.getAttributeValue(EbsPaymentGatewayWrapper.TXN_REFERENCE_NO);

            if(paymentId != null){

                hkPaymentResponse.setGatewayReferenceId(paymentId);
                hkPaymentResponse.setRrn(transactionId);
                hkPaymentResponse.setAmount(NumberUtils.toDouble(amount));
                hkPaymentResponse.setResponseMsg(status);
                updateResponseStatus(hkPaymentResponse, transactionType, isFlagged,status);

            } else {
                hkPaymentResponse.setPaymentStatus(EnumPaymentStatus.ERROR.asPaymenStatus());
                hkPaymentResponse.setErrorLog(errorMessage);
            }
        }

        return hkPaymentResponse;
    }

    private void updateResponseStatus(HkPaymentResponse hkPaymentResponse, String transactionType , String isFlagged, String status){
        if(transactionType.equalsIgnoreCase(EnumPaymentTransactionType.REFUND.getName())){
            updateRefundPaymentStatus(hkPaymentResponse,status);
        } else {
            updateSalePaymentStatus(hkPaymentResponse,transactionType,isFlagged);
        }
    }

    private void updateRefundPaymentStatus(HkPaymentResponse hkPaymentResponse,String status) {
        if(GatewayResponseKeys.EbsConstants.PROCESSING.getKey().equalsIgnoreCase(status)){
            hkPaymentResponse.setPaymentStatus(EnumPaymentStatus.REFUND_REQUEST_IN_PROCESS.asPaymenStatus());
        } else if (GatewayResponseKeys.EbsConstants.PROCESSED.getKey().equalsIgnoreCase(status)) {
            hkPaymentResponse.setPaymentStatus(EnumPaymentStatus.REFUNDED.asPaymenStatus());
        } else {
            hkPaymentResponse.setPaymentStatus(EnumPaymentStatus.ERROR.asPaymenStatus());
        }
    }

    private void updateSalePaymentStatus(HkPaymentResponse hkPaymentResponse, String transactionType, String isFlagged) {
        if (isFlagged.equalsIgnoreCase(GatewayResponseKeys.EbsConstants.IS_FLAGGED_FALSE.getKey())) {
            hkPaymentResponse.setPaymentStatus(EnumPaymentStatus.SUCCESS.asPaymenStatus());
        } else if (isFlagged.equalsIgnoreCase(GatewayResponseKeys.EbsConstants.IS_FLAGGED_TRUE.getKey())) {
            hkPaymentResponse.setPaymentStatus(EnumPaymentStatus.AUTHORIZATION_PENDING.asPaymenStatus());
        } else {
            hkPaymentResponse.setPaymentStatus(EnumPaymentStatus.FAILURE.asPaymenStatus());
        }
    }

    @Override
    public HkPaymentResponse refundPayment(Payment basePayment, Double amount) throws HealthkartPaymentGatewayException {
        String baseGatewayOrderId = basePayment.getGatewayOrderId();
        String gatewayReferenceId = basePayment.getGatewayReferenceId();
        HkPaymentResponse hkRefundPaymentResponse = null;
        try{
            // Call payment gateway
            if(baseGatewayOrderId != null && gatewayReferenceId != null){
                Element element = callPaymentGateway(null,gatewayReferenceId, null, amount.toString(), EbsPaymentGatewayWrapper.TXN_ACTION_REFUND);

                hkRefundPaymentResponse = verifyAndCreateHkResponsePayment(element, baseGatewayOrderId,EnumPaymentTransactionType.REFUND.getName());
            }

        } catch (Exception e){
            logger.debug("Ebs Payment gateway exception :",e);
            //throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.UNKNOWN);
        }
        return hkRefundPaymentResponse;
    }

    private Map<String, Object> createHKPaymentResponseObject(Map<String, Object> hkrespObject, Element ele) {

        if (ele != null) {

            String transactionId = ele.getAttributeValue(GatewayResponseKeys.EbsConstants.TXN_TRANSACTION_ID.getKey());
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
                hkrespObject.put(GatewayResponseKeys.HKConstants.ROOT_REFER_NO.getKey(), transactionId);
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
    private Element callPaymentGateway(String gatewayOrderId, String gatewayReferenceId, String rrn,String amount, String action) throws Exception {

        Element ele = null;
        String propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + EBS_LIVE_PROPERTIES;
        Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);
        //String action = EbsPaymentGatewayWrapper.TXN_ACTION_STATUS;
        //Instantiate an HttpClient
        HttpClient client = new HttpClient();
        String url = EbsPaymentGatewayWrapper.EBS_TXN_URL;

        //Instantiate a GET HTTP method
        PostMethod method = new PostMethod(url);
        method.setRequestHeader("Content-type", "text/xml; charset=ISO-8859-1");


        //Define name-value pairs to set into the QueryString
        NameValuePair nvp1 = new NameValuePair(EbsPaymentGatewayWrapper.TXN_SECRET_KEY, (String) properties.get(GatewayResponseKeys.EbsConstants.GATEWAY_SECRET_KEY.getKey()));
        NameValuePair nvp2 = new NameValuePair(EbsPaymentGatewayWrapper.TXN_ACTION, action);
        NameValuePair nvp3 = new NameValuePair(EbsPaymentGatewayWrapper.Amount, amount);  //txnAmount
        NameValuePair nvp4 = new NameValuePair(EbsPaymentGatewayWrapper.TXN_REF_NO, gatewayOrderId);   // AccountID
        NameValuePair nvp5 = new NameValuePair(EbsPaymentGatewayWrapper.PaymentID, gatewayReferenceId); //txnPaymentId
        NameValuePair nvp6 = new NameValuePair(EbsPaymentGatewayWrapper.TransactionID, rrn); //TODO: change here too
        //   Account id should be the last parameter
        NameValuePair nvp7 = new NameValuePair(EbsPaymentGatewayWrapper.TXN_ACCOUNT_ID, (String) properties.get(GatewayResponseKeys.EbsConstants.ACCOUNT_ID.getKey()));



        String res = null;
        if (action.equals(EbsPaymentGatewayWrapper.TXN_ACTION_STATUS)) {
            method.setQueryString(new NameValuePair[]{nvp1, nvp2, nvp4, nvp7});
        } else if (action.equals(EbsPaymentGatewayWrapper.TXN_ACTION_REFUND) || action.equals(EbsPaymentGatewayWrapper.TXN_ACTION_CAPTURE) || action.equals(EbsPaymentGatewayWrapper.TXN_ACTION_CANCEL)) {
            method.setQueryString(new NameValuePair[]{nvp1, nvp2, nvp3, nvp5, nvp7});
        } else if (action.equalsIgnoreCase(EbsPaymentGatewayWrapper.TXN_ACTION_STATUS_PAYMENT_ID)){
            method.setQueryString(new NameValuePair[]{nvp1,nvp2, nvp5, nvp6, nvp7 });    //TODO: npv4->npv2
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

    private HkPaymentResponse createPayment(String gatewayOrderId, String gatewayReferenceId, String rrn , String respMsg, String txnType, String amount, String authIdCode){
        HkPaymentResponse hkPaymentResponse = new HkPaymentResponse(gatewayOrderId,gatewayReferenceId,respMsg,
                                                    EnumGateway.EBS.asGateway(),null,null,rrn,authIdCode,NumberUtils.toDouble(amount));

        if(txnType.equalsIgnoreCase(EnumPaymentTransactionType.SALE.getName())){
            hkPaymentResponse.setGatewayOrderId(gatewayOrderId);
            hkPaymentResponse.setTransactionType(EnumPaymentTransactionType.SALE.getName());
        } else if (txnType.equalsIgnoreCase(EnumPaymentTransactionType.REFUND.getName())){
            hkPaymentResponse.setTransactionType(EnumPaymentTransactionType.REFUND.getName());
        }
        return hkPaymentResponse;
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
