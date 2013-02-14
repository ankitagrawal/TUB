package com.hk.domain.courier;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

/**
 * User: Neha * Date: Dec 6, 2012 * Time: 11:29:48 AM
 * To change this template use File | Settings | File Templates.
 */


@Entity
@Table(name = "pickup_status")
public class PickupStatus implements java.io.Serializable {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "name", nullable = false, length = 45)
	private String name;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return id != null ? id.toString() : "";
	}
}

