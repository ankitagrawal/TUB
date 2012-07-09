package com.hk.admin.pact.service.shippingOrder;

import com.hk.domain.order.ReplacementOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;

import java.util.ArrayList;

import org.springframework.stereotype.Component;


/**
 * Created by IntelliJ IDEA. User: Rahul Agarwal Date: Mar 13, 2012 Time: 7:33:48 AM To change this template use File |
 * Settings | File Templates.
 */

public interface ReplacementOrderService {
  
    public ReplacementOrder createReplaceMentOrder(ShippingOrder shippingOrder, ArrayList<LineItem> lineItems, Boolean isRto);

    public void save(ReplacementOrder replacementOrder);
}