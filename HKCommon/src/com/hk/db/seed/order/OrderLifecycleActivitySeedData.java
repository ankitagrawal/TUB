package db.seed.master;


import com.google.inject.Inject;
import mhc.common.constants.order.EnumOrderLifecycleActivity;
import mhc.domain.OrderLifecycleActivity;
import mhc.service.dao.order.OrderLifecycleActivityDao;

import java.util.List;
import java.util.ArrayList;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class OrderLifecycleActivitySeedData {

  @Inject
  OrderLifecycleActivityDao orderLifecycleActivityDao;

  public void insert(java.lang.String name, java.lang.Long id) {
    OrderLifecycleActivity orderLifecycleActivity = new OrderLifecycleActivity();
    orderLifecycleActivity.setName(name);
    orderLifecycleActivity.setId(id);
    orderLifecycleActivityDao.save(orderLifecycleActivity);
  }

  public void invokeInsert() {
    List<Long> pkList = new ArrayList<Long>();

    for (EnumOrderLifecycleActivity enumOrderLifecycleActivity : EnumOrderLifecycleActivity.values()) {

      if (pkList.contains(enumOrderLifecycleActivity.getId()))
        throw new RuntimeException("Duplicate key " + enumOrderLifecycleActivity.getId());
      else pkList.add(enumOrderLifecycleActivity.getId());

      insert(enumOrderLifecycleActivity.getName(), enumOrderLifecycleActivity.getId());
    }
  }

}
