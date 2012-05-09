package com.hk.domain.inventory;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hk.domain.catalog.product.ProductVariant;

@Entity
@Table (name = "last_stockout")
public class LastStockout implements java.io.Serializable {


  @Id
  @GeneratedValue (strategy = GenerationType.AUTO)
  @Column (name = "id", unique = true, nullable = false)
  private Long id;

  @ManyToOne (fetch = FetchType.LAZY)
  @JoinColumn (name = "product_variant_id", nullable = false)
  private ProductVariant productVariant;

  @Temporal (TemporalType.TIMESTAMP)
  @Column (name = "stockout_date", nullable = false, length = 19)
  private Date stockoutDate;


  @Column (name = "unit_sales", nullable = false)
  private Long unitSales;


  @Column (name = "growth_factor", nullable = false, precision = 6)
  private Double growthFactor;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ProductVariant getProductVariant() {
    return this.productVariant;
  }

  public void setProductVariant(ProductVariant productVariant) {
    this.productVariant = productVariant;
  }

  public Date getStockoutDate() {
    return this.stockoutDate;
  }

  public void setStockoutDate(Date stockoutDate) {
    this.stockoutDate = stockoutDate;
  }

  public Long getUnitSales() {
    return this.unitSales;
  }

  public void setUnitSales(Long unitSales) {
    this.unitSales = unitSales;
  }

  public Double getGrowthFactor() {
    return this.growthFactor;
  }

  public void setGrowthFactor(Double growthFactor) {
    this.growthFactor = growthFactor;
  }


}

