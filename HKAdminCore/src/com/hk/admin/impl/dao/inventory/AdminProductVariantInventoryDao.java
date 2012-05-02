package com.hk.admin.impl.dao.inventory;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.inventory.ProductVariantInventory;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;

@SuppressWarnings("unchecked")
@Repository
public class AdminProductVariantInventoryDao extends BaseDaoImpl {


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

    public void removeInventory(SkuItem skuItem) {
        getSession().createQuery("delete from ProductVariantInventory pvi where pvi.skuItem = :skuItem").setParameter("skuItem", skuItem).executeUpdate();
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

    public List<ProductVariantInventory> getCheckedOutSkuItems(ShippingOrder shippingOrder, Sku sku) {
        return (List<ProductVariantInventory>) getSession().createQuery(
                "from ProductVariantInventory pvi where pvi.sku = :sku and pvi.qty = :qty and pvi.shippingOrder = :shippingOrder").setParameter("sku", sku).setParameter("qty", -1L).setParameter(
                "shippingOrder", shippingOrder).list();
    }

    public List<ProductVariantInventory> getCheckedOutSkuItems(ShippingOrder shippingOrder, LineItem lineItem) {
        return (List<ProductVariantInventory>) getSession().createQuery(
                "from ProductVariantInventory pvi where pvi.sku = :sku and pvi.qty = :qty and pvi.shippingOrder = :shippingOrder order by pvi.id desc").setParameter("sku",
                lineItem.getSku()).setParameter("qty", -1L).setParameter("shippingOrder", shippingOrder).setMaxResults(lineItem.getQty().intValue()).list();
    }

    public List<ProductVariantInventory> getCheckedOutSkuItems(ShippingOrder shippingOrder) {
        return (List<ProductVariantInventory>) getSession().createQuery("from ProductVariantInventory pvi where pvi.qty = :qty and pvi.shippingOrder = :shippingOrder").setParameter(
                "qty", -1L).setParameter("shippingOrder", shippingOrder).list();
    }

}