package com.hk.constants.shippingOrder;

import com.hk.domain.order.ShippingOrderStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public enum EnumReplacementOrderStatus {

	Damaged(10L, "Shipment was damaged"),
	WrongDispatched(20L, "Wrong variant was sent"),
    CustomerUnavailable(30L, "Customer was not present hence shipment was RTO"),
	WrongAddress(40L, "Shipment was RTO due to wrong address mentioned");


	private String name;

	private Long id;

	EnumReplacementOrderStatus(Long id, String name) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}


    public static List<Long> getStatusForReplacementOrder() {
        return Arrays.asList(EnumReplacementOrderStatus.Damaged.getId(),
                EnumReplacementOrderStatus.WrongDispatched.getId());
    }

	public static List<Long> getStatusForReplacementForRTO() {
        return Arrays.asList(EnumReplacementOrderStatus.CustomerUnavailable.getId(),
                EnumReplacementOrderStatus.WrongAddress.getId());
    }
}