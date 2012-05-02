package com.hk.domain.catalog.product;
// Generated 25 Mar, 2011 11:57:39 AM by Hibernate Tools 3.2.4.CR1


import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.hk.domain.catalog.category.Category;

/**
 * ProductGroup generated by hbm2java
 */
@Entity
@Table(name = "product_group")
/*@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)*/
public class ProductGroup implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "name", nullable = false, length = 80)
  private String name;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(
      name = "product_group_has_product_variant",
      joinColumns = {@JoinColumn(name = "product_group_id", nullable = false, updatable = false)},
      inverseJoinColumns = {@JoinColumn(name = "product_variant_id", nullable = false, updatable = false)}
  )
  private Set<ProductVariant> productVariants = new HashSet<ProductVariant>(0);

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(
      name = "product_group_not_has_product_variant",
      joinColumns = {@JoinColumn(name = "product_group_id", nullable = false, updatable = false)},
      inverseJoinColumns = {@JoinColumn(name = "product_variant_id", nullable = false, updatable = false)}
  )
  private Set<ProductVariant> excludeProductVariants = new HashSet<ProductVariant>(0);

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(
      name = "product_group_has_category",
      joinColumns = {@JoinColumn(name = "product_group_id", nullable = false, updatable = false)},
      inverseJoinColumns = {@JoinColumn(name = "category_name", nullable = false, updatable = false)}
  )
  private Set<Category> categories = new HashSet<Category>(0);

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(
      name = "product_group_not_has_category",
      joinColumns = {@JoinColumn(name = "product_group_id", nullable = false, updatable = false)},
      inverseJoinColumns = {@JoinColumn(name = "category_name", nullable = false, updatable = false)}
  )
  private Set<Category> categoriesExcluded = new HashSet<Category>(0);

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(
      name = "product_group_has_product",
      joinColumns = {@JoinColumn(name = "product_group_id", nullable = false, updatable = false)},
      inverseJoinColumns = {@JoinColumn(name = "product_id", nullable = false, updatable = false)}
  )
  private Set<Product> products = new HashSet<Product>(0);

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

  public Set<ProductVariant> getProductVariants() {
    return this.productVariants;
  }

  public void setProductVariants(Set<ProductVariant> productVariants) {
    this.productVariants = productVariants;
  }

  public Set<ProductVariant> getExcludeProductVariants() {
    return this.excludeProductVariants;
  }

  public void setExcludeProductVariants(Set<ProductVariant> excludeProductVariants) {
    this.excludeProductVariants = excludeProductVariants;
  }

  public Set<Category> getCategories() {
    return this.categories;
  }

  public void setCategories(Set<Category> categories) {
    this.categories = categories;
  }

  public Set<Product> getProducts() {
    return this.products;
  }

  public void setProducts(Set<Product> products) {
    this.products = products;
  }

  public Set<Category> getCategoriesExcluded() {
    return categoriesExcluded;
  }

  public void setCategoriesExcluded(Set<Category> categoriesExcluded) {
    this.categoriesExcluded = categoriesExcluded;
  }

  public boolean contains(ProductVariant productVariant) {
    if (excludeProductVariants != null) {
      if (excludeProductVariants.contains(productVariant)) return false;
    }
    if (categoriesExcluded != null) {
      for (Category category : categoriesExcluded) {
        if (category.contains(productVariant)) return false;
      }
    }
    if (productVariants != null) {
      if (productVariants.contains(productVariant)) return true;
    }
    if (products != null) {
      for (Product product : products) {
        if (product.contains(productVariant)) return true;
      }
    }
    if (categories != null) {
      for (Category category : categories) {
        if (category.contains(productVariant)) return true;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return id == null ? "" : id.toString();
  }
}


