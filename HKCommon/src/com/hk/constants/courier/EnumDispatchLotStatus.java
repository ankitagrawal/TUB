package com.hk.constants.courier;

import com.hk.domain.courier.DispatchLotStatus;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 12/6/12
 * Time: 3:02 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumDispatchLotStatus {
	Generated(10L, "Generated"),
	InTransit(20L, "In Transit"),
	PartiallyReceived(30L, "Partially Received"),
	Received(40L, "Received"),
	Cancelled(50L, "Cancelled");

	private Long id;
	private String name;

	EnumDispatchLotStatus(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public DispatchLotStatus getDispatchLotStatus() {
		DispatchLotStatus dispatchLotStatus = new DispatchLotStatus();
		dispatchLotStatus.setId(this.id);
		dispatchLotStatus.setName(this.name);
		return dispatchLotStatus;
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
