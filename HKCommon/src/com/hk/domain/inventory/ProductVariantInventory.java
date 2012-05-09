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
import com.hk.domain.core.InvTxnType;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.user.User;

@Entity
@Table(name = "product_variant_inventory")
public class ProductVariantInventory implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, length = 12)
    private Long                  id;

    /*
     * @Deprecated @JsonSkip @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "product_variant_id", nullable =
     *             false) private ProductVariant productVariant;
     */

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku_id", nullable = false)
    private Sku                   sku;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku_item_id")
    private SkuItem               skuItem;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_order_id")
    private ShippingOrder         shippingOrder;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_item_id")
    private LineItem              lineItem;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grn_line_item_id")
    private GrnLineItem           grnLineItem;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rv_line_item_id")
    private RvLineItem            rvLineItem;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_transfer_line_item_id")
    private StockTransferLineItem stockTransferLineItem;

    @Column(name = "qty", nullable = false)
    private Long                  qty;

    @JsonSkip
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "txn_date", length = 19)
    private Date                  txnDate;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inv_txn_type_id", nullable = false)
    private InvTxnType            invTxnType;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User                  txnBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /*
     * public ProductVariant getProductVariant() { return productVariant; } public void setProductVariant(ProductVariant
     * productVariant) { this.productVariant = productVariant; }
     */

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

    public GrnLineItem getGrnLineItem() {
        return grnLineItem;
    }

    public void setGrnLineItem(GrnLineItem grnLineItem) {
        this.grnLineItem = grnLineItem;
    }

    public RvLineItem getRvLineItem() {
        return rvLineItem;
    }

    public void setRvLineItem(RvLineItem rvLineItem) {
        this.rvLineItem = rvLineItem;
    }

    public StockTransferLineItem getStockTransferLineItem() {
        return stockTransferLineItem;
    }

    public void setStockTransferLineItem(StockTransferLineItem stockTransferLineItem) {
        this.stockTransferLineItem = stockTransferLineItem;
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

    public InvTxnType getInvTxnType() {
        return invTxnType;
    }

    public void setInvTxnType(InvTxnType invTxnType) {
        this.invTxnType = invTxnType;
    }

    public User getTxnBy() {
        return txnBy;
    }

    public void setTxnBy(User txnBy) {
        this.txnBy = txnBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ProductVariantInventory))
            return false;

        ProductVariantInventory that = (ProductVariantInventory) o;

        if (id != null ? !id.equals(that.getId()) : that.getId() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return id == null ? "" : id.toString();
    }

}