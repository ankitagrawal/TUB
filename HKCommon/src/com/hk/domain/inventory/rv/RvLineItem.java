package com.hk.domain.inventory.rv;
// Generated Oct 5, 2011 4:39:10 PM by Hibernate Tools 3.2.4.CR1


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
import javax.persistence.Transient;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.akube.framework.gson.JsonSkip;
import com.hk.domain.sku.SkuItem;
import org.apache.commons.lang.builder.EqualsBuilder;


@SuppressWarnings("serial")
@Entity
@Table(name = "rv_line_item")
public class RvLineItem implements java.io.Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reconciliation_voucher_id", nullable = false)
    private ReconciliationVoucher reconciliationVoucher;

    @Column(name = "qty", nullable = false)
    private Long qty;

    @Column(name = "cost_price", precision = 8)
    private Double costPrice;

    @Column(name = "mrp", precision = 8)
    private Double mrp;

    @Column(name = "batch_number")
    private String batchNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "mfg_date", length = 19)
    private Date mfgDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiry_date", length = 19)
    private Date expiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku_group_id")
    private SkuGroup skuGroup;

    @Transient
    private ProductVariant productVariant;

    @Transient
    private SkuItem skuItem;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku_id", nullable = false)
    private Sku sku;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reconciliation_type_id")
    private ReconciliationType reconciliationType;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "reconciled_qty")
    private Long reconciledQty;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReconciliationVoucher getReconciliationVoucher() {
        return reconciliationVoucher;
    }

    public void setReconciliationVoucher(ReconciliationVoucher reconciliationVoucher) {
        this.reconciliationVoucher = reconciliationVoucher;
    }

    public Long getQty() {
        return this.qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public Double getCostPrice() {
        return this.costPrice;
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

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
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

    public ReconciliationType getReconciliationType() {
        return reconciliationType;
    }

    public void setReconciliationType(ReconciliationType reconciliationType) {
        this.reconciliationType = reconciliationType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getReconciledQty() {
        return reconciledQty;
    }

    public void setReconciledQty(Long reconciledQty) {
        this.reconciledQty = reconciledQty;
    }


    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof RvLineItem) {
            RvLineItem rvLineItem = (RvLineItem) o;
            if (this.id != null && rvLineItem.getId() != null) {
                return this.id.equals(rvLineItem.getId());
            } else {
                EqualsBuilder equalsBuilder = new EqualsBuilder();
                if (this.getProductVariant() == null || rvLineItem.getProductVariant() == null
                        || this.getBatchNumber() == null || rvLineItem.getBatchNumber() == null || this.reconciliationType.getId() == null || rvLineItem.getReconciliationType().getId() == null) {
                    return false;

                }
                equalsBuilder.append(this.productVariant.getId(), rvLineItem.getProductVariant().getId());
                equalsBuilder.append(this.batchNumber, rvLineItem.getBatchNumber());
                equalsBuilder.append(this.reconciliationType.getId(), rvLineItem.getReconciliationType().getId());
                return equalsBuilder.isEquals();
            }
        }
        return false;


    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


    public SkuGroup getSkuGroup() {
        return skuGroup;
    }

    public void setSkuGroup(SkuGroup skuGroup) {
        this.skuGroup = skuGroup;
    }

    public SkuItem getSkuItem() {
        return skuItem;
    }

    public void setSkuItem(SkuItem skuItem) {
        this.skuItem = skuItem;
    }
}


