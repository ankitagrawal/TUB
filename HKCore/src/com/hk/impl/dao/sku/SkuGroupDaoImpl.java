package com.hk.impl.dao.sku;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.sku.SkuGroupDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
@Repository
public class SkuGroupDaoImpl extends BaseDaoImpl implements SkuGroupDao {

    public List<SkuGroup> getAllCheckedInBatches(ProductVariant productVariant) {
        return (List<SkuGroup>) getSession().createQuery("from SkuGroup sg where sg.sku.productVariant = :productVariant").setParameter("productVariant", productVariant).list();
    }

    public List<SkuGroup> getAllCheckedInBatches(Sku sku) {
        return (List<SkuGroup>) getSession().createQuery("from SkuGroup sg where sg.sku = :sku").setParameter("sku", sku).list();
    }

    public void resetInventoryByBrand(String brand) {
        List<Long> toBeRemovedIds = (List<Long>) getSession().createQuery("select id from SkuGroup sg where sg.sku.productVariant.product.brand = :brand").setParameter("brand",
                brand).list();
        if (toBeRemovedIds != null && !toBeRemovedIds.isEmpty()) {
            getSession().createQuery("delete from SkuGroup sg where sg.id in (:toBeRemovedIds)").setParameterList("toBeRemovedIds", toBeRemovedIds).executeUpdate();
        }
    }
    
    public SkuGroup getSkuGroup(String barcode) {
        List<SkuGroup> skuGroups = getSession().
           createQuery("from SkuGroup sg where sg.barcode = :barcode").
           setParameter("barcode", barcode).
           list();
       return skuGroups != null && !skuGroups.isEmpty() ? skuGroups.get(0) : null;
     }

    public void resetInventory(ProductVariant productVariant) {
        List<Long> toBeRemovedIds = (List<Long>) getSession().createQuery("select id from SkuGroup sg where sg.sku.productVariant = :productVariant").setParameter(
                "productVariant", productVariant).list();
        if (toBeRemovedIds != null && !toBeRemovedIds.isEmpty()) {
            getSession().createQuery("delete from SkuGroup sg where sg.id in (:toBeRemovedIds)").setParameterList("toBeRemovedIds", toBeRemovedIds).executeUpdate();
        }
    }

  public List<SkuGroup> getCurrentCheckedInBatchGrn(GoodsReceivedNote grn , Sku sku) {
           return (List<SkuGroup>) getSession().
         createQuery("from SkuGroup sg where sg.goodsReceivedNote = :grn and sg.sku = :sku").
           setParameter("grn" , grn).
         setParameter("sku",sku ).
           list();
   }


  public List<SkuGroup> getCurrentCheckedInBatchNotInGrn(GoodsReceivedNote grn, Sku sku) {
    return (List<SkuGroup>) getSession().
        createQuery("from SkuGroup sg where sg.goodsReceivedNote != :grn and sg.sku = :sku").
        setParameter("grn", grn).
        setParameter("sku", sku).
        list();
  }

  public List<SkuGroup> getInStockSkuGroupByQty(Sku sku) {
    List<SkuGroup> inStockSkuItems = new ArrayList<SkuGroup>();
    String inStockSkuItemIdQuery = "select pvi.skuItem.id from ProductVariantInventory pvi where pvi.sku =:sku " +
        "group by pvi.skuItem.id having sum(pvi.qty) > 0";
    List<Long> inStockSkuItemIds = (List<Long>) getSession().createQuery(inStockSkuItemIdQuery)
        .setParameter("sku", sku)
        .list();
    if (inStockSkuItemIds != null && inStockSkuItemIds.size() > 0) {
      String query = "select si.skuGroup from SkuItem si where si.id in (:inStockSkuItemIds) order by si.skuGroup.id";
      inStockSkuItems = (List<SkuGroup>) getSession().createQuery(query)
          .setParameterList("inStockSkuItemIds", inStockSkuItemIds)
          .list();
    }
    return inStockSkuItems;

  }

}
