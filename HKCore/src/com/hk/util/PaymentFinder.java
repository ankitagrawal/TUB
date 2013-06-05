package com.hk.util;

import com.akube.framework.util.BaseUtils;
import com.citruspay.pg.exception.CitruspayException;
import com.citruspay.pg.model.Enquiry;
import com.citruspay.pg.model.EnquiryCollection;
import com.hk.domain.payment.Payment;
import com.hk.manager.payment.CitrusPaymentGatewayWrapper;
import com.hk.manager.payment.EbsPaymentGatewayWrapper;
import com.opus.epg.sfa.java.PGResponse;
import com.opus.epg.sfa.java.PGSearchResponse;
import com.opus.epg.sfa.java.PostLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;


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


    public static Map<String, Object> findEbsTransaction(String gatewayOrderId, String txnPaymentId, String txnAmount, String action) {
        Map<String, Object> paymentResultMap = new HashMap<String, Object>();
        //Instantiate an HttpClient
        HttpClient client = new HttpClient();
        String url = EbsPaymentGatewayWrapper.EBS_TXN_URL;

        //Instantiate a GET HTTP method
        PostMethod method = new PostMethod(url);
        method.setRequestHeader("Content-type", "text/xml; charset=ISO-8859-1");


        //Define name-value pairs to set into the QueryString
        NameValuePair nvp1 = new NameValuePair(EbsPaymentGatewayWrapper.TXN_SECRET_KEY, "5b436e8c7edd4411eecd21cad20539b4");
        NameValuePair nvp2 = new NameValuePair(EbsPaymentGatewayWrapper.TXN_ACTION, action);
        NameValuePair nvp4 = new NameValuePair(EbsPaymentGatewayWrapper.TXN_REF_NO, gatewayOrderId);
        NameValuePair nvp5 = new NameValuePair(EbsPaymentGatewayWrapper.PaymentID, txnPaymentId);
        NameValuePair nvp3 = new NameValuePair(EbsPaymentGatewayWrapper.Amount, txnAmount);
        //   Account id should be the last parameter
        NameValuePair nvp6 = new NameValuePair(EbsPaymentGatewayWrapper.TXN_ACCOUNT_ID, "10258");

        try {
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
            Iterator elementListIterator = xmlElementList.listIterator();
            while (elementListIterator.hasNext()) {
                Element ele = (Element) elementListIterator.next();
                String trnsid = ele.getAttributeValue(EbsPaymentGatewayWrapper.TXN_TRANSACTION_ID);
                String paymentId = ele.getAttributeValue(EbsPaymentGatewayWrapper.TXN_PAYMENT_ID);
                String amount = ele.getAttributeValue(EbsPaymentGatewayWrapper.amount);
                String dateTime = ele.getAttributeValue(EbsPaymentGatewayWrapper.TXN_DATETIME);
                String mode = ele.getAttributeValue(EbsPaymentGatewayWrapper.mode);
                String referenceNo = ele.getAttributeValue(EbsPaymentGatewayWrapper.TXN_REFERENCE_NO);
                String transactionType = ele.getAttributeValue(EbsPaymentGatewayWrapper.TXN_TRANSACTION_TYPE);
                String status = ele.getAttributeValue(EbsPaymentGatewayWrapper.TXN_STATUS);
                String isFlagged = ele.getAttributeValue(EbsPaymentGatewayWrapper.TXN_IS_FLAGGED);
                String errorCode = ele.getAttributeValue(EbsPaymentGatewayWrapper.TXN_ERROR_CODE);
                String errorMessage = ele.getAttributeValue(EbsPaymentGatewayWrapper.TXN_ERROR_MSG);
                if (paymentId != null) {
                    paymentResultMap.put(EbsPaymentGatewayWrapper.TXN_TRANSACTION_ID, trnsid);
                    paymentResultMap.put(EbsPaymentGatewayWrapper.TXN_PAYMENT_ID, paymentId);
                    paymentResultMap.put(EbsPaymentGatewayWrapper.amount, amount);
                    paymentResultMap.put(EbsPaymentGatewayWrapper.TXN_DATETIME, dateTime);
                    paymentResultMap.put(EbsPaymentGatewayWrapper.mode, mode);
                    paymentResultMap.put(EbsPaymentGatewayWrapper.TXN_REFERENCE_NO, referenceNo);
                    paymentResultMap.put(EbsPaymentGatewayWrapper.TXN_TRANSACTION_TYPE, transactionType);
                    paymentResultMap.put(EbsPaymentGatewayWrapper.TXN_STATUS, status);
                    paymentResultMap.put(EbsPaymentGatewayWrapper.TXN_IS_FLAGGED, isFlagged);
                } else {
                    paymentResultMap.put(EbsPaymentGatewayWrapper.TXN_ERROR_CODE, errorCode);
                    paymentResultMap.put(EbsPaymentGatewayWrapper.TXN_ERROR_MSG, errorMessage);
                }

            }
        } catch (IOException e) {
            logger.debug(" Exception  while sending the request to Ebs Gateway " + gatewayOrderId, e);
        }
        catch (Exception e) {
            logger.debug(" Exception  while trying to search for payment details for EBS  payment " + gatewayOrderId, e);
        }

        finally {
            //release connection
            method.releaseConnection();
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

        com.opus.epg.sfa.java.Merchant oMerchant = new com.opus.epg.sfa.java.Merchant();

        try {
            com.opus.epg.sfa.java.PostLib oPostLib = new PostLib();
            oMerchant.setMerchantTxnSearch(merchantId, startDate, endDate);
            PGSearchResponse oPgSearchResp = oPostLib.postTxnSearch(oMerchant);
            ArrayList oPgRespArr = oPgSearchResp.getPGResponseObjects();
            System.out.println("PGSearchResponse received from payment gateway:" + oPgSearchResp.toString());
            logger.error("PGSearchResponse received from payment gateway:" + oPgSearchResp.toString());
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
                    paymentResultMap.put("Response Message", transaction.getRespMessage() == null ? "" : transaction.getRespMessage());
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
            } else {
                System.out.println("Seems, an Empty collection was returned");
            }
            return paymentResultMapList;
        } catch (CitruspayException e) {
            logger.debug("There was an exception while trying to search for payment details for payment ", e);
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static Map<String,Object> refundIciciPayment(Payment payment,String merchantId){
        Map<String,Object> paymentResultMap = new HashMap<String,Object>();
        com.opus.epg.sfa.java.Merchant oMerchant 	= new com.opus.epg.sfa.java.Merchant();
        try{
            PostLib oPostLib	= new PostLib();
            oMerchant.setMerchantRelatedTxnDetails(merchantId,null,null,
                    payment.getGatewayOrderId(),null,payment.getRrn(),
                    payment.getAuthIdCode(),null,null,
                    "INR","Refund",payment.getAmount().toString(),null,null,
                    null,null,null,null);

            PGResponse oPgResp = oPostLib.postRelatedTxn(oMerchant);
            if(oPgResp != null){
                // logger.error("PGSearchResponse received from payment gateway:" + PGResponse.toString());
                paymentResultMap.put("Response Code",oPgResp.getRespCode());
                paymentResultMap.put("Response Message",oPgResp.getRespMessage());
                paymentResultMap.put("Txn Id",oPgResp.getTxnId());
                paymentResultMap.put("Epg Txn Id",oPgResp.getEpgTxnId());
                paymentResultMap.put("AuthIdCode",oPgResp.getAuthIdCode());
                paymentResultMap.put("RRN",oPgResp.getRRN());
                //paymentResultMap.put("Refund Amount","");
            }

        }catch(Exception e){
            logger.debug("There is an error while processing refund from ICICI"+payment.getGatewayOrderId(),e);
            System.out.println(e.getMessage());
        }
        return paymentResultMap;
    }


    public static void main(String[] args) {


//        Map<String, Object> paymentResultMap = findCitrusPayment("1934755-56691");
//        Map<String, Object> paymentResultMap = findIciciPayment("1936895-17020", "00007518");

        List<Map<String, Object>> transactionList = findTransactionListIcici("20130101", "20130102", "00007518");
//        List<Map<String, Object>> transactionList = findTransactionListCitrus("20130101", "20130102");

        for (Map<String, Object> paymentResultMap : transactionList) {
            for (Map.Entry<String, Object> stringObjectEntry : paymentResultMap.entrySet()) {
                System.out.println(stringObjectEntry.getKey() + "-->" + stringObjectEntry.getValue());
                logger.error(stringObjectEntry.getKey() + "-->" + stringObjectEntry.getValue());
            }

        }


    }
}
