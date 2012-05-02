package com.hk.domain.order;
// Generated 25 Mar, 2011 11:57:39 AM by Hibernate Tools 3.2.4.CR1


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.Comment;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.comparator.OrderLifecycleComparator;
import com.hk.domain.core.CancellationType;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.courier.Courier;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.payment.Payment;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;

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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "offer_instance_id")
  private OfferInstance offerInstance;

  @Column(name = "amount", precision = 11)
  private Double amount;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "create_date", nullable = false, length = 19)
  private Date createDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "update_date", nullable = false, length = 19)
  private Date updateDate;

  @Column(name = "gateway_order_id", length = 30)
  private String gatewayOrderId;

  @Column(name = "user_comments")
  private String userComments;

  /* @Column(name = "basket_category", length = 45)
    private String basketCategory;
  */

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
  private Set<OrderCategory> categories = new HashSet<OrderCategory>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
  private Set<CartLineItem> cartLineItems = new HashSet<CartLineItem>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
  private Set<Payment> payments = new HashSet<Payment>(0);

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
  private List<Comment> comments = new ArrayList<Comment>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cancellation_type_id")
  private CancellationType cancellationType;

  @Column(name = "cancellation_remark")
  private String cancellationRemark;

  /**
   * TODO: #warehouse should be used from shipping order;
   */
  /*@Deprecated
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "accounting_invoice_number_id")
  private AccountingInvoiceNumber accountingInvoiceNumber;*/

  @Column(name = "is_b2b_order")
  private Boolean b2bOrder;

  @Column(name = "is_priority_order")
  private Boolean priorityOrder;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
  private List<OrderLifecycle> orderLifecycles = new ArrayList<OrderLifecycle>();

  /*@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
  private List<AccountingInvoice> accountingInvoices = new ArrayList<AccountingInvoice>(0);*/

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "baseOrder")
  private Set<ShippingOrder> shippingOrders = new HashSet<ShippingOrder>(0);


  
  @Column(name = "version", nullable = false)
  private Long version = new Long(1);

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

  public Date getUpdateDate() {
    return this.updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

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
  public List<CartLineItem> getExclusivelyComboCartLineItems() {
    List<CartLineItem> cartLineItemList = new ArrayList<CartLineItem>();
    Long oldComboInstanceId = null;
    for (CartLineItem cartLineItem : this.getProductCartLineItems()) {
      if (cartLineItem.getComboInstance() != null) {
        if (oldComboInstanceId == null || oldComboInstanceId != cartLineItem.getComboInstance().getId()) {
          cartLineItemList.add(cartLineItem);
          oldComboInstanceId = cartLineItem.getComboInstance().getId();
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

  /*@Deprecated
  public AccountingInvoiceNumber getAccountingInvoiceNumber() {
    return accountingInvoiceNumber;
  }

  @Deprecated
  public void setAccountingInvoiceNumber(AccountingInvoiceNumber accountingInvoiceNumber) {
    this.accountingInvoiceNumber = accountingInvoiceNumber;
  }*/

  public List<OrderLifecycle> getOrderLifecycles() {
    Collections.sort(orderLifecycles, new OrderLifecycleComparator());
    return orderLifecycles;
  }

  public void setOrderLifecycles(List<OrderLifecycle> orderLifecycles) {
    this.orderLifecycles = orderLifecycles;
  }

  /*@Deprecated
  public List<AccountingInvoice> getAccountingInvoices() {
    Collections.sort(accountingInvoices, new AccountingInvoiceComparator());
    return accountingInvoices;
  }

  @Deprecated
  public void setAccountingInvoices(List<AccountingInvoice> accountingInvoices) {
    this.accountingInvoices = accountingInvoices;
  }*/

  public Boolean isB2bOrder() {
    return b2bOrder;
  }

  public Boolean getB2bOrder() {
    return b2bOrder;
  }

  public void setB2bOrder(Boolean b2bOrder) {
    this.b2bOrder = b2bOrder;
  }

  public Boolean getPriorityOrder() {
    return priorityOrder;
  }

  public void setPriorityOrder(Boolean priorityOrder) {
    this.priorityOrder = priorityOrder;
  }

  public Set<ShippingOrder> getShippingOrders() {
    return shippingOrders;
  }

  public void setShippingOrders(Set<ShippingOrder> shippingOrders) {
    this.shippingOrders = shippingOrders;
  }

  /*@Deprecated
  public List<AccountingInvoice> getRetailInvoices() {
    List<AccountingInvoice> invoices = new ArrayList<AccountingInvoice>();
    for (AccountingInvoice accountingInvoice : accountingInvoices) {
      if (accountingInvoice.getRetailInvoiceId() != null) {
        invoices.add(accountingInvoice);
      }
    }
    Collections.sort(invoices, new AccountingInvoiceComparator());
    return invoices;
  }

  public List<AccountingInvoice> getB2BInvoices() {
    List<AccountingInvoice> invoices = new ArrayList<AccountingInvoice>();
    for (AccountingInvoice accountingInvoice : accountingInvoices) {
      if (accountingInvoice.getB2bInvoiceId() != null) {
        invoices.add(accountingInvoice);
      }
    }
    Collections.sort(invoices, new AccountingInvoiceComparator());
    return invoices;
  }

  @Deprecated
  public List<AccountingInvoice> getSeviceInvoices() {
    List<AccountingInvoice> invoices = new ArrayList<AccountingInvoice>();
    for (AccountingInvoice accountingInvoice : accountingInvoices) {
      if (accountingInvoice.getServiceInvoiceId() != null) {
        invoices.add(accountingInvoice);
      }
    }
    Collections.sort(invoices, new AccountingInvoiceComparator());
    return invoices;
  }*/

  @Override
  public String toString() {
    return id != null ? id.toString() : "";
  }

  /* public String getOrderDetails() {
    return "Order Detail { id= " + id.toString() + ", amount=" + amount + ", gatewayOrderId=" + gatewayOrderId + ", lineItems=" + cartLineItems.size() + '}';
  }*/

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
   // return EnumPaymentMode.COD.getId().equals(getPayment().getPaymentMode().getId());
      //TODO:rewrite
      return true;
  }

/*
  public ProductVariant getTopDealVariant() {
    ProductVariant topOrderedVariant = null;
    for (CartLineItem cartLineItem : this.getProductCartLineItems()) {
      if (topOrderedVariant == null) {
        topOrderedVariant = cartLineItem.getProductVariant();
      } else if (cartLineItem.getProductVariant().getDiscountPercent() > topOrderedVariant.getDiscountPercent()) {
        topOrderedVariant = cartLineItem.getProductVariant();
      }
    }
    return topOrderedVariant;
  }
*/

  @Deprecated
  public List<CartLineItem> getProductLineItemWithAwb(String awb) {

    //TODO: #warehouse implement this in shipping order

    return null;
    /*List<LineItem> lineItemList = new ArrayList<LineItem>();
    for (CartLineItem cartLineItem : this.getProductCartLineItems()) {
      if (awb.equals(lineItem.getTrackingId())) {
        lineItemList.add(lineItem);
      }
    }
    return lineItemList;*/
  }

  @Deprecated
  public List<CartLineItem> getProductLineItemByCourier(Courier courier) {
    //TODO: #warehouse implement this in shipping order

    return null;
    /*List<LineItem> lineItemList = new ArrayList<LineItem>();
    for (LineItem lineItem : this.getProductCartLineItems()) {
      if (courier.equals(lineItem.getCourier())) {
        lineItemList.add(lineItem);
      }
    }
    return lineItemList;*/
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public String getBasketCategory() {
    if (categories != null && !categories.isEmpty()) {
      return categories.iterator().next().getCategory().getName();
    }
    return "NA";
  }

  /*public void setBasketCategory(String basketCategory) {
    this.basketCategory = basketCategory;
  }*/
}


