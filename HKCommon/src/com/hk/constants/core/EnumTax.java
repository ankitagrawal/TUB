package com.hk.constants.core;

import com.hk.domain.core.Tax;

public enum EnumTax {

    SERVICE_10_3(1L, "Service Tax @ 10.3%", .103D, "VAT"),
		VAT_5_25(2L, "VAT @ 5.25%", .0525D, "VAT"),
		VAT_13_125(3L, "VAT @ 13.125%", .13125D, "VAT"),
    VAT_0(6L, "0.0", 0.0D, "VAT"),
		VAT_12_5(7L, ".125", .125D, "VAT"),
    VAT_5(8L, "0.05", .05D, "VAT"),
    VAT_12_36(10L, "0.1236", .1236D, "VAT"),
		VAT_10_3(32L, "0.103",0.103D, "VAT"),
		NA(51L, "NA", 0.0D, "VAT"),

		VAT_SEC_0(110L, "ZeroPercent", 0.00D, "VAT_SECONDARY"),
		VAT_SEC_1(120L, "OnePercent", 0.010D, "VAT_SECONDARY"),
		CST(130L, "TwoPercent", 0.020D, "CST"),
		VAT_SEC_4(135L, "FourPercent", 0.040D,"VAT_SECONDARY"),
		VAT_SEC_5(140L, "FivePercent", 0.050D, "VAT_SECONDARY"),
		VAT_SEC_12_5(150L, "TwelveDotFivePercent", 0.1250D, "VAT_SECONDARY"),
		VAT_SEC_13_125(160L, "ThirteenDotOneTwoFivePercent", 0.13125D, "VAT_SECONDARY"),
		VAT_SEC_14_5(165L, "FourteenDotFive",0.1450D, "VAT_SECONDARY"),
		VAT_SEC_20(170L, "TwentyPercent",0.20D, "VAT_SECONDARY");

  private Double value;
  private String name;
	private Long id;
	private String type;

  EnumTax(Long id, String name, Double value, String type) {
    this.value = value;
    this.name = name;
	  this.id = id;
	  this.type=type;
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
