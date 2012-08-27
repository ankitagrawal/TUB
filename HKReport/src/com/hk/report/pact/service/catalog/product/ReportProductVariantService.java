package com.hk.report.pact.service.catalog.product;

import java.util.Date;
import java.util.List;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.report.dto.inventory.ExpiryAlertReportDto;
import com.hk.report.dto.inventory.InventorySoldDto;
import com.hk.report.dto.inventory.RTOReportDto;
import com.hk.report.dto.inventory.RVReportDto;
import com.hk.report.dto.inventory.StockReportDto;

public interface ReportProductVariantService {
    public List<InventorySoldDto> findInventorySoldByDate(Date startDate, Date endDate);

    public InventorySoldDto findInventorySoldByDateAndProduct(Date startDate, Date endDate, String productId);

    public Long findSkuInventorySold(Date startDate, Date endDate, Sku sku);

    public List<ExpiryAlertReportDto> getToBeExpiredProductDetails(Date startDate, Date endDate, Warehouse warehouse);

    public StockReportDto getStockDetailsByProductVariant(String productVariantId, Warehouse warehouse, Date startDate, Date endDate);

    public List<RTOReportDto> getRTOProductsDetail(Date startDate, Date endDate, Warehouse warehouse);

    public List<RVReportDto> getReconciliationVoucherDetail(String productVariantId, Warehouse warehouse, Date startDate, Date endDate);

    public List<GrnLineItem> getGrnLineItemForPurchaseOrder(ProductVariant productVariant, Warehouse warehouse, Date startDate, Date endDate);

}
