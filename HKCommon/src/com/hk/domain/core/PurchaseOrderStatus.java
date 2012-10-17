package com.hk.domain.core;
// Generated Oct 6, 2011 9:57:43 AM by Hibernate Tools 3.2.4.CR1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PurchaseOrderStatus generated by hbm2java
 */
@Entity
@Table(name = "purchase_order_status")
public class PurchaseOrderStatus implements java.io.Serializable, Comparable<PurchaseOrderStatus>{


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
		if (!(obj instanceof PurchaseOrderStatus)) {
			return false;
		}
		PurchaseOrderStatus purchaseOrderStatus = (PurchaseOrderStatus) obj;
		return (this.id.equals((purchaseOrderStatus.getId())));
	}

	public int compareTo(PurchaseOrderStatus purchaseOrderStatus) {
		return this.getId().compareTo(purchaseOrderStatus.getId());

	}

}


