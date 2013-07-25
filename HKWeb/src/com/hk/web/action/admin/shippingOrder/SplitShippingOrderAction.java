package com.hk.web.action.admin.shippingOrder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.pact.service.splitter.ShippingOrderSplitter;
import com.hk.web.action.admin.queue.ActionAwaitingQueueAction;
import com.hk.web.action.error.AdminPermissionAction;

@Component
public class SplitShippingOrderAction extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(SplitShippingOrderAction.class);
	
    private ShippingOrder shippingOrder;
    private List<LineItem> lineItems;

    @Autowired
    ShippingOrderSplitter shippingOrderSplitter;

    @DontValidate
    @DefaultHandler
    @Secure(hasAnyPermissions = {PermissionConstants.SPLIT_SO}, authActionBean = AdminPermissionAction.class)
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/order/splitShippingOrder.jsp");
    }

    @Secure(hasAnyPermissions = {PermissionConstants.SPLIT_SO}, authActionBean = AdminPermissionAction.class)
    public Resolution splitShippingOrder() {

    	List<String> messages = new ArrayList<String>();
    	Set<LineItem> selectedLineItems = new HashSet<LineItem>();
        for (LineItem lineItem : lineItems) {
            if (lineItem != null) {
                logger.debug("lineItem: " + lineItem.getSku().getProductVariant());
                selectedLineItems.add(lineItem);
            }
        }
    	boolean orderSplitSuccess = shippingOrderSplitter.autoSplitSO(shippingOrder, selectedLineItems, messages);
    	
    	if(orderSplitSuccess) {
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

	/**
	 * @return the shippingOrderSplitter
	 */
	public ShippingOrderSplitter getShippingOrderSplitter() {
		return shippingOrderSplitter;
	}

	/**
	 * @param shippingOrderSplitter the shippingOrderSplitter to set
	 */
	public void setShippingOrderSplitter(ShippingOrderSplitter shippingOrderSplitter) {
		this.shippingOrderSplitter = shippingOrderSplitter;
	}

}