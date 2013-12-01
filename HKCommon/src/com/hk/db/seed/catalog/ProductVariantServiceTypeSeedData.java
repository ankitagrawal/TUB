package com.hk.db.seed.catalog;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.catalog.product.EnumProductVariantServiceType;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.core.ProductVariantServiceType;

@Component
public class ProductVariantServiceTypeSeedData extends BaseSeedData {

    public void insert(java.lang.String name, java.lang.Long id) {
        ProductVariantServiceType productVariantServiceType = new ProductVariantServiceType();
        productVariantServiceType.setName(name);
        productVariantServiceType.setId(id);
        save(productVariantServiceType);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumProductVariantServiceType enumProductVariantServiceType : EnumProductVariantServiceType.values()) {

            if (pkList.contains(enumProductVariantServiceType.getId()))
                throw new RuntimeException("Duplicate key " + enumProductVariantServiceType.getId());
            else
                pkList.add(enumProductVariantServiceType.getId());

            insert(enumProductVariantServiceType.getName(), enumProductVariantServiceType.getId());
        }
    }

}
