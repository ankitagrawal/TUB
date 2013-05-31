package com.hk.web.action.admin.queue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hk.loyaltypg.service.LoyaltyProgramService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.order.ShippingOrder;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * Created by IntelliJ IDEA. User: Pratham Date: Jul 1, 2011 Time: 12:33:24 PM To change this template use File |
 * Settings | File Templates.
 */
@Component
public class DeliveryAwaitingQueueAction extends BasePaginatedAction {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ShippingOrderService shippingOrderService;
    @Autowired
    private AdminShippingOrderService adminShippingOrderService;
    @Autowired
    private ShippingOrderStatusService shippingOrderStatusService;
    @Autowired
    AwbService awbService;

    Page shippingOrderPage;
    @Autowired
    CourierService courierService;

    List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>();

    private Long orderId;
    private String gatewayOrderId;
    private Courier courier;
    private Date deliveryDate;
    Date startDate;
    Date endDate;
    private String trackingId;

    @DontValidate
    @DefaultHandler
    @Secure(hasAnyPermissions = {PermissionConstants.VIEW_DELIVERY_QUEUE}, authActionBean = AdminPermissionAction.class)
    public Resolution pre() {

        List<Courier> courierList = new ArrayList<Courier>();
        if (courier == null) {
            courierList = getCourierService().getAllCouriers();
        } else {
            courierList.add(courier);
        }

        ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
        shippingOrderSearchCriteria.setShippingOrderStatusList(shippingOrderStatusService.getOrderStatuses(EnumShippingOrderStatus.getStatusForDeliveryAwaiting()));
        shippingOrderSearchCriteria.setOrderId(orderId).setGatewayOrderId(gatewayOrderId);
        shippingOrderSearchCriteria.setCourierList(courierList);

        shippingOrderPage = getShippingOrderService().searchShippingOrders(shippingOrderSearchCriteria, getPageNo(), getPerPage());
        if (shippingOrderPage != null) {
            shippingOrderList = shippingOrderPage.getList();
        }

        return new ForwardResolution("/pages/admin/deliveryAwaitingQueue.jsp");
    }

    @Secure(hasAnyPermissions = {PermissionConstants.VIEW_DELIVERY_QUEUE}, authActionBean = AdminPermissionAction.class)
    public Resolution searchOrders() {

        List<Courier> courierList = new ArrayList<Courier>();
        List<Awb> awbList = new ArrayList<Awb>();

        if (trackingId != null) {
            awbList = awbService.getAvailableAwbListForCourierByWarehouseCodStatus(courier, trackingId, null, null, EnumAwbStatus.Used.getAsAwbStatus());
            if(awbList.isEmpty()){
                addRedirectAlertMessage(new SimpleMessage("InValid Tracking ID"));
                return new ForwardResolution("/pages/admin/searchShippingOrder.jsp");
            }
        } else if (courier != null) {
            courierList.add(courier);
        } else {
            courierList = getCourierService().getAllCouriers();
        }

        ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
        shippingOrderSearchCriteria.setShippingOrderStatusList(getShippingOrderStatusService().getOrderStatuses(EnumShippingOrderStatus.getStatusSearchingInDeliveryQueue()));
        shippingOrderSearchCriteria.setOrderId(orderId).setGatewayOrderId(gatewayOrderId);
        shippingOrderSearchCriteria.setCourierList(courierList);
        shippingOrderSearchCriteria.setAwbList(awbList);

        shippingOrderPage = getShippingOrderService().searchShippingOrders(shippingOrderSearchCriteria, getPageNo(), getPerPage());
        if (shippingOrderPage != null) {
            shippingOrderList = shippingOrderPage.getList();
        }

        return new ForwardResolution("/pages/admin/deliveryAwaitingQueue.jsp");
    }

    @Secure(hasAnyPermissions = {PermissionConstants.UPDATE_DELIVERY_QUEUE}, authActionBean = AdminPermissionAction.class)
    public Resolution markShippingOrderAsDelivered() {
        if (shippingOrderList != null && !shippingOrderList.isEmpty()) {
            for (ShippingOrder shippingOrder : shippingOrderList) {
                getAdminShippingOrderService().markShippingOrderAsDelivered(shippingOrder);
            }
            addRedirectAlertMessage(new SimpleMessage("Orders have been marked as Delivered"));
        } else {
            addRedirectAlertMessage(new SimpleMessage("Please select at least one order to be marked as Delivered"));
        }
        return new RedirectResolution(DeliveryAwaitingQueueAction.class);
    }

    public int getPerPageDefault() {
        return 30;
    }

    public int getPageCount() {
        return shippingOrderPage == null ? 0 : shippingOrderPage.getTotalPages();
    }

    public int getResultCount() {
        return shippingOrderPage == null ? 0 : shippingOrderPage.getTotalResults();
    }

    public Set<String> getParamSet() {
        Set<String> params = new HashSet<String>();
        params.add("courier");
        // params.add("startDate");
        // params.add("endDate");
        params.add("orderId");
        params.add("gatewayOrderId");
        params.add("trackingId");
        return params;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getGatewayOrderId() {
        return gatewayOrderId;
    }

    public void setGatewayOrderId(String gatewayOrderId) {
        this.gatewayOrderId = gatewayOrderId;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<ShippingOrder> getShippingOrderList() {
        return shippingOrderList;
    }

    public void setShippingOrderList(List<ShippingOrder> shippingOrderList) {
        this.shippingOrderList = shippingOrderList;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public ShippingOrderService getShippingOrderService() {
        return shippingOrderService;
    }

    public void setShippingOrderService(ShippingOrderService shippingOrderService) {
        this.shippingOrderService = shippingOrderService;
    }

    public ShippingOrderStatusService getShippingOrderStatusService() {
        return shippingOrderStatusService;
    }

    public void setShippingOrderStatusService(ShippingOrderStatusService shippingOrderStatusService) {
        this.shippingOrderStatusService = shippingOrderStatusService;
    }

    public CourierService getCourierService() {
        return courierService;
    }

    public void setCourierService(CourierService courierService) {
        this.courierService = courierService;
    }

    public AdminShippingOrderService getAdminShippingOrderService() {
        return adminShippingOrderService;
    }

    public void setAdminShippingOrderService(AdminShippingOrderService adminShippingOrderService) {
        this.adminShippingOrderService = adminShippingOrderService;
    }

}