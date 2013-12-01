package com.hk.comparator;

import com.hk.domain.catalog.category.Category;


/**
 * @author vaibhav.adlakha
 */
public class BasketCategory implements Comparable<BasketCategory> {

  private long qty;
  private double amount;
  private Category category;

  public BasketCategory(Category category) {
    this.category = category;
  }

  public BasketCategory addQty(long qty) {
    this.qty += qty;
    return this;
  }

  public Category getCategory() {
    return category;
  }

  public BasketCategory addAmount(double amount) {
    this.amount += amount;
    return this;
  }

  public long getQty() {
    return qty;
  }

  public double getAmount() {
    return amount;
  }

  public int compareTo(BasketCategory otherBasketCategory) {
    if (this.qty > otherBasketCategory.getQty()) {
      return -1;
    } else if (this.qty == otherBasketCategory.getQty()) {
      if (this.amount > otherBasketCategory.getAmount()) {
        return -1;
      } else if (this.amount == otherBasketCategory.getAmount()) {
        return 0;
      } else {
        return 1;
      }
    }
    return 1;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o instanceof BasketCategory) {
      BasketCategory basketCategory = (BasketCategory) o;

      return this.category.equals(basketCategory.getCategory());
    }

    return false;
  }

  @Override
  public int hashCode() {
    return this.getCategory().hashCode();
  }
}
