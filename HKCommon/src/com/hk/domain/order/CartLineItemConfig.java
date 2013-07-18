package com.hk.domain.order;
// Generated Feb 7, 2012 4:39:32 PM by Hibernate Tools 3.2.4.CR1


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import com.hk.domain.catalog.product.VariantConfigOption;

/**
 * LineItemConfig generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "cart_line_item_config")
public class CartLineItemConfig implements java.io.Serializable {


  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;


  @Column(name = "is_left", nullable = false)
  private boolean isLeft;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cartLineItemConfig")
  @Sort(type = SortType.NATURAL)
  private Set<CartLineItemConfigValues> cartLineItemConfigValues = new TreeSet<CartLineItemConfigValues>();

	public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public boolean isIsLeft() {
    return this.isLeft;
  }

  public void setIsLeft(boolean isLeft) {
    this.isLeft = isLeft;
  }

  public boolean isLeft() {
    return isLeft;
  }

  public void setLeft(boolean left) {
    isLeft = left;
  }

  public Set<CartLineItemConfigValues> getCartLineItemConfigValues() {
    return cartLineItemConfigValues;
  }

  public void setLineItemConfigValues(Set<CartLineItemConfigValues> lineItemConfigValues) {
    this.cartLineItemConfigValues = lineItemConfigValues;
  }

  @Override
  public String toString() {
    return id != null ? id.toString() : "";
  }

  public double getPrice() {
    double price = 0;
    for (CartLineItemConfigValues configValue : getCartLineItemConfigValues()) {
      VariantConfigOption configOption = configValue.getVariantConfigOption();
      if (configOption != null) {
        @SuppressWarnings("unused")
        String addParam = configOption.getAdditionalParam();
        price += configValue.getAdditionalPrice();
      }
    }
    return price;
  }
  
  public double getCostPrice(){
      double costPrice = 0;
      for (CartLineItemConfigValues configValue : getCartLineItemConfigValues()) {
        VariantConfigOption configOption = configValue.getVariantConfigOption();
        if (configOption != null) {
          @SuppressWarnings("unused")
          String addParam = configOption.getAdditionalParam();
          costPrice += configValue.getCostPrice();
        }
      }
      return costPrice;
  }

  @SuppressWarnings("unchecked")
@Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if(o == null) return false;

    CartLineItemConfig config = (CartLineItemConfig) o;
    List<CartLineItemConfigValues> curConfigValues = new ArrayList(this.getCartLineItemConfigValues());
    List<CartLineItemConfigValues> otherConfigValues = new ArrayList(config.getCartLineItemConfigValues());
    boolean isEqual = true;

    if (curConfigValues != null && otherConfigValues != null) {
      isEqual = curConfigValues.size() == otherConfigValues.size();
      if (isEqual) {
        Collections.sort(curConfigValues);
        Collections.sort(otherConfigValues);

        for (int i = 0; (i < curConfigValues.size() && isEqual); i++) {
          isEqual = curConfigValues.get(i).equals(otherConfigValues.get(i));
        }
      }
    }
    return isEqual;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(this.id).append(this.cartLineItemConfigValues).toHashCode();
  }
  
  public String getConfigDetails(){
	  StringBuilder details = new StringBuilder("");
	  for(CartLineItemConfigValues config: this.cartLineItemConfigValues){
		  String name = config.getVariantConfigOption().getName();
		  String value = config.getValue();
		  details.append(name+" : "+value+", ");
	  }
	  return details.append("|").toString();
  }
}


