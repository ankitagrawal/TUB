package com.hk.admin.pact.service.pos;

import com.hk.domain.order.Order;
import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.dto.pos.POSSummaryDto;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nitin Kumar
 * Date: 7/18/13
 * Time: 2:01 PM
 * To change this template use File | Settings | File Templates.
 */

public interface POSReportService {

  public List<Order> storeSalesReport(Long storeId, Date startDate, Date endDate);

  public POSSummaryDto storeDailySalesSummaryReport(List<Order> saleList, List<ReverseOrder> returnList);

  public List<ReverseOrder> storeReturnReport(Long storeId, Date startDate, Date endDate);
}
