package com.myProject.web.dao;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

 @Entity
 @Table(name="user")
public class User {
	
	@Id
	@NotBlank(message = "User name cannot be blank")
	@Size(min=8 , max=15, message="user name must be 8 -15 character long")
	
	private String username;
	@NotBlank
	@Pattern(regexp="^\\S+$")
	@Size(min=8 , max=15, message="password must be 8 -15 character long")
	private String password;
	private String authority;
	@Email
	private String email;
	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}
	private boolean enabled = false;
	
	
	public User(){
		
	}
	
	
	public User(String username, String password, String authority,
			boolean enabled) {
		super();
		this.username = username;
		this.password = password;
		this.authority = authority;
		this.enabled = enabled;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
