package com.hk.db.seed.inventory;

import com.hk.domain.inventory.po.PurchaseInvoiceStatus;
import com.hk.domain.core.PurchaseFormType;
import com.hk.constants.inventory.EnumPurchaseInvoiceStatus;
import com.hk.constants.inventory.EnumPurchaseFormType;
import com.hk.db.seed.BaseSeedData;

import java.util.List;
import java.util.ArrayList;


public class PurchaseFormTypeSeedData extends BaseSeedData {

    public void insert(java.lang.String name, java.lang.String description) {
        PurchaseFormType purchaseFormType = new PurchaseFormType();
          purchaseFormType.setName(name);
          purchaseFormType.setDescription(description);
          save(purchaseFormType);
      }

      public void invokeInsert(){
        for (EnumPurchaseFormType enumPurchaseFormType : EnumPurchaseFormType.values()) {

            PurchaseFormType purchaseFormType = getBaseDao().get(PurchaseFormType.class, enumPurchaseFormType.getName());
            if (purchaseFormType == null) {
                insert(enumPurchaseFormType.getName(), enumPurchaseFormType.getDescription());
            } else {
                purchaseFormType.setName(enumPurchaseFormType.getName());
                purchaseFormType.setDescription(enumPurchaseFormType.getDescription());
                save(purchaseFormType);
            }
        }
      }
    
}
