package com.hk.admin.pact.service.reconciliation;

import com.hk.domain.core.PaymentMode;
import com.hk.domain.courier.Courier;
import com.hk.domain.order.OrderPaymentReconciliation;
import com.hk.domain.order.ShippingOrder;

import java.util.Date;
import java.util.List;

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

	public List<OrderPaymentReconciliation> findPaymentDifferenceInCODOrders(Long shippingOrderId, String gatewayOrderId, Date startDate, Date endDate, Courier courier);

	public List<OrderPaymentReconciliation> findPaymentDifferenceInPrepaidOrders(Long baseOrderId, String gatewayOrderId, Date startDate, Date endDate);

}
