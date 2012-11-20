package com.hk.admin.impl.service.reconciliation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.admin.pact.dao.order.AdminOrderDao;
import com.hk.admin.pact.dao.reconciliation.AdminReconciliationDao;
import com.hk.admin.pact.dao.shippingOrder.AdminShippingOrderDao;
import com.hk.admin.pact.service.reconciliation.AdminReconciliationService;
import com.hk.admin.util.XslUtil;
import com.hk.constants.XslConstants;
import com.hk.domain.courier.Courier;
import com.hk.domain.order.Order;
import com.hk.domain.order.OrderPaymentReconciliation;
import com.hk.domain.order.ShippingOrder;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;

/**
 * Created with IntelliJ IDEA. User: Rohit Date: 9/6/12 Time: 2:13 PM To change this template use File | Settings | File
 * Templates.
 */

@Service
public class AdminReconciliationServiceImpl implements AdminReconciliationService {
    private static Logger         logger = LoggerFactory.getLogger(AdminReconciliationServiceImpl.class);
    @Autowired
    private AdminShippingOrderDao adminShippingOrderDao;
    @Autowired
    AdminReconciliationDao        adminReconciliationDao;
    @Autowired
    AdminOrderDao                 adminOrderDao;

    public void parseExcelForShippingOrder(String excelFilePath, String sheetName, String paymentProcess) throws Exception {
        List<OrderPaymentReconciliation> orderPaymentReconciliationList = new ArrayList<OrderPaymentReconciliation>();
        ExcelSheetParser parser = new ExcelSheetParser(excelFilePath, sheetName);
        Iterator<HKRow> rowIterator = parser.parse();
        int rowCount = 1;
        List<String> gatewayOrderIdListInExcel = new ArrayList<String>();
        try {
            while (rowIterator.hasNext()) {
                HKRow row = rowIterator.next();
                String gatewayOrderId = row.getColumnValue(XslConstants.GATEWAY_ORDER_ID);
                String reconciled = row.getColumnValue(XslConstants.RECONCILED);
                Double amount = XslUtil.getDouble(row.getColumnValue(XslConstants.AMOUNT));
                checkExcelSanity(gatewayOrderId, reconciled, amount);
                gatewayOrderIdListInExcel.add(gatewayOrderId);
                rowCount++;
            }
            rowCount = 0;
            List<ShippingOrder> shippingOrderListInDB = getAdminShippingOrderDao().getShippingOrderByGatewayOrderList(gatewayOrderIdListInExcel);
            List<String> gatewayOrderIdListInDB = new ArrayList<String>();
            Map<String, ShippingOrder> shippingOrderGatewayOrderMap = new HashMap<String, ShippingOrder>();
            for (ShippingOrder shippingOrder : shippingOrderListInDB) {
                gatewayOrderIdListInDB.add(shippingOrder.getGatewayOrderId());
                shippingOrderGatewayOrderMap.put(shippingOrder.getGatewayOrderId(), shippingOrder);

            }
            gatewayOrderIdListInExcel.removeAll(gatewayOrderIdListInDB);

            if (gatewayOrderIdListInExcel.size() > 0) {
                String invalidGatewayOrderId = "";
                for (String gatewayOrderId : gatewayOrderIdListInExcel) {
                    invalidGatewayOrderId += "     " + gatewayOrderId;

                }
                throw new Exception("Following gateway order ids are invalid : " + invalidGatewayOrderId);

            } else {
                rowIterator = parser.parse();
                while (rowIterator.hasNext()) {
                    HKRow row = rowIterator.next();
                    String gatewayOrderId = row.getColumnValue(XslConstants.GATEWAY_ORDER_ID);
                    String reconciled = row.getColumnValue(XslConstants.RECONCILED);
                    Double amount = XslUtil.getDouble(row.getColumnValue(XslConstants.AMOUNT));
                    if (shippingOrderGatewayOrderMap.containsKey(gatewayOrderId)) {
                        ShippingOrder shippingOrder = shippingOrderGatewayOrderMap.get(gatewayOrderId);
                        OrderPaymentReconciliation orderPaymentReconciliation = updateOrCreateOrderPaymentReconciliationBySO(shippingOrder, paymentProcess, amount, reconciled);
                        orderPaymentReconciliationList.add(orderPaymentReconciliation);
                        logger.debug("preparing orderPaymentReconciliation object for row : " + rowCount);
                    }
                    rowCount++;
                }
            }

        } catch (Exception e) {
            logger.error("Exception @ Row:" + (rowCount + 1) + e.getMessage());
            throw new Exception("Exception @ Row:" + (rowCount + 1) + ": " + e.getMessage(), e);
        }

        getAdminReconciliationDao().saveOrUpdate(orderPaymentReconciliationList);
    }

