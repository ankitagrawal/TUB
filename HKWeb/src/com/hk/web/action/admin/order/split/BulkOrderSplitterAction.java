package com.hk.web.action.admin.order.split;

import com.akube.framework.gson.JsonUtils;
import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.core.search.OrderSearchCriteria;
import com.hk.domain.order.Order;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.OrderSplitterService;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 7/24/12
 * Time: 11:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class BulkOrderSplitterAction extends BaseAction {

	@Autowired
	OrderStatusService orderStatusService;

	@Autowired
	OrderSplitterService orderSplitterService;

	@Autowired
	OrderService orderService;

	Order order;
    private static Logger logger = LoggerFactory.getLogger(BulkOrderSplitterAction.class);



    @DefaultHandler
    @Secure(hasAnyPermissions = {PermissionConstants.BULK_SPLIT_BO}, authActionBean = AdminPermissionAction.class)
	public Resolution bulkSplitOrders() {
		OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();
		orderSearchCriteria.setOrderStatusList(orderStatusService.getOrderStatuses(Arrays.asList(EnumOrderStatus.Placed)));

		List<Order> orderList = orderService.searchOrders(orderSearchCriteria);
		if (orderList != null) {
            logger.info("Size of order list fetched for unsplit orders is " + orderList.size());
			for (Order order : orderList) {
                logger.warn("order to be split is " + order.getGatewayOrderId());
                orderService.splitBOCreateShipmentEscalateSOAndRelatedTasks(order);
			}
		}
		return new ForwardResolution("/pages/admin/shipment/shipmentCostCalculator.jsp");
	}

    @Secure(hasAnyPermissions = {PermissionConstants.SPLIT_BO}, authActionBean = AdminPermissionAction.class)
	public Resolution splitSingleOrder() {
		boolean shippingOrderExists = orderService.splitBOCreateShipmentEscalateSOAndRelatedTasks(order);
		String message = "";
		if (shippingOrderExists) {
			message = "BO has been Split into SO";
		} else {
			message = "BO Can not be split";
		}
		Map<String, Object> data = new HashMap<String, Object>(1);
		data.put("orderStatus", JsonUtils.hydrateHibernateObject(order.getOrderStatus()));
		HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, message, data);
		return new JsonResolution(healthkartResponse);
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}

