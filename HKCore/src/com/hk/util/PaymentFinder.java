package com.hk.util;

import com.akube.framework.util.BaseUtils;
import com.citruspay.pg.exception.CitruspayException;
import com.citruspay.pg.model.Enquiry;
import com.citruspay.pg.model.EnquiryCollection;
import com.hk.domain.payment.Payment;
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

    public static Map<String, Object> refundCitrusPayment(Payment payment) {

        Map<String, Object> paymentResultMap = new HashMap<String, Object>();
        String gatewayOrderId = payment.getGatewayOrderId();

        Map<String, Object> paymentSearchMap = new HashMap<String, Object>();
        com.citruspay.pg.util.CitruspayConstant.merchantKey = "26635c9f27d46c139c7feb3e2960ee1b1780ac28";
        paymentSearchMap.put("merchantAccessKey", "6Z1PA7WZEVIRHMGKG1VG");
        paymentSearchMap.put("pgTxnId", payment.getGatewayReferenceId());
        paymentSearchMap.put("transactionId", gatewayOrderId);
        paymentSearchMap.put("RRN", payment.getRrn());
        paymentSearchMap.put("authIdCode", payment.getAuthIdCode());
        paymentSearchMap.put("amount", payment.getAmount());
        paymentSearchMap.put("currencyCode", "INR");
        paymentSearchMap.put("txnType", "R");

        try {
            com.citruspay.pg.model.Refund refund = com.citruspay.pg.model.Refund
                    .create(paymentSearchMap);
            if (refund != null) {
                logger.info("PGSearchResponse received from payment gateway " + refund.getRespMessage());
                System.out.println("PGSearchResponse received from payment gateway  " + refund.getRespMessage());
                    paymentResultMap.put("Response Code", refund.getRespCode());
                    paymentResultMap.put("Response Message", refund.getRespMessage());
                    paymentResultMap.put("Txn Id", refund.getMerTxnId() == null ? "" : refund
                            .getMerTxnId());
                    paymentResultMap.put("Epg Txn Id", refund.getPgTxnId() == null ? "" : refund
                            .getPgTxnId());
                    paymentResultMap.put("AuthIdCode", refund.getAuthIdCode() == null ? "" : refund
                            .getAuthIdCode());
                    paymentResultMap.put("Issuer Ref. No.", refund.getRRN() == null ? "" : refund.getRRN());
                    paymentResultMap.put("Refund Amount", refund.getAmount() == null ? "" : refund
                            .getAmount());
            }

        } catch (CitruspayException e) {
            logger.debug("There was an exception while trying to refund payment " + gatewayOrderId, e);
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("hi" + e.getMessage());
            logger.debug("There was an exception while trying to refund payment " + gatewayOrderId, e);
        }

        return paymentResultMap;

    }

    public static List<Map<String, Object>> findTransactionListIcici(String startDate, String endDate, String merchantId) {
        Map<String, Object> paymentResultMap = new HashMap<String, Object>();
        List<Map<String, Object>> paymentResultMapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> paymentSearchMap = new HashMap<String, Object>();

        com.opus.epg.sfa.java.Merchant oMerchant 	= new com.opus.epg.sfa.java.Merchant();

        try {
            com.opus.epg.sfa.java.PostLib oPostLib	= new PostLib();
            oMerchant.setMerchantTxnSearch(merchantId,startDate,endDate);
            PGSearchResponse    oPgSearchResp=oPostLib.postTxnSearch(oMerchant);
            ArrayList oPgRespArr = oPgSearchResp.getPGResponseObjects();
            System.out.println("PGSearchResponse received from payment gateway:"+ oPgSearchResp.toString());
            logger.error("PGSearchResponse received from payment gateway:"+ oPgSearchResp.toString());
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
                    logger.error("PGResponse object:" + oPgResp.toString());
                    paymentResultMapList.add(paymentResultMap);
                }
            }
            return paymentResultMapList;
        } catch (Exception e) {
                logger.debug("There was an exception while trying to search for payment details ", e);
        }
        return null;
    }


    public static List<Map<String, Object>> findTransactionListCitrus(String startDate, String endDate) {
        Map<String, Object> paymentResultMap = new HashMap<String, Object>();
        List<Map<String, Object>> paymentResultMapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> paymentSearchMap = new HashMap<String, Object>();
        com.citruspay.pg.util.CitruspayConstant.merchantKey = "26635c9f27d46c139c7feb3e2960ee1b1780ac28";
        paymentSearchMap.put("merchantAccessKey", "6Z1PA7WZEVIRHMGKG1VG");
        paymentSearchMap.put("txnStartDate", startDate);
        paymentSearchMap.put("txnEndDate", endDate);
        paymentSearchMap.put("bankName", "ABC BANK");

        try {
            com.citruspay.pg.model.TransactionSearchCollection transactionSearchCollection = com.citruspay.pg.model.TransactionSearch.all(paymentSearchMap);
            System.out.println("Response Code: " + transactionSearchCollection.getRespCode());
            System.out.println("Response Message : " + transactionSearchCollection.getRespMessage());
            System.out.println("No of Transactions : " + transactionSearchCollection.getTransaction().size());
            if (transactionSearchCollection.getTransaction() != null && !transactionSearchCollection.getTransaction().isEmpty()) {
                for (int index = 0; index < transactionSearchCollection.getTransaction().size(); index++) {
                    com.citruspay.pg.model.TransactionSearch transaction = (com.citruspay.pg.model.TransactionSearch) transactionSearchCollection.getTransaction().get(index);
                    paymentResultMap.put("Response Code", transaction.getRespCode());
                    paymentResultMap.put("Response Message", transaction.getRespMessage()== null ? "" : transaction.getRespMessage());
                    paymentResultMap.put("Merchant Txn Id", transaction.getMerchantTxnId() == null ? "" : transaction.getMerchantTxnId());
                    paymentResultMap.put("Txn Id", transaction.getTxnId() == null ? "" : transaction.getTxnId());
                    paymentResultMap.put("Epg Txn Id", transaction.getPgTxnId() == null ? "" : transaction.getPgTxnId());
                    paymentResultMap.put("AuthIdCode", transaction.getAuthIdCode() == null ? "" : transaction.getAuthIdCode());
                    paymentResultMap.put("Issuer Ref. No.", transaction.getRRN() == null ? "" : transaction.getRRN());
                    paymentResultMap.put("TxnType", transaction.getTxnType() == null ? "" : transaction.getTxnType());
                    paymentResultMap.put("Amount", transaction.getAmount() == null ? "" : transaction.getAmount());
                    paymentResultMap.put("Date", transaction.getTxnDateTime() == null ? "" : transaction.getTxnDateTime().substring(0, 10));
                    paymentResultMapList.add(paymentResultMap);
                }
            } else{
                System.out.println("Seems, an Empty collection was returned");
            }
            return paymentResultMapList;
        } catch (CitruspayException e) {
            logger.debug("There was an exception while trying to search for payment details for payment ", e);
            System.out.println(e.getMessage());
        }
        return null;
    }


    public static void main(String[] args) {


//        Map<String, Object> paymentResultMap = findCitrusPayment("1934755-56691");
//        Map<String, Object> paymentResultMap = findIciciPayment("1936895-17020", "00007518");

        List<Map<String, Object>> transactionList = findTransactionListIcici("20130101", "20130102", "00007518");
//        List<Map<String, Object>> transactionList = findTransactionListCitrus("20130101", "20130102");

        for (Map<String, Object> paymentResultMap : transactionList) {
            for (Map.Entry<String, Object> stringObjectEntry : paymentResultMap.entrySet()) {
                System.out.println(stringObjectEntry.getKey()  +  "-->"  +  stringObjectEntry.getValue());
                logger.error(stringObjectEntry.getKey() + "-->" + stringObjectEntry.getValue());
            }

        }


    }
}
