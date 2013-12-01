package com.hk.domain.sku;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 11/26/12
 * Time: 3:15 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "sku_item_status")
public class SkuItemStatus implements Serializable {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "name", nullable = false, length = 45)
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof SkuItemStatus)) {
			return false;
		}

		SkuItemStatus skuItemStatus = (SkuItemStatus) obj;
		if (this.id != null && skuItemStatus.getId() != null) {
			return (this.id.equals(skuItemStatus.getId()));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
