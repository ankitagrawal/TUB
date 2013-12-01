package com.hk.manager.payment;

import com.CheckSumResponseBean;
import com.TPSLUtil;
import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.service.PaymentGatewayWrapper;
import com.hk.exception.HealthkartPaymentGatewayException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class TekprocessTestPaymentGatewayWrapper extends BasePaymentGatewayWrapper<CCAvenueDummyPaymentGatewayWrapper> implements PaymentGatewayWrapper {
	private static Logger logger = LoggerFactory.getLogger(TekprocessTestPaymentGatewayWrapper.class);

	public static String merchantCode = "T1441";
	public static String param_msg = "msg";

	public static String key_MerchantCode = "MerchantCode";
	public static String key_CustomerID = "CustomerID";
	public static String key_TxnReferenceNo = "TxnReferenceNo";
	public static String key_BankReferenceNo = "BankReferenceNo";
	public static String key_TxnAmount = "TxnAmount";
	public static String key_BankID = "BankID";
	public static String key_BankMerchantID = "BankMerchantID";
	public static String key_TxnType = "TxnType";
	public static String key_CurrencyName = "CurrencyName";
	public static String key_ItemCode = "ItemCode";
	public static String key_SecurityType = "SecurityType";
	public static String key_SecurityID = "SecurityID";
	public static String key_SecurityPassword = "SecurityPassword";
	public static String key_TxnDate = "TxnDate";
	public static String key_AuthStatus = "AuthStatus";
	public static String key_SettlementType = "SettlementType";
	public static String key_AdditionalInfo1 = "AdditionalInfo1";
	public static String key_AdditionalInfo2 = "AdditionalInfo2";
	public static String key_AdditionalInfo3 = "AdditionalInfo3";
	public static String key_AdditionalInfo4 = "AdditionalInfo4";
	public static String key_AdditionalInfo5 = "AdditionalInfo5";
	public static String key_AdditionalInfo6 = "AdditionalInfo6";
	public static String key_AdditionalInfo7 = "AdditionalInfo7";
	public static String key_ErrorStatus = "ErrorStatus";
	public static String key_ErrorDescription = "ErrorDescription";
	public static String key_CheckSum = "CheckSum";

	public static String authStatus_Success = "0300";
	public static String authStatus_Fail = "0399";
	public static String authStatus_PendingApproval = "0000";

	@SuppressWarnings("unused")
    private String url;

	/**
	 * Response Msg :
	 * <br/>
	 * merchantCode|CustomerID|TxnReferenceNo|BankReferenceNo|TxnAmount|BankID|BankMerchantID|TxnType|CurrencyName|ItemCode
	 * |SecurityType|SecurityID|SecurityPassword|TxnDate|AuthStatus|SettlementType|AdditionalInfo1|AdditionalInfo2
	 * |AdditionalInfo3|AdditionalInfo4|AdditionalInfo5|AdditionalInfo6|AdditionalInfo7|ErrorStatus|ErrorDescription|CheckSum
	 *
	 * @param msg
	 * @return
	 */
	public static Map<String, String> parseResponse(String msg) {
		Map<String, String> paramMap = new HashMap<String, String>();
		String[] tokens = StringUtils.split(msg, "|");
		paramMap.put(key_MerchantCode, tokens[0]);
		paramMap.put(key_TxnReferenceNo, tokens[1]);
		paramMap.put(key_CustomerID, tokens[2]);
		paramMap.put(key_BankReferenceNo, tokens[3]);
		paramMap.put(key_TxnAmount, tokens[4]);
		paramMap.put(key_BankID, tokens[5]);
		paramMap.put(key_BankMerchantID, tokens[6]);
		paramMap.put(key_TxnType, tokens[7]);
		paramMap.put(key_CurrencyName, tokens[8]);
		paramMap.put(key_ItemCode, tokens[9]);
		paramMap.put(key_SecurityType, tokens[10]);
		paramMap.put(key_SecurityID, tokens[11]);
		paramMap.put(key_SecurityPassword, tokens[12]);
		paramMap.put(key_TxnDate, tokens[13]);
		paramMap.put(key_AuthStatus, tokens[14]);
		paramMap.put(key_SettlementType, tokens[15]);
		paramMap.put(key_AdditionalInfo1, tokens[16]);
		paramMap.put(key_AdditionalInfo2, tokens[17]);
		paramMap.put(key_AdditionalInfo3, tokens[18]);
		paramMap.put(key_AdditionalInfo4, tokens[19]);
		paramMap.put(key_AdditionalInfo5, tokens[20]);
		paramMap.put(key_AdditionalInfo6, tokens[21]);
		paramMap.put(key_AdditionalInfo7, tokens[22]);
		paramMap.put(key_ErrorStatus, tokens[23]);
		paramMap.put(key_ErrorDescription, tokens[24]);
		paramMap.put(key_CheckSum, tokens[25]);
		return paramMap;
	}

	public static void main(String[] args) {
		String msg = "T1441|1-66480|16516045|NA|485.00|470|NA|NA|INR|NA|NA|NA|NA|03-05-2011 03:57:29|0300|NA|T1441|1|NA|NA|NA|NA|NA|NA|NA|471056122490";
		Map<String, String> map = parseResponse(msg);
//    System.out.println("map = "+map);
	}

	public String getGatewayUrl() {
		return "https://www.tekprocess.co.in/PaymentGateway/TransactionRequest.jsp";
	}

	public static void verifyChecksum(String msg, String propertyFilePath) throws HealthkartPaymentGatewayException {
		CheckSumResponseBean checkSumResponseBean = new CheckSumResponseBean();
		checkSumResponseBean.setStrMSG(msg);
		checkSumResponseBean.setStrPropertyPath(propertyFilePath);
		TPSLUtil util = new TPSLUtil();
		String strCheckSumValue = util.transactionResponseMessage(checkSumResponseBean);

		Map<String, String> paramMap = TekprocessTestPaymentGatewayWrapper.parseResponse(msg);
		String checksumFromMap = paramMap.get(TekprocessTestPaymentGatewayWrapper.key_CheckSum);

		logger.info("checksum as extracted from map = " + checksumFromMap);
		logger.info("checksum as calculated = " + strCheckSumValue);

		if (!strCheckSumValue.equals(checksumFromMap)) {
			throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.CHECKSUM_MISMATCH);
		}
	}

	public void setGatewayUrl(String url) {
		this.url = url;
	}

}
