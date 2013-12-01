package com.hk.domain.order;

// Generated 25 Mar, 2011 11:57:39 AM by Hibernate Tools 3.2.4.CR1

import com.akube.framework.gson.JsonSkip;
import com.hk.constants.clm.CLMConstants;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.Comment;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.comparator.OrderLifecycleComparator;
import com.hk.domain.core.CancellationType;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.payment.Payment;
import com.hk.domain.store.Store;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.domain.user.UserCodCall;

import javax.persistence.*;
import java.util.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "base_order")
public class Order implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(name = "reward_points_used", nullable = true)
    private Double rewardPointsUsed;

    @Column(name = "referred_order", nullable = false)
    private boolean referredOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_status_id", nullable = false)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;


    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_instance_id")
    private OfferInstance offerInstance;

    @Column(name = "amount", precision = 11)
    private Double amount;

    @JsonSkip
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_dt", nullable = false, length = 19)
    private Date createDate = new Date();

    /*
     * @JsonSkip @Temporal(TemporalType.TIMESTAMP) @Column(name = "update_date", nullable = false, length = 19) private
     * Date updateDate;
     */

    @Column(name = "gateway_order_id", length = 30)
    private String gatewayOrderId;

    @Column(name = "user_comments")
    private String userComments;

    /*
     * @Column(name = "basket_category", length = 45) private String basketCategory;
     */
    @JsonSkip
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
    private Set<OrderCategory> categories = new HashSet<OrderCategory>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
    private Set<CartLineItem> cartLineItems = new HashSet<CartLineItem>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "baseOrder")
    private Set<Subscription> subscriptions = new HashSet<Subscription>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
    private Set<Payment> payments = new HashSet<Payment>(0);

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
    private List<Comment> comments = new ArrayList<Comment>();

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancellation_type_id")
    private CancellationType cancellationType;

    @Column(name = "cancellation_remark")
    private String cancellationRemark;

    @Column(name = "utm_campaign")
    private String utmCampaign;

    @Column(name = "is_b2b_order")
    private Boolean b2bOrder;

    @Column(name = "is_subscription_order", nullable = false)
    private Boolean subscriptionOrder;

    @JsonSkip
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
    private List<OrderLifecycle> orderLifecycles = new ArrayList<OrderLifecycle>();

    @JsonSkip
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "baseOrder")
    private Set<ShippingOrder> shippingOrders = new HashSet<ShippingOrder>(0);

    @Column(name = "version", nullable = false)
    private Long version = new Long(1);

    @Column(name = "score", nullable = true)
    private Long score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_referrer_for_order_id")
    private PrimaryReferrerForOrder primaryReferrerForOrder;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "secondary_referrer_for_order_id")
    private SecondaryReferrerForOrder secondaryReferrerForOrder;

    @JsonSkip
    @Column(name = "target_dispatch_date", nullable = true)
    private Date targetDispatchDate;

    @JsonSkip
    @Column(name = "target_del_date", nullable = true)
    private Date targetDelDate;

    @JsonSkip
    @Column(name = "confirmation_date", nullable = true)
    private Date confirmationDate;

    @Column(name = "is_delivery_email_sent", nullable = false)
    private Boolean deliveryEmailSent = false;

    @Column(name = "comment_type")
    private Long commentType;

    @JsonSkip
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "baseOrder")
    private UserCodCall userCodCall;


    public boolean isPriorityOrder() {
        if (this.score != null) {
            return (this.score >= CLMConstants.thresholdScore);
        } else {
            return false;
        }
    }

    public Order() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Double getRewardPointsUsed() {
        return rewardPointsUsed;
    }

    public void setRewardPointsUsed(Double rewardPointsUsed) {
        this.rewardPointsUsed = rewardPointsUsed;
    }

    public boolean isReferredOrder() {
        return referredOrder;
    }

    public void setReferredOrder(boolean referredOrder) {
        this.referredOrder = referredOrder;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public OrderStatus getOrderStatus() {
        return this.orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public OfferInstance getOfferInstance() {
        return offerInstance;
    }

    public void setOfferInstance(OfferInstance offerInstance) {
        this.offerInstance = offerInstance;
    }

    /*
     * public Date getUpdateDate() { return this.updateDate; } public void setUpdateDate(Date updateDate) {
     * this.updateDate = updateDate; }
     */

    public String getGatewayOrderId() {
        return gatewayOrderId;
    }

    public void setGatewayOrderId(String gatewayOrderId) {
        this.gatewayOrderId = gatewayOrderId;
    }

    public String getUserComments() {
        return userComments;
    }

    public void setUserComments(String userComments) {
        this.userComments = userComments;
    }

    @Deprecated
    public List<CartLineItem> getCartLineItems(EnumCartLineItemType enumCartLineItemType) {
        List<CartLineItem> cartLineItems = new ArrayList<CartLineItem>();
        for (CartLineItem cartLineItem : this.getCartLineItems()) {
            if (cartLineItem.getLineItemType().getId().equals(enumCartLineItemType.getId())) {
                cartLineItems.add(cartLineItem);
            }
        }
        return cartLineItems;
    }

    @Deprecated
    public CartLineItem getOrderLevelDiscountCartLineItemForPV(ProductVariant productVariant) {
        List<CartLineItem> orderLevelDiscountCartLineItems = getCartLineItems(EnumCartLineItemType.OrderLevelDiscount);
        for (CartLineItem cartLineItem : orderLevelDiscountCartLineItems) {
            if (cartLineItem.getProductVariant().equals(productVariant)) {
                return cartLineItem;
            }
        }
        return null;
    }

    @Deprecated
    public List<CartLineItem> getExclusivelyProductCartLineItems() {
        List<CartLineItem> cartLineItemList = new ArrayList<CartLineItem>();
        for (CartLineItem cartLineItem : this.getProductCartLineItems()) {
            if (cartLineItem.getComboInstance() == null) {
                cartLineItemList.add(cartLineItem);
            }
        }
        return cartLineItemList;
    }

    @Deprecated
    public Set<CartLineItem> getExclusivelyComboCartLineItems() {
        Set<CartLineItem> cartLineItemList = new HashSet<CartLineItem>(0);
        Set<Long> comboInstanceIds = new HashSet<Long>(0);
        for (CartLineItem cartLineItem : this.getProductCartLineItems()) {
            if (cartLineItem.getComboInstance() != null) {
                if (cartLineItemList.size() == 0 || (!comboInstanceIds.contains(cartLineItem.getComboInstance().getId()))) {
                    cartLineItemList.add(cartLineItem);
                    comboInstanceIds.add(cartLineItem.getComboInstance().getId());
                }
            }
        }
        return cartLineItemList;
    }

    @Deprecated
    public boolean getIsExclusivelyServiceOrder() {
        Boolean isExclusivelyServiceOrder = true;
        for (CartLineItem cartLineItem : this.getProductCartLineItems()) {
            isExclusivelyServiceOrder = isExclusivelyServiceOrder && cartLineItem.getProductVariant().getProduct().getService();
        }
        return isExclusivelyServiceOrder;
    }

    @Deprecated
    public boolean getContainsServices() {
        for (CartLineItem cartLineItem : this.getProductCartLineItems()) {
            Boolean isService = cartLineItem.getProductVariant().getProduct().isService();
            if (isService != null && isService) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public List<CartLineItem> getProductCartLineItems() {
        return getCartLineItems(EnumCartLineItemType.Product);
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }

    public List<Comment> getComments() {
        return this.comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public CancellationType getCancellationType() {
        return cancellationType;
    }

    public void setCancellationType(CancellationType cancellationType) {
        this.cancellationType = cancellationType;
    }

    public String getCancellationRemark() {
        return cancellationRemark;
    }

    public void setCancellationRemark(String cancellationRemark) {
        this.cancellationRemark = cancellationRemark;
    }

    public String getUtmCampaign() {
        return utmCampaign;
    }

    public void setUtmCampaign(String utmCampaign) {
        this.utmCampaign = utmCampaign;
    }

    public PrimaryReferrerForOrder getPrimaryReferrerForOrder() {
        return primaryReferrerForOrder;
    }

    public void setPrimaryReferrerForOrder(PrimaryReferrerForOrder primaryReferrerForOrder) {
        this.primaryReferrerForOrder = primaryReferrerForOrder;
    }

    public SecondaryReferrerForOrder getSecondaryReferrerForOrder() {
        return secondaryReferrerForOrder;
    }

    public void setSecondaryReferrerForOrder(SecondaryReferrerForOrder secondaryReferrerForOrder) {
        this.secondaryReferrerForOrder = secondaryReferrerForOrder;
    }

    public List<OrderLifecycle> getOrderLifecycles() {
        Collections.sort(orderLifecycles, new OrderLifecycleComparator());
        return orderLifecycles;
    }

    public void setOrderLifecycles(List<OrderLifecycle> orderLifecycles) {
        this.orderLifecycles = orderLifecycles;
    }

    public Boolean isB2bOrder() {
        return b2bOrder != null ? b2bOrder : false;
    }

    public Boolean getB2bOrder() {
        return b2bOrder;
    }

    public void setB2bOrder(Boolean b2bOrder) {
        this.b2bOrder = b2bOrder;
    }

    public Boolean isSubscriptionOrder() {
        if (subscriptionOrder != null) {
            return subscriptionOrder;
        } else {
            return false;
        }
    }

    public Boolean getSubscriptionOrder() {
        return subscriptionOrder;
    }

    public void setSubscriptionOrder(Boolean subscriptionOrder) {
        this.subscriptionOrder = subscriptionOrder;
    }

    public Set<ShippingOrder> getShippingOrders() {
        return shippingOrders;
    }

    public void setShippingOrders(Set<ShippingOrder> shippingOrders) {
        this.shippingOrders = shippingOrders;
    }

    @Override
    public String toString() {
        return id != null ? id.toString() : "";
    }

    public List<CartLineItem> getLineItemsOfType(EnumCartLineItemType enumCartLineItemType) {
        List<CartLineItem> cartLineItems = new ArrayList<CartLineItem>();
        for (CartLineItem cartLineItem : this.cartLineItems) {
            if (enumCartLineItemType.getId().equals(cartLineItem.getLineItemType().getId())) {
                cartLineItems.add(cartLineItem);
            }
        }
        return cartLineItems;
    }

    public Set<CartLineItem> getCartLineItems() {
        return cartLineItems;
    }

    public void setCartLineItems(Set<CartLineItem> cartLineItems) {
        this.cartLineItems = cartLineItems;
    }

    public Set<OrderCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<OrderCategory> categories) {
        this.categories = categories;
    }

    @Transient
    public boolean isCOD() {
        return EnumPaymentMode.COD.getId().equals(getPayment().getPaymentMode().getId());
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getBasketCategory() {
        if (categories != null && !categories.isEmpty()) {
            return categories.iterator().next().getCategory().getName();
        }
        return "NA";
    }

    public Set<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Date getTargetDispatchDate() {
        return targetDispatchDate;
    }

    public void setTargetDispatchDate(Date targetDelDate) {
        this.targetDispatchDate = targetDelDate;
    }

    public Boolean getDeliveryEmailSent() {
        return deliveryEmailSent;
    }

    public void setDeliveryEmailSent(Boolean deliveryEmailSent) {
        this.deliveryEmailSent = deliveryEmailSent;
    }

    public Boolean isDeliveryEmailSent() {
        return deliveryEmailSent;
    }

    public Long getCommentType() {
        return commentType;
    }

    public void setCommentType(Long commentType) {
        this.commentType = commentType;
    }

    public Date getTargetDelDate() {
        return targetDelDate;
    }

    public void setTargetDelDate(Date targetDelDate) {
        this.targetDelDate = targetDelDate;
    }

    public Date getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(Date confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public UserCodCall getUserCodCall() {
        return userCodCall;
    }

    public void setUserCodCall(UserCodCall userCodCall) {
        this.userCodCall = userCodCall;
    }
}
