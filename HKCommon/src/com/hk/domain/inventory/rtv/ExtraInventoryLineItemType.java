package com.hk.domain.inventory.rtv;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "extra_inventory_line_item_type")
public class ExtraInventoryLineItemType implements Serializable {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "name", nullable = false)
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
		return id == null ? "" : id.toString();
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ExtraInventoryLineItemType))
			return false;

		ExtraInventoryLineItemType extraInventoryLineItemType = (ExtraInventoryLineItemType) o;

		if (id != null ? !id.equals(extraInventoryLineItemType.getId()) : extraInventoryLineItemType.getId() != null)
			return false;

		return true;
	}

}
