package com.hk.domain.catalog.product.combo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;



@SuppressWarnings("serial")
@Entity
@Table(name = "combo")
@PrimaryKeyJoinColumn(name="product_combo_id")
public class Combo extends Product implements java.io.Serializable {

  @Column(name = "tag_line", length = 100)
  private String tagLine;

  @Column(name = "hk_price", nullable = false)
  private Double hkPrice;

  @Column(name = "marked_price", nullable = false)
  private Double markedPrice;

  @Column(name = "discount_percentage", nullable = false)
  private Double discountPercent;

  @Column(name = "shipping_base_qty", nullable = false)
  private Long shippingBaseQty;

  @Column(name = "shipping_base_price", nullable = false)
  private Double shippingBasePrice;

  @Column(name = "shipping_add_qty", nullable = false)
  private Long shippingAddQty;

  @Column(name = "shipping_add_price", nullable = false)
  private Double shippingAddPrice;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "combo")
  private List<ComboProduct> comboProducts = new ArrayList<ComboProduct>(0);

  @Transient
  private Long qty;

  public Combo(){
    
  }

  public Double getDiscountOnHkPrice() {
    return markedPrice * discountPercent;
  }

  public List<ProductVariant> getChosenVariants(){
    List<ProductVariant> chosenVariantList = new ArrayList<ProductVariant>();
    for (ComboProduct product : comboProducts) {
        chosenVariantList.addAll(product.getAllowedProductVariants());
    }
    return chosenVariantList;
  }

  public Long getQty() {
    return qty;
  }

  public void setQty(Long qty) {
    this.qty = qty;
  }

  public String getTagLine() {
    return tagLine;
  }

  public void setTagLine(String tagLine) {
    this.tagLine = tagLine;
  }

  public Double getHkPrice() {
    return hkPrice;
  }

  public void setHkPrice(Double hkPrice) {
    this.hkPrice = hkPrice;
  }

  public Double getMarkedPrice() {
    return markedPrice;
  }

  public void setMarkedPrice(Double markedPrice) {
    this.markedPrice = markedPrice;
  }

  public Double getDiscountPercent() {
    return discountPercent;
  }

  public void setDiscountPercent(Double discountPercent) {
    this.discountPercent = discountPercent;
  }

  public Long getShippingBaseQty() {
    return shippingBaseQty;
  }

  public void setShippingBaseQty(Long shippingBaseQty) {
    this.shippingBaseQty = shippingBaseQty;
  }

  public Double getShippingBasePrice() {
    return shippingBasePrice;
  }

  public void setShippingBasePrice(Double shippingBasePrice) {
    this.shippingBasePrice = shippingBasePrice;
  }

  public Long getShippingAddQty() {
    return shippingAddQty;
  }

  public void setShippingAddQty(Long shippingAddQty) {
    this.shippingAddQty = shippingAddQty;
  }

  public Double getShippingAddPrice() {
    return shippingAddPrice;
  }

  public void setShippingAddPrice(Double shippingAddPrice) {
    this.shippingAddPrice = shippingAddPrice;
  }

  public List<ComboProduct> getComboProducts() {
    return comboProducts;
  }

  public void setComboProducts(List<ComboProduct> comboProducts) {
    this.comboProducts = comboProducts;
  }

 
}