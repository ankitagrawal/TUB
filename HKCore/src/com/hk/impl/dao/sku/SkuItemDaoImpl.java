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
import com.hk.domain.sku.SkuItemStatus;
import com.hk.domain.inventory.ProductVariantInventory;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.dao.sku.SkuGroupDao;
import com.hk.constants.sku.EnumSkuItemStatus;

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


		private DetachedCriteria getSkuItemCriteria(SkuGroup skuGroup, SkuItemStatus skuItemStatus) {
			DetachedCriteria skuItemCriteria = DetachedCriteria.forClass(SkuItem.class);
			if (skuGroup != null) {
				skuItemCriteria.add(Restrictions.eq("skuGroup", skuGroup));
			}
			if (skuItemStatus != null) {
				skuItemCriteria.add(Restrictions.eq("skuItemStatus", skuItemStatus));
			}
			return skuItemCriteria;
		}


		public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup) {
			DetachedCriteria skuItemCriteria = getSkuItemCriteria(skuGroup, EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
			return findByCriteria(skuItemCriteria);
		}


}











