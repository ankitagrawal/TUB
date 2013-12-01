package com.hk.domain.inventory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 1/18/13
 * Time: 6:28 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "stock_transfer_status")
public class StockTransferStatus implements Serializable {
	@Id
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "name", nullable = false)
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

	public boolean equals(Object obj) {
		if (!(obj instanceof StockTransferStatus)) {
			return false;
		}
		StockTransferStatus stockTransferStatus = (StockTransferStatus) obj;
		return (this.id.equals((stockTransferStatus.getId())));
	}

}
