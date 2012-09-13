package com.hk.admin.pact.service.reconciliation;

import com.hk.domain.core.PaymentMode;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 9/6/12
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AdminReconciliationService {

	public void parseExcelForShippingOrder(String excelFilePath, String sheetName, PaymentMode paymentMode) throws Exception;

	public void parseExcelForBaseOrder(String excelFilePath, String sheetName, PaymentMode paymentMode) throws Exception;

}
