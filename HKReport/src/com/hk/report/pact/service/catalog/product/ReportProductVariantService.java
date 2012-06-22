package com.hk.report.pact.service.catalog.product;

import java.util.Date;
import java.util.List;

import com.hk.domain.warehouse.Warehouse;
import com.hk.report.dto.inventory.ExpiryAlertReportDto;
import com.hk.report.dto.inventory.InventorySoldDto;
import com.hk.report.dto.inventory.RTOReportDto;
import com.hk.report.dto.inventory.StockReportDto;

public interface ReportProductVariantService {
  public List<InventorySoldDto> findInventorySoldByDate(Date startDate, Date endDate);

  public InventorySoldDto findInventorySoldByDateAndProduct(Date startDate, Date endDate, String productId);

  public List<ExpiryAlertReportDto> getToBeExpiredProductDetails(Date startDate, Date endDate, Warehouse warehouse);

  public StockReportDto getStockDetailsByProductVariant(String productVariantId, Warehouse warehouse, Date startDate, Date endDate);

  public List<RTOReportDto> getRTOProductsDetail(Date startDate, Date endDate);

  public List getReconciliationVoucherDetail(String productVariantId, Warehouse warehouse, Date startDate, Date endDate);

}
