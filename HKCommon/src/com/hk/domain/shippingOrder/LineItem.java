package com.hk.domain.shippingOrder;

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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.akube.framework.gson.JsonSkip;
import com.hk.domain.core.Tax;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.sku.Sku;

@SuppressWarnings("serial")
@Entity
@Table(name = "line_item")
@SQLDelete(sql = "UPDATE line_item SET deleted = 1  WHERE id = ? and version=? ")
@Where(clause = "deleted = 0")
public class LineItem implements java.io.Serializable, Comparable<LineItem> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long          id;

    @Column(name = "qty", nullable = false)
    private Long          qty;

    @Column(name = "marked_price", nullable = false)
    private Double        markedPrice;

    @Column(name = "hk_price", nullable = false)
    private Double        hkPrice;

    @Column(name = "discount_on_hk_price", nullable = false)
    private Double        discountOnHkPrice;

    @Column(name = "cost_price", nullable = false)
    private Double        costPrice;

    @Column(name = "version", nullable = false)
    private Long          version            = new Long(1);

    @Column(name = "deleted", nullable = false)
    private Boolean       deleted            = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku_id")
    private Sku           sku;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_order_id")
    private ShippingOrder shippingOrder;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_line_item_id")
    private CartLineItem  cartLineItem;

    @Column(name = "shipping_charge", nullable = false)
    private Double        shippingCharges    = 0D;

    @Column(name = "cod_charge", nullable = false)
    private Double        codCharges         = 0D;

    @Column(name = "reward_point_discount", nullable = false)
    private Double        rewardPoints       = 0D;

    @Column(name = "order_level_discount", nullable = false)
    private Double        orderLevelDiscount = 0D;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_id")
    private Tax           tax;

    @JsonSkip
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_dt", nullable = false, length = 19)
    private Date          createDate         = new Date();

    @Column(name = "unfixed", nullable = true)
    private boolean unFixed;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShippingOrder getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(ShippingOrder shippingOrder) {
        this.shippingOrder = shippingOrder;
    }

    public CartLineItem getCartLineItem() {
        return cartLineItem;
    }

    public void setCartLineItem(CartLineItem cartLineItem) {
        this.cartLineItem = cartLineItem;
    }

    public Long getQty() {
        if (this.qty == null) {
            return 0L;
        }
        return this.qty;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public void setQty(Long qty) {
        if (qty == null) {
            this.qty = new Long(0);
        }
        this.qty = qty;
    }

    public Double getMarkedPrice() {
        if (this.markedPrice == null) {
            return 0D;
        }
        return this.markedPrice;
    }

    public void setMarkedPrice(Double markedPrice) {
        this.markedPrice = markedPrice;
    }

    public Double getDiscountOnHkPrice() {
        if (discountOnHkPrice == null) {
            return 0D;
        }
        return discountOnHkPrice;
    }

    public void setDiscountOnHkPrice(Double discountOnHkPrice) {
        this.discountOnHkPrice = discountOnHkPrice;
    }

    public Double getHkPrice() {
        if (this.hkPrice == null) {
            return 0D;
        }
        return this.hkPrice;
    }

    public void setHkPrice(Double hkPrice) {
        this.hkPrice = hkPrice;
    }

    public Double getCostPrice() {
        if (this.costPrice == null) {
            return 0D;
        }
        return this.costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }

    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }

    @Override
    public String toString() {
        return id == null ? "" : id.toString();
    }

    public String getLineItemDetails() {
        String lineItemid = id != null ? id.toString() : "";
        String productVariantString = sku != null ? sku.toString() : "";
        return "lineItem Details, id=" + lineItemid + ", productVariant=" + productVariantString + ", qty=" + qty + ", markedPrice=" + markedPrice + ", hkPrice=" + hkPrice
                + ", discountOnHkPrice=" + discountOnHkPrice + // ", lineItemType=" + lineItemType.getName() +
                // ", lineItemStatus=" + lineItemStatusString +
                ", order=" + shippingOrder.toString();
    }

    public int compareTo(LineItem lineItem) {
        if (this.getId() < lineItem.getId())
            return -1;
        if (this.getId() > lineItem.getId())
            return 1;
        return 0;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (o instanceof LineItem) {
            LineItem lineItem = (LineItem) o;

            if (this.id != null && lineItem.getId() != null && this.id.equals(lineItem.getId())) {
                return true;
            } else {
                EqualsBuilder equalsBuilder = new EqualsBuilder();
                if (this.getShippingOrder() != null && lineItem.getShippingOrder() != null) {
                    equalsBuilder.append(this.getShippingOrder().getId(), lineItem.getShippingOrder().getId());
                }
                if (this.getSku() != null && lineItem.getSku() != null) {
                    equalsBuilder.append(this.getSku().getId(), lineItem.getSku().getId());
                }
                if (this.qty != null && lineItem.getQty() != null) {
                    equalsBuilder.append(this.qty, lineItem.getQty());
                }
                if (this.cartLineItem != null && lineItem.getCartLineItem() != null) {
                    equalsBuilder.append(this.cartLineItem, lineItem.getCartLineItem());
                }
                /*
                 * if (this.lineItemType != null && lineItem.getLineItemType() != null) {
                 * equalsBuilder.append(this.lineItemType.getId(), lineItem.getLineItemType().getId()); }
                 */
                return equalsBuilder.isEquals();
            }

        }

        return false;
    }

    @Override
    public int hashCode() {
        if (this.id != null) {
            return new HashCodeBuilder().append(this.id).toHashCode();
        } else {
            HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
            if (this.getShippingOrder() != null) {
                hashCodeBuilder.append(this.getShippingOrder().getId());
            }
            if (this.getSku() != null) {
                hashCodeBuilder.append(this.getSku().getId());
            }
            /*
             * if (this.lineItemType != null) { hashCodeBuilder.append(this.lineItemType.getId()); }
             */
            return hashCodeBuilder.toHashCode();
        }
    }

    public Double getShippingCharges() {
        return shippingCharges;
    }

    public void setShippingCharges(Double shippingCharges) {
        this.shippingCharges = shippingCharges;
    }

    public Double getCodCharges() {
        return codCharges;
    }

    public void setCodCharges(Double codCharges) {
        this.codCharges = codCharges;
    }

    public Double getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(Double rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public Double getOrderLevelDiscount() {
        return orderLevelDiscount;
    }

    public void setOrderLevelDiscount(Double orderLevelDiscount) {
        this.orderLevelDiscount = orderLevelDiscount;
    }

    public Tax getTax() {
        return tax;
    }

    public void setTax(Tax tax) {
        this.tax = tax;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public boolean isUnFixed() {
        return unFixed;
    }

    public void setUnFixed(boolean unFixed) {
        this.unFixed = unFixed;
    }
}
