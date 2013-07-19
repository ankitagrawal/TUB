package com.hk.admin.pact.service.pos;

import com.hk.dto.pos.POSSalesDto;
import com.hk.dto.pos.POSReturnItemDto;

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

  public List<POSSalesDto> storeSalesReport(Long storeId, Date startDate, Date endDate);
 // public List<Payment> dailyStoreSalesReport(Long storeId, Date startDate, Date endDate);

  public List<POSReturnItemDto> storeReturnReport(Long storeId, Date startDate, Date endDate);
}
