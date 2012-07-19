package com.hk.constants.shipment;

import java.util.Arrays;
import java.util.List;


public enum EnumPacker {
	Packer1("A"),
	Packer2("B"),
	Packer3("C"),
	Packer4("D"),
	Packer5("E"),
	Packer6("F"),
	Packer7("G"),
	Packer8("H"),
	Packer9("I"),
	Packer10("J"),
	Packer11("K"),
	Packer12("L"),
	Packer13("M"),
	Packer14("N"),
	Packer15("O"),
	Packer16("P"),
	Packer17("Q"),
	Packer18("R"),
	Packer19("S"),
	Packer20("T"),;

	private String name;

	EnumPacker(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static List<EnumPacker> getAll() {
		return Arrays.asList(EnumPacker.Packer1, EnumPacker.Packer2, EnumPacker.Packer3, EnumPacker.Packer4, EnumPacker.Packer5, EnumPacker.Packer6, EnumPacker.Packer7, EnumPacker.Packer8, EnumPacker.Packer9, EnumPacker.Packer10, EnumPacker.Packer11, EnumPacker.Packer12, EnumPacker.Packer13, EnumPacker.Packer14, EnumPacker.Packer15, EnumPacker.Packer16, EnumPacker.Packer17, EnumPacker.Packer18, EnumPacker.Packer19, EnumPacker.Packer20
		);

	}

}