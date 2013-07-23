package com.hk.domain.sku;

import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Jul 23, 2013
 * Time: 5:25:30 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "sku_item_owner_status")
public class SkuItemOwnerStatus   implements Serializable {

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

		SkuItemOwnerStatus skuItemOwnerStatus = (SkuItemOwnerStatus) obj;
		if (this.id != null && skuItemOwnerStatus.getId() != null) {
			return (this.id.equals(skuItemOwnerStatus.getId()));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

}
