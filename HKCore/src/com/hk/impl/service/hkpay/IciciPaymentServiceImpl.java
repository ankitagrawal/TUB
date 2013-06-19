package com.hk.impl.service.hkpay;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.payment.*;
import com.hk.domain.payment.Gateway;
import com.hk.domain.payment.Payment;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.EmailManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.service.UserService;
import com.hk.pact.service.payment.HkPaymentService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.pojo.HkPaymentResponse;
import com.hk.web.AppConstants;
import com.opus.epg.sfa.java.PGResponse;
import com.opus.epg.sfa.java.PGSearchResponse;
import com.opus.epg.sfa.java.PostLib;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Shakti Singh
 * Date: 5/23/13
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("IciciService")
public class IciciPaymentServiceImpl implements HkPaymentService {

    private static Logger logger = LoggerFactory.getLogger(IciciPaymentServiceImpl.class);

    private static final String ICICI_LIVE_PROPERTIES = "/icici.live.properties";
    private static final String ICICI_MOTO_LIVE_PROPERTIES = "/icicimoto.live.properties";

    @Autowired
    private PaymentManager paymentManager;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private EmailManager emailManager;
    @Autowired
    private UserService userService;

    /*@Override
    public Map<String, Object> seekHkPaymentResponse(String gatewayOrderId) {
        Map<String, Object> paymentResultMap = new HashMap<String, Object>();
        try {
            // call gateway and get gateway response object
            PGResponse response = callPaymentGateway(gatewayOrderId);
            // create HK response object
            paymentResultMap = createHKPaymentResponseObject(paymentResultMap, response);
            // filter and mail if status has been changed
            //filterAndSendMail(gatewayOrderId, paymentResultMap);

        } catch (Exception e) {
            logger.debug("There was an exception while trying to search for payment details for payment " + gatewayOrderId, e);
        }
        return paymentResultMap;
    }*/

    /*@Override
    public Payment updatePayment(String gatewayOrderId) {
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
                } else if (resp_code.equals(GatewayResponseKeys.HKConstants.ERROR.getKey())) {
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
    }*/

    @Override
    public List<HkPaymentResponse> seekPaymentFromGateway(Payment basePayment) {
        List<HkPaymentResponse> hkPaymentResponseList = null;
        try {
            if(basePayment != null){
                String gatewayOrderId = basePayment.getGatewayOrderId();
                ArrayList responseList = callIciciPaymentGateway(gatewayOrderId, basePayment.getGateway());
                hkPaymentResponseList = createHkPaymentResponse(responseList, gatewayOrderId);
            }

        } catch (Exception e){
            logger.debug("Icici gateway exception occurred ",e);
        }

        return hkPaymentResponseList;
    }


