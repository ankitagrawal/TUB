package com.hk.domain.offer;
// Generated 25 Mar, 2011 11:57:39 AM by Hibernate Tools 3.2.4.CR1


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hk.domain.catalog.product.ProductGroup;
import com.hk.domain.order.CartLineItem;
import com.hk.util.CartLineItemWrapper;
import com.hk.util.OfferTriggerMatcher;


@SuppressWarnings("serial")
@Entity
@Table(name = "offer_trigger")
public class OfferTrigger implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_group_id")
  private ProductGroup productGroup;

  @Column(name = "description", nullable = false, length = 200)
  private String description;

  @Column(name = "amount", precision = 8)
  private Double amount;

  @Column(name = "qty")
  private Long qty;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ProductGroup getProductGroup() {
    return this.productGroup;
  }

  public void setProductGroup(ProductGroup productGroup) {
    this.productGroup = productGroup;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Double getAmount() {
    return this.amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public Long getQty() {
    return qty;
  }

  public void setQty(Long qty) {
    this.qty = qty;
  }

  public OfferTriggerMatcher match(Set<CartLineItemWrapper> cartLineItemWrappers) {
    return new OfferTriggerMatcher(cartLineItemWrappers, this);
  }

  // this one takes in cartLineItems, but may not handle all the use cases
  public OfferTriggerMatcher easyMatch(Set<CartLineItem> cartLineItems) {
    return new OfferTriggerMatcher(this, cartLineItems);
    
  }
  

  @Override
  public String toString() {
    return id == null ? "" : id.toString();
  }
  
}


