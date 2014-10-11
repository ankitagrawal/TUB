package com.myproject.test.spring;

public class Address {

	private String street;
	private String postcode;
	public Address(String street, String postcode) {
	;
		this.street = street;
		this.postcode = postcode;
	}
	@Override
	public String toString() {
		return "Address [street=" + street + ", postcode=" + postcode + "]";
	}
	
	
	
	
}
