package com.hk.report.pact.dao.catalog.product;

import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.warehouse.Warehouse;
import com.hk.report.dto.inventory.*;

import java.util.Date;
import java.util.List;

public interface ReportProductVariantDao{

    public List<InventorySoldDto> findInventorySoldByDate(Date startDate, Date endDate);

    public InventorySoldDto findInventorySoldByDateAndProduct(Date startDate, Date endDate, String productId);

    public List<ExpiryAlertReportDto> getToBeExpiredProductDetails(Date startDate, Date endDate, Warehouse warehouse);

    public Long getOpeningStockOfProductVariantOnDate(String productVariant, Date txnDate, Warehouse warehouse);

    public Long getDamageRtoCheckedInQty(String productVariant, Date startDate, Date endDate, Warehouse warehouse);

    public List<ShippingOrder> getShippingOrdersByReturnDate(Date startDate, Date endDate, EnumShippingOrderStatus shippingOrderStatus);

    public List<RTOFineReportDto> getRTOFineProductVariantDetails(ShippingOrder shippingOrder);

    public List<RTODamageReportDto> getRTODamageProductVariantDetails(ShippingOrder shippingOrder);

    public List<StockReportDto> getProductVariantStockBetweenDates(String productVariant, Date startDate, Date endDate, Warehouse warehouse);

    public Long getStockLeftQty(String productVariant, Warehouse warehouse);

    public List<RVReportDto> getReconciliationVoucherDetail(String productVariantId, Warehouse warehouse, Date startDate, Date endDate);

    public List<GrnLineItem> getPurchaseOrderByProductVariant(ProductVariant productVariant, Date startDate, Date endDate);
}
