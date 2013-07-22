package com.hk.admin.impl.service.pos;


import com.hk.admin.pact.service.pos.POSReportService;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.dto.pos.POSSalesDto;
import com.hk.dto.pos.POSReturnItemDto;
import com.hk.dto.pos.POSSummaryDto;
import com.hk.pact.dao.pos.POSDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.util.io.HkXlsWriter;
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


  public List<Order> storeSalesReport(Long storeId, Date startDate, Date endDate) {

    if(startDate == null)
    {
      Date date = new Date();
      Calendar cal = new GregorianCalendar();
      cal.setTime(date);
      cal.add(Calendar.DAY_OF_YEAR,-1);
      startDate= cal.getTime();

      cal.clear(Calendar.HOUR);
      cal.clear(Calendar.MINUTE);
      cal.clear(Calendar.SECOND);
      cal.clear(Calendar.MILLISECOND);
      endDate= cal.getTime();
    }
    OrderStatus orderstatusReturned = orderStatusService.find(EnumOrderStatus.Delivered);
    List<OrderStatus> orderStatusList = new ArrayList<OrderStatus>();
    orderStatusList.add(orderstatusReturned);
    return posDao.findSaleForTimeFrame(201L, startDate, endDate, orderStatusList);

  /*  HkXlsWriter xlsWriter = new HkXlsWriter();

    xlsWriter.addHeader("CASH", "CASH");
    xlsWriter.addHeader("CREDIT", "CREDIT");
    xlsWriter.addHeader("NO_OF_BILLS", "NO_OF_BILLS");
    xlsWriter.addHeader("APC", "APC");
    xlsWriter.addHeader("DISCOUNT", "DISCOUNT");
    xlsWriter.addHeader("MARGIN", "MARGIN");
    xlsWriter.addHeader("COLLECTION", "COLLECTION");


    //  OrderStatus orderstatusDelivered = orderStatusService.find(EnumOrderStatus.Delivered);
    //  OrderStatus orderstatusReturned = orderStatusService.find(EnumOrderStatus.Delivered);
    List<OrderStatus> orderStatuses = new ArrayList<OrderStatus>();

    //  List<Order> orders = reportOrderService.findOrdersForTimeFrame( startDate, endDate, orderstatusDelivered,null) ;
    List<POSSalesDto> t = posDao.findSaleForTimeFrame(storeId, startDate, endDate);
    //List<Payment> t = posDao.findSaleForTimeFrame(storeId, startDate, endDate);
    System.out.println("temp");
    return t;*/
  }

  public POSSummaryDto storeDailySalesSummaryReport(List<Order> saleList){
    Iterator saleIterator = saleList.iterator();
    Double creditCardAmtCollected=0D;
    Double creditCardAmtRefunded=0D;
    Double cashAmtCollected=0D;
    Double cashAmtRefunded=0D;
    Long itemsSold=0L;
    Long itemReturned=0L;
    while(saleIterator.hasNext()){
      Order order = (Order) saleIterator.next();
      Set<Payment> payments = order.getPayments();
      Iterator paymentIterator = payments.iterator();
      if(order.getOrderStatus().getId().equals(EnumOrderStatus.Delivered.getId()))
      {itemsSold=itemsSold+order.getCartLineItems().size();
        while(paymentIterator.hasNext())
        {
          Payment orderPayment = (Payment) paymentIterator.next();
          if(orderPayment.getPaymentMode().getId().equals(EnumPaymentMode.COUNTER_CASH.getId()))
            cashAmtCollected= cashAmtCollected+orderPayment.getAmount();
          if(orderPayment.getPaymentMode().getId().equals(EnumPaymentMode.OFFLINE_CARD_PAYMENT.getId()))
            creditCardAmtCollected= creditCardAmtCollected+orderPayment.getAmount();
        }}
      else if(order.getOrderStatus().getId().equals(EnumOrderStatus.RTO.getId()))
      {itemReturned=itemReturned+order.getCartLineItems().size();
        while(paymentIterator.hasNext())
        {
          Payment orderPayment = (Payment) paymentIterator.next();
          if(orderPayment.getPaymentMode().getId().equals(EnumPaymentMode.COUNTER_CASH.getId()))
            cashAmtRefunded= cashAmtRefunded+orderPayment.getAmount();
          if(orderPayment.getPaymentMode().getId().equals(EnumPaymentMode.OFFLINE_CARD_PAYMENT.getId()))
            creditCardAmtRefunded= creditCardAmtRefunded+orderPayment.getAmount();
        }}



    }
     return (new POSSummaryDto(cashAmtCollected,cashAmtRefunded,creditCardAmtCollected,creditCardAmtRefunded,itemsSold,itemReturned));

  }

  public List<Order> storeReturnReport(Long storeId, Date startDate, Date endDate) {
    if(startDate == null)
    {
      Date date = new Date();
      Calendar cal = new GregorianCalendar();
      cal.setTime(date);
      cal.add(Calendar.DAY_OF_YEAR,-1);
      startDate= cal.getTime();

      cal.clear(Calendar.HOUR);
      cal.clear(Calendar.MINUTE);
      cal.clear(Calendar.SECOND);
      cal.clear(Calendar.MILLISECOND);
      endDate= cal.getTime();
    }
     OrderStatus orderstatusReturned = orderStatusService.find(EnumOrderStatus.RTO);
     OrderStatus orderstatusPartialReturned = orderStatusService.find(EnumOrderStatus.PartialRTO);
     List<OrderStatus> orderStatusList = new ArrayList<OrderStatus>();
     orderStatusList.add(orderstatusReturned);
     orderStatusList.add(orderstatusPartialReturned);
     return posDao.findReturnItemForTimeFrame(201L,startDate,endDate,orderStatusList);
  }

}
