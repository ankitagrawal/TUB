package com.hk.dao.sku;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;

@Repository
public class SkuDao extends BaseDaoImpl {


  public Sku getSku(ProductVariant productVariant, Warehouse warehouse) {
	  String queryString = "from Sku sku where sku.warehouse = ? and sku.productVariant = ? ";
	  return (Sku) find(queryString, new Object[]{warehouse, productVariant});
  }

   @SuppressWarnings("unchecked")
   public Sku getMaxVATSku(ProductVariant productVariant) {
	   String queryString = "from Sku sku where sku.productVariant = ? order by sku.tax.value asc";
	   List<Sku> skuList = find(queryString, new Object[] {productVariant});
	   if(skuList != null && skuList.size() > 0){
		   return skuList.get(0);
	   } 
	   return null;
  }

  @SuppressWarnings("unchecked")
  public List<Sku> getSkus(ProductVariant productVariant) {
	  String queryString = " from Sku sku where sku.productVariant = ? ";
	  return  (List<Sku>) find(queryString, new Object[] {productVariant});

  }

  
  @SuppressWarnings("unchecked")
  public List<Sku> filterProductVariantsByWarehouse(List<ProductVariant> productVariants, Warehouse warehouse) {
    DetachedCriteria criteria = DetachedCriteria.forClass(Sku.class);
    criteria.add(Restrictions.in("productVariant", productVariants));
    criteria.add(Restrictions.eq("warehouse", warehouse));
    return (List<Sku>) findByCriteria(criteria);
  }


}

