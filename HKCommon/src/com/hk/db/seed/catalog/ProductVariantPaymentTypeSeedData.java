package com.hk.db.seed.catalog;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.catalog.product.EnumProductVariantPaymentType;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.core.ProductVariantPaymentType;

@Component
public class ProductVariantPaymentTypeSeedData extends BaseSeedData {

    public void insert(java.lang.String name, java.lang.Long id) {
        ProductVariantPaymentType productVariantPaymentType = new ProductVariantPaymentType();
        productVariantPaymentType.setName(name);
        productVariantPaymentType.setId(id);
        save(productVariantPaymentType);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumProductVariantPaymentType enumProductVariantPaymentType : EnumProductVariantPaymentType.values()) {

            if (pkList.contains(enumProductVariantPaymentType.getId()))
                throw new RuntimeException("Duplicate key " + enumProductVariantPaymentType.getId());
            else
                pkList.add(enumProductVariantPaymentType.getId());

            insert(enumProductVariantPaymentType.getName(), enumProductVariantPaymentType.getId());
        }
    }

}
