package com.hk.domain.order;

import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "replacement_order_reason")
public class ReplacementOrderReason {
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
		if (!(obj instanceof ReplacementOrderReason)) {
			return false;
		}
		ReplacementOrderReason replacementOrderReason = (ReplacementOrderReason) obj;
		if (this.id != null && replacementOrderReason.getId() != null) {
			return this.id.equals(replacementOrderReason.getId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).toHashCode();
	}

}