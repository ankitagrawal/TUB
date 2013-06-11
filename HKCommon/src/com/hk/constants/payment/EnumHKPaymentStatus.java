package com.hk.constants.payment;


/**
 * Created with IntelliJ IDEA.
 * User: Shakti Singh
 * Date: 6/11/13
 * Time: 5:57 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumHKPaymentStatus {
    SUCCESS(0L,"Success"),
    FAILURE(1L,"Failure"),
    AUTHENTICATION_PENDING(2L,"Authentication Pending"),
    REJECTED_BY_ISSUER(3L, "Rejected By Issuer"),
    REJECTED_BY_GATEWAY(4L, "Rejected By Gateway"),
    INVALID_MERCHANT_ID(5L,"Invalid Merchant Id"),
    AUTHENTICATION_FAILED(6L,"Authentication Failed"),
    REFUND_INITIATED(7L,"Refund Initiated"),
    REFUNDED(8L, "Refunded"),
    REFUND_FAILED(9L,"Refund Failed"),
    ;

    private Long id;
    private String name;

    private EnumHKPaymentStatus(Long id, String name) {
        this.id = id;
        this.name = name;
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
