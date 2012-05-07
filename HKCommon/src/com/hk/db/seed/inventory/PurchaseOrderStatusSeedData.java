package db.seed.master;


import com.google.inject.Inject;
import mhc.domain.PurchaseOrderStatus;
import mhc.service.dao.PurchaseOrderStatusDao;
import mhc.common.constants.EnumPurchaseOrderStatus;

import java.util.List;
import java.util.ArrayList;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class PurchaseOrderStatusSeedData {

  @Inject
  PurchaseOrderStatusDao purchaseOrderStatusDao;

  public void insert(java.lang.String name, java.lang.Long id) {
    PurchaseOrderStatus purchaseOrderStatus = new PurchaseOrderStatus();
      purchaseOrderStatus.setName(name);
      purchaseOrderStatus.setId(id);
    purchaseOrderStatusDao.save(purchaseOrderStatus);
  }

  public void invokeInsert(){
    List<Long> pkList = new ArrayList<Long>();

    for (EnumPurchaseOrderStatus enumPOStatus : EnumPurchaseOrderStatus.values()) {

      if (pkList.contains(enumPOStatus.getId())) throw new RuntimeException("Duplicate key "+ enumPOStatus.getId());
      else pkList.add(enumPOStatus.getId());

      insert(enumPOStatus.getName(), enumPOStatus.getId());
    }
  }

}
