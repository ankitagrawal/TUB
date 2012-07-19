package com.hk.constants.shipment;

import java.util.Arrays;
import java.util.List;


public enum EnumPicker {
	Picker1("1"),
	Picker2("2"),
	Picker3("3"),
	Picker4("4"),
	Picker5("5"),
	Picker6("6"),
	Picker7("7"),
	Picker8("8"),
	Picker9("9"),
	Picker10("10"),
	Picker11("11"),
	Picker12("12"),
	Picker13("13"),
	Picker14("14"),
	Picker15("15"),
	Picker16("16"),
	Picker17("17"),
	Picker18("18"),
	Picker19("19"),
	Picker20("20"),;

	private String name;

	EnumPicker(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static List<EnumPicker> getAll() {
		return Arrays.asList(EnumPicker.Picker1, EnumPicker.Picker2, EnumPicker.Picker3, EnumPicker.Picker4, EnumPicker.Picker5, EnumPicker.Picker6, EnumPicker.Picker7, EnumPicker.Picker8, EnumPicker.Picker9, EnumPicker.Picker10, EnumPicker.Picker11, EnumPicker.Picker12, EnumPicker.Picker13, EnumPicker.Picker14, EnumPicker.Picker15, EnumPicker.Picker16, EnumPicker.Picker17, EnumPicker.Picker18, EnumPicker.Picker19, EnumPicker.Picker20
		);

	}

}