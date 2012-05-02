package com.hk.domain.accounting;
// Generated 30 Dec, 2011 7:02:04 PM by Hibernate Tools 3.2.4.CR1


import java.util.Date;
import java.util.HashSet;
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

import com.hk.domain.catalog.Supplier;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.warehouse.Warehouse;

@SuppressWarnings("serial")
@Entity
@Table (name = "debit_note")
public class DebitNote implements java.io.Serializable {

  @Id
  @GeneratedValue (strategy = GenerationType.AUTO)
  @Column (name = "id", unique = true, nullable = false)
  private Long id;

  @ManyToOne (fetch = FetchType.LAZY)
  @JoinColumn (name = "supplier_id", nullable = false)
  private Supplier supplier;

  @ManyToOne (fetch = FetchType.LAZY)
  @JoinColumn (name = "goods_received_note_id", nullable = true)
  private GoodsReceivedNote goodsReceivedNote;

  @ManyToOne (fetch = FetchType.LAZY)
  @JoinColumn (name = "debit_note_status_id", nullable = false)
  private DebitNoteStatus debitNoteStatus;

  @Temporal (TemporalType.TIMESTAMP)
  @Column (name = "create_date", nullable = false, length = 19)
  private Date createDate;

  @Column (name = "is_debit_to_supplier")
  private Boolean isDebitToSupplier;

  @ManyToOne (fetch = FetchType.LAZY)
  @JoinColumn (name = "warehouse_id")
  private Warehouse warehouse;

  @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "debitNote")
  private Set<DebitNoteLineItem> debitNoteLineItems = new HashSet<DebitNoteLineItem>(0);

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Supplier getSupplier() {
    return supplier;
  }

  public void setSupplier(Supplier supplier) {
    this.supplier = supplier;
  }

  public GoodsReceivedNote getGoodsReceivedNote() {
    return this.goodsReceivedNote;
  }

  public void setGoodsReceivedNote(GoodsReceivedNote goodsReceivedNote) {
    this.goodsReceivedNote = goodsReceivedNote;
  }

  public DebitNoteStatus getDebitNoteStatus() {
    return this.debitNoteStatus;
  }

  public void setDebitNoteStatus(DebitNoteStatus debitNoteStatus) {
    this.debitNoteStatus = debitNoteStatus;
  }

  public Date getCreateDate() {
    return this.createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Boolean getIsDebitToSupplier() {
    return this.isDebitToSupplier;
  }

  public void setIsDebitToSupplier(Boolean isDebitToSupplier) {
    this.isDebitToSupplier = isDebitToSupplier;
  }

  public Set<DebitNoteLineItem> getDebitNoteLineItems() {
    return this.debitNoteLineItems;
  }

  public void setDebitNoteLineItems(Set<DebitNoteLineItem> debitNoteLineItems) {
    this.debitNoteLineItems = debitNoteLineItems;
  }

  public Warehouse getWarehouse() {
    return warehouse;
  }

  public void setWarehouse(Warehouse warehouse) {
    this.warehouse = warehouse;
  }
}


