package com.hk.db.seed.order;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.referrer.EnumSecondaryReferrerForOrder;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.order.SecondaryReferrerForOrder;

/**
 * Generated
 */
@Component
public class SecondaryReferrerForOrderSeedData extends BaseSeedData {
  public void insert(java.lang.String name, java.lang.Long id) {
    SecondaryReferrerForOrder secondaryReferrerForOrder = new SecondaryReferrerForOrder();
      secondaryReferrerForOrder.setName(name);
      secondaryReferrerForOrder.setId(id);
      save(secondaryReferrerForOrder);
  }

  public void invokeInsert(){
    List<Long> pkList = new ArrayList<Long>();

    for (EnumSecondaryReferrerForOrder enumSecondaryReferrerForOrder : EnumSecondaryReferrerForOrder.values()) {

      if (pkList.contains(enumSecondaryReferrerForOrder.getId())) throw new RuntimeException("Duplicate key "+enumSecondaryReferrerForOrder.getId());
      else pkList.add(enumSecondaryReferrerForOrder.getId());

      insert(enumSecondaryReferrerForOrder.getName(), enumSecondaryReferrerForOrder.getId());
    }
  }

}
