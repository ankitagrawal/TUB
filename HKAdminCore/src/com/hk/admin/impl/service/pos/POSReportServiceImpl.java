package com.hk.admin.impl.service.pos;

import com.hk.admin.pact.dao.order.AdminOrderDao;
import com.hk.admin.pact.service.pos.POSReportService;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.dto.pos.POSSummaryDto;
import com.hk.pact.dao.courier.ReverseOrderDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class POSReportServiceImpl implements POSReportService {

  @Autowired
  private AdminOrderDao adminOrderDao;
  @Autowired
  private ReverseOrderDao reverseOrderDao;
  @Autowired
  private OrderStatusService orderStatusService;
  @Autowired
  private ShippingOrderStatusService shippingOrderStatusService;
  @Autowired
  private WarehouseService warehouseService;

  public List<Order> storeSalesReport(Long storeId, Date startDate, Date endDate) {
    if (startDate == null) {
      startDate = getStartDate();
      endDate = new Date();
    }
    //OrderStatus orderStatusReturned = orderStatusService.find(EnumOrderStatus.Delivered);
    OrderStatus orderStatusReturned =EnumOrderStatus.Delivered.asOrderStatus();
    List<OrderStatus> orderStatusList = new ArrayList<OrderStatus>();
    orderStatusList.add(orderStatusReturned);
    return adminOrderDao.findSaleForTimeFrame(storeId, startDate, endDate, orderStatusList);
  }

  public POSSummaryDto storeDailySalesSummaryReport(List<Order> saleList, List<ReverseOrder> returnList) {

    Double creditCardAmtCollected = 0D;
    Double creditCardAmtRefunded = 0D;
    Double cashAmtCollected = 0D;
    Double cashAmtRefunded = 0D;
    Long itemsSold = 0L;
    Long itemReturned = 0L;
    for (Order order : saleList) {
      itemsSold = itemsSold + order.getCartLineItems().size();
      for (Payment payment : order.getPayments()) {
        if (payment.getPaymentMode().getId().equals(EnumPaymentMode.COUNTER_CASH.getId())) {
          cashAmtCollected = cashAmtCollected + payment.getAmount();
        }
        if (payment.getPaymentMode().getId().equals(EnumPaymentMode.OFFLINE_CARD_PAYMENT.getId())) {
          creditCardAmtCollected = creditCardAmtCollected + payment.getAmount();
        }
      }
    }

    for (ReverseOrder reverseOrder : returnList) {
      cashAmtRefunded = cashAmtRefunded + reverseOrder.getAmount();
      itemReturned = itemReturned + reverseOrder.getReverseLineItems().size();
    }

    return (new POSSummaryDto(cashAmtCollected, cashAmtRefunded, creditCardAmtCollected, creditCardAmtRefunded, itemsSold, itemReturned));
  }

  public List<ReverseOrder> storeReturnReport(Long storeId, Date startDate, Date endDate) {
    if (startDate == null) {
      startDate = getStartDate();
      endDate = new Date();
    }
    return reverseOrderDao.findReverseOrderForTimeFrame(storeId, startDate, endDate);
  }

  public Date getStartDate() {
    Date date = new Date();
    Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    cal.set(Calendar.HOUR, 0);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }
}
