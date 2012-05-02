package com.hk.domain.catalog.product.combo;
// Generated Oct 10, 2011 12:28:44 PM by Hibernate Tools 3.2.4.CR1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hk.domain.catalog.product.ProductVariant;

/**
 * ComboInstanceHasProductVariant generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "combo_instance_has_product_variant")
public class ComboInstanceHasProductVariant implements java.io.Serializable {


  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "combo_instance_id", nullable = false)
  private ComboInstance comboInstance;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_variant_id", nullable = false)
  private ProductVariant productVariant;


  @Column(name = "qty", nullable = false)
  private Long qty;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ComboInstance getComboInstance() {
    return comboInstance;
  }

  public void setComboInstance(ComboInstance comboInstance) {
    this.comboInstance = comboInstance;
  }

  public ProductVariant getProductVariant() {
    return productVariant;
  }

  public void setProductVariant(ProductVariant productVariant) {
    this.productVariant = productVariant;
  }

  public Long getQty() {
    return this.qty;
  }

  public void setQty(Long qty) {
    this.qty = qty;
  }


}


