package com.hk.domain.inventory.rtv;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Dec 18, 2012
 * Time: 6:14:52 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "rtv_note")

public class RtvNote implements Serializable{

  @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column (name = "id", unique = true, nullable = false)
	private Long id;

  @ManyToOne  (fetch = FetchType.LAZY)
  @JoinColumn (name = "extra_inventory_id" , nullable = false)
  private ExtraInventory extraInventory;

   @ManyToOne (fetch = FetchType.LAZY)
   @JoinColumn (name = "rtv_note_status_id", nullable = false)
   private RtvNoteStatus rtvNoteStatus;

   @Column (name = "is_debit_to_supplier")
   private Boolean isDebitToSupplier;

  @Column (name = "reconciled")
  private Boolean reconciled;

  @Column (name = "remarks")
  private String remarks;

  @Temporal (TemporalType.TIMESTAMP)
	@Column (name = "create_dt", nullable = false, length = 19)
	private Date createDate = new Date();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ExtraInventory getExtraInventory() {
    return extraInventory;
  }

  public void setExtraInventory(ExtraInventory extraInventory) {
    this.extraInventory = extraInventory;
  }

  public RtvNoteStatus getRtvNoteStatus() {
    return rtvNoteStatus;
  }

  public void setRtvNoteStatus(RtvNoteStatus rtvNoteStatus) {
    this.rtvNoteStatus = rtvNoteStatus;
  }

  public Boolean isDebitToSupplier() {
    return isDebitToSupplier;
  }

  public void setDebitToSupplier(Boolean debitToSupplier) {
    isDebitToSupplier = debitToSupplier;
  }

  public Boolean isReconciled() {
    return reconciled;
  }

  public void setReconciled(Boolean reconciled) {
    this.reconciled = reconciled;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
}
