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

import com.akube.framework.gson.JsonSkip;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;

@Entity
@Table(name = "product_variant_damage_inventory")
public class ProductVariantDamageInventory implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, length = 12)
  private String id;

  @JsonSkip
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sku_id", nullable = false)
  private Sku sku;

  @JsonSkip
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sku_item_id")
  private SkuItem skuItem;

  @JsonSkip
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shipping_order_id")
  private ShippingOrder shippingOrder;

  @JsonSkip
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "line_item_id")
  private LineItem lineItem;

  @Column(name = "qty", nullable = false)
  private Long qty;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "txn_date", length = 19)
  private Date txnDate;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Sku getSku() {
    return sku;
  }

  public void setSku(Sku sku) {
    this.sku = sku;
  }

  public SkuItem getSkuItem() {
    return skuItem;
  }

  public void setSkuItem(SkuItem skuItem) {
    this.skuItem = skuItem;
  }

  public ShippingOrder getShippingOrder() {
    return shippingOrder;
  }

  public void setShippingOrder(ShippingOrder shippingOrder) {
    this.shippingOrder = shippingOrder;
  }

  public LineItem getLineItem() {
    return lineItem;
  }

  public void setLineItem(LineItem lineItem) {
    this.lineItem = lineItem;
  }

  public Long getQty() {
    return qty;
  }

  public void setQty(Long qty) {
    this.qty = qty;
  }

  public Date getTxnDate() {
    return txnDate;
  }

  public void setTxnDate(Date txnDate) {
    this.txnDate = txnDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ProductVariantDamageInventory)) return false;

    ProductVariantDamageInventory that = (ProductVariantDamageInventory) o;

    if (id != null ? !id.equals(that.getId()) : that.getId() != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  @Override
  public String toString() {
    return id == null ? "" : id;
  }


}