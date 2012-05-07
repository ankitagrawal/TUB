package db.seed.master;

import com.google.inject.Inject;
import mhc.service.dao.TaxDao;
import mhc.domain.Courier;
import mhc.domain.Tax;
import mhc.common.constants.EnumCourier;
import mhc.common.constants.EnumTax;

public class TaxSeedData {

  @Inject TaxDao taxDao;

  public void insert(java.lang.String name, java.lang.Double value) {
    Tax tax = new Tax();
    tax.setName(name);
    tax.setValue(value);
    taxDao.save(tax);
  }

  public void invokeInsert() {
    for (EnumTax enumTax : EnumTax.values()) {

      Tax tax = taxDao.findByName(enumTax.getName());
      if (tax == null) {
        insert(enumTax.getName(), enumTax.getValue());
      } else {
        tax.setValue(enumTax.getValue());
        taxDao.save(tax);
      }
    }
  }

}
