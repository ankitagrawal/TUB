package com.hk.admin.pact.service.shippingOrder;

import com.hk.domain.order.ReplacementOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;


/**
 * Created by IntelliJ IDEA. User: Rahul Agarwal Date: Mar 13, 2012 Time: 7:33:48 AM To change this template use File |
 * Settings | File Templates.
 */

public interface ReplacementOrderService {
  
    public void createReplaceMentOrder(ShippingOrder shippingOrder, List<LineItem> lineItems, Boolean isRto);

    public void save(ReplacementOrder replacementOrder);
}