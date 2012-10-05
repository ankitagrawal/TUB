package com.hk.admin.pact.service.reconciliation;

import java.util.Date;
import java.util.List;

import com.hk.domain.courier.Courier;
import com.hk.domain.order.OrderPaymentReconciliation;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 9/6/12
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AdminReconciliationService {

	public void parseExcelForShippingOrder(String excelFilePath, String sheetName, String paymentProcess) throws Exception;

	public void parseExcelForBaseOrder(String excelFilePath, String sheetName, String paymentProcess) throws Exception;

	public List<OrderPaymentReconciliation> findPaymentDifferenceInCODOrders(Long shippingOrderId, String gatewayOrderId, Date startDate, Date endDate, Courier courier, String paymentProcess) throws Exception;

	public List<OrderPaymentReconciliation> findPaymentDifferenceInPrepaidOrders(Long baseOrderId, String gatewayOrderId, Date startDate, Date endDate, String paymentProcess) throws Exception;

}
