package com.hk.db.seed.inventory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.inventory.EnumPurchaseOrderStatus;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.core.PurchaseOrderStatus;

@Component
public class PurchaseOrderStatusSeedData extends BaseSeedData {

    public void insert(java.lang.String name, java.lang.Long id) {
        PurchaseOrderStatus purchaseOrderStatus = new PurchaseOrderStatus();
        purchaseOrderStatus.setName(name);
        purchaseOrderStatus.setId(id);
        save(purchaseOrderStatus);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumPurchaseOrderStatus enumPOStatus : EnumPurchaseOrderStatus.values()) {

            if (pkList.contains(enumPOStatus.getId()))
                throw new RuntimeException("Duplicate key " + enumPOStatus.getId());
            else
                pkList.add(enumPOStatus.getId());

            insert(enumPOStatus.getName(), enumPOStatus.getId());
        }
    }

}