    @Override
    public HkPaymentResponse refundPayment(Payment basePayment, Double amount) throws HealthkartPaymentGatewayException {
        HkPaymentResponse hkPaymentResponse = null;
        Gateway gateway = basePayment.getGateway();
        String propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + ICICI_LIVE_PROPERTIES;
        if(gateway != null && EnumGateway.CITRUS.getId().equals(gateway.getId())){
            propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + ICICI_MOTO_LIVE_PROPERTIES;
        }
        Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);
        String merchantId = (String) properties.get(GatewayResponseKeys.Icici.merchantId);
        com.opus.epg.sfa.java.Merchant oMerchant = new com.opus.epg.sfa.java.Merchant();
        try {
            PostLib oPostLib = new PostLib();
            oMerchant.setMerchantRelatedTxnDetails(merchantId, merchantId, merchantId, basePayment.getGatewayOrderId(), basePayment.getGatewayReferenceId()
                    ,basePayment.getRrn(),basePayment.getAuthIdCode(),  null,null,"INR", "enq.Refund", amount.toString(),
                    null, null, null, null, null, null);

            PGResponse oPgResp = oPostLib.postRelatedTxn(oMerchant);
            if (oPgResp != null) {
                hkPaymentResponse = createPayment(oPgResp.getTxnId(), oPgResp.getEpgTxnId(), oPgResp.getRRN(), oPgResp.getRespMessage(),
                                                  oPgResp.getTxnType(), null, oPgResp.getAuthIdCode());
                setRefundPaymentStatus(oPgResp.getRespCode(),hkPaymentResponse);
            }

        } catch (Exception e) {
            logger.debug("There is an error while processing refund from Icici " + basePayment.getGatewayOrderId(), e);
        }
        return hkPaymentResponse;
    }

    private Map<String, Object> createHKPaymentResponseObject(Map<String, Object> hkrespObject, PGResponse oPgResp) {
        if (oPgResp != null) {
            hkrespObject.put(GatewayResponseKeys.HKConstants.Amount.getKey(), null);//TODO: icici doesn't return amount
            hkrespObject.put(GatewayResponseKeys.HKConstants.GATEWAY_REFERENCE_ID.getKey(), oPgResp.getEpgTxnId());
            hkrespObject.put(GatewayResponseKeys.HKConstants.ROOT_REFER_NO.getKey(), oPgResp.getRRN());
            hkrespObject.put(GatewayResponseKeys.HKConstants.AUTH_ID_CODE.getKey(), oPgResp.getAuthIdCode());
            hkrespObject.put(GatewayResponseKeys.HKConstants.RESPONSE_MSG.getKey(), oPgResp.getRespMessage());
            if (oPgResp.getRespCode().equals(GatewayResponseKeys.IciciConstants.TXN_SUCCESSFUL.getKey())) {
                hkrespObject.put(GatewayResponseKeys.HKConstants.RESPONSE_CODE.getKey(), GatewayResponseKeys.HKConstants.SUCCESS.getKey());
            } else if (oPgResp.getRespCode().equals(GatewayResponseKeys.IciciConstants.REJECTED_BY_ISSUER.getKey()) || oPgResp.getRespCode().equals(GatewayResponseKeys.IciciConstants.REJECTED_BY_GATEWAY.getKey())) {
                hkrespObject.put(GatewayResponseKeys.HKConstants.RESPONSE_CODE.getKey(), GatewayResponseKeys.HKConstants.ERROR.getKey());
            } else {
                hkrespObject.put(GatewayResponseKeys.HKConstants.RESPONSE_CODE.getKey(), GatewayResponseKeys.HKConstants.FAILED.getKey());
            }
        }
        return hkrespObject;
    }

    private PGResponse callPaymentGateway(String gatewayOrderId) throws Exception {

        PGResponse response = null;
        String propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + ICICI_LIVE_PROPERTIES;
        Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);
        String merchantId = (String) properties.get(GatewayResponseKeys.Icici.merchantId);
        Map<String, Object> paymentResultMap = new HashMap<String, Object>();
        com.opus.epg.sfa.java.Merchant oMerchant = new com.opus.epg.sfa.java.Merchant();

        PostLib oPostLib = new PostLib();
        oMerchant.setMerchantOnlineInquiry(merchantId, gatewayOrderId);
        PGSearchResponse oPgSearchResp = oPostLib.postStatusInquiry(oMerchant);
        ArrayList oPgRespArr = oPgSearchResp.getPGResponseObjects();
        logger.debug("PGSearchResponse received from payment gateway:" + oPgSearchResp.toString());

        if (oPgRespArr != null && !oPgRespArr.isEmpty()) {
            response = (PGResponse) oPgRespArr.get(0);
        }

        return response;
    }

    private ArrayList callIciciPaymentGateway(String gatewayOrderId, Gateway gateway) throws Exception {
        String propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + ICICI_LIVE_PROPERTIES;
        if(gateway != null && EnumGateway.CITRUS.getId().equals(gateway.getId())){
            propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + ICICI_MOTO_LIVE_PROPERTIES;
        }
        Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);
        String merchantId = (String) properties.get(GatewayResponseKeys.Icici.merchantId);
        com.opus.epg.sfa.java.Merchant oMerchant = new com.opus.epg.sfa.java.Merchant();

        PostLib oPostLib = new PostLib();
        oMerchant.setMerchantOnlineInquiry(merchantId, gatewayOrderId);
        PGSearchResponse oPgSearchResp = oPostLib.postStatusInquiry(oMerchant);
        ArrayList oPgRespArr = oPgSearchResp.getPGResponseObjects();
        return oPgRespArr;
    }

    private List<HkPaymentResponse> createHkPaymentResponse(ArrayList responseList, String gatewayOrderId) {
        List<HkPaymentResponse> hkPaymentResponseList = null;
        int index = 0;
        if(responseList != null && !responseList.isEmpty()){
            for(index = 0; index < responseList.size(); index++){
                PGResponse pgResponse = (PGResponse) responseList.get(index);
                HkPaymentResponse hkPaymentResponse = parseAndCreateHkPaymentResponse(pgResponse, gatewayOrderId);
                if(hkPaymentResponseList == null){
                    hkPaymentResponseList = new ArrayList<HkPaymentResponse>();
                }
                if(hkPaymentResponse != null){
                    hkPaymentResponseList.add(hkPaymentResponse);
                }
            }
        }

        return hkPaymentResponseList;
    }

    private HkPaymentResponse parseAndCreateHkPaymentResponse(PGResponse pgResponse, String gatewayOrderId) {
        HkPaymentResponse hkPaymentResponse = null;
        if(pgResponse != null) {
            String transactionId = pgResponse.getTxnId();
            if (transactionId != null){
                hkPaymentResponse = createPayment(transactionId, pgResponse.getEpgTxnId(), pgResponse.getRRN(), pgResponse.getRespMessage(), pgResponse.getTxnType(), null, pgResponse.getAuthIdCode());
            } else {
                hkPaymentResponse = createPayment(gatewayOrderId, pgResponse.getEpgTxnId(), pgResponse.getRRN(), pgResponse.getRespMessage(), pgResponse.getTxnType(), null, pgResponse.getAuthIdCode());
            }
            setPaymentStatus(pgResponse.getRespCode(), hkPaymentResponse);
        }
        return hkPaymentResponse;
    }

    private void setPaymentStatus(String respCode, HkPaymentResponse hkPaymentResponse) {
        if(respCode != null && hkPaymentResponse != null){
            String transactionType = hkPaymentResponse.getTransactionType();
            if(EnumPaymentTransactionType.SALE.getName().equalsIgnoreCase(transactionType)){
                setSalePaymentStatus(respCode,hkPaymentResponse);
            } else {
                setRefundPaymentStatus(respCode,hkPaymentResponse);
            }
        }
    }

    private void setRefundPaymentStatus(String respCode, HkPaymentResponse hkPaymentResponse) {
        if (GatewayResponseKeys.IciciConstants.TXN_SUCCESSFUL.getKey().equalsIgnoreCase(respCode)) {
            hkPaymentResponse.setHKPaymentStatus(EnumHKPaymentStatus.SUCCESS);
        } else {
            hkPaymentResponse.setHKPaymentStatus(EnumHKPaymentStatus.FAILURE);
        }
    }

    private void setSalePaymentStatus(String respCode, HkPaymentResponse hkPaymentResponse) {
        if (GatewayResponseKeys.IciciConstants.TXN_SUCCESSFUL.getKey().equalsIgnoreCase(respCode)) {
            hkPaymentResponse.setHKPaymentStatus(EnumHKPaymentStatus.SUCCESS);
        } else if (GatewayResponseKeys.IciciConstants.REJECTED_BY_ISSUER.getKey().equalsIgnoreCase(respCode)) {
            hkPaymentResponse.setHKPaymentStatus(EnumHKPaymentStatus.FAILURE);
            hkPaymentResponse.setErrorLog(GatewayResponseKeys.CitrusConstants.REJECTED_BY_ISSUER_MSG.getKey());
        } else if ( GatewayResponseKeys.IciciConstants.REJECTED_BY_GATEWAY.getKey().equalsIgnoreCase(respCode)) {
            hkPaymentResponse.setHKPaymentStatus(EnumHKPaymentStatus.FAILURE);
            hkPaymentResponse.setErrorLog(GatewayResponseKeys.CitrusConstants.REJECTED_BY_GATEWAY_MSG.getKey());
        }
    }

    private HkPaymentResponse createPayment(String gatewayOrderId, String gatewayReferenceId, String rrn , String respMsg, String txnType, String amount, String authIdCode){

        HkPaymentResponse hkPaymentResponse = new HkPaymentResponse(gatewayOrderId,gatewayReferenceId,respMsg,
                EnumGateway.ICICI.asGateway(),null,null,rrn,authIdCode,NumberUtils.toDouble(amount));

        if(txnType.equalsIgnoreCase(EnumPaymentTransactionType.SALE.getName())){
            hkPaymentResponse.setGatewayOrderId(gatewayOrderId);
            hkPaymentResponse.setTransactionType(EnumPaymentTransactionType.SALE.getName());
        } else if (txnType.equalsIgnoreCase(EnumPaymentTransactionType.REFUND.getName())){
            hkPaymentResponse.setTransactionType(EnumPaymentTransactionType.REFUND.getName());
        }
        return hkPaymentResponse;
    }

}
