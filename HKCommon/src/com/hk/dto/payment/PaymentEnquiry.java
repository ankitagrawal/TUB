package com.hk.dto.payment;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 12/5/13
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class PaymentEnquiry {
    Long enqCode;
    String enqMessage;
    List<EnqPaymentList> list;
}
