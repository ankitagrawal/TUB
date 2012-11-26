package com.hk.domain.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "shipping_order_status")
public class ShippingOrderStatus {
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
    return id == null ? "" : id.toString();
  }

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ShippingOrderStatus)) {
			return false;
		}
		ShippingOrderStatus shippingOrderStatus = (ShippingOrderStatus) obj;
		if (this.id != null && shippingOrderStatus.getId() != null) {
			return this.id.equals(shippingOrderStatus.getId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

}
