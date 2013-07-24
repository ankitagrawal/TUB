package com.hk.admin.pact.service.pos;

import com.hk.domain.order.Order;
import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.dto.pos.POSSaleItemDto;
import com.hk.dto.pos.POSSummaryDto;
import com.hk.dto.pos.PosSkuGroupSearchDto;

import java.io.File;
import java.util.Date;
import java.util.List;

public interface POSReportService {

  public List<Order> storeSalesReport(Long storeId, Date startDate, Date endDate);

  public List<POSSaleItemDto> storeSalesReportWithDiscount(List<Order> orders);

  public POSSummaryDto storeDailySalesSummaryReport(List<Order> saleList, List<ReverseOrder> returnList);

  public List<ReverseOrder> storeReturnReport(Long storeId, Date startDate, Date endDate);

	public File generatePosStockReport(File xlsFile, List<PosSkuGroupSearchDto> posSkuGroupSearchDtoList);
}
