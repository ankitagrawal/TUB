package com.hk.api.client.dto.order;

import com.hk.api.client.dto.HKUserDetailDto;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 12/4/12
 * Time: 2:06 PM
 */
public class HKOrderDto {
    private HKCartLineItemDto[] cartLineItems;
    private HKUserDetailDto user;
    private HKPaymentDto payment;
    private int storeId;
}