    public void parseExcelForBaseOrder(String excelFilePath, String sheetName, String paymentProcess) throws Exception {

        List<OrderPaymentReconciliation> orderPaymentReconciliationList = new ArrayList<OrderPaymentReconciliation>();
        ExcelSheetParser parser = new ExcelSheetParser(excelFilePath, sheetName);
        Iterator<HKRow> rowIterator = parser.parse();
        int rowCount = 1;
        List<String> gatewayOrderIdListInExcel = new ArrayList<String>();
        try {
            while (rowIterator.hasNext()) {
                HKRow row = rowIterator.next();
                String gatewayOrderId = row.getColumnValue(XslConstants.GATEWAY_ORDER_ID);
                String reconciled = row.getColumnValue(XslConstants.RECONCILED);
                Double amount = XslUtil.getDouble(row.getColumnValue(XslConstants.AMOUNT));
                checkExcelSanity(gatewayOrderId, reconciled, amount);
                gatewayOrderIdListInExcel.add(gatewayOrderId);
                rowCount++;
            }
            rowCount = 0;
            List<Order> baseOrderListInDB = getAdminOrderDao().getOrdersByGatewayOrderList(gatewayOrderIdListInExcel);
            List<String> gatewayOrderIdListInDB = new ArrayList<String>();
            Map<String, Order> gatewayOrderbaseOrderMap = new HashMap<String, Order>();
            for (Order order : baseOrderListInDB) {
                gatewayOrderIdListInDB.add(order.getGatewayOrderId());
                gatewayOrderbaseOrderMap.put(order.getGatewayOrderId(), order);
            }
            gatewayOrderIdListInExcel.removeAll(gatewayOrderIdListInDB);

            if (gatewayOrderIdListInExcel.size() > 0) {
                String invalidGatewayOrderId = "";
                for (String gatewayOrderId : gatewayOrderIdListInExcel) {
                    invalidGatewayOrderId += "     " + gatewayOrderId;
                }
                throw new Exception("Following gateway order ids are invalid : " + invalidGatewayOrderId);

            } else {
                rowIterator = parser.parse();
                while (rowIterator.hasNext()) {
                    HKRow row = rowIterator.next();
                    String gatewayOrderId = row.getColumnValue(XslConstants.GATEWAY_ORDER_ID);
                    String reconciled = row.getColumnValue(XslConstants.RECONCILED);
                    Double amount = XslUtil.getDouble(row.getColumnValue(XslConstants.AMOUNT));
                    if (gatewayOrderbaseOrderMap.containsKey(gatewayOrderId)) {
                        Order order = gatewayOrderbaseOrderMap.get(gatewayOrderId);
                        OrderPaymentReconciliation orderPaymentReconciliation = updateOrCreateOrderPaymentReconciliationByBO(order, paymentProcess, amount, reconciled);
                        orderPaymentReconciliationList.add(orderPaymentReconciliation);
                        for (ShippingOrder shippingOrder : order.getShippingOrders()) {
                            orderPaymentReconciliation = updateOrCreateOrderPaymentReconciliationBySO(shippingOrder, paymentProcess, shippingOrder.getAmount(), reconciled);
                            orderPaymentReconciliationList.add(orderPaymentReconciliation);
                        }

                        logger.debug("preparing orderPaymentReconciliation object for row : " + rowCount);
                    }
                    rowCount++;
                }
            }

        } catch (Exception e) {
            logger.error("Exception @ Row:" + (rowCount + 1) + e.getMessage());
            throw new Exception("Exception @ Row:" + (rowCount + 1) + ": " + e.getMessage(), e);
        }

        getAdminReconciliationDao().saveOrUpdate(orderPaymentReconciliationList);

    }

