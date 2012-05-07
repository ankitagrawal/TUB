package com.hk.db.seed.core;

import com.hk.constants.core.EnumTax;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.core.Tax;

public class TaxSeedData extends BaseSeedData {

    public void insert(java.lang.String name, java.lang.Double value) {
        Tax tax = new Tax();
        tax.setName(name);
        tax.setValue(value);
        save(tax);
    }

    public void invokeInsert() {
        for (EnumTax enumTax : EnumTax.values()) {

            Tax tax = getBaseDao().get(Tax.class, enumTax.getName());
            if (tax == null) {
                insert(enumTax.getName(), enumTax.getValue());
            } else {
                tax.setValue(enumTax.getValue());
                save(tax);
            }
        }
    }

}
