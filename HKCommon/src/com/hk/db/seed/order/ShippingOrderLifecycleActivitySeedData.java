package db.seed.master;


import com.google.inject.Inject;
import mhc.common.constants.order.EnumOrderLifecycleActivity;
import mhc.common.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import mhc.domain.OrderLifecycleActivity;
import mhc.domain.order.ShippingOrderLifeCycleActivity;
import mhc.service.dao.order.OrderLifecycleActivityDao;
import mhc.service.dao.shippingOrder.ShippingOrderLifeCycleActivityDao;

import java.util.List;
import java.util.ArrayList;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class ShippingOrderLifecycleActivitySeedData {

  @Inject
  ShippingOrderLifeCycleActivityDao shippingOrderLifeCycleActivityDao;

  public void insert(String name, Long id) {
    ShippingOrderLifeCycleActivity shippingOrderLifeCycleActivity = new ShippingOrderLifeCycleActivity();
    shippingOrderLifeCycleActivity.setName(name);
    shippingOrderLifeCycleActivity.setId(id);
    shippingOrderLifeCycleActivityDao.save(shippingOrderLifeCycleActivity);
  }

  public void invokeInsert() {
    List<Long> pkList = new ArrayList<Long>();

    for (EnumShippingOrderLifecycleActivity enumShippingOrderLifecycleActivity : EnumShippingOrderLifecycleActivity.values()) {

      if (pkList.contains(enumShippingOrderLifecycleActivity.getId()))
        throw new RuntimeException("Duplicate key " + enumShippingOrderLifecycleActivity.getId());
      else pkList.add(enumShippingOrderLifecycleActivity.getId());

      insert(enumShippingOrderLifecycleActivity.getName(), enumShippingOrderLifecycleActivity.getId());
    }
  }

}