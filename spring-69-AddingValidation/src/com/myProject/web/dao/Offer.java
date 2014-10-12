package com.myProject.web.dao;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Offer {
	
	String id ;
	@Size(min=5, max=50, message="name must between 5 and 100")
	String name;
	
	@NotNull
	@Pattern(regexp=".*\\@.*\\..*", message="This does not appear to be mail")
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
	
	
	

}
