package com.hk.admin.impl.dao.inventory;

import com.hk.admin.dto.inventory.CreateInventoryFileDto;
import com.hk.admin.pact.dao.inventory.AdminProductVariantInventoryDao;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.VariantConfig;
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
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
                new String[]{"skuItem"}, new Object[]{skuItem});

        if (pvi != null) {
            delete(pvi);
        }
        // getSession().createQuery("delete ").setParameter("skuItem", skuItem).executeUpdate();
    }

    public Long getCheckedInPVIAgainstReturn(LineItem lineItem) {
        return (Long) getSession().createQuery("select count(pvi.id) from ProductVariantInventory pvi where pvi.lineItem = :lineItem " + "and pvi.invTxnType.id " +
                "in(" + EnumInvTxnType.RETURN_CHECKIN_GOOD.getId() + "," + EnumInvTxnType.RETURN_CHECKIN_DAMAGED.getId() + "," +
                EnumInvTxnType.RETURN_CHECKIN_EXPIRED.getId() + ")").setParameter("lineItem", lineItem).uniqueResult();
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
        Long netInvForSkuItem = (Long) getSession().createQuery("select sum(pvi.qty) from ProductVariantInventory pvi where pvi.sku = :sku and pvi.shippingOrder = :shippingOrder and pvi.lineItem = :lineItem").setParameter(
                "sku", lineItem.getSku()).setParameter("shippingOrder", shippingOrder).setParameter("lineItem", lineItem).uniqueResult();
        if (netInvForSkuItem != null && netInvForSkuItem <= -1L) {
            isCheckedForSkuItem = true;
        }

        if (isCheckedForSkuItem) {
            checkedOutPVI = (List<ProductVariantInventory>) getSession().createQuery(
                    "from ProductVariantInventory pvi where pvi.sku = :sku and pvi.qty = :qty and pvi.shippingOrder = :shippingOrder and pvi.lineItem = :lineItem order by pvi.id desc").setParameter("sku",
                    lineItem.getSku()).setParameter("qty", -1L).setParameter("shippingOrder", shippingOrder).setParameter("lineItem", lineItem).setMaxResults(lineItem.getQty().intValue()).list();
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


//    public List<CreateInventoryFileDto> getDetailsForUncheckedItems(String brand, Warehouse warehouse) {
//           String sql = "select pvi as productVariantInventory, sg as skuGroup, sg.barcode as barcode, p.name as name, sg.expiryDate as expiryDate, sum(pvi.qty) as sumQty, pv as productVariant "
//                   + "from ProductVariantInventory pvi join pvi.skuItem si join si.skuGroup sg "
//                   + "join sg.sku s join s.productVariant pv join pv.product p where p.brand = :brand ";
//           if (warehouse != null) {
//               sql = sql + " and s.warehouse = :warehouse ";
//           }
//           sql = sql + " group by sg.id having sum(pvi.qty) > 0";
//
//           Query query = getSession().createQuery(sql).setParameter("brand", brand);
//           if (warehouse != null) {
//               query.setParameter("warehouse", warehouse);
//           }
//           query.setResultTransformer(Transformers.aliasToBean(CreateInventoryFileDto.class)).list();
//
//           return query.list();
//       }
//

    public List<CreateInventoryFileDto> getDetailsForUncheckedItems(String brand, Warehouse warehouse) {
        Long checkedInSkuItemStatus = EnumSkuItemStatus.Checked_IN.getId();
        String sql = "select si as skuItem, sg as skuGroup ,si.barcode as barcode, pv as productVariant, p as product,p.name as name, sg.expiryDate as expiryDate, count(si.id) as sumQty"/*,  sg as skuGroup, pv as productVariant, p as product, si.barcode as barcode, p.name as name, sg.expiryDate as expiryDate, count(si.id) as sumQty "*/
                + " from SkuItem si join si.skuItemStatus as sis join si.skuGroup sg join sg.sku s join s.productVariant pv join pv.product p "
                + " where sis.id = :checkedInSkuItemStatus ";

        if (brand != null) {
            sql = sql + " and  p.brand = :brand ";
        }
        if (warehouse != null) {
            sql = sql + " and s.warehouse = :warehouse ";
        }
        sql = sql + " group by si.id ";
        Query query = getSession().createQuery(sql);

        if (brand != null) {
            query.setParameter("brand", brand);
        }
        if (warehouse != null) {
            query.setParameter("warehouse", warehouse);
        }
        query.setParameter("checkedInSkuItemStatus", checkedInSkuItemStatus);

        return query.setResultTransformer(Transformers.aliasToBean(CreateInventoryFileDto.class)).list();

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

    /*public List<CreateInventoryFileDto> getDetailsForUncheckedItems(String brand) {
        return getDetailsForUncheckedItems(brand, null);
    }*/

    /*public List<CreateInventoryFileDto> getDetailsForUncheckedItems(String brand, Warehouse warehouse) {
         List<CreateInventoryFileDto> createInventoryFileDtoList = new ArrayList<CreateInventoryFileDto>();

         //GRN
         String sql = "select sg.barcode as barcode, p.name as name, sg.expiryDate as expiryDate, sg.batchNumber as batchNumber," +
           " sum(pvi.qty) as sumQty, grnli.mrp as markedPrice, pv as productVariant "
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
         List<CreateInventoryFileDto> createInventoryFileDtoListForGRN = query.setResultTransformer(Transformers.aliasToBean(CreateInventoryFileDto.class)).list();
         if (createInventoryFileDtoListForGRN != null && !createInventoryFileDtoListForGRN.isEmpty()) {
             createInventoryFileDtoList.addAll(createInventoryFileDtoListForGRN);
         }

         //RV
         String sql2 = "select sg.barcode as barcode, p.name as name, sg.expiryDate as expiryDate, sg.batchNumber as batchNumber," +
           " sum(pvi.qty) as sumQty, rvli.mrp as markedPrice, pv as productVariant "
           + "from ProductVariantInventory pvi join pvi.skuItem si join si.skuGroup sg join sg.reconciliationVoucher rv join rv.rvLineItems rvli "
           + "join sg.sku s join s.productVariant pv join pv.product p where p.brand = :brand and rvli.sku.id = s.id ";
         if (warehouse != null) {
             sql2 = sql2 + " and s.warehouse = :warehouse ";
         }
         sql2 = sql2 + " group by si.skuGroup having sum(pvi.qty) > 0";

         Query query2 = getSession().createQuery(sql2).setParameter("brand", brand);
         if (warehouse != null) {
             query2.setParameter("warehouse", warehouse);
         }
         List<CreateInventoryFileDto> createInventoryFileDtoListForRV = query2.setResultTransformer(Transformers.aliasToBean(CreateInventoryFileDto.class)).list();
         if (createInventoryFileDtoListForRV != null && !createInventoryFileDtoListForRV.isEmpty()) {
             createInventoryFileDtoList.addAll(createInventoryFileDtoListForRV);
         }

         //ST
         String sql3 = "select sg.barcode as barcode, p.name as name, sg.expiryDate as expiryDate, sg.batchNumber as batchNumber," +
           " sum(pvi.qty) as sumQty, stli.mrp as markedPrice, pv as productVariant "
           + "from ProductVariantInventory pvi join pvi.skuItem si join si.skuGroup sg join sg.stockTransfer st join st.stockTransferLineItems stli "
           + "join sg.sku s join s.productVariant pv join pv.product p where p.brand = :brand and stli.sku.id = s.id ";
         if (warehouse != null) {
             sql3 = sql3 + " and s.warehouse = :warehouse ";
         }
         sql3 = sql3 + " group by si.skuGroup having sum(pvi.qty) > 0";

         Query query3 = getSession().createQuery(sql3).setParameter("brand", brand);
         if (warehouse != null) {
             query3.setParameter("warehouse", warehouse);
         }
         List<CreateInventoryFileDto> createInventoryFileDtoListForST = query3.setResultTransformer(Transformers.aliasToBean(CreateInventoryFileDto.class)).list();
         if (createInventoryFileDtoListForST != null && !createInventoryFileDtoListForST.isEmpty()) {
             createInventoryFileDtoList.addAll(createInventoryFileDtoListForST);
         }

         return createInventoryFileDtoList;
     }*/

    public Long getCheckedinItemCountForStockTransferLineItem(StockTransferLineItem stockTransferLineItem) {
        String query = "select count(pvi.id) from ProductVariantInventory pvi where pvi.stockTransferLineItem = :stockTransferLineItem and pvi.qty = :checkedInQty";
        return (Long) getSession().createQuery(query).setParameter("stockTransferLineItem", stockTransferLineItem).setLong("checkedInQty", 1L).uniqueResult();
    }

    public void updateProductVariantsConfig(String id, Long variantconfigId) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ProductVariant.class);
        detachedCriteria.add(Restrictions.eq("id", id));
        ProductVariant productVariant = (ProductVariant) findByCriteria(detachedCriteria).get(0);

        DetachedCriteria detachedCriteriaVariantConfig = DetachedCriteria.forClass(VariantConfig.class);
        detachedCriteriaVariantConfig.add(Restrictions.eq("id", variantconfigId));
        VariantConfig variantConfig = (VariantConfig) findByCriteria(detachedCriteriaVariantConfig).get(0);

        productVariant.setVariantConfig(variantConfig);
        save(productVariant);

    }

    public List<VariantConfig> getAllVariantConfig() {
        return getAll(VariantConfig.class);
    }

    public List<SkuItem> getCheckedInOrOutSkuItems(RvLineItem rvLineItem, StockTransferLineItem stockTransferLineItem, GrnLineItem grnLineItem, LineItem lineItem, Long transferQty) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ProductVariantInventory.class);
        if (rvLineItem != null) {
            criteria.add(Restrictions.eq("rvLineItem", rvLineItem));
        }
        if (stockTransferLineItem != null) {
            criteria.add(Restrictions.eq("stockTransferLineItem", stockTransferLineItem));
        }
        if (grnLineItem != null) {
            criteria.add(Restrictions.eq("grnLineItem", grnLineItem));
        }

        if (lineItem != null) {
            criteria.add(Restrictions.eq("lineItem", lineItem));
        }
        criteria.add(Restrictions.eq("qty", transferQty));
        criteria.setProjection(Projections.distinct(Projections.property("skuItem")));
        return (List<SkuItem>) findByCriteria(criteria);

    }


    public List<CreateInventoryFileDto> getCheckedInSkuGroup(String brand, Warehouse warehouse, Product product, ProductVariant productVariant) {

        Long checkedInSkuItemStatus = EnumSkuItemStatus.Checked_IN.getId();
        String productId = null;
        String productVariantId = null;
        if (product != null) {
            productId = product.getId();
        }
        if (productVariant != null) {
            productVariantId = productVariant.getId();
        }
        String sql = "select sg as skuGroup, pv as productVariant, p as product, sg.barcode as barcode, p.name as name, sg.expiryDate as expiryDate, count(si.id) as sumQty "
                + " from SkuItem si join si.skuGroup sg  join si.skuItemStatus as sis "
                + " join sg.sku s join s.productVariant pv join pv.product p where sis.id = :checkedInSkuItemStatus ";

        if (productId != null) {
            sql = sql + " and p.id = :productId ";
        }

        if (productVariantId != null) {
            sql = sql + " and pv.id = :productVariantId ";
        }

        if (brand != null) {
            sql = sql + " and  p.brand = :brand ";
        }
        if (warehouse != null) {
            sql = sql + " and s.warehouse = :warehouse ";
        }

        sql = sql + " group by sg.id ";

        Query query = getSession().createQuery(sql);
        if (product != null) {
            query.setParameter("productId", productId);
        }

        if (productVariantId != null) {
            query.setParameter("productVariantId", productVariantId);
        }

        if (brand != null) {
            query.setParameter("brand", brand);
        }
        if (warehouse != null) {
            query.setParameter("warehouse", warehouse);
        }

        query.setParameter("checkedInSkuItemStatus", checkedInSkuItemStatus);

        return query.setResultTransformer(Transformers.aliasToBean(CreateInventoryFileDto.class)).list();

    }

    @Transactional
    public void deletePVIBySkuItem(List<SkuItem> skuItemList) {
        List<ProductVariantInventory> productVariantInventoryList = getSession().createQuery("from ProductVariantInventory pvi where pvi.skuItem in(:skuItemList)")
                .setParameterList("skuItemList", skuItemList).list();
        if (productVariantInventoryList != null && productVariantInventoryList.size() > 0) {
            deleteAll(productVariantInventoryList);
        }

    }

}