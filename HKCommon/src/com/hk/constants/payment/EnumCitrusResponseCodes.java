package com.hk.constants.payment;

/**
 * Created by IntelliJ IDEA.
 * User: USER
 * Date: 12/21/11
 * Time: 1:35 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumCitrusResponseCodes {

    Transaction_Successful("0", "Transaction Successful"),
    Rejected_By_Issuer("1", "Rejected By Issuer/Bank"),
    Rejected_By_Gateway("2", "Rejected By Gateway"),
    Cancelled_By_User("3", "Cancelled By User"),
    TxStatusSuccess("SUCCESS", "Transaction Successful"),
    TxStatusFAIL("FAIL", "Fail Transaction"),
    TxStatusCANCELED("CANCELED", "CANCELED Transaction"),
    TxStatusSESSION_EXPIRED("SESSION_EXPIRED", "SESSION_EXPIRED Transaction"),
    Internal_Processing_Error("PG001", "Internal Processing Error"),
    Required_parameter_missing("WS001", "Required parameter missing"),
    Merchant_is_SSL_type_MOTO_txn_not_allowed("ON006", "Merchant is SSL type. MOTO txn not allowed"),
    Merchant_is_MOTO_type_SSL_txn_not_allowed("ON007", "Merchant is MOTO type.  SSL txn not allowed"),
    NO_RECORDS_FOUND("ON010", "No records found for this search criteria"),
    Invalid_Credentials("ON003", "Invalid Credentials. Invalid Merchant Id or Key"),
    Merchant_Velocity_Limit_Value_Reached("AU022", "Merchant Velocity Limit for Value reached"),
    Merchant_Velocity_Limit_Volume_Reached("AU023", "Merchant Velocity Limit for Volume reached"),
    Duplicate_Merchant_TransactionId("TS005", "Duplicate Merchant Transaction Id"),
    Different_Currency("TS006", "Currency for the Related Txn is different than the Root Currency"),
    Related_Txn_NotAllowed("TS009", "Related Txn not allowed"),
    Duplicate_Request_Txn_InProgress("TS010", "Duplicate Request:related txn already in progress"),
    Cannot_Process_Root("TS011", "Can Not Process Related txn: root txn is in progress"),
    MerchantId_NOT_SAME("TS013", "MerchantId of Root and Related transactions are not same"),
    Root_Transaction_NotFound("TS014", "Root Transaction Not Found for the Given Txn Id"),
    Root_Transaction_Unsuccessful("TS015", "Can Not Process Related Txn. Root Transaction Was Not Succesful"),
    Different_AuthId_Code("TS016", "Can Not Process Related Txn. AuthId Code is Different"),
    Different_RRN("TS017", "Can Not Process Related Txn. RRN is Different"),
    Related_Txn_Amount_Greater_Than_Original("TS018", "Related Txn Amount Cannot be greater than original Txn Amount"),
    Original_Txn_Cancelled("TS019", "Related Txn Not Allowed As Original Txn is Cancelled"),
    Only_Partial_Refunds_Allowed("TS020", "Only Partial Refunds Allowed"),
    Success("200", "Success"),
    Unauthorized_User("401", "Unauthorized user"),
    Bad_Request("400", "Bad Request"),;

    private java.lang.String name;
    private java.lang.String id;

    EnumCitrusResponseCodes(java.lang.String id, java.lang.String name) {
        this.name = name;
        this.id = id;
    }

    public java.lang.String getName() {
        return name;
    }

    public java.lang.String getId() {
        return id;
    }

}
