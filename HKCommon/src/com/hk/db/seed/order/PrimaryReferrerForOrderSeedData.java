package com.hk.db.seed.order;

import com.hk.domain.order.PrimaryReferrerForOrder;
import com.hk.constants.referrer.EnumPrimaryReferrerForOrder;
import com.hk.db.seed.BaseSeedData;

import java.util.List;
import java.util.ArrayList;

/**
 * Generated
 */

public class PrimaryReferrerForOrderSeedData extends BaseSeedData {  
  public void insert(java.lang.String name, java.lang.Long id) {
    PrimaryReferrerForOrder primaryReferrerForOrder = new PrimaryReferrerForOrder();
      primaryReferrerForOrder.setName(name);
      primaryReferrerForOrder.setId(id);
      save(primaryReferrerForOrder);
  }

  public void invokeInsert(){
    List<Long> pkList = new ArrayList<Long>();

    for (EnumPrimaryReferrerForOrder enumPrimaryReferrerForOrder : EnumPrimaryReferrerForOrder.values()) {

      if (pkList.contains(enumPrimaryReferrerForOrder.getId())) throw new RuntimeException("Duplicate key "+enumPrimaryReferrerForOrder.getId());
      else pkList.add(enumPrimaryReferrerForOrder.getId());

      insert(enumPrimaryReferrerForOrder.getName(), enumPrimaryReferrerForOrder.getId());
    }
  }

}
