
package com.hk.db.seed.inventory;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.inventory.EnumPurchaseInvoiceStatus;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.inventory.po.PurchaseInvoiceStatus;


@Component
public class PurchaseInvoiceStatusSeedData extends BaseSeedData {


  public void insert(java.lang.String name, java.lang.Long id) {
    PurchaseInvoiceStatus purchaseInvoiceStatus = new PurchaseInvoiceStatus();
    purchaseInvoiceStatus.setName(name);
    purchaseInvoiceStatus.setId(id);
    save(purchaseInvoiceStatus);
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
