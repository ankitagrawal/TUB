package com.hk.report.pact.dao.catalog.product;

import java.util.Date;
import java.util.List;

import com.hk.report.dto.inventory.InventorySoldDto;
import com.hk.report.dto.inventory.ExpiryAlertReportDto;
import com.hk.report.dto.inventory.RTOFineReportDto;
import com.hk.report.dto.inventory.RTODamageReportDto;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.order.ShippingOrder;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;

public interface ReportProductVariantDao{

  public List<InventorySoldDto> findInventorySoldByDate(Date startDate, Date endDate);

  public InventorySoldDto findInventorySoldByDateAndProduct(Date startDate, Date endDate, String productId);

  public List<ExpiryAlertReportDto> getToBeExpiredProductDetails(Date startDate, Date endDate, Warehouse warehouse);

  public Long getOpeningStockOfProductVariantOnDate(String productVariant, Date txnDate, Warehouse warehouse);

  public Long getCheckedOutQtyOfProductVariantBetweenDates(String productVariant, Date startDate, Date endDate, Warehouse warehouse);

  public Long getReconcileCheckedOutQtyOfProductVariantBetweenDates(String productVariant, Date startDate, Date endDate, Warehouse warehouse);

  public Long getCheckedInQtyByInventoryTxnType(String productVariant, Date startDate, Date endDate, Warehouse warehouse, Long inventoryTxnType);

  public Long getDamageRtoCheckedInQty(String productVariant, Date startDate, Date endDate, Warehouse warehouse);

  public List<ShippingOrder> getShippingOrdersByReturnDate(Date startDate, Date endDate, EnumShippingOrderStatus shippingOrderStatus);

  public List<RTOFineReportDto> getRTOFineProductVariantDetails(ShippingOrder shippingOrder);

  public List<RTODamageReportDto> getRTODamageProductVariantDetails(ShippingOrder shippingOrder);
  
}
