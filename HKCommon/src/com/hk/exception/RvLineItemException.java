package com.hk.exception;

import com.hk.domain.inventory.rv.RvLineItem;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Dec 21, 2012
 * Time: 10:07:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class RvLineItemException extends HealthkartRuntimeException {
	RvLineItem rvLineItem;

	public RvLineItemException(String message, RvLineItem rvLineItem) {
		super(message);
		this.rvLineItem = rvLineItem;
	}

	public RvLineItem getRvLineItem() {
		return rvLineItem;
	}

	public void setRvLineItem(RvLineItem rvLineItem) {
		this.rvLineItem = rvLineItem;
	}
}
