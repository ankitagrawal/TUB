package com.hk.db.seed.marketing;


import com.hk.domain.marketing.ProductReferrer;
import com.hk.db.seed.BaseSeedData;
import com.hk.constants.marketing.EnumProductReferrer;

import java.util.List;
import java.util.ArrayList;

/**
 * Generated
 */
public class ProductReferrerSeedData extends BaseSeedData {

  public void insert(java.lang.String name, java.lang.Long id) {
    ProductReferrer productReferrer = new ProductReferrer();
      productReferrer.setName(name);
      productReferrer.setId(id);
      save(productReferrer);
  }

  public void invokeInsert(){
    List<Long> pkList = new ArrayList<Long>();

    for (EnumProductReferrer enumProductReferrer : EnumProductReferrer.values()) {

      if (pkList.contains(enumProductReferrer.getId())) throw new RuntimeException("Duplicate key "+enumProductReferrer.getId());
      else pkList.add(enumProductReferrer.getId());

      insert(enumProductReferrer.getName(), enumProductReferrer.getId());
    }
  }

}
