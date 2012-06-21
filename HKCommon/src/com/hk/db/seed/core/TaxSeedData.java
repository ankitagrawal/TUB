package com.hk.db.seed.core;

import com.hk.constants.core.EnumTax;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.core.Tax;
import com.hk.pact.dao.TaxDao;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class TaxSeedData extends BaseSeedData {
  @Autowired
  TaxDao taxDao;

    public void insert(java.lang.String name, java.lang.Double value) {
        Tax tax = new Tax();
        tax.setName(name);
        tax.setValue(value);
        save(tax);
    }

    public void invokeInsert() {
        for (EnumTax enumTax : EnumTax.values()) {

            Tax tax = taxDao.findByName(enumTax.getName());
            if (tax == null) {
                insert(enumTax.getName(), enumTax.getValue());
            } else {
                tax.setValue(enumTax.getValue());
                save(tax);
            }
        }
    }

}
