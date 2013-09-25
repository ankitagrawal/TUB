package com.hk.constants.payment;

/**
 * Created with IntelliJ IDEA.
 * User: Shakti Singh
 * Date: 6/3/13
 * Time: 8:21 AM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumPaymentTransactionType {

    SALE(0L, "Sale"),
    REFUND(1L,"Refund"),
    ;

    private String name;
    private Long id;

    EnumPaymentTransactionType(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
