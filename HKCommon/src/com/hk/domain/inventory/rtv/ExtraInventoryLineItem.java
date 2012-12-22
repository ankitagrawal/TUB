package com.hk.domain.inventory.rtv;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.core.Tax;

/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Dec 18, 2012
 * Time: 4:27:33 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "extra_inventory_line_item")

@NamedQueries({
    @NamedQuery(name = "getExtraInventoryLineItemsByExtraInventoryId", query = "select eilt from ExtraInventoryLineItem eilt where extraInventory.id = :extraInventoryId")
})

public class ExtraInventoryLineItem implements Serializable{

  @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

  @OneToOne
  @JoinColumn (name="po_line_item_id", nullable = false)
  private PoLineItem poLineItem;

  @ManyToOne (fetch = FetchType.LAZY)
  @JoinColumn (name = "extra_inventory_id", nullable = false)
  private ExtraInventory extraInventory;

  @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sku_id")
	private Sku sku;

  @ManyToOne (fetch = FetchType.LAZY)
  @JoinColumn (name = "tax_id", nullable = false)
  private Tax tax;

  @Column (name = "product_name")
  private String productName;

  @Column (name = "received_qty", nullable = false)
  private Long receivedQty;

  @Column (name = "cost_price", nullable = false)
  private Double costPrice;

  @Column (name = "mrp" , nullable = false)
  private Double mrp;

  @Temporal (TemporalType.TIMESTAMP)
	@Column (name = "create_dt", nullable = false, length = 19)
	private Date createDate = new Date();

	@Temporal (TemporalType.TIMESTAMP)
	@Column (name = "update_dt", length = 19)
	private Date updateDate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PoLineItem getPoLineItem() {
    return poLineItem;
  }

  public void setPoLineItem(PoLineItem poLineItem) {
    this.poLineItem = poLineItem;
  }

  public ExtraInventory getExtraInventory() {
    return extraInventory;
  }

  public void setExtraInventory(ExtraInventory extraInventory) {
    this.extraInventory = extraInventory;
  }

  public Sku getSku() {
    return sku;
  }

  public void setSku(Sku sku) {
    this.sku = sku;
  }

  public Tax getTax() {
    return tax;
  }

  public void setTax(Tax tax) {
    this.tax = tax;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public Long getReceivedQty() {
    return receivedQty;
  }

  public void setReceivedQty(Long receivedQty) {
    this.receivedQty = receivedQty;
  }

  public Double getCostPrice() {
    return costPrice;
  }

  public void setCostPrice(Double costPrice) {
    this.costPrice = costPrice;
  }

  public Double getMrp() {
    return mrp;
  }

  public void setMrp(Double mrp) {
    this.mrp = mrp;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }
}
