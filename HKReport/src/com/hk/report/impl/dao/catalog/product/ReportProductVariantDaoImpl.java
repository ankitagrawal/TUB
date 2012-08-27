
package com.hk.report.impl.dao.catalog.product;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.report.dto.inventory.ExpiryAlertReportDto;
import com.hk.report.dto.inventory.InventorySoldDto;
import com.hk.report.dto.inventory.RTODamageReportDto;
import com.hk.report.dto.inventory.RTOFineReportDto;
import com.hk.report.dto.inventory.RVReportDto;
import com.hk.report.dto.inventory.StockReportDto;
import com.hk.report.pact.dao.catalog.product.ReportProductVariantDao;

@Repository
public class ReportProductVariantDaoImpl extends BaseDaoImpl implements ReportProductVariantDao {

    @SuppressWarnings("unchecked")
    public List<InventorySoldDto> findInventorySoldByDate(Date startDate, Date endDate) {
        return (List<InventorySoldDto>) getSession().createQuery(
                "select count(li.id) as countSold,li.sku.productVariant.product.id as productId,"
                        + " li.sku.productVariant.product.name as productName"
                        + " from LineItem li"
                        + " where li.shippingOrder.shippingOrderStatus.id in ( "
                        + EnumShippingOrderStatus.SO_Shipped.getId() + "," + EnumShippingOrderStatus.SO_Delivered.getId() +" ) and li.shippingOrder.shipment.shipDate  < :endDate "
                        + " group by li.sku.productVariant.product.id  order by count(li.id) desc").setParameter("startDate", startDate).setParameter("endDate", endDate).setResultTransformer(
                Transformers.aliasToBean(InventorySoldDto.class)).list();
    }

    public InventorySoldDto findInventorySoldByDateAndProduct(Date startDate, Date endDate, String productId) {
            return (InventorySoldDto) getSession().createQuery(
              "select count(li.id) as countSold,li.sku.productVariant.product.id as productId," + " li.sku.productVariant.product.name as productName" + " from LineItem li"
                + " where  li.shippingOrder.shippingOrderStatus.id in ( "
                + EnumShippingOrderStatus.SO_Shipped.getId() + "," + EnumShippingOrderStatus.SO_Delivered.getId() +" ) and li.shippingOrder.shipment.shipDate > :startDate "
                + " and li.shippingOrder .shipment.shipDate < :endDate " + " and li.sku.productVariant.product.id = :productId order by count(li.id) desc ").setParameter(
              "startDate", startDate).setParameter("endDate", endDate).setParameter("productId", productId).setResultTransformer(Transformers.aliasToBean(InventorySoldDto.class)).uniqueResult();
        }

    public Long findSkuInventorySold(Date startDate, Date endDate, Sku sku) {
        String sql = "select sum(li.qty) as countSold "
                + " from LineItem li"
                + " where  li.shippingOrder.shippingOrderStatus.id in (:shippingOrderStatus) and li.shippingOrder.shipment.shipDate > :startDate "
                + " and li.shippingOrder.shipment.shipDate < :endDate and li.sku = :sku ";

        Long inventory = (Long)getSession().createQuery(sql).setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("sku", sku)
                .setParameterList("shippingOrderStatus", Arrays.asList(EnumShippingOrderStatus.SO_Shipped.getId(), EnumShippingOrderStatus.SO_Delivered.getId())).uniqueResult();
        if(inventory != null) {
            return inventory;
        }
        return 0L;
    }

    public List<ExpiryAlertReportDto> getToBeExpiredProductDetails(Date startDate, Date endDate, Warehouse warehouse) {
        String query = "select sg as skuGroup, sum(pvi.qty) as batchQty from SkuGroup sg, ProductVariantInventory pvi where sg.expiryDate between :startDate and :endDate " +
                "and pvi.sku.warehouse = :warehouse and pvi.skuItem.skuGroup.id = sg.id group by sg having sum(pvi.qty) > 0";

        return (List<ExpiryAlertReportDto>)getSession().createQuery(query).setParameter("startDate", startDate)
                .setParameter("endDate", endDate).setParameter("warehouse", warehouse).setResultTransformer(Transformers.aliasToBean(ExpiryAlertReportDto.class)).list();
    }

    public Long getOpeningStockOfProductVariantOnDate(String productVariant, Date txnDate, Warehouse warehouse) {
        String query = "select coalesce(sum(pvi.qty), 0) from ProductVariantInventory pvi where pvi.sku.productVariant.id = :productVariant and pvi.txnDate < :txnDate " +
                "and pvi.sku.warehouse = :warehouse ";
        return (Long) findUniqueByNamedParams(query, new String[]{"productVariant", "txnDate", "warehouse"}, new Object[]{productVariant, txnDate, warehouse});
    }

    public List<StockReportDto> getProductVariantStockBetweenDates(String productVariant, Date startDate, Date endDate, Warehouse warehouse) {
        String query = "select coalesce(sum(pvi.qty), 0) as inventoryQty, pvi.invTxnType.id as inventoryTxnType from ProductVariantInventory pvi where pvi.sku.productVariant.id = :productVariant " +
                "and pvi.txnDate between :startDate and :endDate " +
                "and pvi.sku.warehouse = :warehouse group by pvi.invTxnType ";
        return getSession().createQuery(query).setParameter("productVariant", productVariant)
                .setParameter("startDate", startDate).setParameter("endDate", endDate)
                .setParameter("warehouse", warehouse).setResultTransformer(Transformers.aliasToBean(StockReportDto.class)).list();
    }

