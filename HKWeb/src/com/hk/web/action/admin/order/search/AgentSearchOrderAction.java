package com.hk.web.action.admin.order.search;

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
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/17/12
 * Time: 5:01 PM
 * To change this template use File | Settings | File Templates.
 */
//@Secure(hasAnyPermissions = { PermissionConstants.SEARCH_ORDERS }, authActionBean = AdminPermissionAction.class)
@Component
@UrlBinding("/agent/order")
public class AgentSearchOrderAction extends BasePaginatedAction {
    private static Logger logger    = LoggerFactory.getLogger(SearchOrderAction.class);

    @Autowired
    OrderService          orderService;

    private PaymentMode paymentMode;

    private String        email;
    private String        phone;

    private List<Order> orderList = new ArrayList<Order>();
    private Page orderPage;
    private ShippingOrder shippingOrder;

    private final int MAX_ORDERS = 3;

    @DefaultHandler
    public Resolution pre() {
        email = getContext().getRequest().getParameter("email");
        OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();
        orderSearchCriteria.setEmail(email).setLogin(email);
        orderSearchCriteria.setOrderAsc(false);
        orderSearchCriteria.getSearchCriteria().addOrder(org.hibernate.criterion.Order.desc("updateDate"));
        orderPage = orderService.searchOrders(orderSearchCriteria, getPageNo(), MAX_ORDERS);
        // orderPage = orderDao.searchOrders(startDate, endDate, orderId, email, name, phone, orderStatus,paymentMode,
        // gatewayOrderId, trackingId, getPageNo(), getPerPage());
        orderList = orderPage.getList();
        return new ForwardResolution("/pages/admin/agentSearchOrder.jsp");
    }

    public Resolution searchOrders() {

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
        params.add("orderStatus");
        params.add("paymentMode");
        params.add("email");
        params.add("orderId");
        params.add("gatewayOrderId");
        params.add("login");
        // params.add("trackingId");
        params.add("name");
        params.add("phone");
        params.add("startDate");
        params.add("endDate");
        return params;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public String getEmail() {
        return email;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
}
