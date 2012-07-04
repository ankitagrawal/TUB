package com.hk.domain.order;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Where;

import com.hk.domain.accounting.AccountingInvoice;
import com.hk.domain.core.CancellationType;
import com.hk.domain.courier.Shipment;
import com.hk.domain.inventory.rv.ReconciliationStatus;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.constants.payment.EnumPaymentMode;


@SuppressWarnings("serial")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "shipping_order")
public class ShippingOrder implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shipping_order_status_id", nullable = false)
  private ShippingOrderStatus shippingOrderStatus;


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


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reconciliation_status_id", nullable = false)
  private ReconciliationStatus reconciliationStatus;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cancellation_type_id")
  private CancellationType cancellationType;


  @Column(name = "cancellation_remark", length = 65535)
  private String cancellationRemark;


  @Column(name = "accounting_invoice_number_id")
  private Long accountingInvoiceNumber;


  @Column(name = "basket_category", length = 45)
  private String basketCategory;

  @Column(name = "is_service_order", nullable = false)
  private boolean isServiceOrder;

  
  @Column(name = "version", nullable = false)
  private Long version = new Long(1);


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "base_order_id")
  private Order baseOrder;



  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "warehouse_id")
  private Warehouse warehouse;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "shipment_id")
  private Shipment shipment;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "shippingOrder")
  @Where(clause = "deleted = 0")
  private Set<LineItem> lineItems = new HashSet<LineItem>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "shippingOrder")
  private Set<AccountingInvoice> accountingInvoices = new HashSet<AccountingInvoice>(0);

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "shippingOrder")
  private Set<ShippingOrderLifecycle> shippingOrderLifecycles = new HashSet<ShippingOrderLifecycle>(0);


  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ShippingOrderStatus getOrderStatus() {
    return shippingOrderStatus;
  }

  public void setOrderStatus(ShippingOrderStatus shippingOrderStatus) {
    this.shippingOrderStatus = shippingOrderStatus;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
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

  public String getGatewayOrderId() {
    return gatewayOrderId;
  }

  public void setGatewayOrderId(String gatewayOrderId) {
    this.gatewayOrderId = gatewayOrderId;
  }

  public ReconciliationStatus getReconciliationStatus() {
    return reconciliationStatus;
  }

  public void setReconciliationStatus(ReconciliationStatus reconciliationStatus) {
    this.reconciliationStatus = reconciliationStatus;
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

  public Long getAccountingInvoiceNumber() {
    return accountingInvoiceNumber;
  }

  public void setAccountingInvoiceNumber(Long accountingInvoiceNumber) {
    this.accountingInvoiceNumber = accountingInvoiceNumber;
  }

  public String getBasketCategory() {
    return basketCategory;
  }

  public void setBasketCategory(String basketCategory) {
    this.basketCategory = basketCategory;
  }

  public Order getBaseOrder() {
    return baseOrder;
  }

  public void setBaseOrder(Order baseOrder) {
    this.baseOrder = baseOrder;
  }

  public Warehouse getWarehouse() {
    return warehouse;
  }

  public void setWarehouse(Warehouse warehouse) {
    this.warehouse = warehouse;
  }

  public Set<LineItem> getLineItems() {
    return lineItems;
  }

  public void setLineItems(Set<LineItem> lineItems) {
    this.lineItems = lineItems;
  }

  public Set<AccountingInvoice> getAccountingInvoices() {
    return accountingInvoices;
  }

  public void setAccountingInvoices(Set<AccountingInvoice> accountingInvoices) {
    this.accountingInvoices = accountingInvoices;
  }

  public Set<ShippingOrderLifecycle> getShippingOrderLifecycles() {
    return shippingOrderLifecycles;
  }

  public void setShippingOrderLifecycles(Set<ShippingOrderLifecycle> shippingOrderLifecycles) {
    this.shippingOrderLifecycles = shippingOrderLifecycles;
  }


  @Transient
  public boolean isCOD() {
    return EnumPaymentMode.COD.getId().equals(getBaseOrder().getPayment().getPaymentMode().getId());
  }

  @Transient
  public boolean getCOD(){
    return isCOD();
  }

  public Shipment getShipment() {
    return shipment;
  }

  public void setShipment(Shipment shipment) {
    this.shipment = shipment;
  }

  public boolean isServiceOrder() {
    return isServiceOrder;
  }

  public void setServiceOrder(boolean serviceOrder) {
    isServiceOrder = serviceOrder;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  @Override
  public String toString() {
    return id != null ? id.toString() : "";
  }
}


