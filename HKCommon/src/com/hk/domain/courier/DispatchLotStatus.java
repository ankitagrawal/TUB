package com.hk.domain.courier;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 12/3/12
 * Time: 5:35 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "dispatch_lot_status")
public class DispatchLotStatus implements Serializable {

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
		if (!(obj instanceof DispatchLotStatus)) {
			return false;
		}
		DispatchLotStatus dispatchLotStatus = (DispatchLotStatus) obj;
		return (this.id.equals((dispatchLotStatus.getId())));
	}

}
