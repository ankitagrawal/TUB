package com.hk.domain.catalog.category;

// Generated 10 Mar, 2011 5:37:39 PM by Hibernate Tools 3.2.4.CR1

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.akube.framework.gson.JsonSkip;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;

/**
 * Category generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "category")
/*@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)*/
public class Category implements java.io.Serializable {

  @Id
  @Column(name = "name", nullable = false, length = 80)
  private String name;

  @Column(name = "display_name", length = 100)
  private String displayName;

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories")
  private List<Product> products = new ArrayList<Product>(0);

  @JsonSkip
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
  private List<CategoryImage> categoryImages = new ArrayList<CategoryImage>();

  public Category() {
  }

  public Category(String name, String displayName) {
    this.name = name;
    this.displayName = displayName;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDisplayName() {
    return this.displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public List<Product> getProducts() {
    return products;
  }

  public void setProducts(List<Product> products) {
    this.products = products;
  }

  @Override
  public String toString() {
    return StringUtils.isNotBlank(name) ? name : "";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o instanceof Category) {
      Category category = (Category) o;

      return new EqualsBuilder().append(this.name, category.getName()).isEquals();
    }

    return false;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(this.name).toHashCode();
  }

  public boolean contains(ProductVariant productVariant) {

    return checkProductInCategory(productVariant.getProduct(), this);
  }

  private boolean checkProductInCategory(Product product, Category category) {
    if (category != null) {
      if (category.getProducts().contains(product)) return true;
    }
    return false;
  }

  public List<CategoryImage> getCategoryImages() {
    return categoryImages;
  }

  public void setCategoryImages(List<CategoryImage> categoryImages) {
    this.categoryImages = categoryImages;
  }

  public static String getNameFromDisplayName(String name) {
    name = name.replaceAll("[&,/\\\\)\\\\(<>\"'%]", " ").replaceAll("[ ]+", " ").trim().replaceAll(" ", "-").replaceAll("[-]+", "-").toLowerCase();
    return name;
  }

  public static void main(String[] args) {
      
      System.out.println(getNameFromDisplayName("Accu-Chek Freedom from Diabetes Combo"));
    /*System.out.println(getNameFromDisplayName("Cloth Diapers/Nappies"));
    System.out.println(getNameFromDisplayName("Pacifiers & Teethers"));
    System.out.println(getNameFromDisplayName("Soaps, Shampoos & Body Wash"));
    System.out.println(getNameFromDisplayName("Soaps, Shampoos (helo) & Body Wash"));
    System.out.println(getNameFromDisplayName("Soaps, Shampoos % (helo) & Body Wash"));*/
  }

}
