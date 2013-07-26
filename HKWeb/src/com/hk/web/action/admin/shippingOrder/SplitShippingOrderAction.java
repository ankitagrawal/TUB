package com.hk.web.action.admin.shippingOrder;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.pact.service.splitter.ShippingOrderProcessor;
import com.hk.web.action.admin.queue.ActionAwaitingQueueAction;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.*;

@Component
public class SplitShippingOrderAction extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(SplitShippingOrderAction.class);
	
    private ShippingOrder shippingOrder;
    private List<LineItem> lineItems;

    @Autowired
    AdminShippingOrderService adminShippingOrderService;

    @Autowired
    ShippingOrderProcessor shippingOrderProcessor;

    @DontValidate
    @DefaultHandler
    @Secure(hasAnyPermissions = {PermissionConstants.SPLIT_SO}, authActionBean = AdminPermissionAction.class)
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/order/splitShippingOrder.jsp");
    }

    @Secure(hasAnyPermissions = {PermissionConstants.SPLIT_SO}, authActionBean = AdminPermissionAction.class)
    public Resolution splitShippingOrder() {

    	List<String> messages = new ArrayList<String>();
        Map<String, ShippingOrder> splittedOrders = new HashMap<String, ShippingOrder>();
        Set<LineItem> selectedLineItems = new HashSet<LineItem>();

        for (LineItem lineItem : lineItems) {
            if (lineItem != null) {
                logger.debug("lineItem: " + lineItem.getSku().getProductVariant());
                selectedLineItems.add(lineItem);
            }
        }
    	boolean orderSplitSuccess = shippingOrderProcessor.autoSplitSO(shippingOrder, selectedLineItems,
                splittedOrders, messages);
    	
    	if(orderSplitSuccess) {
            shippingOrder = splittedOrders.get("oldShippingOrder");
            ShippingOrder newShippingOrder = splittedOrders.get("newShippingOrder");

            //Handling the PO against the shipping Orders
            if(shippingOrder.getPurchaseOrders()!=null && shippingOrder.getPurchaseOrders().size()> 0) {
                adminShippingOrderService.adjustPurchaseOrderForSplittedShippingOrder(shippingOrder, newShippingOrder);
            }
    		addRedirectAlertMessage(new SimpleMessage(messages.get(0)));
            return new RedirectResolution(ActionAwaitingQueueAction.class);
    	} else {
    		addRedirectAlertMessage(new SimpleMessage(messages.get(0)));
            return new RedirectResolution(ActionAwaitingQueueAction.class);
    	}
    	
    }


    public ShippingOrder getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(ShippingOrder shippingOrder) {
        this.shippingOrder = shippingOrder;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

}