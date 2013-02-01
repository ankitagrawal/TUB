package com.hk.util;

import com.akube.framework.util.BaseUtils;
import com.citruspay.pg.exception.CitruspayException;
import com.citruspay.pg.model.Enquiry;
import com.citruspay.pg.model.EnquiryCollection;
import com.hk.manager.payment.CitrusPaymentGatewayWrapper;
import com.opus.epg.sfa.java.PGResponse;
import com.opus.epg.sfa.java.PGSearchResponse;
import com.opus.epg.sfa.java.PostLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 31/01/13
 * Time: 17:05
 * To change this template use File | Settings | File Templates.
 */
public class PaymentFinder {

    private static Logger logger = LoggerFactory.getLogger(PaymentFinder.class);


    public static Map<String, Object> findIciciPayment(String gatewayOrderId, String merchantId) {
        Map<String, Object> paymentResultMap = new HashMap<String, Object>();
        com.opus.epg.sfa.java.Merchant oMerchant = new com.opus.epg.sfa.java.Merchant();
        try {
            PostLib oPostLib = new PostLib();

            oMerchant.setMerchantOnlineInquiry(merchantId
                    , gatewayOrderId);

            PGSearchResponse oPgSearchResp = oPostLib.postStatusInquiry(oMerchant);
            ArrayList oPgRespArr = oPgSearchResp.getPGResponseObjects();
//            System.out.println("PGSearchResponse received from payment gateway:" + oPgSearchResp.toString());
            logger.debug("PGSearchResponse received from payment gateway:" + oPgSearchResp.toString());
            int index = 0;
            if (oPgRespArr != null) {
                for (index = 0; index < oPgRespArr.size(); index++) {
                    PGResponse oPgResp = (PGResponse) oPgRespArr.get(index);
                    paymentResultMap.put("Response Code", oPgResp.getRespCode());
                    paymentResultMap.put("Response Message", oPgResp.getRespMessage());
                    paymentResultMap.put("Txn Id", oPgResp.getTxnId());
                    paymentResultMap.put("Epg Txn Id", oPgResp.getEpgTxnId());
                    paymentResultMap.put("AuthIdCode", oPgResp.getAuthIdCode());
                    paymentResultMap.put("RRN", oPgResp.getRRN());
                    paymentResultMap.put("TxnType", oPgResp.getTxnType());
                    paymentResultMap.put("TxnDateTime", oPgResp.getTxnDateTime());
                    paymentResultMap.put("Cv resp Code", oPgResp.getCVRespCode());

//                    System.out.println("PGResponse object:" + oPgResp.toString());
                    logger.debug("PGResponse object:" + oPgResp.toString());

                }
            }
        } catch (Exception e) {
            logger.debug("There was an exception while trying to search for payment details for payment " + gatewayOrderId, e);
        }
        return paymentResultMap;
    }

    public static Map<String, Object> findCitrusPayment(String gatewayOrderId) {

        Map<String, Object> paymentResultMap = new HashMap<String, Object>();

//        String propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + "/citrus.live.properties";
        String propertyLocatorFileLocation = "D:\\Projects\\HKDev\\HealthKart\\dist\\WEB-INF\\citrus.live.properties";
        Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);

        String key = properties.getProperty(CitrusPaymentGatewayWrapper.key);
        String merchantId = properties.getProperty(CitrusPaymentGatewayWrapper.MerchantId);

        Map<String, Object> paymentSearchMap = new HashMap<String, Object>();
        com.citruspay.pg.util.CitruspayConstant.merchantKey = "26635c9f27d46c139c7feb3e2960ee1b1780ac28";
        paymentSearchMap.put("merchantAccessKey", "6Z1PA7WZEVIRHMGKG1VG");
        paymentSearchMap.put("transactionId", gatewayOrderId);
        paymentSearchMap.put("bankName", "ABC BANK");

        try {
            EnquiryCollection enquiryResult = com.citruspay.pg.model.Enquiry
                    .create(paymentSearchMap);
            logger.info("PGSearchResponse received from payment gateway " + enquiryResult.getRespMsg());
            System.out.println("PGSearchResponse received from payment gateway  " + enquiryResult.getRespMsg());
            List<Enquiry> enqList = enquiryResult.getEnquiry();
            if (enqList != null && !enqList.isEmpty()) {
                for (Enquiry enquiry : enqList) {
                    paymentResultMap.put("Response Code", enquiry.getRespCode());
                    paymentResultMap.put("Response Message", enquiry.getRespMsg());
                    paymentResultMap.put("Txn Id", enquiry.getTxnId() == null ? "" : enquiry
                            .getTxnId());
                    paymentResultMap.put("Epg Txn Id", enquiry.getPgTxnId() == null ? "" : enquiry
                            .getPgTxnId());
                    paymentResultMap.put("AuthIdCode", enquiry.getAuthIdCode() == null ? "" : enquiry
                            .getAuthIdCode());
                    paymentResultMap.put("Issuer Ref. No.", enquiry.getRrn() == null ? "" : enquiry.getRrn());
                    paymentResultMap.put("Txn Amount", enquiry.getAmount() == null ? "" : enquiry
                            .getAmount());
                    paymentResultMap.put("Txn Type", enquiry.getTxnType() == null ? "" : enquiry
                            .getTxnType());
                    paymentResultMap.put("Txn Date", enquiry.getTxnDateTime() == null ? "" : enquiry
                            .getTxnDateTime().substring(0, 10));
                }
            }

        } catch (CitruspayException e) {
            logger.debug("There was an exception while trying to search for payment details for payment " + gatewayOrderId, e);
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("hi" + e.getMessage());
            logger.debug("There was an exception while trying to search for payment details for payment " + gatewayOrderId, e);
        }

        return paymentResultMap;

    }

    public static void main(String[] args) {


        Map<String, Object> paymentResultMap = findCitrusPayment("1934755-56691");
//        Map<String, Object> paymentResultMap = findIciciPayment("1936895-17020", "00007518");

        for (Map.Entry<String, Object> stringObjectEntry : paymentResultMap.entrySet()) {
            System.out.println(stringObjectEntry.getKey()  +  "-->"  +  stringObjectEntry.getValue());
            logger.debug(stringObjectEntry.getKey()  +  "-->"  +  stringObjectEntry.getValue());
        }

    }
}
