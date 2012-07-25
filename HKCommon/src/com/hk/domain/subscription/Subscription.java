package com.hk.domain.subscription;

import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.domain.catalog.product.ProductVariant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.*;

/**
 * Subscription generated by hbm2java
 */
@Entity
@Table(name="subscription")
public class Subscription  implements java.io.Serializable {



    @Id @GeneratedValue(strategy=IDENTITY)

    @Column(name="id", unique=true, nullable=false)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",  nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_line_item_id",  nullable = true)
    private CartLineItem cartLineItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_order_id",  nullable = false)
    private Order baseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id",  nullable = true)
    private Address address;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_status_id", nullable = false)
    private SubscriptionStatus subscriptionStatus;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;


    @Column(name="qty", nullable=false)
    private Long qty;

    @Column(name="subscription_period_days", nullable=false)
    private Long subscriptionPeriodDays;


    @Column(name="qty_per_delivery", nullable=false)
    private Long qtyPerDelivery;

    @Column(name="qty_delivered", nullable=true)
    private Long qtyDelivered;


    @Column(name="frequency_days", nullable=false)
    private Long frequencyDays;


    @Column(name="subscription_price", nullable=false, precision=10)
    private Double subscriptionPrice;


    @Column(name="subscription_discount_percent", nullable=false, precision=7, scale=4)
    private Double subscriptionDiscountPercent;


    @Column(name="hk_discount_at_subscription", nullable=false, precision=7, scale=4)
    private Double hkDiscountAtSubscription;


    @Column(name="hk_price_at_subscription", nullable=false, precision=10)
    private Double hkPriceAtSubscription;


    @Column(name="cost_price_at_subscription", nullable=false, precision=10)
    private Double costPriceAtSubscription;


    @Column(name="marked_price_at_subscription", nullable=false, precision=10)
    private Double markedPriceAtSubscription;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "subscription")
    private List<SubscriptionLifecycle> subscriptionLifecycles = new ArrayList<SubscriptionLifecycle>();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_date", nullable=false, length=19)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="update_date", length=19)
    private Date updateDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="start_date", nullable=false, length=19)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="next_shipment_date", length=19)
    private Date nextShipmentDate;

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return id == null ? "" : id.toString();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Order getBaseOrder() {
        return baseOrder;
    }

    public void setBaseOrder(Order order) {
        this.baseOrder = order;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public SubscriptionStatus getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(SubscriptionStatus subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
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
    public Long getQtyPerDelivery() {
        return this.qtyPerDelivery;
    }

    public void setQtyPerDelivery(Long qtyPerDelivery) {
        this.qtyPerDelivery = qtyPerDelivery;
    }
    public Long getFrequencyDays() {
        return this.frequencyDays;
    }

    public void setFrequencyDays(Long frequencyDays) {
        this.frequencyDays = frequencyDays;
    }
    public Double getSubscriptionPrice() {
        return this.subscriptionPrice;
    }

    public void setSubscriptionPrice(Double subscriptionPrice) {
        this.subscriptionPrice = subscriptionPrice;
    }
    public Double getSubscriptionDiscountPercent() {
        return this.subscriptionDiscountPercent;
    }

    public void setSubscriptionDiscountPercent(Double subscriptionDiscountPercent) {
        this.subscriptionDiscountPercent = subscriptionDiscountPercent;
    }
    public Double getHkDiscountAtSubscription() {
        return this.hkDiscountAtSubscription;
    }

    public void setHkDiscountAtSubscription(Double hkDiscountAtSubscription) {
        this.hkDiscountAtSubscription = hkDiscountAtSubscription;
    }
    public Double getHkPriceAtSubscription() {
        return this.hkPriceAtSubscription;
    }

    public void setHkPriceAtSubscription(Double hkPriceAtSubscription) {
        this.hkPriceAtSubscription = hkPriceAtSubscription;
    }
    public Double getCostPriceAtSubscription() {
        return this.costPriceAtSubscription;
    }

    public void setCostPriceAtSubscription(Double costPriceAtSubscription) {
        this.costPriceAtSubscription = costPriceAtSubscription;
    }
    public Double getMarkedPriceAtSubscription() {
        return this.markedPriceAtSubscription;
    }

    public void setMarkedPriceAtSubscription(Double markedPriceAtSubscription) {
        this.markedPriceAtSubscription = markedPriceAtSubscription;
    }
    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getNextShipmentDate() {
        return this.nextShipmentDate;
    }

    public void setNextShipmentDate(Date nextShipmentDate) {
        this.nextShipmentDate = nextShipmentDate;
    }

    public Long getSubscriptionPeriodDays() {
        return subscriptionPeriodDays;
    }

    public void setSubscriptionPeriodDays(Long subscriptionPeriodDays) {
        this.subscriptionPeriodDays = subscriptionPeriodDays;
    }

    public Long getQtyDelivered() {
        return qtyDelivered;
    }

    public void setQtyDelivered(Long qtyDelivered) {
        this.qtyDelivered = qtyDelivered;
    }

    public List<SubscriptionLifecycle> getSubscriptionLifecycles() {
        return subscriptionLifecycles;
    }

    public void setSubscriptionLifecycles(List<SubscriptionLifecycle> subscriptionLifecycles) {
        this.subscriptionLifecycles = subscriptionLifecycles;
    }

    public CartLineItem getCartLineItem() {
        return cartLineItem;
    }

    public void setCartLineItem(CartLineItem cartLineItem) {
        this.cartLineItem = cartLineItem;
    }
}

