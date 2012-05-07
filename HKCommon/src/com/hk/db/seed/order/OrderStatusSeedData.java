package db.seed.master;


import com.google.inject.Inject;
import mhc.common.constants.order.EnumOrderStatus;
import mhc.domain.OrderStatus;
import mhc.service.dao.order.OrderStatusDao;

import java.util.List;
import java.util.ArrayList;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class OrderStatusSeedData {

  @Inject
  OrderStatusDao orderStatusDao;

  public void insert(java.lang.String name, java.lang.Long id) {
    OrderStatus orderStatus = new OrderStatus();
      orderStatus.setName(name);
      orderStatus.setId(id);
    orderStatusDao.save(orderStatus);
  }

  public void invokeInsert(){
    List<Long> pkList = new ArrayList<Long>();

    for (EnumOrderStatus enumOrderStatus : EnumOrderStatus.values()) {

      if (pkList.contains(enumOrderStatus.getId())) throw new RuntimeException("Duplicate key "+enumOrderStatus.getId());
      else pkList.add(enumOrderStatus.getId());

      insert(enumOrderStatus.getName(), enumOrderStatus.getId());
    }
  }

}
