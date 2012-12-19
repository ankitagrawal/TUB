package com.hk.api.client.dto.order;

import org.codehaus.jackson.annotate.JsonProperty;

public class HKAPIOrderDetailsDTO {

    private String orderId;

    private String userComments;

    private HKAPICartLineItemDTO[] hkapiCartLineItemDTOs;


    public HKAPICartLineItemDTO[] getHkapiCartLineItemDTOs() {
        return hkapiCartLineItemDTOs;
    }


    public void setHkapiCartLineItemDTOs(HKAPICartLineItemDTO[] hkapiCartLineItemDTOs) {
        this.hkapiCartLineItemDTOs = hkapiCartLineItemDTOs;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderid) {
        this.orderId = orderid;
    }

    public String getUserComments() {
        return userComments;
    }

    public void setUserComments(String userComments) {
        this.userComments = userComments;
    }
}
