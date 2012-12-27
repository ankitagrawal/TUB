package com.hk.impl.dao.sku;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Projections;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.inventory.ProductVariantInventory;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.dao.sku.SkuGroupDao;

@SuppressWarnings("unchecked")
@Repository
public class SkuItemDaoImpl extends BaseDaoImpl implements SkuItemDao {
	@Autowired
	SkuGroupDao skuGroupDao;

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

	public SkuGroup getMinMRPUnbookedSkuGroup(ProductVariant productVariant, Long bookedQty) {
		SkuGroup minMRPUnbookedSkuGroup = null;
		String skuItemListQuery = "select pvi.skuItem.id from ProductVariantInventory pvi where pvi.skuItem is not null and pvi.skuItem.skuGroup.mrp is not null and pvi.sku.productVariant = :productVariant group by pvi.skuItem.id having sum(pvi.qty) > 0 order by pvi.skuItem.skuGroup.mrp asc";
		List<Long> skuItemIdList = (List<Long>) getSession().createQuery(skuItemListQuery).setParameter("productVariant", productVariant).list();
		if (skuItemIdList != null && skuItemIdList.size() > bookedQty) {
			List<Long> firstUnBookedSkuItem = skuItemIdList.subList(bookedQty.intValue(), bookedQty.intValue()+1);
			String query = "select distinct si.skuGroup from SkuItem si where si.id = :skuItemId order by si.skuGroup.mrp asc";
			minMRPUnbookedSkuGroup = (SkuGroup) getSession().createQuery(query).setParameter("skuItemId", firstUnBookedSkuItem.get(0)).uniqueResult();
		}
		return minMRPUnbookedSkuGroup;
	}

	public List<SkuItem> getInStockSkuItem(SkuGroup skuGroup) {
		//replace with Criteria query
		List<SkuItem> skuItemListFinal = new ArrayList<SkuItem>();
		List<SkuItem> skuItemList = new ArrayList<SkuItem>(skuGroup.getSkuItems());
		String hqlQuery = "select pvi.skuItem.id from ProductVariantInventory pvi where pvi.skuItem is not null and pvi.skuItem in (:skuItemList)" +
				" group by pvi.skuItem.id having sum(qty) > 0";
		 List<Long>  skuItemIdList = getSession().createQuery(hqlQuery).setParameterList("skuItemList", skuItemList).list();
		if(skuItemIdList != null && skuItemIdList.size() > 0) {
		String query ="from SkuItem sk where sk.id in(:skuIdList)";
		skuItemListFinal = getSession().createQuery(query).setParameterList("skuIdList", skuItemIdList).list();
		}
		return skuItemListFinal;
	}

}











