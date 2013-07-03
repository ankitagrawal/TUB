package com.hk.impl.service.hkpay;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.payment.*;
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
    private PaymentDao paymentDao;

    @Override
    public List<HkPaymentResponse> seekPaymentFromGateway(Payment basePayment) throws HealthkartPaymentGatewayException {
        String gatewayOrderId = basePayment.getGatewayOrderId();
        List<HkPaymentResponse> gatewayPaymentList = new ArrayList<HkPaymentResponse>();

        try{

            // call payment gateway and get the base payment object
            Element element = callPaymentGateway(gatewayOrderId,null, null, null, EbsPaymentGatewayWrapper.TXN_ACTION_STATUS);
            HkPaymentResponse baseGatewayPayment = verifyAndCreateHkResponsePayment(element, gatewayOrderId,EnumPaymentTransactionType.SALE.getName());
            List<Payment> paymentList = paymentDao.searchPayments(null, null, null, null, null, null,null,basePayment,null);

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
            String ebsTransactionType = element.getAttributeValue(EbsPaymentGatewayWrapper.TXN_TRANSACTION_TYPE);
            //String gatewayOrderId = element.getAttributeValue(EbsPaymentGatewayWrapper.TXN_REFERENCE_NO);

            if(paymentId != null){

                hkPaymentResponse.setGatewayReferenceId(paymentId);
                hkPaymentResponse.setRrn(transactionId);
                hkPaymentResponse.setAmount(NumberUtils.toDouble(amount));
                hkPaymentResponse.setResponseMsg(status);
                updateResponseStatus(hkPaymentResponse, transactionType, isFlagged,status, ebsTransactionType);

            } else {
                hkPaymentResponse.setHKPaymentStatus(EnumHKPaymentStatus.FAILURE);
                hkPaymentResponse.setErrorLog(errorMessage);
            }
        }

        return hkPaymentResponse;
    }

    private void updateResponseStatus(HkPaymentResponse hkPaymentResponse, String transactionType , String isFlagged, String status, String ebsTransactionType){
        if(transactionType.equalsIgnoreCase(EnumPaymentTransactionType.REFUND.getName())){
            updateRefundPaymentStatus(hkPaymentResponse,status);
        } else {
            updateSalePaymentStatus(hkPaymentResponse,isFlagged, ebsTransactionType);
        }
    }

    private void updateRefundPaymentStatus(HkPaymentResponse hkPaymentResponse,String status) {
        if(GatewayResponseKeys.EbsConstants.PROCESSING.getKey().equalsIgnoreCase(status)){
            hkPaymentResponse.setHKPaymentStatus(EnumHKPaymentStatus.IN_PROCESS);
        } else if (GatewayResponseKeys.EbsConstants.PROCESSED.getKey().equalsIgnoreCase(status)) {
            hkPaymentResponse.setHKPaymentStatus(EnumHKPaymentStatus.SUCCESS);
        } else {
            hkPaymentResponse.setHKPaymentStatus(EnumHKPaymentStatus.FAILURE);
        }
    }

    private void updateSalePaymentStatus(HkPaymentResponse hkPaymentResponse,  String isFlagged, String ebsTransactionType) {
        if(GatewayResponseKeys.EbsConstants.Authorized.getKey().equalsIgnoreCase(ebsTransactionType)){
            if (isFlagged!= null && isFlagged.equalsIgnoreCase(GatewayResponseKeys.EbsConstants.IS_FLAGGED_FALSE.getKey())) {
                hkPaymentResponse.setHKPaymentStatus(EnumHKPaymentStatus.SUCCESS);
            } else if (isFlagged!=null && isFlagged.equalsIgnoreCase(GatewayResponseKeys.EbsConstants.IS_FLAGGED_TRUE.getKey())) {
                hkPaymentResponse.setHKPaymentStatus(EnumHKPaymentStatus.AUTHENTICATION_PENDING);
            } else {
                hkPaymentResponse.setHKPaymentStatus(EnumHKPaymentStatus.FAILURE);
            }
        } else {
            hkPaymentResponse.setHKPaymentStatus(EnumHKPaymentStatus.FAILURE);
            hkPaymentResponse.setResponseMsg(GatewayResponseKeys.HKConstants.NO_TRANSACTION_FOUND.getKey());
        }

    }

    @Override
    public HkPaymentResponse refundPayment(Payment basePayment, Double amount) throws HealthkartPaymentGatewayException {
        String baseGatewayOrderId = basePayment.getGatewayOrderId();
        String gatewayReferenceId = basePayment.getGatewayReferenceId();
        HkPaymentResponse hkRefundPaymentResponse = null;

        if (baseGatewayOrderId != null && gatewayReferenceId != null) {
            try {
                Element element = callPaymentGateway(null, gatewayReferenceId, null, amount.toString(), EbsPaymentGatewayWrapper.TXN_ACTION_REFUND);

                hkRefundPaymentResponse = verifyAndCreateHkResponsePayment(element, baseGatewayOrderId, EnumPaymentTransactionType.REFUND.getName());
            } catch (Exception e) {
                logger.debug("Ebs Payment gateway exception :", e);
            }

        } else {
            throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.PAYMENT_NOT_UPDATED);
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
}
