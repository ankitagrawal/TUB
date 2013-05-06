package com.hk.domain.inventory.po;

import java.io.Serializable;
import javax.persistence.*;


@SuppressWarnings("serial")
@Entity
@Table(name="pi_line_item_type")
public class PiLineItemType implements Serializable {

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
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}


