package com.hk.admin.impl.service.reconciliation;

import com.hk.admin.pact.dao.reconciliation.AdminReconciliationDao;
import com.hk.admin.pact.service.reconciliation.AdminReconciliationService;
import com.hk.admin.util.XslUtil;
import com.hk.constants.XslConstants;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.order.Order;
import com.hk.domain.order.OrderPaymentReconciliation;
import com.hk.domain.order.ShippingOrder;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 9/6/12
 * Time: 2:13 PM
 * To change this template use File | Settings | File Templates.
 */

@Service
public class AdminReconciliationServiceImpl implements AdminReconciliationService {
	private static Logger logger = LoggerFactory.getLogger(AdminReconciliationServiceImpl.class);
	@Autowired
	private ShippingOrderDao shippingOrderDao;
	@Autowired
	AdminReconciliationDao adminReconciliationDao;
	@Autowired
	OrderDao orderDao;

	public void parseExcelForShippingOrder(String excelFilePath, String sheetName, PaymentMode paymentMode) throws Exception {
		List<OrderPaymentReconciliation> orderPaymentReconciliationList = new ArrayList<OrderPaymentReconciliation>();
		ExcelSheetParser parser = new ExcelSheetParser(excelFilePath, sheetName);
		Iterator<HKRow> rowIterator = parser.parse();
		int rowCount = 1;
		try {
			while (rowIterator.hasNext()) {
				HKRow row = rowIterator.next();
				String gatewayOrderId = row.getColumnValue(XslConstants.GATEWAY_ORDER_ID);
				String reconciled = row.getColumnValue(XslConstants.RECONCILED);
				Double amount = XslUtil.getDouble(row.getColumnValue(XslConstants.AMOUNT));
				if (!reconciled.equalsIgnoreCase("N") && !reconciled.equalsIgnoreCase("Y")) {
					throw new Exception("Reconciliation status should be 'Y' or 'N' only. ");
				}
				ShippingOrder shippingOrder = getShippingOrderDao().findByGatewayOrderId(gatewayOrderId);
				if (shippingOrder == null) {
					throw new Exception("Gateway order Id does not exist");
				}

				if (amount == null || amount.doubleValue() != shippingOrder.getAmount().doubleValue()) {
					throw new Exception("Reconciliation Amount does not match with the actual shipping order amount");
				}

				OrderPaymentReconciliation orderPaymentReconciliation = getAdminReconciliationDao().getOrderPaymentReconciliationBySO(shippingOrder);
				if (orderPaymentReconciliation == null) {
					orderPaymentReconciliation = createOrderPaymentReconciliation(null, shippingOrder, paymentMode, amount, reconciled);
				} else {
					orderPaymentReconciliation.setReconciled(reconciled.equalsIgnoreCase("Y"));
					orderPaymentReconciliation.setReconciledAmount(amount);
				}

				orderPaymentReconciliationList.add(orderPaymentReconciliation);


				rowCount++;
				logger.debug("OrderPaymentReconciliation saved for Shipping Order: " + shippingOrder);

			}
		} catch (Exception e) {
			logger.error("Exception @ Row:" + (rowCount + 1) + e.getMessage());
			throw new Exception("Exception @ Row:" + (rowCount + 1) + ": " + e.getMessage(), e);
		}

		getAdminReconciliationDao().saveOrUpdate(orderPaymentReconciliationList);
	}

	public void parseExcelForBaseOrder(String excelFilePath, String sheetName, PaymentMode paymentMode) throws Exception {
		List<OrderPaymentReconciliation> orderPaymentReconciliationList = new ArrayList<OrderPaymentReconciliation>();
		ExcelSheetParser parser = new ExcelSheetParser(excelFilePath, sheetName);
		Iterator<HKRow> rowIterator = parser.parse();
		int rowCount = 1;
		try {
			while (rowIterator.hasNext()) {
				HKRow row = rowIterator.next();
				String gatewayOrderId = row.getColumnValue(XslConstants.GATEWAY_ORDER_ID);
				String reconciled = row.getColumnValue(XslConstants.RECONCILED);
				Double amount = XslUtil.getDouble(row.getColumnValue(XslConstants.AMOUNT));
				if (!reconciled.equalsIgnoreCase("N") && !reconciled.equalsIgnoreCase("Y")) {
					throw new Exception("Reconciliation status should be 'Y' or 'N' only. ");
				}
				Order order = getOrderDao().findByGatewayOrderId(gatewayOrderId);
				if (order == null) {
					throw new Exception("Gateway order Id does not exist");
				}

				if (amount == null || amount.doubleValue() != order.getAmount().doubleValue()) {
					throw new Exception("Reconciliation Amount does not match with the actual shipping order amount");
				}

				OrderPaymentReconciliation orderPaymentReconciliation = getAdminReconciliationDao().getOrderPaymentReconciliationByBaseOrder(order);
				if (orderPaymentReconciliation == null) {
					orderPaymentReconciliation = createOrderPaymentReconciliation(order, null, paymentMode, amount, reconciled);

					orderPaymentReconciliationList.add(orderPaymentReconciliation);

					for (ShippingOrder shippingOrder : order.getShippingOrders()) {
						orderPaymentReconciliation = createOrderPaymentReconciliation(null, shippingOrder, paymentMode, shippingOrder.getAmount(), reconciled);
						orderPaymentReconciliationList.add(orderPaymentReconciliation);
					}

				} else {
					orderPaymentReconciliation.setReconciled(reconciled.equalsIgnoreCase("Y"));
					orderPaymentReconciliation.setReconciledAmount(amount);
					orderPaymentReconciliationList.add(orderPaymentReconciliation);
					for (ShippingOrder shippingOrder : order.getShippingOrders()) {
						orderPaymentReconciliation = getAdminReconciliationDao().getOrderPaymentReconciliationBySO(shippingOrder);
						orderPaymentReconciliation.setReconciled(reconciled.equalsIgnoreCase("Y"));
						orderPaymentReconciliation.setReconciledAmount(amount);
						orderPaymentReconciliationList.add(orderPaymentReconciliation);
					}
				}

				rowCount++;
				logger.debug("OrderPaymentReconciliation saved for Base Order: " + order);

			}
		} catch (Exception e) {
			logger.error("Exception @ Row:" + (rowCount + 1) + e.getMessage());
			throw new Exception("Exception @ Row:" + (rowCount + 1) + ": " + e.getMessage(), e);
		}
		getAdminReconciliationDao().saveOrUpdate(orderPaymentReconciliationList);
	}

