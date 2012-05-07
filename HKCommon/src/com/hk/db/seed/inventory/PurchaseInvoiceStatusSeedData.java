
package db.seed.master;


import com.google.inject.Inject;
import mhc.common.constants.EnumPurchaseInvoiceStatus;
import mhc.domain.PurchaseInvoiceStatus;
import mhc.service.dao.PurchaseInvoiceStatusDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class PurchaseInvoiceStatusSeedData {

  @Inject
  PurchaseInvoiceStatusDao purchaseInvoiceStatusDao;

  public void insert(java.lang.String name, java.lang.Long id) {
    PurchaseInvoiceStatus purchaseInvoiceStatus = new PurchaseInvoiceStatus();
    purchaseInvoiceStatus.setName(name);
    purchaseInvoiceStatus.setId(id);
    purchaseInvoiceStatusDao.save(purchaseInvoiceStatus);
  }

  public void invokeInsert() {
    List<Long> pkList = new ArrayList<Long>();

    for (EnumPurchaseInvoiceStatus purchaseInvoiceStatus : EnumPurchaseInvoiceStatus.values()) {

      if (pkList.contains(purchaseInvoiceStatus.getId())) throw new RuntimeException("Duplicate key " + purchaseInvoiceStatus.getId());
      else pkList.add(purchaseInvoiceStatus.getId());

      insert(purchaseInvoiceStatus.getName(), purchaseInvoiceStatus.getId());
    }
  }

}
