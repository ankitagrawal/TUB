package com.hk.constants.courier;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 21/01/13
 * Time: 17:08
 * To change this template use File | Settings | File Templates.
 */
public enum  EnumCourierOperations {

    HK_SHIPPING(2L, "HK_SHIPPING"),
    CUSTOMER_RETURNS(3L, "CUSTOMER_RETURNS"),
    COLLECT_FROM_SUPPLIER(5L, "COLLECT_FROM_SUPPLIER"),
    DEBIT_NOTE(7L, "DEBIT_NOTE"),
    DISPATCH_LOT(11L, "DISPATCH_LOT"),
    VENDOR_DROP_SHIP(13L, "VENDOR_DROP_SHIP"),
    REVERSE_PICKUP(17L, "REVERSE_PICKUP");

    private String name;
    private Long id;

    @Override
    public String toString() {
        return "EnumCourierOperations{" +
                "id=" + id +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    EnumCourierOperations(long id, String name) {
        this.id = id;
        this.name = name;
    }



	public  static List<EnumCourierOperations> getAllCourierOperations() {
		return Arrays.asList(HK_SHIPPING, CUSTOMER_RETURNS, COLLECT_FROM_SUPPLIER, DEBIT_NOTE, DISPATCH_LOT, VENDOR_DROP_SHIP, REVERSE_PICKUP);

	}

	public static List<Long> getFactorsOfOperationBit(Long number) {
		List<Long> operationBitSetIdList = new ArrayList<Long>();
		for (EnumCourierOperations courierOperations : getAllCourierOperations()) {
			if (((number.longValue()) % (courierOperations.getId().longValue())) == 0) {
				operationBitSetIdList.add(courierOperations.getId());
			}
		}
		return operationBitSetIdList;

	}
}
