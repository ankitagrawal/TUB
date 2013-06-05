package com.hk.constants.payment;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Shakti Singh
 * Date: 5/16/13
 * Time: 3:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class GatewayResponseKeys {

    public enum HKConstants{

        GATEWAY_ORDER_ID("gatewayOrderId"),
        Amount("amount"),
        GATEWAY_REFERENCE_ID("gatewayReferenceId"),
        RESPONSE_MSG("responseMsg"),
        AUTH_ID_CODE("authIdCode"),
        ERROR_LOG("errorLog"),
        ROOT_REFER_NO("rootReferenceNumber"),
        TXN_STATUS("transactionStatus"),
        RETURN_URL("returnUrl"),
        CANCEL_URL("cancelUrl"),
        PAYMENT_DATE("paymentDate"),
        SUCCESS("Success"),
        FAILED("Failure"),
        ERROR("Error"),
        RESPONSE_CODE("respCode"),
        AUTH_PEND("Authorization Pending");


        private String key;

        private HKConstants(String key){
            this.key = key;
        }

        public String getKey(){
            return this.key;
        }
    }

    public class HK {
        public static final String gatewayOrderId = "gatewayOrderId";
        public static final String amount = "amount";
        public static final String gatewayReferenceId = "gatewayReferenceId";
        public static final String responseMsg = "responseMsg";
        public static final String authIdCode = "authIdCode";
        public static final String errorLog = "errorLog";
        public static final String rootReferenceNumber = "rootReferenceNumber";
        public static final String transactionStatus = "transactionStatus";
        public static final String returnUrl = "returnUrl";
        public static final String cancelUrl = "cancelUrl";
        public static final String paymentDate = "paymentDate";
        public static final String success = "success";
        public static final String failed= "failed";
    }

    public enum CitrusConstants{
        RESPONSE_CODE("RespCode"),
        RESPONSE_MSG("respMsg"),
        TAX_ID("txnId"),
        TAX_REF_NO("TxRefNo"),
        PG_TAX_NO("pgTxnNo"),
        TX_STATUS("TxStatus"),
        PG_RESP_CODE("pgRespCode"),
        TX_MSG("TxMsg"),
        AUTH_ID_CODE("authCode"),
        AMOUNT("amount"),
        MANDATORY_ERR_MSG("mandatoryErrorMsg"),
        PAID_TXN_EXISTS("paidTxnExists"),
        RRN("rrn"),
        PG_TAX_ID("pgTxnId"),
        TXN_ID("transactionId"),
        MERCHANT_ACCESS_KEY("merchantAccessKey"),
        CURRENCY_CODE("currencyCode"),
        TXN_TYPE("txnType"),
        KEY("key"),
        TXN_DATE_TIME("txnDateTime"),
        REFUND_KEY("R"),
        SUCCESS_MSG("SUCCESS"),
        SUCCESS_CODE("0"),
        REJECTED_BY_ISSUER("1"),
        REJECTED_BY_GATEWAY("2"),
        CANCELLED_BY_USER("3"),
        FAIL("FAIL"),
        REFUND_SUCCESS_CODE("0"),
        MANDATORY_FIELD_MISSING_COD("400"),
        GOOD_ENQ_COD("200"),
        BAD_ENQ_COD("401"),
        INR("INR"),
        REFUND_SEEK_SUCCESS_CODE("11"),
        ;

        private String key;

        private CitrusConstants(String key){
            this.key = key;
        }

        public String getKey(){
            return this.key;
        }
    }



    public class Citrus{

        public static final String responseCode = "Response Code";
        public static final String responseMessage = "Response Message";
        public static final String TxId = "TxId";
        public static final String TxRefNo = "TxRefNo";
        public static final String pgTxnNo = "pgTxnNo";
        public static final String TxStatus = "TxStatus";
        public static final String pgRespCode = "pgRespCode";
        public static final String TxMsg = "TxMsg";
        public static final String authIdCode = "authIdCode";
        public static final String amount = "Txn Amount";
        public static final String mandatoryErrorMsg = "mandatoryErrorMsg";
        public static final String paidTxnExists = "paidTxnExists";
        public static final String rrn = "RRN";
        public static final String pgTxnId="pgTxnId";
        public static final String transactionId="transactionId";
        public static final String merchantAccessKey="merchantAccessKey";
        public static final String currencyCode="currencyCode";
        public static final String txnType="txnType";
        public static final String key="key";

        public static final String SuccessMsg = "SUCCESS";
        public static final String SuccessCode = "SUCCESS";
        public static final String FAIL = "FAIL";
    }

    public class TechProcess {
        public static final String merchantCode = "L1340";
        public static final String param_msg = "msg";

        public static final String key_MerchantCode = "MerchantCode";
        public static final String key_CustomerID = "CustomerID";
        public static final String key_TxnReferenceNo = "TxnReferenceNo";
        public static final String key_BankReferenceNo = "BankReferenceNo";
        public static final String key_TxnAmount = "TxnAmount";
        public static final String key_BankID = "BankID";
        public static final String key_BankMerchantID = "BankMerchantID";
        public static final String key_TxnType = "TxnType";
        public static final String key_CurrencyName = "CurrencyName";
        public static final String key_ItemCode = "ItemCode";
        public static final String key_SecurityType = "SecurityType";
        public static final String key_SecurityID = "SecurityID";
        public static final String key_SecurityPassword = "SecurityPassword";
        public static final String key_TxnDate = "TxnDate";
        public static final String key_AuthStatus = "AuthStatus";
        public static final String key_SettlementType = "SettlementType";
        public static final String key_AdditionalInfo1 = "AdditionalInfo1";
        public static final String key_AdditionalInfo2 = "AdditionalInfo2";
        public static final String key_AdditionalInfo3 = "AdditionalInfo3";
        public static final String key_AdditionalInfo4 = "AdditionalInfo4";
        public static final String key_AdditionalInfo5 = "AdditionalInfo5";
        public static final String key_AdditionalInfo6 = "AdditionalInfo6";
        public static final String key_AdditionalInfo7 = "AdditionalInfo7";
        public static final String key_ErrorStatus = "ErrorStatus";
        public static final String key_ErrorDescription = "ErrorDescription";
        public static final String key_CheckSum = "CheckSum";

        public static final String authStatus_Success = "0300";
        public static final String authStatus_Fail = "0399";
        public static final String authStatus_PendingApproval = "0000";
    }

    public class Paypal {
        public static final String token = "token";
        public static final String payerID = "PayerID";
        public static final String gateway = "gateway";
        public static final String amount = "amount";
        public static final String responseAmount = "PAYMENTINFO_0_AMT";
        public static final String correlationId = "CORRELATIONID";
        public static final String ack = "ACK";
        public static final String errorCode = "L_ERRORCODE0";
        public static final String errorShortMsg = "L_SHORTMESSAGE0";
        public static final String errorLongMsg = "L_LONGMESSAGE0";
        public static final String paymentStatus = "PAYMENTINFO_0_PAYMENTSTATUS";
        public static final String pendingReason = "PAYMENTINFO_0_PENDINGREASON";

        public static final String Success_Ack = "Success";
        public static final String Success_with_Warning_Ack = "SuccessWithWarning";
        public static final String Payment_Completed_Status = "Completed";
        public static final String Payment_Pending_Status = "Pending";
        public static final String Payment_Pending_Reason = "echeck";

    }

    public enum EbsConstants{

        ENCRYPTED_DATA("reqResParameter"),
        RESP_CODE("ResponseCode"),
        RESP_MSG("ResponseMessage"),
        IS_FLAGGED("isFlagged"),
        IS_FLAGGED_TRUE("YES"),
        IS_FLAGGED_FALSE("NO"),
        AUTH_STATUS_SUCCESS("0"),
        AMOUNT("amount"),
        GATEWAY_SECRET_KEY("secret_key"),
        ACCOUNT_ID("account_id") ,
        TXN_TRANSACTION_ID("transactionId"),
        TXN_PAYMENT_ID("paymentId"),
        TXN_STATUS("status"),
        TXN_ERROR_CODE("errorCode"),
        TXN_ERROR_MSG("error");


        private String key;

        private EbsConstants(String key){
            this.key = key;
        }

        public String getKey(){
            return this.key;
        }

    }
    public class Ebs {
        public static final String encryptedData = "reqResParameter";
        public static final String responseCode = "ResponseCode";
        public static final String responseMessage = "ResponseMessage";
        public static final String isFlagged = "IsFlagged";
        public static final String isFlaggedTrue = "YES";
        public static final String isFlaggedFalse = "NO";
        public static final String authStatusSuccess = "0";
        public static final String amount = "amount";
    }

    public class CCAvenue {
        public static final String param_AuthDesc = "AuthDesc";
        public static final String AuthDesc_Success = "Y";
        public static final String AuthDesc_PendingApproval = "B";
        public static final String AuthDesc_Fail = "N";
    }

    public enum IciciConstants{
        MERCHANT_ID("MerchantId"),
        CURR_CODE("CurrCode"),
        REFUND("Refund"),
        RESP_CODE("Response Code"),
        RESP_MSG("Response Message"),
        MERCHANT_TXN_ID("Merchant Txn Id"),
        EPG_TXN_ID("Epg Txn Id"),
        AUTH_ID_CODE("AuthId Code"),
        RRN("rrn"),
        CV_RESP_CODE("cvRespCode"),
        FDMS_SCORE("FDMS Score"),
        FDMS_RESULT("FDMS Result"),
        COOKIE("Cookie"),

        TXN_SUCCESSFUL("0"),
        REJECTED_BY_ISSUER("1"),
        REJECTED_BY_GATEWAY("2");

        private String key;

        private IciciConstants(String key){
            this.key = key;
        }

        public String getKey(){
            return this.key;
        }
    }

    public class Icici{
        public static final String merchantId="MerchantId";
        public static final String currCode = "CurrCode";
        public static final String refund = "Refund";
        public static final String responseCode = "Response Code";
        public static final String responseMessage = "Response Message";
        public static final String merchantTxnId = "Merchant Txn Id";
        public static final String epgTxnId = "Epg Txn Id";
        public static final String authIdCode = "AuthId Code";
        public static final String rrn = "RRN";
        public static final String cvRespCode = "cvRespCode";
        public static final String fdmsScore = "FDMS Score";
        public static final String fdmsResult = "FDMS Result";
        public static final String cookie = "Cookie";

        public static final String txn_successful = "0";
        public static final String rejected_by_switch = "1";
        public static final String rejected_by_gateway = "2";

    }

}
