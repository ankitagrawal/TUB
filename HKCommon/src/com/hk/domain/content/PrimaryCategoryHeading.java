package com.hk.domain.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.akube.framework.gson.JsonSkip;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;

@Entity
@Table(name = "primary_category_heading")
/*@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)*/

public class PrimaryCategoryHeading implements java.io.Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false, length = 5)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_name", nullable = false)
  private Category category;

  @Column(name = "display_name", nullable = false, length = 80)
  private String name;

  @Column(name = "link", nullable = true, length = 100)
  private String link;

  @Column(name = "ranking", nullable = true)
  private Integer ranking;

//  @JsonSkip
//  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//  @JoinTable(
//      name = "heading_has_product",
//      joinColumns = {@JoinColumn(name = "heading_id", nullable = false, updatable = false)},
//      inverseJoinColumns = {@JoinColumn(name = "product_id", nullable = false, updatable = false)}
//  )
//  private List<Product> products = new ArrayList<Product>(0);


  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

//  public void setProducts(List<Product> products) {
//    this.products = products;
//  }
//
//  public List<Product> getProducts() {
//    return products;
//  }
//
//
//  public List<Product> getProductSortedByOrderRanking() {
//    Collections.sort(products, new ProductComparator());
//    return products;
//  }

//  public class ProductComparator implements Comparator<Product> {
//    public int compare(Product o1, Product o2) {
//      if (o1.getOrderRanking() != null && o2.getOrderRanking() != null) {
//        return o1.getOrderRanking().compareTo(o2.getOrderRanking());
//      }
//      return -1;
//    }
//  }


  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  public Integer getRanking() {
    return ranking;
  }

  public void setRanking(Integer ranking) {
    this.ranking = ranking;
  }
}