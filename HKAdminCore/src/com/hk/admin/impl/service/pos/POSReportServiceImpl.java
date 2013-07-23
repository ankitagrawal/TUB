package com.hk.admin.impl.service.pos;


import com.hk.admin.pact.service.pos.POSReportService;
import com.hk.admin.pact.service.reverseOrder.ReverseOrderService;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.dto.pos.POSSummaryDto;
import com.hk.pact.dao.pos.POSDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

//import com.hk.report.pact.service.order.ReportOrderService;

/**
 * Created with IntelliJ IDEA.
 * User: Nitin Kumar
 * Date: 7/18/13
 * Time: 2:13 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class POSReportServiceImpl implements POSReportService {


  @Autowired
  private POSDao posDao;
  @Autowired
  private OrderStatusService orderStatusService;
  @Autowired
  private ShippingOrderStatusService shippingOrderStatusService;
  @Autowired
  private ReverseOrderService reverseOrderService;
  @Autowired
  private WarehouseService warehouseService;


  public List<Order> storeSalesReport(Long storeId, Date startDate, Date endDate) {

    if (startDate == null) {
      Date date = new Date();
      Calendar cal = new GregorianCalendar();
      cal.setTime(date);
      endDate = cal.getTime();

      cal.set(Calendar.HOUR, 0);
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MILLISECOND, 0);
      startDate = cal.getTime();
    }
    OrderStatus orderstatusReturned = orderStatusService.find(EnumOrderStatus.Delivered);
    List<OrderStatus> orderStatusList = new ArrayList<OrderStatus>();
    orderStatusList.add(orderstatusReturned);
    return posDao.findSaleForTimeFrame(storeId, startDate, endDate, orderStatusList);
  }

  public POSSummaryDto storeDailySalesSummaryReport(List<Order> saleList, List<ReverseOrder> returnList) {
    Iterator saleIterator = saleList.iterator();
    Iterator returnIterator = returnList.iterator();
    Double creditCardAmtCollected = 0D;
    Double creditCardAmtRefunded = 0D;
    Double cashAmtCollected = 0D;
    Double cashAmtRefunded = 0D;
    Long itemsSold = 0L;
    Long itemReturned = 0L;
    while (saleIterator.hasNext()) {
      Order order = (Order) saleIterator.next();
      Set<Payment> payments = order.getPayments();
      Iterator paymentIterator = payments.iterator();
      if (order.getOrderStatus().getId().equals(EnumOrderStatus.Delivered.getId())) {
        itemsSold = itemsSold + order.getCartLineItems().size();
        while (paymentIterator.hasNext()) {
          Payment orderPayment = (Payment) paymentIterator.next();
          if (orderPayment.getPaymentMode().getId().equals(EnumPaymentMode.COUNTER_CASH.getId()))
            cashAmtCollected = cashAmtCollected + orderPayment.getAmount();
          if (orderPayment.getPaymentMode().getId().equals(EnumPaymentMode.OFFLINE_CARD_PAYMENT.getId()))
            creditCardAmtCollected = creditCardAmtCollected + orderPayment.getAmount();
        }
      }
     /* else if(order.getOrderStatus().getId().equals(EnumOrderStatus.RTO.getId()))
      {itemReturned=itemReturned+order.getCartLineItems().size();
        while(paymentIterator.hasNext())
        {
          Payment orderPayment = (Payment) paymentIterator.next();
          if(orderPayment.getPaymentMode().getId().equals(EnumPaymentMode.COUNTER_CASH.getId()))
            cashAmtRefunded= cashAmtRefunded+orderPayment.getAmount();
          if(orderPayment.getPaymentMode().getId().equals(EnumPaymentMode.OFFLINE_CARD_PAYMENT.getId()))
            creditCardAmtRefunded= creditCardAmtRefunded+orderPayment.getAmount();
        }}*/
    }

    while (returnIterator.hasNext()) {
      ReverseOrder reverseOrder = (ReverseOrder) returnIterator.next();
      cashAmtRefunded = cashAmtRefunded + reverseOrder.getAmount();
      itemReturned = itemReturned + reverseOrder.getReverseLineItems().size();
    }

    return (new POSSummaryDto(cashAmtCollected, cashAmtRefunded, creditCardAmtCollected, creditCardAmtRefunded, itemsSold, itemReturned));

  }

  public List<ReverseOrder> storeReturnReport(Long storeId, Date startDate, Date endDate) {
    if (startDate == null) {
      Date date = new Date();
      Calendar cal = new GregorianCalendar();
      cal.setTime(date);
      endDate = cal.getTime();
      cal.set(Calendar.HOUR, 0);
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MILLISECOND, 0);
      startDate = cal.getTime();
    }

    return posDao.findReturnItemForTimeFrame(storeId, startDate, endDate);
  }

}
