package com.hk.impl.dao.sku;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.sku.SkuItemDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
@Repository
public class SkuItemDaoImpl extends BaseDaoImpl implements SkuItemDao {

  public List<SkuGroup> getInStockSkuGroups(Sku sku) {
     List<SkuGroup> skuGroupList = new ArrayList<SkuGroup>();
     String skuItemListQuery = "select pvi.skuItem.id from ProductVariantInventory pvi where pvi.skuItem is not null " +
         "and pvi.sku = :sku group by pvi.skuItem.id having sum(pvi.qty) > 0";
     List<Long> skuItemIdList = (List<Long>) getSession().createQuery(skuItemListQuery)
         .setParameter("sku", sku)
         .list();
     if (skuItemIdList != null && skuItemIdList.size() > 0) {
       String query = "select distinct si.skuGroup from SkuItem si where si.id in (:skuItemIdList) " +
                    "and si.skuGroup.sku = :sku order by si.skuGroup.expiryDate asc ";

       skuGroupList = (List<SkuGroup>) getSession().createQuery(query)
           .setParameterList("skuItemIdList", skuItemIdList)
           .setParameter("sku", sku)
           .list();
     }
     return skuGroupList;
   }
 

}











