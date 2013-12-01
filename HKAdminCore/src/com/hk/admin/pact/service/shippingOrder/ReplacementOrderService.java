package com.hk.admin.pact.service.shippingOrder;

import java.util.List;

import com.hk.domain.order.ReplacementOrder;
import com.hk.domain.order.ReplacementOrderReason;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;

/**
 * Created by IntelliJ IDEA. User: Rahul Agarwal Date: Mar 13, 2012 Time: 7:33:48 AM To change this template use File |
 * Settings | File Templates.
 */

public interface ReplacementOrderService {

    public ReplacementOrder createReplaceMentOrder(ShippingOrder shippingOrder, List<LineItem> lineItems, Boolean isRto, ReplacementOrderReason replacementOrderReason, String roComment);

	public List<ReplacementOrder> getReplacementOrderForRefShippingOrder(Long refShippingOrderId);

}                                                                                              