package com.hk.db.seed.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.order.EnumCartLineItemType;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.core.CartLineItemType;

@Component
public class CartLineItemTypeSeedData extends BaseSeedData {

    public void insert(java.lang.String name, java.lang.Long id) {
        CartLineItemType lineItemType = new CartLineItemType();
        lineItemType.setName(name);
        lineItemType.setId(id);
        save(lineItemType);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumCartLineItemType enumLineItemType : EnumCartLineItemType.values()) {

            if (pkList.contains(enumLineItemType.getId()))
                throw new RuntimeException("Duplicate key " + enumLineItemType.getId());
            else
                pkList.add(enumLineItemType.getId());

            insert(enumLineItemType.getName(), enumLineItemType.getId());
        }
    }

}
