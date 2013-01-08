package com.hk.web.action.admin.shippingOrder;

import java.util.*;

import com.hk.admin.pact.service.shippingOrder.ShipmentService;
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
import com.hk.constants.core.RoleConstants;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.helper.ShippingOrderHelper;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.web.action.admin.queue.ActionAwaitingQueueAction;
import com.hk.web.action.error.AdminPermissionAction;

@Component
public class SplitShippingOrderAction extends BaseAction {

    private static Logger logger = LoggerFactory.getLogger(SplitShippingOrderAction.class);

    private ShippingOrder shippingOrder;
    private List<LineItem> lineItems;
    @Autowired
    private ShippingOrderService shippingOrderService;
    @Autowired
    private ShippingOrderStatusService shippingOrderStatusService;
    @Autowired
    private LineItemDao lineItemDao;
    @Autowired
    ShippingOrderDao shippingOrderDao;
    @Autowired
    ShipmentService shipmentService;

    private boolean dropShipItemPresentInSelectedItems;
    private boolean dropShipItemPresentInRemainingItems;

    @DontValidate
    @DefaultHandler
    @Secure(hasAnyRoles = {RoleConstants.ADMIN, RoleConstants.GOD, RoleConstants.CATEGORY_MANAGER}, authActionBean = AdminPermissionAction.class)
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/order/splitShippingOrder.jsp");
    }

    public Resolution splitShippingOrder() {

        if (shippingOrder != null && EnumShippingOrderStatus.SO_ActionAwaiting.getId().equals(shippingOrder.getOrderStatus().getId())) {

            Set<LineItem> selectedLineItems = new HashSet<LineItem>();
           
            for (LineItem lineItem : lineItems) {
                if (lineItem != null) {
                    logger.debug("lineItem: " + lineItem.getSku().getProductVariant());
                    selectedLineItems.add(lineItem);
                }
            }
            if (selectedLineItems.size() == shippingOrder.getLineItems().size()) {
                addRedirectAlertMessage(new SimpleMessage("Invalid LineItem selection for Shipping Order : " + shippingOrder.getGatewayOrderId() + ". Cannot be split."));
                return new RedirectResolution(ActionAwaitingQueueAction.class);
            }

            Set<LineItem> originalShippingItems = shippingOrder.getLineItems();
            originalShippingItems.removeAll(selectedLineItems);

            for (LineItem remainingLineItem : originalShippingItems) {
                if ((remainingLineItem.getSku().getProductVariant().getProduct().isDropShipping())) {
                    dropShipItemPresentInRemainingItems = true;
                    break;
                }
            }

            ShippingOrder newShippingOrder = shippingOrderService.createSOWithBasicDetails(shippingOrder.getBaseOrder(), shippingOrder.getWarehouse());
            newShippingOrder.setBaseOrder(shippingOrder.getBaseOrder());
            newShippingOrder.setServiceOrder(false);
            newShippingOrder.setOrderStatus(shippingOrderStatusService.find(EnumShippingOrderStatus.SO_ActionAwaiting));
            newShippingOrder.setBasketCategory(shippingOrder.getBasketCategory());
            newShippingOrder = shippingOrderService.save(newShippingOrder);

            for (LineItem selectedLineItem : selectedLineItems) {
                selectedLineItem.setShippingOrder(newShippingOrder);
                if ((selectedLineItem.getSku().getProductVariant().getProduct().isDropShipping())) {
                    dropShipItemPresentInSelectedItems = true;
                }

                lineItemDao.save(selectedLineItem);
            }
            shippingOrderDao.refresh(newShippingOrder);

            if (dropShipItemPresentInSelectedItems) {
                newShippingOrder.setDropShipping(true);
            } else {
                newShippingOrder.setDropShipping(false);
            }
            ShippingOrderHelper.updateAccountingOnSOLineItems(newShippingOrder, newShippingOrder.getBaseOrder());
            newShippingOrder.setAmount(ShippingOrderHelper.getAmountForSO(newShippingOrder));
            newShippingOrder = shippingOrderService.setGatewayIdAndTargetDateOnShippingOrder(newShippingOrder);
            newShippingOrder = shippingOrderService.save(newShippingOrder);
            shipmentService.createShipment(newShippingOrder);

            /**
             * Fetch previous shipping order and recalculate amount
             */

            shippingOrderDao.refresh(shippingOrder);
            //shippingOrder = shippingOrderService.find(shippingOrder.getId());
            ShippingOrderHelper.updateAccountingOnSOLineItems(shippingOrder, shippingOrder.getBaseOrder());
            shippingOrder.setAmount(ShippingOrderHelper.getAmountForSO(shippingOrder));

            if (dropShipItemPresentInRemainingItems) {
                shippingOrder.setDropShipping(true);
            } else {
                shippingOrder.setDropShipping(false);
            }
            shippingOrder = shippingOrderService.save(shippingOrder);

            shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Split);

            addRedirectAlertMessage(new SimpleMessage("Shipping Order : " + shippingOrder.getGatewayOrderId() + " was split manually."));
            return new RedirectResolution(ActionAwaitingQueueAction.class);
        } else {
            addRedirectAlertMessage(new SimpleMessage("Shipping Order : " + shippingOrder.getGatewayOrderId() + " is in incorrect status cannot be split."));
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


    public void setShippingOrderService(ShippingOrderService shippingOrderService) {
        this.shippingOrderService = shippingOrderService;
    }


    public void setShippingOrderStatusService(ShippingOrderStatusService shippingOrderStatusService) {
        this.shippingOrderStatusService = shippingOrderStatusService;
    }


    public void setLineItemDao(LineItemDao lineItemDao) {
        this.lineItemDao = lineItemDao;
    }
}