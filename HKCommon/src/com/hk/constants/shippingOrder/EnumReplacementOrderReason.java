package com.hk.constants.shippingOrder;

import java.util.Arrays;
import java.util.List;


public enum EnumReplacementOrderReason {

	Damaged(10L, "courier-Shipment was damaged"),                                                     //RTO, replacement
	WrongDispatched(20L, "Wrong variant was sent"),                                           //replacement
    CustomerUnavailable(30L, "Customer-Customer was not present hence shipment was RTO"),              //RTO
	WrongAddress(40L, "Customer-RTO due to wrong address mentioned"),                                  //RTO
	ServiceUnavailable(50L, "courier-RTO due to service unavailability of the courier."),             //RTO
	Expired(60L, "The product is past the expiry date."),                                     //replacement
	Missing(70L, "The product went missing during transit."),                                 //replacement
	DelayInDelivery(80L, "courier-Delay in Delivery"),                                                //RTO
	ShipmentOffload(90L, "courier-Shipment Offload"),                                                 //RTO
	CustomerNotInterested(100L, "Customer-Customer not interested"),                                   //RTO
	CodAmountNotReady(110L, "Customer-COD Amount not ready"),                                          //RTO
	CustomerNotContactable(120L, "Customer-Customer not contactable"),
	OutsideDeliveryArea(130L, "courier-Outside Delivery Area"),                             //RTO
	FakeRTO(140L, "courier-Fake RTO"),                                                    //RTO
	ShipmentMisrouted(150L, "courier-Shipment Misrouted");                                //RTO



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
                EnumReplacementOrderReason.WrongDispatched.getId(),
		        EnumReplacementOrderReason.Missing.getId(),
		        EnumReplacementOrderReason.Expired.getId());
    }

	public static List<Long> getReasonForReplacementForRTO() {
        return Arrays.asList(
		        EnumReplacementOrderReason.ServiceUnavailable.getId(),
		        EnumReplacementOrderReason.DelayInDelivery.getId(),
		        EnumReplacementOrderReason.ShipmentOffload.getId(),
		        EnumReplacementOrderReason.Damaged.getId(),
		        EnumReplacementOrderReason.CustomerNotInterested.getId(),
		        EnumReplacementOrderReason.CodAmountNotReady.getId(),
		        EnumReplacementOrderReason.CustomerNotContactable.getId(),
		        EnumReplacementOrderReason.CustomerUnavailable.getId(),
                EnumReplacementOrderReason.WrongAddress.getId(),
		        EnumReplacementOrderReason.OutsideDeliveryArea.getId(),
		        EnumReplacementOrderReason.FakeRTO.getId(),
		        EnumReplacementOrderReason.ShipmentMisrouted.getId()
        );
    }

	public static List<Long> getCourierRelatedReasonForRto(){
		return Arrays.asList(
				EnumReplacementOrderReason.OutsideDeliveryArea.getId(),
				EnumReplacementOrderReason.ShipmentOffload.getId(),
				EnumReplacementOrderReason.ServiceUnavailable.getId()
		);
	}
}