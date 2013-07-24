package com.hk.admin.impl.service.pos;

import com.hk.admin.pact.dao.order.AdminOrderDao;
import com.hk.admin.pact.service.pos.POSReportService;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.dto.pos.POSSaleItemDto;
import com.hk.dto.pos.POSSummaryDto;
import com.hk.pact.dao.courier.ReverseOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class POSReportServiceImpl implements POSReportService {

  @Autowired
  private AdminOrderDao adminOrderDao;
  @Autowired
  private ReverseOrderDao reverseOrderDao;

  public List<Order> storeSalesReport(Long storeId, Date startDate, Date endDate) {
    if (startDate == null) {
      startDate = getStartDate();
    }
    if (endDate == null) {
      endDate = new Date();
    }
    OrderStatus orderStatusReturned = EnumOrderStatus.Delivered.asOrderStatus();
    List<OrderStatus> orderStatusList = new ArrayList<OrderStatus>();
    orderStatusList.add(orderStatusReturned);
    return adminOrderDao.findSaleForTimeFrame(storeId, startDate, endDate, orderStatusList);
  }

  public List<POSSaleItemDto> storeSalesReportWithDiscount(List<Order> orders) {

    List<POSSaleItemDto> posSaleItems = new ArrayList<POSSaleItemDto>();
    for (Order order : orders) {
      double discount = 0.0;
      for (CartLineItem lineItem : order.getCartLineItems()) {
        discount = discount + lineItem.getDiscountOnHkPrice();
      }
      posSaleItems.add(new POSSaleItemDto(discount, order));
    }
    return posSaleItems;
  }

  public POSSummaryDto storeDailySalesSummaryReport(List<Order> saleList, List<ReverseOrder> returnList) {

    double creditCardAmtCollected = 0.0;
    double creditCardAmtRefunded = 0.0;
    double cashAmtCollected = 0.0;
    double cashAmtRefunded = 0.0;
    double totalAmountCollected = 0.0;
    double avgAmtPerInvoice = 0.0;
    Long itemsSold = 0L;
    Long itemReturned = 0L;
    double apc = 0.0;
    for (Order order : saleList) {
      itemsSold = itemsSold + order.getCartLineItems().size();
      for (Payment payment : order.getPayments()) {
        if (payment.getPaymentMode().getId().equals(EnumPaymentMode.COUNTER_CASH.getId())) {
          cashAmtCollected = cashAmtCollected + payment.getAmount();
        }
         else if (payment.getPaymentMode().getId().equals(EnumPaymentMode.OFFLINE_CARD_PAYMENT.getId())) {
          creditCardAmtCollected = creditCardAmtCollected + payment.getAmount();
        }
      }
    }
    totalAmountCollected = cashAmtCollected + creditCardAmtCollected;
    avgAmtPerInvoice = totalAmountCollected / saleList.size();
    apc = (Double.valueOf(itemsSold)) / saleList.size();

    for (ReverseOrder reverseOrder : returnList) {
      cashAmtRefunded = cashAmtRefunded + reverseOrder.getAmount();
      itemReturned = itemReturned + reverseOrder.getReverseLineItems().size();
    }
    return (new POSSummaryDto(cashAmtCollected, cashAmtRefunded, creditCardAmtCollected, creditCardAmtRefunded, itemsSold, itemReturned, totalAmountCollected, avgAmtPerInvoice, apc));
  }

  public List<ReverseOrder> storeReturnReport(Long storeId, Date startDate, Date endDate) {
    if (startDate == null) {
      startDate = getStartDate();
    }
    if (endDate == null) {
      endDate = new Date();
    }
    return reverseOrderDao.findReverseOrderForTimeFrame(storeId, startDate, endDate);
  }

  public Date getStartDate() {
    Date date = new Date();
    Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }
}
