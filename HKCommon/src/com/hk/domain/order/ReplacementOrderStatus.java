package com.hk.domain.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "replacement_order_status")
public class ReplacementOrderStatus {
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

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ReplacementOrderStatus)) {
			return false;
		}
		ReplacementOrderStatus replacementOrderStatus = (ReplacementOrderStatus) obj;
		if (this.id != null && replacementOrderStatus.getId() != null) {
			return this.id.equals(replacementOrderStatus.getId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

}