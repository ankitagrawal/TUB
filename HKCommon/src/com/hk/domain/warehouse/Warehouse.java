package com.hk.domain.warehouse;
// Generated Dec 29, 2011 4:35:17 PM by Hibernate Tools 3.2.4.CR1


import com.akube.framework.gson.JsonSkip;
import com.hk.domain.store.Store;
import com.hk.domain.user.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Warehouse generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "warehouse")
public class Warehouse implements java.io.Serializable {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;


	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Column(name = "line1", nullable = true, length = 100)
	private String line1;

	@Column(name = "line2", nullable = true, length = 100)
	private String line2;


	@Column(name = "city", nullable = false, length = 45)
	private String city;


	@Column(name = "state", nullable = false, length = 45)
	private String state;

	@Column(name = "pincode", nullable = false, length = 45)
	private String pincode;


	@Column(name = "wh_phone", nullable = false, length = 45)
	private String whPhone;

	@Column(name = "tin", nullable = false, length = 45)
	private String tin;

	@JsonSkip
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(
			name = "warehouse_has_user",
			joinColumns = {@JoinColumn(name = "warehouse_id", nullable = false, updatable = false)},
			inverseJoinColumns = {@JoinColumn(name = "user_id", nullable = false, updatable = false)}
	)
	private Set<User> users = new HashSet<User>(0);

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;


	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getWhPhone() {
		return this.whPhone;
	}

	public void setWhPhone(String whPhone) {
		this.whPhone = whPhone;
	}

	public Set<User> getUsers() {
		return this.users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getTin() {
		return tin;
	}

	public void setTin(String tin) {
		this.tin = tin;
	}

	@Override
	public String toString() {
		return id == null ? "" : id.toString();
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}
}


