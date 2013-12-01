package com.hk.db.seed.inventory;

import org.springframework.stereotype.Component;

import com.hk.constants.inventory.EnumPurchaseFormType;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.core.PurchaseFormType;


@Component
public class PurchaseFormTypeSeedData extends BaseSeedData {

    public void insert(java.lang.String name, java.lang.String description, Long id) {
        PurchaseFormType purchaseFormType = new PurchaseFormType();
          purchaseFormType.setName(name);
          purchaseFormType.setDescription(description);
          purchaseFormType.setId(id);
          save(purchaseFormType);
      }

      public void invokeInsert(){
        for (EnumPurchaseFormType enumPurchaseFormType : EnumPurchaseFormType.values()) {

            PurchaseFormType purchaseFormType = getBaseDao().get(PurchaseFormType.class, enumPurchaseFormType.getId());
            if (purchaseFormType == null) {
                insert(enumPurchaseFormType.getName(), enumPurchaseFormType.getDescription(), enumPurchaseFormType.getId());
            } else {
                purchaseFormType.setName(enumPurchaseFormType.getName());
                purchaseFormType.setDescription(enumPurchaseFormType.getDescription());
                purchaseFormType.setId(enumPurchaseFormType.getId());
                save(purchaseFormType);
            }
        }
      }
    
}
