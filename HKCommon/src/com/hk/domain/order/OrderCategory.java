package com.hk.domain.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.hk.domain.catalog.category.Category;

@Entity
@Table(name = "base_order_category")
public class OrderCategory {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "base_order_id", nullable = false)
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_name", nullable = false)
  private Category category;

  @Column(name = "is_primary")
  private Boolean isPrimary=false;


  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public Category getCategory() {
    return category;
  }

  public Boolean isPrimary() {
    return isPrimary;
  }

  public void setPrimary(Boolean primary) {
    isPrimary = primary;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

	  if (o instanceof OrderCategory) {
		  OrderCategory orderCategory = (OrderCategory) o;

		  return new EqualsBuilder().append(this.order.getId(), orderCategory.getOrder().getId()).append(this.category.getName(), orderCategory.getCategory().getName()).isEquals();
	  }

    return false;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(this.order.getId()).append(this.category.getName()).toHashCode();
  }

	public void setCategory(Category category) {
		this.category = category;
	}
}
