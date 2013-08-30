package com.hk.domain.sku;

// Generated Oct 4, 2011 9:25:12 PM by Hibernate Tools 3.2.4.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.akube.framework.gson.JsonSkip;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.StockTransfer;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.constants.sku.EnumSkuGroupStatus;
import org.apache.commons.lang.builder.EqualsBuilder;

@Entity
@Table (name = "sku_group")
/* @Cache(usage = CacheConcurrencyStrategy.READ_WRITE) */
public class SkuGroup implements java.io.Serializable ,Comparable<SkuGroup>{

	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	@Column (name = "id", unique = true, nullable = false)
	private Long id;

	@Column (name = "barcode", length = 45)
	private String barcode;

	@Column (name = "batch_number", nullable = false, length = 45)
	private String batchNumber;

	@Temporal (TemporalType.TIMESTAMP)
	@Column (name = "mfg_date", length = 19)
	private Date mfgDate;

	@Temporal (TemporalType.DATE)
	@Column (name = "expiry_date", length = 10)
	private Date expiryDate;

	@JsonSkip
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "sku_id", nullable = false)
	private Sku sku;

	@JsonSkip
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "goods_received_note_id")
	private GoodsReceivedNote goodsReceivedNote;

	@JsonSkip
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "reconciliation_voucher_id")
	private ReconciliationVoucher reconciliationVoucher;

	@JsonSkip
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "stock_transfer_id")
	private StockTransfer stockTransfer;

	@Column (name = "invoice_number")
	private String invoiceNumber;

	@Temporal (TemporalType.TIMESTAMP)
	@Column (name = "invoice_date", length = 19)
	private Date invoiceDate;

	@Temporal (TemporalType.TIMESTAMP)
	@Column (name = "create_date", nullable = false, length = 19)
	private Date createDate;

	@Column (name = "cost_price",nullable = false)
	private Double costPrice;

	@Column (name = "mrp",nullable = false)
	private Double mrp;

    @Column (name="status", nullable=false)
    @Enumerated(EnumType.STRING)
    private EnumSkuGroupStatus status = EnumSkuGroupStatus.NA;

  @Column(name = "foreign_sku_group_id", nullable = true)
  private Long foreignSkuGroupId;

  @JsonSkip
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "skuGroup")
  private Set<SkuItem> skuItems = new HashSet<SkuItem>(0);

	@Transient
	private Long qty;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Date getMfgDate() {
		return mfgDate;
	}

	public void setMfgDate(Date mfgDate) {
		this.mfgDate = mfgDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public GoodsReceivedNote getGoodsReceivedNote() {
		return goodsReceivedNote;
	}

	public void setGoodsReceivedNote(GoodsReceivedNote goodsReceivedNote) {
		this.goodsReceivedNote = goodsReceivedNote;
	}

	public ReconciliationVoucher getReconciliationVoucher() {
		return reconciliationVoucher;
	}

	public void setReconciliationVoucher(ReconciliationVoucher reconciliationVoucher) {
		this.reconciliationVoucher = reconciliationVoucher;
	}

	public StockTransfer getStockTransfer() {
		return stockTransfer;
	}

	public void setStockTransfer(StockTransfer stockTransfer) {
		this.stockTransfer = stockTransfer;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Set<SkuItem> getSkuItems() {
		return skuItems;
	}

	public void setSkuItems(Set<SkuItem> skuItems) {
		this.skuItems = skuItems;
	}

	public Long getQty() {
		return qty;
	}

	public void setQty(Long qty) {
		this.qty = qty;
	}

	public Sku getSku() {
		return sku;
	}

	public void setSku(Sku sku) {
		this.sku = sku;
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

    public EnumSkuGroupStatus getStatus() {
        return status;
    }

    public void setStatus(EnumSkuGroupStatus status) {
        this.status = status;
    }

  public Long getForeignSkuGroupId() {
    return foreignSkuGroupId;
  }

  public void setForeignSkuGroupId(Long foreignSkuGroupId) {
    this.foreignSkuGroupId = foreignSkuGroupId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SkuGroup)) {
      return false;
    }
    SkuGroup skuGroup = (SkuGroup) o;

		if (this.id != null && skuGroup.getId() != null) {
			return this.id.equals(skuGroup.getId());
		}else {
      EqualsBuilder equalsBuilder = new EqualsBuilder();
      if(this.mrp!=null && skuGroup.getMrp()!=null){
      	equalsBuilder.append(this.mrp, skuGroup.getMrp());
      }
      if(this.costPrice!=null && skuGroup.getCostPrice()!=null){
      	equalsBuilder.append(this.costPrice, skuGroup.getCostPrice());
      }
      if(this.mfgDate!=null && skuGroup.getMfgDate()!=null){
      	equalsBuilder.append(this.mfgDate, skuGroup.getMfgDate());
      }
      if(this.expiryDate!=null && skuGroup.getExpiryDate()!=null){
      	equalsBuilder.append(this.expiryDate, skuGroup.getExpiryDate());
      }
      return equalsBuilder.isEquals();
		}
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public String toString() {
		return this.id != null ? this.id.toString() : "";
	}

    public int compareTo(SkuGroup skuGroup) {
        return this.getId().compareTo(skuGroup.getId());

    }
}
