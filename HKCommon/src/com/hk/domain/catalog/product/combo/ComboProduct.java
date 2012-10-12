package com.hk.domain.catalog.product.combo;

import java.util.ArrayList;
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
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;


@SuppressWarnings("serial")
@Entity
@Table(name = "combo_has_product")
public class ComboProduct implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "combo_id", nullable = false)
  //@NotFound(action=NotFoundAction.IGNORE)
  private Combo combo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @Column(name = "qty")
  private Long qty;

  @JsonSkip
  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(
      name = "combo_product_allowed_variant",
      joinColumns = {@JoinColumn(name = "combo_has_product_id", nullable = false, updatable = false)},
      inverseJoinColumns = {@JoinColumn(name = "product_variant_id", nullable = false, updatable = false)}
  )
  private List<ProductVariant> allowedProductVariants = new ArrayList<ProductVariant>(0);

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Combo getCombo() {
    return combo;
  }

  public void setCombo(Combo combo) {
    this.combo = combo;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Long getQty() {
    return qty;
  }

  public void setQty(Long qty) {
    this.qty = qty;
  }

  public List<ProductVariant> getAllowedProductVariants() {
    return allowedProductVariants;
  }

  public List<ProductVariant> getAllowedInStockVariants() {
    List<ProductVariant> inStockVariants = new ArrayList<ProductVariant>();
    for (ProductVariant allowedProductVariant : allowedProductVariants) {
      if(!allowedProductVariant.isDeleted() && !allowedProductVariant.isOutOfStock()){
        inStockVariants.add(allowedProductVariant);
      }
    }
    return inStockVariants;
  }

  public void setAllowedProductVariants(List<ProductVariant> allowedProductVariants) {
    this.allowedProductVariants = allowedProductVariants;
  }
}