    public Long getStockLeftQty(String productVariant, Warehouse warehouse) {
        String query = "select coalesce(sum(pvi.qty), 0) from ProductVariantInventory pvi " +
                " where pvi.sku.productVariant.id = :productVariant and pvi.sku.warehouse = :warehouse ";
        return (Long) findUniqueByNamedParams(query, new String[]{"productVariant", "warehouse"}, new Object[]{productVariant, warehouse});
    }

    public Long getDamageRtoCheckedInQty(String productVariant, Date startDate, Date endDate, Warehouse warehouse) {
        String query = "select coalesce(sum(pvi.qty), 0) from ProductVariantDamageInventory pvi where pvi.sku.productVariant.id = :productVariant " +
                " and pvi.txnDate between :startDate and :endDate " +
                "and pvi.sku.warehouse = :warehouse ";
        return (Long) findUniqueByNamedParams(query, new String[]{"productVariant", "startDate", "endDate", "warehouse"}, new Object[]{productVariant, startDate, endDate, warehouse});
    }

    public List<RTOFineReportDto> getRTOFineProductVariantDetails(ShippingOrder shippingOrder) {
        String query = "select pvi.sku.productVariant as productVariant, count(pvi.id) as rtoCheckinCount from ProductVariantInventory pvi  " +
                "where pvi.shippingOrder = :shippingOrder and pvi.qty = :qty group by pvi.lineItem ";
        return (List<RTOFineReportDto>) getSession().createQuery(query).setParameter("shippingOrder", shippingOrder).setParameter("qty", 1L)
                .setResultTransformer(Transformers.aliasToBean(RTOFineReportDto.class)).list();
    }

    public List<RTODamageReportDto> getRTODamageProductVariantDetails(ShippingOrder shippingOrder) {
        String query = "select pvdi.sku.productVariant as productVariant ,count(pvdi.id) as rtoDamageCheckinCount from ProductVariantDamageInventory pvdi " +
                "where pvdi.shippingOrder = :shippingOrder and pvdi.qty = :qty group by pvdi.lineItem ";
        return (List<RTODamageReportDto>) getSession().createQuery(query).setParameter("shippingOrder", shippingOrder).setParameter("qty", 1L)
                .setResultTransformer(Transformers.aliasToBean(RTODamageReportDto.class)).list();
    }

     public List<ShippingOrder> getShippingOrdersByReturnDate(Date startDate, Date endDate, EnumShippingOrderStatus shippingOrderStatus,Warehouse warehouse) {
      String query = "from ShippingOrder so where so.shipment.returnDate between :startDate and :endDate and so.shippingOrderStatus.id = :shippingOrderStatus and so.warehouse= :warehouse";
        return  (List<ShippingOrder>)getSession().createQuery(query).setParameter("startDate", startDate)
                .setParameter("endDate", endDate).setParameter("shippingOrderStatus", shippingOrderStatus.getId()).setParameter("warehouse",warehouse).list();
    }

    public List<RVReportDto> getReconciliationVoucherDetail(String productVariantId, Warehouse warehouse, Date startDate, Date endDate) {
        String query = "select coalesce(sum(pvi.qty), 0) as qtyRV, rv as reconciliationVoucher, pvi as productVariantInventory " +
                " from ProductVariantInventory pvi join pvi.rvLineItem rvli join rvli.reconciliationVoucher rv " +
                " where pvi.sku.productVariant.id = :productVariant and pvi.txnDate between :startDate and :endDate " +
                " and pvi.sku.warehouse = :warehouse group by rv ";

        return getSession().createQuery(query).setParameter("productVariant", productVariantId)
                .setParameter("warehouse", warehouse).setParameter("startDate", startDate).setParameter("endDate", endDate)
                .setResultTransformer(Transformers.aliasToBean(RVReportDto.class)).list();
    }

    public List<GrnLineItem> getGrnLineItemForPurchaseOrder(ProductVariant productVariant, Warehouse warehouse, Date startDate, Date endDate) {
        /*String query1 = "select po from PurchaseOrder po join po.goodsReceivedNotes grn join grn.grnLineItems grnli join grnli.sku sku join sku.productVariant pv " +
                " where pv = :productVariant and grn.grnDate between :startDate and :endDate";
*/
        String sql = "select grnli from GrnLineItem grnli join grnli.goodsReceivedNote grn join grn.purchaseOrder po " +
                " where grnli.sku.warehouse = :warehouse and grn.grnDate between :startDate and :endDate";
        if(productVariant != null) {
            sql = sql + " and grnli.sku.productVariant = :productVariant ";
        }
        Query query = getSession().createQuery(sql).setParameter("startDate", startDate).setParameter("warehouse", warehouse).setParameter("endDate", endDate);
        if(productVariant != null){
            query.setParameter("productVariant", productVariant);

        }
        return  query.list();
    }

}
