package com.hk.impl.service.hkpay;

import com.akube.framework.util.BaseUtils;
import com.citruspay.pg.exception.CitruspayException;
import com.citruspay.pg.model.Enquiry;
import com.citruspay.pg.model.EnquiryCollection;
import com.citruspay.pg.model.Refund;
import com.hk.constants.payment.EnumGateway;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.payment.EnumPaymentTransactionType;
import com.hk.constants.payment.GatewayResponseKeys;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.payment.Payment;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.EmailManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.service.UserService;
import com.hk.pact.service.payment.HkPaymentService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.pojo.HkPaymentResponse;
import com.hk.web.AppConstants;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Shakti Singh
 * Date: 5/22/13
 * Time: 7:06 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("CitrusService")
public class CitrusPaymentServiceImpl implements HkPaymentService {

    private static Logger logger = LoggerFactory.getLogger(CitrusPaymentServiceImpl.class);

    private static final String CITRUS_LIVE_PROPERTIES = "/citrus.live.properties";

    @Autowired
    private PaymentManager paymentManager;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private EmailManager emailManager;
    @Autowired
    private UserService userService;

    public Map<String, Object> seekHkPaymentResponse(String gatewayOrderId) {

        Map<String, Object> hkrespObject = new HashMap<String, Object>();


        try {
            // call gateway and fetch gateway response
            EnquiryCollection enqCol = callPaymentGateway(gatewayOrderId);
            // create HK response object
            hkrespObject = createHKPaymentResponseObject(hkrespObject, enqCol, gatewayOrderId);
            // filter and mail if status has been changed
            //filterAndSendMail(gatewayOrderId, hkrespObject);

        } catch (CitruspayException e) {
            logger.debug("There was an exception while trying to search for payment details for payment " + gatewayOrderId, e);
        } catch (Exception e) {
            logger.debug("There was an exception while trying to search for payment details for payment " + gatewayOrderId, e);
        }
        return hkrespObject;
    }

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
        List<HkPaymentResponse> hkPaymentResponseList = null;
        try{
            EnquiryCollection enquiryCollection = callPaymentGateway(gatewayOrderId);
            List<Enquiry> enquiryList = verifyGatewayAndReturnListOfEnquiryObject(enquiryCollection);
            hkPaymentResponseList = createHkPaymentResponse(enquiryList, gatewayOrderId);
        }  catch (Exception e){
            logger.debug("Citrus Payment gateway exception :",e);
        }
        return hkPaymentResponseList;
    }

    @Override
    public HkPaymentResponse refundPayment(Payment basePayment, Double amount) throws HealthkartPaymentGatewayException {

        Map<String, Object> paymentSearchMap = new HashMap<String, Object>();
        String propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + CITRUS_LIVE_PROPERTIES;
        Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);
        //"26635c9f27d46c139c7feb3e2960ee1b1780ac28"
        com.citruspay.pg.util.CitruspayConstant.merchantKey = (String)properties.get(GatewayResponseKeys.Citrus.key) ;
        paymentSearchMap.put(GatewayResponseKeys.Citrus.merchantAccessKey,  properties.get(GatewayResponseKeys.Citrus.merchantAccessKey));
        paymentSearchMap.put(GatewayResponseKeys.Citrus.pgTxnId, basePayment.getGatewayReferenceId());
        paymentSearchMap.put(GatewayResponseKeys.Citrus.transactionId, basePayment.getGatewayOrderId());
        paymentSearchMap.put(GatewayResponseKeys.Citrus.rrn, basePayment.getRrn());
        paymentSearchMap.put(GatewayResponseKeys.Citrus.authIdCode, basePayment.getAuthIdCode());
        paymentSearchMap.put(GatewayResponseKeys.Citrus.amount, amount);
        paymentSearchMap.put(GatewayResponseKeys.Citrus.currencyCode, properties.get(GatewayResponseKeys.Citrus.currencyCode));
        paymentSearchMap.put(GatewayResponseKeys.Citrus.txnType, properties.get(GatewayResponseKeys.Citrus.txnType));

        try{
            com.citruspay.pg.model.Refund refund = com.citruspay.pg.model.Refund.create(paymentSearchMap);

            if(refund != null){
                return verifyRefundPaymentStatusAndReturnPayment(refund);
            }

        } catch (CitruspayException e){
            logger.debug("Citrus Exception occurred : ",e);
            throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.UNKNOWN);
        }
        return null;
    }

    private HkPaymentResponse verifyRefundPaymentStatusAndReturnPayment(Refund refund) throws HealthkartPaymentGatewayException {
        HkPaymentResponse hkPaymentResponse = null;
        if(GatewayResponseKeys.CitrusConstants.REFUND_SUCCESS_CODE.getKey().equalsIgnoreCase(refund.getRespCode())){
            hkPaymentResponse = createPayment(refund.getMerTxnId(),refund.getTxnId(),refund.getRRN(),refund.getRespMessage(),EnumPaymentTransactionType.REFUND.getName(),refund.getAmount(),refund.getAuthIdCode());
        } else if (GatewayResponseKeys.CitrusConstants.MANDATORY_FIELD_MISSING_COD.getKey().equalsIgnoreCase(refund.getRespCode())){
            logger.debug("Citrus Refund : Mandatory Fields missing "+ refund.getRespCode() + ":" + refund.getRespMessage());
            throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.MANDATORY_FIELD_MISSING);
        }
        return hkPaymentResponse;
    }

    private List<Enquiry> verifyGatewayAndReturnListOfEnquiryObject(EnquiryCollection enquiryCollection) throws HealthkartPaymentGatewayException{
        List<Enquiry> enquiryList = null;
        if(enquiryCollection != null){
            if(GatewayResponseKeys.CitrusConstants.GOOD_ENQ_COD.getKey().equalsIgnoreCase(enquiryCollection.getRespCode())){
                enquiryList = enquiryCollection.getEnquiry();
            } else if (GatewayResponseKeys.CitrusConstants.MANDATORY_FIELD_MISSING_COD.getKey().equalsIgnoreCase(enquiryCollection.getRespCode())|| GatewayResponseKeys.CitrusConstants.BAD_ENQ_COD.getKey().equalsIgnoreCase(enquiryCollection.getRespCode())) {
                logger.debug("Citrus Payment bad enquiry "+ enquiryCollection.getRespCode() + ":" + enquiryCollection.getRespMsg());
                throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.MANDATORY_FIELD_MISSING);
            } else {
                logger.debug("Unknown error from citrus end "+ enquiryCollection.getRespCode() + ":" + enquiryCollection.getRespMsg());
                //new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.UNKNOWN);
            }
        }
        return enquiryList;
    }

    private List<HkPaymentResponse> createHkPaymentResponse(List<Enquiry> enquiryList, String gatewayOrderId) throws HealthkartPaymentGatewayException {
        List<HkPaymentResponse> paymentList = null;
        if (enquiryList != null && !enquiryList.isEmpty()){
            paymentList = new ArrayList<HkPaymentResponse>();
            for(Enquiry enquiry : enquiryList){

                // create a payment object
                HkPaymentResponse hkPaymentResponse = createPayment(gatewayOrderId, enquiry.getPgTxnId(), enquiry.getRrn(), enquiry.getRespMsg(), enquiry.getTxnType(), enquiry.getAmount(),enquiry.getAuthIdCode());
                verifyAndSetPaymentStatus(enquiry.getRespCode(),hkPaymentResponse);
                paymentList.add(hkPaymentResponse);
            }

        } else {
            logger.debug("Seek from Citrus returns either empty or null enquiry list");
        }
        return paymentList;
    }

    private void verifyAndSetPaymentStatus(String respCode, HkPaymentResponse hkPaymentResponse){
        if (respCode.equalsIgnoreCase(GatewayResponseKeys.CitrusConstants.SUCCESS_CODE.getKey()) || respCode.equalsIgnoreCase(GatewayResponseKeys.CitrusConstants.REFUND_SUCCESS_CODE.getKey())) {
            hkPaymentResponse.setPaymentStatus(EnumPaymentStatus.SUCCESS.asPaymenStatus());
        } else if (respCode.equalsIgnoreCase(GatewayResponseKeys.CitrusConstants.REJECTED_BY_ISSUER.getKey())) {
            hkPaymentResponse.setPaymentStatus(EnumPaymentStatus.FAILURE.asPaymenStatus());
        } else {
            hkPaymentResponse.setPaymentStatus(EnumPaymentStatus.ERROR.asPaymenStatus());
        }
    }

    private HkPaymentResponse createPayment(String gatewayOrderId, String gatewayReferenceId, String rrn , String respMsg, String txnType, String amount, String authIdCode){

        HkPaymentResponse hkPaymentResponse = new HkPaymentResponse(gatewayOrderId,gatewayReferenceId,respMsg,
                                                                    EnumGateway.CITRUS.asGateway(),null,null,rrn,authIdCode,NumberUtils.toDouble(amount));

        if(txnType.equalsIgnoreCase(EnumPaymentTransactionType.SALE.getName())){
            hkPaymentResponse.setGatewayOrderId(gatewayOrderId);
            hkPaymentResponse.setTransactionType(EnumPaymentTransactionType.SALE.getName());
        } else if (txnType.equalsIgnoreCase(EnumPaymentTransactionType.REFUND.getName())){
            hkPaymentResponse.setTransactionType(EnumPaymentTransactionType.REFUND.getName());
        }
        return hkPaymentResponse;
    }

    //TODO: Citrus doesn't return gatewayOrder Id, hence bind the same value from which seek is called
    private Map<String, Object> createHKPaymentResponseObject(Map<String, Object> hkrespObject, EnquiryCollection enqCol, String gatewayOrderId) {

        if (enqCol != null) {
            hkrespObject.put(GatewayResponseKeys.HKConstants.GATEWAY_ORDER_ID.getKey(), gatewayOrderId);
            hkrespObject.put(GatewayResponseKeys.HKConstants.RESPONSE_MSG.getKey(), enqCol.getRespMsg());
            List<Enquiry> enqList = enqCol.getEnquiry();
            if (enqList != null && !enqList.isEmpty()) {
                Enquiry enq = enqList.get(0);//TODO:
                if (enq != null) {

                    hkrespObject.put(GatewayResponseKeys.HKConstants.Amount.getKey(), enq.getAmount());
                    hkrespObject.put(GatewayResponseKeys.HKConstants.GATEWAY_REFERENCE_ID.getKey(), enq.getTxnId());
                    hkrespObject.put(GatewayResponseKeys.HKConstants.ROOT_REFER_NO.getKey(), enq.getRrn());
                    hkrespObject.put(GatewayResponseKeys.HKConstants.AUTH_ID_CODE.getKey(), enq.getAuthIdCode());
                    hkrespObject.put(GatewayResponseKeys.HKConstants.RESPONSE_MSG.getKey(), enq.getRespMsg());

                    if (enq.getRespCode().equalsIgnoreCase(GatewayResponseKeys.CitrusConstants.SUCCESS_CODE.getKey())) {
                        hkrespObject.put(GatewayResponseKeys.HKConstants.RESPONSE_CODE.getKey(), GatewayResponseKeys.HKConstants.SUCCESS.getKey());
                    } else if (enq.getRespCode().equalsIgnoreCase(GatewayResponseKeys.CitrusConstants.REJECTED_BY_ISSUER.getKey())) {
                        hkrespObject.put(GatewayResponseKeys.HKConstants.RESPONSE_CODE.getKey(), GatewayResponseKeys.HKConstants.FAILED.getKey());
                    } else {
                        hkrespObject.put(GatewayResponseKeys.HKConstants.RESPONSE_CODE.getKey(), GatewayResponseKeys.HKConstants.ERROR.getKey());
                    }

                } else {
                    hkrespObject.put(GatewayResponseKeys.HKConstants.RESPONSE_CODE.getKey(), GatewayResponseKeys.HKConstants.ERROR.getKey());
                }
            } else {
                hkrespObject.put(GatewayResponseKeys.HKConstants.RESPONSE_CODE.getKey(), GatewayResponseKeys.HKConstants.ERROR.getKey());
            }
        }
        return hkrespObject;
    }

    private EnquiryCollection callPaymentGateway(String gatewayOrderId) throws Exception {

        Enquiry enq = null;
        Map<String, Object> paymentSearchMap = new HashMap<String, Object>();
        String propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + CITRUS_LIVE_PROPERTIES;
        Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);
        com.citruspay.pg.util.CitruspayConstant.merchantKey = (String) properties.get(GatewayResponseKeys.CitrusConstants.KEY.getKey());
        paymentSearchMap.put(GatewayResponseKeys.CitrusConstants.MERCHANT_ACCESS_KEY.getKey(), properties.get(GatewayResponseKeys.CitrusConstants.MERCHANT_ACCESS_KEY.getKey()));
        paymentSearchMap.put(GatewayResponseKeys.CitrusConstants.TXN_ID.getKey(), gatewayOrderId);

        EnquiryCollection enquiryResult = com.citruspay.pg.model.Enquiry.create(paymentSearchMap);
        logger.info("PGSearchResponse received from payment gateway " + enquiryResult.getRespMsg());
        /*List<Enquiry> enqList = enquiryResult.getEnquiry();
        if (enqList != null && !enqList.isEmpty()) {
            enq = enqList.get(0);//TODO: double check whether only one element gets returned all the time
        }*/
        return enquiryResult;
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
