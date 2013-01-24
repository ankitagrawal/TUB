package com.hk.domain.warehouse;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User: Rahul
 * Date: Jan 15, 2013
 * Time: 5:43:54 PM
 */

@Entity
@Table(name = "warehouse_type")
public class WarehouseType {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "wh_type", nullable = false, length = 45)
	private String wh_type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWh_type() {
		return wh_type;
	}

	public void setWh_type(String wh_type) {
		this.wh_type = wh_type;
	}
}
