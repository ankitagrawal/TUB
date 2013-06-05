package com.hk.impl.service.hkpay;

import com.akube.framework.util.BaseUtils;
import com.citruspay.pg.exception.CitruspayException;
import com.citruspay.pg.model.Enquiry;
import com.citruspay.pg.model.EnquiryCollection;
import com.hk.constants.payment.GatewayResponseKeys;
import com.hk.domain.payment.Payment;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.EmailManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.service.UserService;
import com.hk.pact.service.payment.HkPaymentService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.web.AppConstants;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
            // create HK respose objcet
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
