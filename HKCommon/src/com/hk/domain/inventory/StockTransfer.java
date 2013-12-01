package com.hk.domain.inventory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;

/**
 * Created by IntelliJ IDEA.
 * User: Developer
 * Date: Apr 27, 2012
 * Time: 11:41:00 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "stock_transfer")

public class StockTransfer implements java.io.Serializable , Comparable<StockTransfer>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", nullable = false, length = 19)
	private Date createDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "checkout_date", length = 19)
	private Date checkoutDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "checkin_date", length = 19)
	private Date checkinDate;

	@Column(name = "remarks")
	private String remarks;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", nullable = false)
	private User createdBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "received_by")
	private User receivedBy;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "stockTransfer")
	private List<StockTransferLineItem> stockTransferLineItems = new ArrayList<StockTransferLineItem>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_warehouse_id",  nullable = false)
	private Warehouse fromWarehouse;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_warehouse_id",  nullable = false)
	private Warehouse toWarehouse;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_transfer_status_id")
	private StockTransferStatus stockTransferStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(Date checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public Date getCheckinDate() {
		return checkinDate;
	}

	public void setCheckinDate(Date checkinDate) {
		this.checkinDate = checkinDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getReceivedBy() {
		return receivedBy;
	}

	public void setReceivedBy(User receivedBy) {
		this.receivedBy = receivedBy;
	}

	public List<StockTransferLineItem> getStockTransferLineItems() {
		return stockTransferLineItems;
	}

	public void setStockTransferLineItems(List<StockTransferLineItem> stockTransferLineItems) {
		this.stockTransferLineItems = stockTransferLineItems;
	}

	public Warehouse getFromWarehouse() {
		return fromWarehouse;
	}

	public void setFromWarehouse(Warehouse fromWarehouse) {
		this.fromWarehouse = fromWarehouse;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Warehouse getToWarehouse() {
		return toWarehouse;

	}

	public void setToWarehouse(Warehouse toWarehouse) {
		this.toWarehouse = toWarehouse;
	}

	public StockTransferStatus getStockTransferStatus() {
		return stockTransferStatus;
	}

	public void setStockTransferStatus(StockTransferStatus stockTransferStatus) {
		this.stockTransferStatus = stockTransferStatus;
	}

	@Override
    public String toString() {
        return id == null ? "" : id.toString();
    }


    public int compareTo(StockTransfer stockTransfer) {
        return this.getId().compareTo(stockTransfer.getId());

    }
}
