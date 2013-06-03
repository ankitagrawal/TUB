package com.hk.impl.service.hkpay;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.payment.GatewayResponseKeys;
import com.hk.domain.payment.Payment;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.EmailManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.service.UserService;
import com.hk.pact.service.payment.HkPaymentService;
import com.hk.pact.service.payment.PaymentService;
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
    }

    @Override
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
    }

    @Override
    public List<Payment> seekPaymentFromGateway(Payment basePayment) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Payment refundPayment(Payment basePayment,Double amount) throws HealthkartPaymentGatewayException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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

//    private void filterAndSendMail(String gatewayOrderId, Map<String, Object> respObject) {
//
//        Payment payment = paymentService.findByGatewayOrderId(gatewayOrderId);
//
//        if (respObject != null && !respObject.isEmpty()) {
//
//            String resp_code = (String) respObject.get(GatewayResponseKeys.HKConstants.RESPONSE_CODE.getKey());
//            if (!isPaymentStatusEqual(payment.getPaymentStatus().getName(), resp_code)) {
//                emailManager.sendAdminPaymentStatusChangeEmail(userService.getLoggedInUser(), payment.getPaymentStatus().getName(), resp_code, gatewayOrderId);
//            }
//        }
//
//    }
//
//    private boolean isPaymentStatusEqual(String oldStatus, String newStatus) {
//        return (oldStatus != null && oldStatus.equalsIgnoreCase(newStatus));
//    }
}