    private void checkExcelSanity(String gatewayOrderId, String reconciled, Double amount) throws Exception {
        if (gatewayOrderId == null) {
            throw new Exception("Invalid gateway order id ");
        }
        if (reconciled == null) {
            throw new Exception("Invalid reconciliation status");
        }
        if (!reconciled.equalsIgnoreCase("N") && !reconciled.equalsIgnoreCase("Y")) {
            throw new Exception("Reconciliation status should be 'Y' or 'N' only. ");
        }
        if (amount == null) {
            throw new Exception("Invalid amount");
        }
    }

    private OrderPaymentReconciliation updateOrCreateOrderPaymentReconciliationBySO(ShippingOrder shippingOrder, String paymentProcess, Double amount, String reconciled) {
        OrderPaymentReconciliation orderPaymentReconciliation = getAdminReconciliationDao().getOrderPaymentReconciliationBySO(shippingOrder);
        if (orderPaymentReconciliation == null) {
            orderPaymentReconciliation = new OrderPaymentReconciliation();
        }
        orderPaymentReconciliation.setShippingOrder(shippingOrder);
        orderPaymentReconciliation.setBaseOrder(shippingOrder.getBaseOrder());
        orderPaymentReconciliation.setPaymentProcessType(paymentProcess);
        orderPaymentReconciliation.setReconciled(reconciled.equalsIgnoreCase("Y"));
        orderPaymentReconciliation.setReconciledAmount(amount);
        return orderPaymentReconciliation;
    }

    private OrderPaymentReconciliation updateOrCreateOrderPaymentReconciliationByBO(Order order, String paymentProcess, Double amount, String reconciled) {
        OrderPaymentReconciliation orderPaymentReconciliation = getAdminReconciliationDao().getOrderPaymentReconciliationByBaseOrder(order);
        if (orderPaymentReconciliation == null) {
            orderPaymentReconciliation = new OrderPaymentReconciliation();
        }
        orderPaymentReconciliation.setBaseOrder(order);
        orderPaymentReconciliation.setPaymentProcessType(paymentProcess);
        orderPaymentReconciliation.setReconciled(reconciled.equalsIgnoreCase("Y"));
        orderPaymentReconciliation.setReconciledAmount(amount);
        return orderPaymentReconciliation;
    }

    public List<OrderPaymentReconciliation> findPaymentDifferenceInCODOrders(Long shippingOrderId, String gatewayOrderId, Date startDate, Date endDate, Courier courier,
            String paymentProcess) throws Exception {
        return getAdminReconciliationDao().findPaymentDifferenceInCODOrders(shippingOrderId, gatewayOrderId, startDate, endDate, courier, paymentProcess);
    }

    public List<OrderPaymentReconciliation> findPaymentDifferenceInPrepaidOrders(Long baseOrderId, String gatewayOrderId, Date startDate, Date endDate, String paymentProcess)
            throws Exception {
        return getAdminReconciliationDao().findPaymentDifferenceInPrepaidOrders(baseOrderId, gatewayOrderId, startDate, endDate, paymentProcess);
    }

    public AdminReconciliationDao getAdminReconciliationDao() {
        return adminReconciliationDao;
    }

    public void setAdminReconciliationDao(AdminReconciliationDao adminReconciliationDao) {
        this.adminReconciliationDao = adminReconciliationDao;
    }

    public AdminShippingOrderDao getAdminShippingOrderDao() {
        return adminShippingOrderDao;
    }

    public void setAdminShippingOrderDao(AdminShippingOrderDao adminShippingOrderDao) {
        this.adminShippingOrderDao = adminShippingOrderDao;
    }

    public AdminOrderDao getAdminOrderDao() {
        return adminOrderDao;
    }

    public void setAdminOrderDao(AdminOrderDao adminOrderDao) {
        this.adminOrderDao = adminOrderDao;
    }
}
