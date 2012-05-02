package com.hk.constants.core;

import com.hk.domain.core.Tax;

public enum EnumTax {

  SERVICE_10_3("0.103", .103D),
  VAT_0("0.0", 0.0D),
  VAT_5("0.05", .05D),
  VAT_12_5("0.125", .125D),
  NA("NA", .0D),;

  private Double value;
  private String name;

  EnumTax(String name, Double value) {
    this.value = value;
    this.name = name;
  }

  public Double getValue() {
    return value;
  }

  public String getName() {
    return name;
  }

  public Tax asTax() {
    Tax tax = new Tax();
    tax.setName(name);
    tax.setValue(value);
    return tax;
  }
}
