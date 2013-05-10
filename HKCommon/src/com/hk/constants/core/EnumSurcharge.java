package com.hk.constants.core;

import com.hk.domain.core.Surcharge;

/**
 * Generated
 */
public enum EnumSurcharge {
	ZeroPercent(10L, "ZeroPercent", 0.0D),
	//For Himachal
	OnePercent(20L, "OnePercent", 0.01D),
	OneDotFivePercent(25L, "OneDotFivePercent", 0.015D),
	TwoPercent(30L, "TwoPercent", 0.02D),
	//For Banglore
	FourPercent(35L, "FourPercent", 0.04D),
	FivePercent(40L, "FivePercent", 0.05D),
	TwelveDotFivePercent(50L, "TwelveDotFivePercent", 0.125D),
	ThirteenDotOneTwoFivePercent(60L, "ThirteenDotOneTwoFivePercent", 0.13125D),
	//For Chennai without C form
	FourteenDotFive(65L, "FourteenDotFive", 0.145),
	TwentyPercent(70L, "TwentyPercent", 0.2D),;
	private String name;
	private Long id;
	private Double value;

	EnumSurcharge(Long id, String name, Double value) {
		this.name = name;
		this.value = value;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}

	public Double getValue() {
		return value;
	}

	public Surcharge asSurcharge() {
		Surcharge surcharge = new Surcharge();
		surcharge.setId(this.getId());
		surcharge.setName(this.getName());
		surcharge.setValue(this.getValue());
		return surcharge;
	}

}