	/*public void insertOrderPaymentReconciliationForSO(String excelFilePath, String sheetName, PaymentMode paymentMode) {

		ExcelSheetParser parser = new ExcelSheetParser(excelFilePath, sheetName);
				Iterator<HKRow> rowIterator = parser.parse();
				int rowCount = 1;
				try {
					while (rowIterator.hasNext()) {
						HKRow row = rowIterator.next();
						String gatewayOrderId = row.getColumnValue(XslConstants.GATEWAY_ORDER_ID);
						String reconciled = row.getColumnValue(XslConstants.RECONCILED);
						Double amount = XslUtil.getDouble(row.getColumnValue(XslConstants.AMOUNT));
						ShippingOrder shippingOrder = getShippingOrderDao().findByGatewayOrderId(gatewayOrderId);
						if (shippingOrder == null) {
							throw new Exception("Gateway order Id does not exist");
						}

						if (amount == null || amount.doubleValue() != shippingOrder.getAmount().doubleValue()) {
							throw new Exception("Reconciliation Amount does not match with the actual shipping order amount");
						}
						mapSOReconciled.put(shippingOrder, reconciled);

						rowCount++;
						logger.debug("OrderPaymentReconciliation saved for Shipping Order: " + shippingOrder);

					}
				} catch (Exception e) {
					logger.error("Exception @ Row:" + (rowCount + 1) + e.getMessage());
					throw new Exception("Exception @ Row:" + (rowCount + 1) + ": " + e.getMessage(), e);
				}

		List<OrderPaymentReconciliation> orderPaymentReconciliationList = new ArrayList<OrderPaymentReconciliation>();
		OrderPaymentReconciliation orderPaymentReconciliation = null;
		for (ShippingOrder shippingOrder : mapSOReconciled.keySet()) {
			String reconciled = mapSOReconciled.get(shippingOrder);
			orderPaymentReconciliation = getAdminReconciliationDao().getOrderPaymentReconciliationBySO(shippingOrder);
										if (orderPaymentReconciliation == null) {
											orderPaymentReconciliation = new OrderPaymentReconciliation();
											orderPaymentReconciliation.setBaseOrder(shippingOrder.getBaseOrder());
											orderPaymentReconciliation.setShippingOrder(shippingOrder);
											orderPaymentReconciliation.setPaymentMode(paymentMode);
										}
										orderPaymentReconciliation.setReconciled(reconciled.equalsIgnoreCase("Y"));
										orderPaymentReconciliation.setReconciledAmount(amount);
						orderPaymentReconciliationList.add(orderPaymentReconciliation);

		}

		getAdminReconciliationDao().saveOrUpdate(orderPaymentReconciliationList);

	}
*/
	private OrderPaymentReconciliation createOrderPaymentReconciliation(Order order, ShippingOrder shippingOrder, PaymentMode paymentMode, Double amount, String reconciled) {
		OrderPaymentReconciliation orderPaymentReconciliation = new OrderPaymentReconciliation();
		if (order != null) {
			orderPaymentReconciliation.setBaseOrder(order);
		}
		if (shippingOrder != null) {
			orderPaymentReconciliation.setShippingOrder(shippingOrder);
			orderPaymentReconciliation.setBaseOrder(shippingOrder.getBaseOrder());
		}
		orderPaymentReconciliation.setPaymentMode(paymentMode);
		orderPaymentReconciliation.setReconciledAmount(amount);
		orderPaymentReconciliation.setReconciled(reconciled.equalsIgnoreCase("Y"));
		return orderPaymentReconciliation;
	}

	public AdminReconciliationDao getAdminReconciliationDao() {
		return adminReconciliationDao;
	}

	public void setAdminReconciliationDao(AdminReconciliationDao adminReconciliationDao) {
		this.adminReconciliationDao = adminReconciliationDao;
	}

	public OrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public ShippingOrderDao getShippingOrderDao() {
		return shippingOrderDao;
	}

	public void setShippingOrderDao(ShippingOrderDao shippingOrderDao) {
		this.shippingOrderDao = shippingOrderDao;
	}
}
