package com.hk.constants.core;

import com.hk.domain.core.Tax;

import java.util.ArrayList;
import java.util.List;

public enum EnumTax {

    SERVICE_10_3(1L, "0.103", .103D, "VAT"),
		VAT_5_25(2L, ".0525", .0525D, "VAT"),
		VAT_13_125(3L, ".13125", .13125D, "VAT"),
//		VAT_negative_5(5L, "VAT - 5.25%", .0525D, "VAT"),
    VAT_0(6L, "0.0", 0.0D, "VAT"),
		VAT_12_5(7L, "0.125", .125D, "VAT"),
    VAT_5(8L, "0.05", .05D, "VAT"),
    VAT_12_36(10L, "0.1236", .1236D, "VAT"),
//		VAT_10_3(32L, "0.103",0.103D, "VAT"),
//		NA(51L, "NA", 0.0D, "VAT"),
		VAT_2(52L, "0.02", 0.02D, "VAT"),

		VAT_14_5(53L, "0.145", 0.145D, "VAT"),
		VAT_5_5(54L, "0.055", 0.055D, "VAT"),

		VAT_SEC_0(110L, "ZeroPercent", 0.00D, "VAT_SECONDARY"),
		VAT_SEC_1(120L, "OnePercent", 0.010D, "VAT_SECONDARY"),
		VAT_SEC_1_5(125L, "OneDotFivePercent", 0.015D, "VAT_SECONDARY"),
		CST(130L, "TwoPercent", 0.020D, "CST"),
		VAT_SEC_4(135L, "FourPercent", 0.040D,"VAT_SECONDARY"),
		VAT_SEC_5(140L, "FivePercent", 0.050D, "VAT_SECONDARY"),
		VAT_SEC_5_5(145L, "FiveDotFive",0.0550D, "VAT_SECONDARY"),
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

	public Long getId(){
		return id;
	}

	public String getType() {
		return type;
	}

	public Tax asTax() {
    Tax tax = new Tax();
    tax.setName(name);
    tax.setValue(value);
    return tax;
  }


	public static List<EnumTax> getEnumTaxByType(String type){
		List<EnumTax> enumTaxList = new ArrayList<EnumTax>();
		//todo: use a for loop and compare the type.
		if(type.equals(TaxConstants.VAT_TYPE)){
			enumTaxList.add(EnumTax.SERVICE_10_3);
			enumTaxList.add(EnumTax.VAT_5_25);
			enumTaxList.add(EnumTax.VAT_13_125);
//			enumTaxList.add(EnumTax.VAT_negative_5);
			enumTaxList.add(EnumTax.VAT_0);
			enumTaxList.add(EnumTax.VAT_12_5);
			enumTaxList.add(EnumTax.VAT_5);
			enumTaxList.add(EnumTax.VAT_12_36);
//			enumTaxList.add(EnumTax.VAT_10_3);
//			enumTaxList.add(EnumTax.NA);
			enumTaxList.add(EnumTax.VAT_2);
		}
		return enumTaxList;
	}
}
