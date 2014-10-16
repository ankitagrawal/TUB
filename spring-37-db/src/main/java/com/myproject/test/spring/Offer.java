package com.myproject.test.spring;


public class Offer {
	
	String id ;
	
	
	String name;
	
	
	String email;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
	@Override
	public String toString() {
		return "Offer [id=" + id + ", name=" + name + ", email=" + email + "]";
	}
}
