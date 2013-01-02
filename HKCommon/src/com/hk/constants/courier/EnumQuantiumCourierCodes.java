package com.hk.constants.courier;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Dec 27, 2012
 * Time: 4:36:30 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumQuantiumCourierCodes {

	IT ("IT" ,"Origin Shipped"),
	OL ("OL", "Offload /Bag Missing by Airlines"),
	MR("MR", "Mis route"),
	RTDH ("RTDH","Received at destn Hub"),
	NRH("NRH","Not Received at Hub"),
 	PIR ("PIR","Physical Item Recd"),
	DIP ("DIP","Delivery In Progress"),
	OFTP ("OFTP", "Onforward to third Party for delivery"),
	OFTB ("OFTB", "Onforward to Branch"),
	BW("BW", "Bad Weather"),
 	HW ("HW", "Held at warehouse"),
 	NRB ("NRB","Not Received at Branch"),
	DS("DS","Delivery Successful"),
	DU("DU","Delivery UnSuccessful"),
	RD("RD", "Re-Delivery"),
	RDS ("RDS","Re-Delivery Successful"),
	RDU("RDU","Re-Delivery UnSuccessful"),
	HC("HC","Hold for collection"),
	RDD("RDD","Re-Directed"),
	CS("CS","Card Sent"),
	RTO("RTO","Return To Origin"),
	FRK("FRK","Franked"),
	URO("URO","Undelivered Received at Origin"),
	URBS("URBS", "Undelivered returned back to Shipper");

	private java.lang.String code;
	private java.lang.String name;

    EnumQuantiumCourierCodes(java.lang.String code, java.lang.String name) {
        this.code = code;
		this.name = name;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
