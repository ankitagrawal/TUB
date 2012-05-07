package db.seed.master;


import com.google.inject.Inject;
import mhc.common.constants.order.EnumOrderStatus;
import mhc.common.constants.shippingOrder.EnumShippingOrderStatus;
import mhc.domain.order.ShippingOrderStatus;
import mhc.service.dao.shippingOrder.ShippingOrderStatusDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class ShippingOrderStatusSeedData {

  @Inject
  ShippingOrderStatusDao shippingOrderStatusDao;

  public void insert(String name, Long id) {
    ShippingOrderStatus shippingOrderStatus = new ShippingOrderStatus();
    shippingOrderStatus.setName(name);
    shippingOrderStatus.setId(id);
    shippingOrderStatusDao.save(shippingOrderStatus);
  }

  public void invokeInsert() {
    List<Long> pkList = new ArrayList<Long>();

    for (EnumShippingOrderStatus enumShippingOrderStatus : EnumShippingOrderStatus.values()) {

      if (pkList.contains(enumShippingOrderStatus.getId()))
        throw new RuntimeException("Duplicate key " + enumShippingOrderStatus.getId());
      else pkList.add(enumShippingOrderStatus.getId());

      insert(enumShippingOrderStatus.getName(), enumShippingOrderStatus.getId());
    }
  }

}