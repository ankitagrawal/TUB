package com.hk.constants.inventory;

import com.hk.domain.inventory.StockTransferStatus;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 1/18/13
 * Time: 6:25 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumStockTransferStatus {
	Generated(10L, "Generated"),
	Stock_Transfer_Out_In_Process(20L, "Stock Transfer Out In Process"),
	Stock_Transfer_Out_Completed(30L, "Stock Transfer Out Completed"),
	Stock_Transfer_CheckIn_In_Process(40L, "Checkin In Process"),
	Closed(50L, "Closed");

	private Long id;
	private String name;

	EnumStockTransferStatus(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public StockTransferStatus getStockTransferStatus() {
		StockTransferStatus stockTransferStatus = new StockTransferStatus();
		stockTransferStatus.setId(this.id);
		stockTransferStatus.setName(this.name);
		return stockTransferStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
