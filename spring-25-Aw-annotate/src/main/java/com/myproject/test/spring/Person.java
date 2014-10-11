package com.myproject.test.spring;

public class Person {
	
	private int id;
	private String name ;
	private int taxId;
	
	private Address address;
	
	
  public Person(int id, String name) {
		
		this.id = id;
		this.name = name;
	}
  
  


public Address getAddress() {
	return address;
}




public void setAddress(Address address) {
	this.address = address;
}




public int getTaxId() {
	return taxId;
}






@Override
public String toString() {
	return "Person [id=" + id + ", name=" + name + ", taxId=" + taxId
			+ ", address=" + address + "]";
}













public void setTaxId(int taxId) {
	this.taxId = taxId;
}






public void speak(){
	  System.out.println(" heelo i am here");
  }
}
