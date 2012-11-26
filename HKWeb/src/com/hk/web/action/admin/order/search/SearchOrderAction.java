package com.hk.web.action.admin.order.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.core.search.OrderSearchCriteria;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.pact.service.clm.KarmaProfileService;
import com.hk.pact.service.order.OrderService;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.SEARCH_ORDERS }, authActionBean = AdminPermissionAction.class)
@Component
public class SearchOrderAction extends BasePaginatedAction {

    /*private static Logger logger    = LoggerFactory.getLogger(SearchOrderAction.class);*/

    @Autowired
    OrderService          orderService;
    @Autowired
    KarmaProfileService karmaProfileService;

    private OrderStatus   orderStatus;
    private Long          orderId;

    private String        gatewayOrderId;
    private PaymentMode   paymentMode;

    private String        login;
    private String        email;
    private String        name;
    private String        phone;

    private Date          startDate;
    private Date          endDate;

    private List<Order>   orderList = new ArrayList<Order>();
    private Page          orderPage;
    private Order         order;
    private ShippingOrder shippingOrder;

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/searchOrder.jsp");
    }

    public Resolution searchOrders() {
        OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();
        orderSearchCriteria.setOrderId(orderId);
        orderSearchCriteria.setGatewayOrderId(gatewayOrderId);
        // We need to do this not null check otherwise there would an 'in' clause for empty list
        if (orderStatus != null)
            orderSearchCriteria.setOrderStatusList(Arrays.asList(orderStatus));
        if (paymentMode != null) {
            orderSearchCriteria.setPaymentModes(Arrays.asList(paymentMode));
        }
        orderSearchCriteria.setPaymentStartDate(startDate).setPaymentEndDate(endDate);
        orderSearchCriteria.setEmail(email).setLogin(login).setName(name).setPhone(phone);
        orderSearchCriteria.setOrderAsc(false);

        orderPage = orderService.searchOrders(orderSearchCriteria, getPageNo(), getPerPage());
        orderList = orderPage.getList();
        return new ForwardResolution("/pages/admin/searchOrder.jsp");
    }

    public int getPerPageDefault() {
        return 20;
    }

    public int getPageCount() {
        return orderPage == null ? 0 : orderPage.getTotalPages();
    }

    public int getResultCount() {
        return orderPage == null ? 0 : orderPage.getTotalResults();
    }

    public Set<String> getParamSet() {
        Set<String> params = new HashSet<String>();
        params.add("email");
        params.add("orderId");
        params.add("gatewayOrderId");
        params.add("login");
        params.add("name");
        params.add("phone");
        return params;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /*
     * public String getTrackingId() { return trackingId; } public void setTrackingId(String trackingId) {
     * this.trackingId = trackingId; }
     */

    public String getGatewayOrderId() {
        return gatewayOrderId;
    }

    public void setGatewayOrderId(String gatewayOrderId) {
        this.gatewayOrderId = gatewayOrderId;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public ShippingOrder getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(ShippingOrder shippingOrder) {
        this.shippingOrder = shippingOrder;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public KarmaProfileService getKarmaProfileService() {
        return karmaProfileService;
    }

    public void setKarmaProfileService(KarmaProfileService karmaProfileService) {
        this.karmaProfileService = karmaProfileService;
    }
}
