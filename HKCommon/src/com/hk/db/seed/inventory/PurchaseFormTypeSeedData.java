package com.hk.db.seed.inventory;

import com.hk.domain.inventory.po.PurchaseInvoiceStatus;
import com.hk.domain.core.PurchaseFormType;
import com.hk.constants.inventory.EnumPurchaseInvoiceStatus;
import com.hk.constants.inventory.EnumPurchaseFormType;
import com.hk.db.seed.BaseSeedData;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Rajni
 * Date: May 29, 2012
 * Time: 12:15:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class PurchaseFormTypeSeedData extends BaseSeedData {

    public void insert(java.lang.String name, java.lang.String description) {
        PurchaseFormType purchaseFormType = new PurchaseFormType();
          purchaseFormType.setName(name);
          purchaseFormType.setDescription(description);
          save(purchaseFormType);
      }

      public void invokeInsert(){
        List<String> pkList = new ArrayList<String>();

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
