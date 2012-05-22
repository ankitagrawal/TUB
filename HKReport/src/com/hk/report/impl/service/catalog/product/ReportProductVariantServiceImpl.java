package com.hk.report.impl.service.catalog.product;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.transform.Transformers;

import com.hk.report.dto.inventory.InventorySoldDto;
import com.hk.report.dto.inventory.ExpiryAlertReportDto;
import com.hk.report.dto.inventory.StockReportDto;
import com.hk.report.pact.dao.catalog.product.ReportProductVariantDao;
import com.hk.report.pact.service.catalog.product.ReportProductVariantService;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.pact.dao.catalog.product.ProductVariantDao;

@Service
public class ReportProductVariantServiceImpl implements ReportProductVariantService {
  @Autowired
  private ReportProductVariantDao reportProductVariantDao;

  @Autowired
  private ProductVariantDao productVariantDao;

  public List<InventorySoldDto> findInventorySoldByDate(Date startDate, Date endDate) {
    return getReportProductVariantDao().findInventorySoldByDate(startDate, endDate);
  }

  public InventorySoldDto findInventorySoldByDateAndProduct(Date startDate, Date endDate, String productId) {
    return getReportProductVariantDao().findInventorySoldByDateAndProduct(startDate, endDate,productId);
  }

  public List<ExpiryAlertReportDto> getToBeExpiredProductDetails(Date startDate, Date endDate, Warehouse warehouse) {
    List<ExpiryAlertReportDto> expiryAlertReportDtoList = reportProductVariantDao.getToBeExpiredProductDetails(startDate, endDate, warehouse);
    List<ExpiryAlertReportDto> expiryAlertReturnList = new ArrayList<ExpiryAlertReportDto>();

    for(ExpiryAlertReportDto expiryAlertReport : expiryAlertReportDtoList) {
      SkuGroup skuGroup= expiryAlertReport.getSkuGroup();
      ProductVariant productVariant = skuGroup.getSku().getProductVariant();
      expiryAlertReport.setProductName(productVariant.getProduct().getName());
      expiryAlertReport.setBatchNumber(skuGroup.getBatchNumber());
      expiryAlertReport.setProductVariantId(productVariant.getId());
      expiryAlertReport.setProductOption(productVariant.getOptionsSlashSeparated());
      expiryAlertReport.setBatchQty(expiryAlertReport.getBatchQty());
      expiryAlertReturnList.add(expiryAlertReport);
    }
    return expiryAlertReturnList;
  }

  public StockReportDto getStockDetailsByProductVariant(String productVariantId, Warehouse warehouse, Date startDate, Date endDate) {
    ProductVariant productVariant = productVariantDao.getVariantById(productVariantId);
    String productName = productVariant.getProduct().getName();
    String productOption = productVariant.getOptionsSlashSeparated();

    Long openingStock = reportProductVariantDao.getOpeningStockOfProductVariantOnDate(productVariantId, startDate, warehouse);
    Long lineItemCheckOut = reportProductVariantDao.getCheckedOutQtyOfProductVariantBetweenDates(productVariantId, startDate, endDate, warehouse);
    Long reconcileCheckOut = reportProductVariantDao.getReconcileCheckedOutQtyOfProductVariantBetweenDates(productVariantId, startDate, endDate, warehouse);
    Long grnLineItemCheckIn = reportProductVariantDao.getCheckedInQtyByInventoryTxnType(productVariantId, startDate, endDate, warehouse, EnumInvTxnType.INV_CHECKIN.getId());
    Long reconcileCheckIn = reportProductVariantDao.getCheckedInQtyByInventoryTxnType(productVariantId, startDate, endDate, warehouse, EnumInvTxnType.RV_CHECKIN.getId());
    Long rtoCheckIn = reportProductVariantDao.getCheckedInQtyByInventoryTxnType(productVariantId, startDate, endDate, warehouse, EnumInvTxnType.RTO_CHECKIN.getId());
    Long moveBackToActionQueueCheckIn = reportProductVariantDao.getCheckedInQtyByInventoryTxnType(productVariantId, startDate, endDate, warehouse, EnumInvTxnType.CANCEL_CHECKIN.getId());
    Long damageRtoCheckin = reportProductVariantDao.getDamageRtoCheckedInQty(productVariantId, startDate, endDate, warehouse);

    Long stockLeft = openingStock + (grnLineItemCheckIn + reconcileCheckIn + rtoCheckIn + moveBackToActionQueueCheckIn)- (lineItemCheckOut + reconcileCheckOut);

    StockReportDto stockReportDto = new StockReportDto();
    stockReportDto.setProductVariant(productVariantId);
    stockReportDto.setProductName(productName);
    stockReportDto.setProductOption(productOption);
    stockReportDto.setOpeningStock(openingStock);
    stockReportDto.setLineItemCheckout(lineItemCheckOut);
    stockReportDto.setReconcileCheckout(reconcileCheckOut);
    stockReportDto.setGrnCheckin(grnLineItemCheckIn);
    stockReportDto.setReconcileCheckin(reconcileCheckIn);
    stockReportDto.setRtoCheckin(rtoCheckIn);
    stockReportDto.setMoveBackToActionQueueCheckin(moveBackToActionQueueCheckIn);
    stockReportDto.setDamageCheckin(damageRtoCheckin);
    stockReportDto.setStockLeft(stockLeft);

    return stockReportDto;
  }

  public ReportProductVariantDao getReportProductVariantDao() {
    return reportProductVariantDao;
  }

  public void setReportProductVariantDao(ReportProductVariantDao reportProductVariantDao) {
    this.reportProductVariantDao = reportProductVariantDao;
  }

  public ProductVariantDao getProductVariantDao() {
    return productVariantDao;
  }

  public void setProductVariantDao(ProductVariantDao productVariantDao) {
    this.productVariantDao = productVariantDao;
  }
}
