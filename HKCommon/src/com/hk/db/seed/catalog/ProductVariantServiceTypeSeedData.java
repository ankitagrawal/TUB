package com.hk.db.seed.catalog;


import java.util.ArrayList;
import java.util.List;

import com.hk.constants.catalog.product.EnumProductVariantServiceType;
import com.hk.domain.core.ProductVariantServiceType;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class ProductVariantServiceTypeSeedData {

  ProductVariantServiceTypeDao productVariantServiceTypeDao;

  public void insert(java.lang.String name, java.lang.Long id) {
    ProductVariantServiceType productVariantServiceType = new ProductVariantServiceType();
      productVariantServiceType.setName(name);
      productVariantServiceType.setId(id);
    productVariantServiceTypeDao.save(productVariantServiceType);
  }

  public void invokeInsert(){
    List<Long> pkList = new ArrayList<Long>();

    for (EnumProductVariantServiceType enumProductVariantServiceType : EnumProductVariantServiceType.values()) {

      if (pkList.contains(enumProductVariantServiceType.getId())) throw new RuntimeException("Duplicate key "+enumProductVariantServiceType.getId());
      else pkList.add(enumProductVariantServiceType.getId());

      insert(enumProductVariantServiceType.getName(), enumProductVariantServiceType.getId());
    }
  }

}
