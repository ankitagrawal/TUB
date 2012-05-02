package web.action.admin.order;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import web.action.error.AdminPermissionAction;

import com.akube.framework.gson.JsonUtils;
import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.admin.pact.service.order.AdminOrderService;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.service.UserService;
import com.hk.service.shippingOrder.ShippingOrderService;
import com.hk.web.HealthkartResponse;

/**
 * User: rahul Time: 11 Feb, 2010 3:26:58 PM
 */
@Secure(hasAnyPermissions = { PermissionConstants.UPDATE_ORDER }, authActionBean = AdminPermissionAction.class)
@Component
public class OrderOnHoldAction extends BaseAction {

    /*
     * OrderManager orderManager; OrderStatusDao orderStatusDao; OrderDao orderDao; UserDao userDao;
     * OrderLifecycleActivityDao orderLifecycleActivityDao;
     */

    private UserService               userService;
    private AdminOrderService         adminOrderService;

    @Autowired
    private ShippingOrderService      shippingOrderService;

    @Autowired
    private AdminShippingOrderService adminShippingOrderService;

    private Order                     order;
    private ShippingOrder             shippingOrder;

    @DefaultHandler
    @JsonHandler
    public Resolution pre() {
        /*
         * User loggedOnUser = null; if (getPrincipal() != null) { loggedOnUser = userDao.find(getPrincipal().getId()); }
         * order = orderManager.putOrderOnHold(order, loggedOnUser); Map<String, Object> data = new HashMap<String,
         * Object>(); data.put("orderStatus", JsonUtils.hydrateHibernateObject(order.getOrderStatus()));
         * HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "", data);
         * return new JsonResolution(healthkartResponse);
         */

        // TODO: # warehouse fix this
        return null;
    }

    @JsonHandler
    public Resolution holdShippingOrder() {
        adminShippingOrderService.putShippingOrderOnHold(shippingOrder);
        return sendResponse(shippingOrder.getOrderStatus());
    }

    @JsonHandler
    public Resolution unHoldShippingOrder() {
        adminShippingOrderService.unHoldShippingOrder(shippingOrder);
        return sendResponse(shippingOrder.getOrderStatus());
    }

    @JsonHandler
    public Resolution holdOrder() {
        adminOrderService.putOrderOnHold(order);
        return sendResponse(order.getOrderStatus());

    }

    @JsonHandler
    public Resolution unHoldOrder() {

        adminOrderService.unHoldOrder(order);
        return sendResponse(order.getOrderStatus());

        /*
         * order.setOrderStatus(orderStatusDao.find(EnumOrderStatus.Pending.getId())); order = orderDao.save(order);
         * User loggedOnUser = null; if (getPrincipal() != null) { loggedOnUser = userDao.find(getPrincipal().getId()); }
         *//**
             * Order lifecycle activity logging - Order Removed OnHold
             */
        /*
         * orderManager.logOrderActivity(order, loggedOnUser,
         * orderLifecycleActivityDao.find(EnumOrderLifecycleActivity.OrderRemovedOnHold.getId()), null); Map<String,
         * Object> data = new HashMap<String, Object>(); data.put("orderStatus",
         * JsonUtils.hydrateHibernateObject(order.getOrderStatus())); HealthkartResponse healthkartResponse = new
         * HealthkartResponse(HealthkartResponse.STATUS_OK, "", data); return new JsonResolution(healthkartResponse);
         */

    }

    private Resolution sendResponse(OrderStatus orderStatus) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("orderStatus", JsonUtils.hydrateHibernateObject(orderStatus));
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "", data);
        return new JsonResolution(healthkartResponse);
    }

    private Resolution sendResponse(ShippingOrderStatus shippingOrderStatus) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("orderStatus", JsonUtils.hydrateHibernateObject(shippingOrderStatus));
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "", data);
        return new JsonResolution(healthkartResponse);
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ShippingOrder getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(ShippingOrder shippingOrder) {
        this.shippingOrder = shippingOrder;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setShippingOrderService(ShippingOrderService shippingOrderService) {
        this.shippingOrderService = shippingOrderService;
    }
}
