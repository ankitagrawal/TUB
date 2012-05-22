
package com.hk.report.impl.dao.catalog.product;

import java.util.Date;
import java.util.List;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.hk.impl.dao.BaseDaoImpl;
import com.hk.report.dto.inventory.InventorySoldDto;
import com.hk.report.dto.inventory.ExpiryAlertReportDto;
import com.hk.report.pact.dao.catalog.product.ReportProductVariantDao;
import com.hk.domain.warehouse.Warehouse;

@Repository
public class ReportProductVariantDaoImpl extends BaseDaoImpl implements ReportProductVariantDao {

  @SuppressWarnings("unchecked")
  public List<InventorySoldDto> findInventorySoldByDate(Date startDate, Date endDate) {
    return (List<InventorySoldDto>) getSession().createQuery(
        "select count(li.id) as countSold,li.sku.productVariant.product.id as productId,"
            + " li.sku.productVariant.product.name as productName"
            + " from LineItem li"
            + " where li.shippingOrder.shippingOrderStatus.id in (180,190) and li.shippingOrder.shipment.shipDate  > :startDate and li.shippingOrder.shipment.shipDate  < :endDate "
            + " group by li.sku.productVariant.product.id  order by count(li.id) desc").setParameter("startDate", startDate).setParameter("endDate", endDate).setResultTransformer(
        Transformers.aliasToBean(InventorySoldDto.class)).list();
  }

  public InventorySoldDto findInventorySoldByDateAndProduct(Date startDate, Date endDate, String productId) {
    return (InventorySoldDto) getSession().createQuery(
        "select count(li.id) as countSold,li.sku.productVariant.product.id as productId," + " li.sku.productVariant.product.name as productName" + " from LineItem li"
            + " where  li.shippingOrder.shippingOrderStatus.id in (180,190) and li.shippingOrder.shipment.shipDate > :startDate "
            + " and li.shippingOrder .shipment.shipDate < :endDate " + " and li.sku.productVariant.product.id = :productId order by count(li.id) desc ").setParameter(
        "startDate", startDate).setParameter("endDate", endDate).setParameter("productId", productId).setResultTransformer(Transformers.aliasToBean(InventorySoldDto.class)).uniqueResult();
  }

  public List<ExpiryAlertReportDto> getToBeExpiredProductDetails(Date startDate, Date endDate, Warehouse warehouse) {
    String query = "select sg as skuGroup, sum(pvi.qty) as batchQty from SkuGroup sg, ProductVariantInventory pvi where sg.expiryDate between :startDate and :endDate " +
        "and pvi.sku.warehouse = :warehouse and pvi.skuItem.skuGroup.id = sg.id group by sg having sum(pvi.qty) > 0";
    
    return (List<ExpiryAlertReportDto>)getSession().createQuery(query).setParameter("startDate", startDate)
        .setParameter("endDate", endDate).setParameter("warehouse", warehouse).setResultTransformer(Transformers.aliasToBean(ExpiryAlertReportDto.class)).list();
  }

  public Long getOpeningStockOfProductVariantOnDate(String productVariant, Date txnDate, Warehouse warehouse) {
    String query = "select coalesce(sum(pvi.qty), 0) from ProductVariantInventory pvi where pvi.sku.productVariant.id = :productVariant and pvi.txnDate < :txnDate " +
        "and pvi.sku.warehouse.id = :warehouse ";
    Long result =  (Long) getSession().createQuery(query).setParameter("productVariant", productVariant)
        .setParameter("txnDate", txnDate).setParameter("warehouse", warehouse.getId()).uniqueResult();
    return result;
  }

  public Long getCheckedOutQtyOfProductVariantBetweenDates(String productVariant, Date startDate, Date endDate, Warehouse warehouse) {
    String query = "select count(pvi.id) from ProductVariantInventory pvi where pvi.sku.productVariant.id = :productVariant " +
        "and pvi.lineItem is not null and pvi.qty = :qty and pvi.txnDate between :startDate and :endDate " +
        "and pvi.sku.warehouse.id = :warehouse ";
    return (Long) getSession().createQuery(query).setParameter("productVariant", productVariant).setParameter("qty", -1L)
        .setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("warehouse", warehouse.getId()).uniqueResult();
  }

  public Long getReconcileCheckedOutQtyOfProductVariantBetweenDates(String productVariant, Date startDate, Date endDate, Warehouse warehouse) {
    String query = "select count(pvi.id) from ProductVariantInventory pvi where pvi.sku.productVariant.id = :productVariant " +
        "and pvi.rvLineItem is not null and pvi.qty = :qty and pvi.txnDate between :startDate and :endDate " +
        "and pvi.sku.warehouse.id = :warehouse ";
    return (Long) getSession().createQuery(query).setParameter("productVariant", productVariant).setParameter("qty", -1L)
        .setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("warehouse", warehouse.getId()).uniqueResult();
  }

  public Long getCheckedInQtyByInventoryTxnType(String productVariant, Date startDate, Date endDate, Warehouse warehouse, Long inventoryTxnType) {
    String query = "select count(pvi.id) from ProductVariantInventory pvi where pvi.sku.productVariant.id = :productVariant " +
        "and pvi.invTxnType.id = :invTxnType and pvi.qty = :qty and pvi.txnDate between :startDate and :endDate " +
        "and pvi.sku.warehouse.id = :warehouse ";
    return (Long) getSession().createQuery(query).setParameter("productVariant", productVariant).setParameter("qty", 1L)
        .setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("warehouse", warehouse.getId())
        .setParameter("invTxnType", inventoryTxnType).uniqueResult();
  }

  public Long getDamageRtoCheckedInQty(String productVariant, Date startDate, Date endDate, Warehouse warehouse) {
    String query = "select count(pvi.id) from ProductVariantDamageInventory pvi where pvi.sku.productVariant.id = :productVariant " +
        "and pvi.qty = :qty and pvi.txnDate between :startDate and :endDate " +
        "and pvi.sku.warehouse.id = :warehouse ";
    return (Long) getSession().createQuery(query).setParameter("productVariant", productVariant).setParameter("qty", 1L)
        .setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("warehouse", warehouse.getId()).uniqueResult();
  }
}
