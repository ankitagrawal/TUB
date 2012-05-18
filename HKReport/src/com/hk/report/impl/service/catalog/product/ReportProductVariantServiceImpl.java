package com.hk.report.impl.service.catalog.product;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.transform.Transformers;

import com.hk.report.dto.inventory.InventorySoldDto;
import com.hk.report.dto.inventory.ExpiryAlertReportDto;
import com.hk.report.pact.dao.catalog.product.ReportProductVariantDao;
import com.hk.report.pact.service.catalog.product.ReportProductVariantService;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.catalog.product.ProductVariant;

@Service
public class ReportProductVariantServiceImpl implements ReportProductVariantService {
  @Autowired
  private ReportProductVariantDao reportProductVariantDao;

  @Override
  public List<InventorySoldDto> findInventorySoldByDate(Date startDate, Date endDate) {
    return getReportProductVariantDao().findInventorySoldByDate(startDate, endDate);
  }

  @Override
  public InventorySoldDto findInventorySoldByDateAndProduct(Date startDate, Date endDate, String productId) {
    return getReportProductVariantDao().findInventorySoldByDateAndProduct(startDate, endDate,productId);
  }

  public ReportProductVariantDao getReportProductVariantDao() {
    return reportProductVariantDao;
  }

  public void setReportProductVariantDao(ReportProductVariantDao reportProductVariantDao) {
    this.reportProductVariantDao = reportProductVariantDao;
  }

  @Override
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
}
