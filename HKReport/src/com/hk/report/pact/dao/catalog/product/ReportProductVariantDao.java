package com.hk.report.pact.dao.catalog.product;

import java.util.Date;
import java.util.List;

import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.report.dto.inventory.ExpiryAlertReportDto;
import com.hk.report.dto.inventory.InventorySoldDto;
import com.hk.report.dto.inventory.RTODamageReportDto;
import com.hk.report.dto.inventory.RTOFineReportDto;
import com.hk.report.dto.inventory.RVReportDto;
import com.hk.report.dto.inventory.StockReportDto;

public interface ReportProductVariantDao{

    public List<InventorySoldDto> findInventorySoldByDate(Date startDate, Date endDate);

    public InventorySoldDto findInventorySoldByDateAndProduct(Date startDate, Date endDate, String productId);

    public Long findSkuInventorySold(Date startDate, Date endDate, Sku sku);

    public List<ExpiryAlertReportDto> getToBeExpiredProductDetails(Date startDate, Date endDate, Warehouse warehouse);

    public Long getOpeningStockOfProductVariantOnDate(String productVariant, Date txnDate, Warehouse warehouse);

    public Long getDamageRtoCheckedInQty(String productVariant, Date startDate, Date endDate, Warehouse warehouse);

   public List<ShippingOrder> getShippingOrdersByReturnDate(Date startDate, Date endDate, EnumShippingOrderStatus shippingOrderStatus,Warehouse warehouse);

    public List<RTOFineReportDto> getRTOFineProductVariantDetails(ShippingOrder shippingOrder);

    public List<RTODamageReportDto> getRTODamageProductVariantDetails(ShippingOrder shippingOrder);

    public List<StockReportDto> getProductVariantStockBetweenDates(String productVariant, Date startDate, Date endDate, Warehouse warehouse);

    public Long getStockLeftQty(String productVariant, Warehouse warehouse);

    public List<RVReportDto> getReconciliationVoucherDetail(String productVariantId, Warehouse warehouse, Date startDate, Date endDate);

    public List<GrnLineItem> getGrnLineItemForPurchaseOrder(ProductVariant productVariant, Warehouse warehouse, Date startDate, Date endDate);
}
