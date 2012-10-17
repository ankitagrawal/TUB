package com.hk.web.action.admin.replacementOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.domain.order.ReplacementOrder;
import com.hk.web.HealthkartResponse;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.shippingOrder.ReplacementOrderService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.helper.ReplacementOrderHelper;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: Jul 4, 2012
 * Time: 3:09:58 PM
 * To change this template use File | Settings | File Templates.
 */
@Secure(hasAnyPermissions = { PermissionConstants.CREATE_REPLACEMENT_ORDER })
@Component
public class ReplacementOrderAction extends BaseAction {
    private String shippingOrderId;
	private String gatewayOrderId;
    private ShippingOrder shippingOrder;
    private Boolean isRto;
    private List<LineItem> lineItems = new ArrayList<LineItem>();
	private List<ReplacementOrder> replacementOrderList;

    @Autowired
    private ShippingOrderService shippingOrderService;

    @Autowired
    private ReplacementOrderService replacementOrderService;

    @Autowired
    private LineItemDao lineItemDao;

    @Autowired
    private InventoryService inventoryService;

	private ReplacementOrder replacementOrder;

    @ValidationMethod(on = "searchShippingOrder")
    public void validateSearch() {
        if (shippingOrderId == null && gatewayOrderId == null) {
            getContext().getValidationErrors().add("1", new SimpleError("Please Enter a Search Parameter"));
        }
    }

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/createReplacementOrder.jsp");
    }

    public Resolution searchShippingOrder() {
	    if(shippingOrderId != null){
            shippingOrder = shippingOrderService.find(Long.parseLong(shippingOrderId));
	    }
	    else if(gatewayOrderId != null){
		    shippingOrder = shippingOrderService.findByGatewayOrderId(gatewayOrderId);
	    }
        if (shippingOrder == null) {
            addRedirectAlertMessage(new SimpleMessage("No shipping order found  "));
            return new ForwardResolution("/pages/admin/createReplacementOrder.jsp");
        }
        for (LineItem lineItem : shippingOrder.getLineItems()) {
            lineItems.add(ReplacementOrderHelper.getLineItemForReplacementOrder(lineItem));
        }
	    shippingOrderId = shippingOrder.getId().toString();
        return new ForwardResolution("/pages/admin/createReplacementOrder.jsp").addParameter("shippingOrderId", shippingOrderId);
    }

    public Resolution createReplacementOrder() {
        if ( (!shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.RTO_Initiated.getId()))
                && (!shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_Returned.getId()))
                && (!shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_Delivered.getId()))
                && (!shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_Shipped.getId()))
                ) {
            addRedirectAlertMessage(new SimpleMessage("Replacement order can be created only for status" + EnumShippingOrderStatus.RTO_Initiated.getName() +
                    " OR <br />" + EnumShippingOrderStatus.SO_Returned.getName() +
                    " OR <br />" + EnumShippingOrderStatus.SO_Shipped.getName() +
                    " OR <br />" + EnumShippingOrderStatus.SO_Delivered.getName()
            )


            );
            return new RedirectResolution("/pages/admin/createReplacementOrder.jsp");
        }

        int valid_item_flag = 0;
        for (LineItem lineItem : lineItems) {
            if(lineItem.getQty() > 0){
                valid_item_flag++;
            }
            if(lineItem.getQty() != 0 && lineItem.getQty() > inventoryService.getAvailableUnbookedInventory(lineItem.getSku())){
                addRedirectAlertMessage(new SimpleMessage("Unable to create replacement order as "+lineItem.getCartLineItem().getProductVariant().getProduct().getName()+" out of stock."));
                return new RedirectResolution("/pages/admin/createReplacementOrder.jsp");
            }
            if(lineItem.getQty() <0 ){
                addRedirectAlertMessage(new SimpleMessage("The quantity of " + lineItem.getCartLineItem().getProductVariant().getProduct().getName() + " cannot be less than zero."));
                return new RedirectResolution("/pages/admin/createReplacementOrder.jsp");
            }
            if (lineItem.getQty() > getLineItemDao().getMatchingLineItemForDuplicateShippingOrder(lineItem, shippingOrder).getQty()) {
                addRedirectAlertMessage(new SimpleMessage("The quantity of " + lineItem.getCartLineItem().getProductVariant().getProduct().getName() + " cannot be more than original quantity."));
                return new RedirectResolution("/pages/admin/createReplacementOrder.jsp");
            }
        }
        if(valid_item_flag == 0){
            addRedirectAlertMessage(new SimpleMessage("The quantity of at least one item should be greater than 0"));
            return new RedirectResolution("/pages/admin/createReplacementOrder.jsp");
        }

        replacementOrder = replacementOrderService.createReplaceMentOrder(shippingOrder, lineItems, isRto);
	    if(replacementOrder == null){
		    addRedirectAlertMessage(new SimpleMessage("Unable to create replacement order."));
            return new RedirectResolution("/pages/admin/createReplacementOrder.jsp");
	    }
	    addRedirectAlertMessage(new SimpleMessage("The Replacement order created. New gateway order id: "+ replacementOrder.getGatewayOrderId()));
        return new ForwardResolution("/pages/admin/createReplacementOrder.jsp");
    }

	@JsonHandler
	public Resolution checkExistingReplacementOrder(){
		Map dataMap = new HashMap();
		if(shippingOrderId != null){
			replacementOrderList = replacementOrderService.getReplacementOrderForRefShippingOrder(Long.parseLong(shippingOrderId));
			if(replacementOrderList.size() > 0){
				HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Shipping order exist", dataMap);
				noCache();
				return new JsonResolution(healthkartResponse);
			}
		}
		HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Shipping order does not exists", dataMap);
		noCache();
		return new JsonResolution(healthkartResponse);
	}

	public Resolution searchReplacementOrders(){
		if(shippingOrderId == null){
			return new ForwardResolution("/pages/admin/searchReplacementOrder.jsp");
		}
		replacementOrderList = replacementOrderService.getReplacementOrderForRefShippingOrder(Long.parseLong(shippingOrderId));
		return new ForwardResolution("/pages/admin/searchReplacementOrder.jsp");
	}

    public String getShippingOrderId() {
        return shippingOrderId;
    }

    public void setShippingOrderId(String shippingOrderId) {
        this.shippingOrderId = shippingOrderId;
    }

    public ShippingOrder getShippingOrder() {
        return this.shippingOrder;
    }

    public void setShippingOrder(ShippingOrder shippingOrder) {
        this.shippingOrder = shippingOrder;
    }

    public Boolean getIsRto() {
        return isRto;
    }

    public void setIsRto(Boolean rto) {
        isRto = rto;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public LineItemDao getLineItemDao() {
        return lineItemDao;
    }

	public ReplacementOrder getReplacementOrder() {
		return replacementOrder;
	}

	public void setReplacementOrder(ReplacementOrder replacementOrder) {
		this.replacementOrder = replacementOrder;
	}

	public List<ReplacementOrder> getReplacementOrderList() {
		return replacementOrderList;
	}

	public void setReplacementOrderList(List<ReplacementOrder> replacementOrderList) {
		this.replacementOrderList = replacementOrderList;
	}

	public String getGatewayOrderId() {
		return gatewayOrderId;
	}

	public void setGatewayOrderId(String gatewayOrderId) {
		this.gatewayOrderId = gatewayOrderId;
	}
}
