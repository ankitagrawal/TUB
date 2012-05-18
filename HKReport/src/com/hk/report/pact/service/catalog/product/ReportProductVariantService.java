package com.hk.report.pact.service.catalog.product;

import java.util.Date;
import java.util.List;

import com.hk.report.dto.inventory.InventorySoldDto;
import com.hk.report.dto.inventory.ExpiryAlertReportDto;
import com.hk.domain.warehouse.Warehouse;

public interface ReportProductVariantService {
  public List<InventorySoldDto> findInventorySoldByDate(Date startDate, Date endDate);

  public InventorySoldDto findInventorySoldByDateAndProduct(Date startDate, Date endDate, String productId);

  public List<ExpiryAlertReportDto> getToBeExpiredProductDetails(Date startDate, Date endDate, Warehouse warehouse);
}
