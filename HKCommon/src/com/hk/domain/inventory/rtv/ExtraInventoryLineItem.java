package com.hk.domain.inventory.rtv;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.akube.framework.gson.JsonSkip;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.core.Surcharge;
import com.hk.domain.core.Tax;
import com.hk.domain.inventory.po.PurchaseInvoice;

/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Dec 18, 2012
 * Time: 4:27:33 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "extra_inventory_line_item", uniqueConstraints = @UniqueConstraint(columnNames = {"extra_inventory_id", "sku_id"}))

@NamedQueries({
    @NamedQuery(name = "getExtraInventoryLineItemsByExtraInventoryId", query = "select eilt from ExtraInventoryLineItem eilt where extraInventory.id = :extraInventoryId"),
    @NamedQuery(name = "getExtraInventoryLineItemById", query = "select eilt from ExtraInventoryLineItem  eilt where id = :extraInventoryLineItemId")
})

public class ExtraInventoryLineItem implements Serializable{

  @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

  @OneToOne
  @JoinColumn (name="po_line_item_id")
  private PoLineItem poLineItem;

  @ManyToOne (fetch = FetchType.LAZY)
  @JoinColumn (name = "extra_inventory_id", nullable = false)
  private ExtraInventory extraInventory;

  @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sku_id")
	private Sku sku;

  @ManyToOne (fetch = FetchType.LAZY)
  @JoinColumn (name = "tax_id")
  private Tax tax;

  @Column (name = "product_name")
  private String productName;

  @Column (name = "received_qty", nullable = false)
  private Long receivedQty;

  @Column (name = "cost_price", nullable = false)
  private Double costPrice;

	@Column(name = "mrp", nullable = false)
	private Double mrp;

	@Column(name = "taxable_amount")
	private Double taxableAmount;

	@Column(name = "tax_amount")
	private Double taxAmount;

	@Column(name = "surcharge_amount")
	private Double surchargeAmount;

	@Column(name = "payable_amount")
	private Double payableAmount;

	@Column(name = "remarks")
	private String remarks;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "surcharge_id")
	private Surcharge surcharge;

  @Column (name = "is_rtv_created")
  private Boolean isRtvCreated;

  @Column (name ="is_grn_created")
  private Boolean isGrnCreated;
  
  @Temporal (TemporalType.TIMESTAMP)
	@Column (name = "create_dt", nullable = false, length = 19)
	private Date createDate = new Date();

	@Temporal (TemporalType.TIMESTAMP)
	@Column (name = "update_dt", length = 19)
	private Date updateDate;
	
	@JsonSkip
	  @ManyToMany(fetch = FetchType.LAZY)
	  @JoinTable(
	          name = "purchase_invoice_has_extra_inventory_line_item",
	          joinColumns = {@JoinColumn(name = "extra_inventory_line_item_id", nullable = false, updatable = false)},
	          inverseJoinColumns = {@JoinColumn(name = "purchase_invoice_id", nullable = false, updatable = false)}
	  )
	  private List<PurchaseInvoice> purchaseInvoices = new ArrayList<PurchaseInvoice>();
	
	@ManyToMany
	@JoinColumn(name="extra_inventory_line_item_type_id")
	private ExtraInventoryLineItemType extraInventoryLineItemType;

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

  public Boolean isRtvCreated() {
    return isRtvCreated;
  }

  public Boolean getRtvCreated() {
    return isRtvCreated;
  }

  public void setRtvCreated(Boolean rtvCreated) {
    isRtvCreated = rtvCreated;
  }

  public Boolean isGrnCreated() {
    return isGrnCreated;
  }

  public void setGrnCreated(Boolean grnCreated) {
    isGrnCreated = grnCreated;
  }
  
public Boolean getGrnCreated() {
    return isGrnCreated;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

	public Double getTaxableAmount() {
		return taxableAmount;
	}

	public void setTaxableAmount(Double taxableAmount) {
		this.taxableAmount = taxableAmount;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getSurchargeAmount() {
		return surchargeAmount;
	}

	public void setSurchargeAmount(Double surchargeAmount) {
		this.surchargeAmount = surchargeAmount;
	}

	public Double getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(Double payableAmount) {
		this.payableAmount = payableAmount;
	}

	public Surcharge getSurcharge() {
		return surcharge;
	}

	public void setSurcharge(Surcharge surcharge) {
		this.surcharge = surcharge;
	}

	public List<PurchaseInvoice> getPurchaseInvoices() {
		return purchaseInvoices;
	}

	public void setPurchaseInvoices(List<PurchaseInvoice> purchaseInvoices) {
		this.purchaseInvoices = purchaseInvoices;
	}

	public ExtraInventoryLineItemType getExtraInventoryLineItemType() {
		return extraInventoryLineItemType;
	}

	public void setExtraInventoryLineItemType(ExtraInventoryLineItemType extraInventoryLineItemType) {
		this.extraInventoryLineItemType = extraInventoryLineItemType;
	}
	
}
