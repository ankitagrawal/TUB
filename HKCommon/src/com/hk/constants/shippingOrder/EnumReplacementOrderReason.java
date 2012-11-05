package com.hk.constants.shippingOrder;

import java.util.Arrays;
import java.util.List;


public enum EnumReplacementOrderReason {

	Damaged(10L, "Shipment was damaged"),
	WrongDispatched(20L, "Wrong variant was sent"),
    CustomerUnavailable(30L, "Customer was not present hence shipment was RTO"),
	WrongAddress(40L, "RTO due to wrong address mentioned"),
	ServiceUnavailable(50L, "RTO due to service unavailability of the courier.");


	private String name;

	private Long id;

	EnumReplacementOrderReason(Long id, String name) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}


    public static List<Long> getReasonForReplacementOrder() {
        return Arrays.asList(EnumReplacementOrderReason.Damaged.getId(),
                EnumReplacementOrderReason.WrongDispatched.getId());
    }

	public static List<Long> getReasonForReplacementForRTO() {
        return Arrays.asList(EnumReplacementOrderReason.CustomerUnavailable.getId(),
                EnumReplacementOrderReason.WrongAddress.getId(), EnumReplacementOrderReason.ServiceUnavailable.getId());
    }
}