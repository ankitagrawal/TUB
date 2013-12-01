package com.hk.domain.inventory.rtv;

import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Dec 18, 2012
 * Time: 1:57:56 PM
 * To change this template use File | Settings | File Templates.
 */


@Entity
@Table(name = "extra_inventory")

@NamedQueries({
    @NamedQuery(name = "getExtraInventoryByPoId", query = "select ev from ExtraInventory ev where purchaseOrder.id = :purchaseOrderId"),
    @NamedQuery(name = "getExtraInventoryById", query = "select ev from ExtraInventory ev where id = :extraInventoryId")
})
public class ExtraInventory implements Serializable{


  @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column (name = "id", unique = true, nullable = false)
	private Long id;

  @OneToOne
  @JoinColumn (name= "purchase_order_id" , nullable = false)
  private PurchaseOrder purchaseOrder;

  @ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "created_by", nullable = false)
	private User createdBy;

  @Column(name = "comments")
	private String comments;

  @ManyToOne
  @JoinColumn (name = "extra_inventory_status_id", nullable = false)
  private ExtraInventoryStatus extraInventoryStatus;

  @Column (name = "is_email_sent")
  private boolean  isEmailSent = false;

  @Temporal (TemporalType.TIMESTAMP)
	@Column (name = "create_dt", nullable = false, length = 19)
	private Date createDate = new Date();

	@Temporal (TemporalType.TIMESTAMP)
	@Column (name = "update_dt", length = 19)
	private Date updateDate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PurchaseOrder getPurchaseOrder() {
    return purchaseOrder;
  }

  public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
    this.purchaseOrder = purchaseOrder;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
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

  public ExtraInventoryStatus getExtraInventoryStatus() {
    return extraInventoryStatus;
  }

  public void setExtraInventoryStatus(ExtraInventoryStatus extraInventoryStatus) {
    this.extraInventoryStatus = extraInventoryStatus;
  }

  public boolean isEmailSent() {
    return isEmailSent;
  }

  public void setEmailSent(boolean emailSent) {
    isEmailSent = emailSent;
  }
}
