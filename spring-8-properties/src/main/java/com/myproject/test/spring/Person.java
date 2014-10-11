package com.myproject.test.spring;

public class Person {
	
	private int id;
	private String name ;
	private int taxId;
	
	
	
	
  public Person(int id, String name) {
		
		this.id = id;
		this.name = name;
	}
  
  











@Override
public String toString() {
	return "Person [id=" + id + ", name=" + name + ", taxId=" + taxId + "]";
}













public int getTaxId() {
	return taxId;
}






public void setTaxId(int taxId) {
	this.taxId = taxId;
}






public void speak(){
	  System.out.println(" heelo i am here");
  }
}
