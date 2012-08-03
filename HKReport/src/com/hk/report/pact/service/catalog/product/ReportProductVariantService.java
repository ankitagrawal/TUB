package com.hk.report.pact.service.catalog.product;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.report.dto.inventory.*;

import java.util.Date;
import java.util.List;

public interface ReportProductVariantService {
    public List<InventorySoldDto> findInventorySoldByDate(Date startDate, Date endDate);

    public InventorySoldDto findInventorySoldByDateAndProduct(Date startDate, Date endDate, String productId);

    public InventorySoldDto findInventorySoldByDateAndProduct(Date startDate, Date endDate, String productId, Warehouse warehouse);

    public List<ExpiryAlertReportDto> getToBeExpiredProductDetails(Date startDate, Date endDate, Warehouse warehouse);

    public StockReportDto getStockDetailsByProductVariant(String productVariantId, Warehouse warehouse, Date startDate, Date endDate);

    public List<RTOReportDto> getRTOProductsDetail(Date startDate, Date endDate);

    public List<RVReportDto> getReconciliationVoucherDetail(String productVariantId, Warehouse warehouse, Date startDate, Date endDate);

    public List<GrnLineItem> getGrnLineItemForPurchaseOrder(ProductVariant productVariant, Warehouse warehouse, Date startDate, Date endDate);

}
