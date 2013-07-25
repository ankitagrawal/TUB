/**
 * 
 */
package com.hk.pact.service.splitter;

import java.util.List;
import java.util.Set;

import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;

/**
 * @author Ankit Chhabra
 *
 */
public interface ShippingOrderProcessor {


    public ShippingOrder autoEscalateShippingOrder(ShippingOrder shippingOrder, boolean firewall);

    public ShippingOrder manualEscalateShippingOrder(ShippingOrder shippingOrder);

    public ShippingOrder automateManualEscalation(ShippingOrder shippingOrder);
    
	public boolean autoSplitSO(ShippingOrder shippingOrder, Set<LineItem> selectedLineItems, List<String> messages);
	
}
