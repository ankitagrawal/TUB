package com.hk.admin.impl.dao.inventory;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.hk.admin.dto.inventory.CreateInventoryFileDto;
import com.hk.admin.pact.dao.inventory.AdminProductVariantInventoryDao;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.inventory.ProductVariantInventory;
import com.hk.domain.inventory.StockTransferLineItem;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;

@SuppressWarnings("unchecked")
@Repository
public class AdminProductVariantInventoryDaoImpl extends BaseDaoImpl implements AdminProductVariantInventoryDao {

    public Long getCheckedoutItemCount(LineItem lineItem) {
        String query = "select sum(pvi.qty) from ProductVariantInventory pvi where pvi.lineItem = :lineItem";
        return (Long) getSession().createQuery(query).setParameter("lineItem", lineItem).uniqueResult();
    }

    public Long getChechedinItemCount(GrnLineItem grnLineItem) {
        String query = "select count(pvi.id) from ProductVariantInventory pvi where pvi.grnLineItem = :grnLineItem and pvi.qty = :checkedInQty";
        return (Long) getSession().createQuery(query).setParameter("grnLineItem", grnLineItem).setLong("checkedInQty", 1L).uniqueResult();
    }

    public void resetInventoryByBrand(String brand) {

        List<Long> toBeRemovedIds = (List<Long>) getSession().createQuery("select id from ProductVariantInventory pvi where pvi.sku.productVariant.product.brand = :brand").setParameter(
                "brand", brand).list();
        if (toBeRemovedIds != null && !toBeRemovedIds.isEmpty()) {
            getSession().createQuery("delete from ProductVariantInventory pvi where pvi.id in (:toBeRemovedIds)").setParameterList("toBeRemovedIds", toBeRemovedIds).executeUpdate();
        }
    }

    public List<ProductVariantInventory> getPVIForStockTransfer(Sku sku, StockTransferLineItem stockTransferLineItem) {
        return (List<ProductVariantInventory>) getSession().createQuery(
                "from ProductVariantInventory pvi where pvi.sku = :sku and pvi.stockTransferLineItem = :stockTransferLineItem").setParameter("sku", sku).setParameter(
                "stockTransferLineItem", stockTransferLineItem).list();
    }

    public void removeInventory(SkuItem skuItem) {

        ProductVariantInventory pvi = (ProductVariantInventory) findUniqueByNamedParams("from ProductVariantInventory pvi where pvi.skuItem = :skuItem",
                new String[] { "skuItem" }, new Object[] { skuItem });

        if (pvi != null) {
            delete(pvi);
        }
        // getSession().createQuery("delete ").setParameter("skuItem", skuItem).executeUpdate();
    }

    public Long getCheckedInPVIAgainstRTO(LineItem lineItem) {
        return (Long) getSession().createQuery("select count(pvi.id) from ProductVariantInventory pvi where pvi.lineItem = :lineItem and pvi.qty = :qty").setParameter("lineItem",
                lineItem).setParameter("qty", 1L).uniqueResult();
    }

    public List<ProductVariantInventory> getPVIForRV(Sku sku, RvLineItem rvLineItem) {
        return (List<ProductVariantInventory>) getSession().createQuery("from ProductVariantInventory pvi where pvi.sku = :sku and pvi.rvLineItem = :rvLineItem").setParameter(
                "sku", sku).setParameter("rvLineItem", rvLineItem).list();
    }

    public List<ProductVariantInventory> getPVIByLineItem(LineItem lineItem) {
        Criteria criteria = getSession().createCriteria(ProductVariantInventory.class);
        criteria.add(Restrictions.eq("lineItem", lineItem));
        return criteria.list();
    }

    public List<ProductVariantInventory> getCheckedOutSkuItems(ShippingOrder shippingOrder, LineItem lineItem) {
        boolean isCheckedForSkuItem = false;
        List<ProductVariantInventory> checkedOutPVI = new ArrayList<ProductVariantInventory>();
        Long netInvForSkuItem = (Long) getSession().createQuery("select sum(pvi.qty) from ProductVariantInventory pvi where pvi.sku = :sku and pvi.shippingOrder = :shippingOrder").setParameter(
                "sku", lineItem.getSku()).setParameter("shippingOrder", shippingOrder).uniqueResult();
        if (netInvForSkuItem != null && netInvForSkuItem <= -1L) {
            isCheckedForSkuItem = true;
        }

        if (isCheckedForSkuItem) {
            checkedOutPVI = (List<ProductVariantInventory>) getSession().createQuery(
                    "from ProductVariantInventory pvi where pvi.sku = :sku and pvi.qty = :qty and pvi.shippingOrder = :shippingOrder order by pvi.id desc").setParameter("sku",
                    lineItem.getSku()).setParameter("qty", -1L).setParameter("shippingOrder", shippingOrder).setMaxResults(lineItem.getQty().intValue()).list();
        }

        return checkedOutPVI;
    }

    /*
     * public List<ProductVariantInventory> getCheckedOutSkuItems(ShippingOrder shippingOrder, LineItem lineItem) {
     * return (List<ProductVariantInventory>) getSession().createQuery( "from ProductVariantInventory pvi where pvi.sku =
     * :sku and pvi.qty = :qty and pvi.shippingOrder = :shippingOrder order by pvi.id desc").setParameter("sku",
     * lineItem.getSku()).setParameter("qty", -1L).setParameter("shippingOrder",
     * shippingOrder).setMaxResults(lineItem.getQty().intValue()).list(); }
     */

    /*
     * public List<ProductVariantInventory> getCheckedOutSkuItems(ShippingOrder shippingOrder) { return (List<ProductVariantInventory>)
     * getSession().createQuery("from ProductVariantInventory pvi where pvi.qty = :qty and pvi.shippingOrder =
     * :shippingOrder").setParameter( "qty", -1L).setParameter("shippingOrder", shippingOrder).list(); }
     */

    public List<CreateInventoryFileDto> getDetailsForUncheckedItems(String brand) {
        return getDetailsForUncheckedItems(brand, null);
    }

    public List<CreateInventoryFileDto> getDetailsForUncheckedItems(String brand, Warehouse warehouse) {
        String sql = "select sg as skuGroup, sg.barcode as barcode, p.name as name, sg.expiryDate as expiryDate, sum(pvi.qty) as sumQty, grnli.mrp as markedPrice, pv as productVariant "
                + "from ProductVariantInventory pvi join pvi.skuItem si join si.skuGroup sg join sg.goodsReceivedNote grn join grn.grnLineItems grnli "
                + "join sg.sku s join s.productVariant pv join pv.product p where p.brand = :brand and grnli.sku.id = s.id ";
        if (warehouse != null) {
            sql = sql + " and s.warehouse = :warehouse ";
        }
        sql = sql + " group by si.skuGroup having sum(pvi.qty) > 0";

        Query query = getSession().createQuery(sql).setParameter("brand", brand);
        if (warehouse != null) {
            query.setParameter("warehouse", warehouse);
        }
        query.setResultTransformer(Transformers.aliasToBean(CreateInventoryFileDto.class)).list();

        return query.list();
    }

    public Long getCheckedinItemCountForStockTransferLineItem(StockTransferLineItem stockTransferLineItem) {
        String query = "select count(pvi.id) from ProductVariantInventory pvi where pvi.stockTransferLineItem = :stockTransferLineItem and pvi.qty = :checkedInQty";
        return (Long) getSession().createQuery(query).setParameter("stockTransferLineItem", stockTransferLineItem).setLong("checkedInQty", 1L).uniqueResult();
    }

}