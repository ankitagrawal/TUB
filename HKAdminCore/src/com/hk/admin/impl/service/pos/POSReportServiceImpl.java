package com.hk.admin.impl.service.pos;


import com.hk.admin.pact.service.pos.POSReportService;
import com.hk.domain.core.OrderStatus;
import com.hk.dto.pos.POSSalesDto;
import com.hk.dto.pos.POSReturnItemDto;
import com.hk.pact.dao.pos.POSDao;
import com.hk.util.io.HkXlsWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


  public List<POSSalesDto> storeSalesReport(Long storeId, Date startDate, Date endDate) {
    //public List<Payment> storeSalesReport(Long storeId, Date startDate, Date endDate) {
    //  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    //  File xlsFile = new File(sdf.format(new Date()) + ".xls");
    //  String xslFilePath = xlsFile.getPath();

    HkXlsWriter xlsWriter = new HkXlsWriter();

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
    return t;
  }

  public List<POSReturnItemDto> storeReturnReport(Long storeId, Date startDate, Date endDate) {
     return posDao.findReturnItemForTimeFrame(201L,startDate,endDate);
  }

}
