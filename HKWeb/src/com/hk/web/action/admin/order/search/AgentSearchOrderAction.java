package com.hk.web.action.admin.order.search;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.util.drishti.PopulateUserDetail;
import com.hk.constants.core.PermissionConstants;
import com.hk.core.search.OrderSearchCriteria;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.User;
import com.hk.domain.user.UserDetail;
import com.hk.pact.service.UserService;
import com.hk.pact.service.clm.KarmaProfileService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.user.UserDetailService;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
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
    OrderService        orderService;

    @Autowired
    UserService         userService;

    @Autowired
    UserDetailService   userDetailService;

    private PaymentMode paymentMode;

    private String        email;
    private String        phone;
    private String        remoteAddress;

    private List<Order> orderList = new ArrayList<Order>();
    private Page orderPage;
    private final int MAX_ORDERS = 3;

    @DefaultHandler
    public Resolution pre() {

        /*PopulateUserDetail populateUserDetail = new PopulateUserDetail("localhost", "healthkart_stag", "root", "admin");
        populateUserDetail.populateItemData();*/

        remoteAddress = getContext().getRequest().getRemoteHost();
        User customer = null;
        Long phNo = 0L;
        if (getContext().getRequest().getParameterMap().containsKey("phone")){
            phone = getContext().getRequest().getParameter("phone");
            phNo = Long.parseLong(phone);
        }
        if (getContext().getRequest().getParameterMap().containsKey("email")){
            email = getContext().getRequest().getParameter("email");
            customer = userService.findByLogin(email);
        }
        List<UserDetail> userDetailList = null;
        if (customer == null){
            userDetailList = userDetailService.findByPhone(phNo);
            if (userDetailList.size() == 1 ){
                customer = userDetailList.get(0).getUser();
                email = customer.getEmail();
            }
        }


        if ((customer != null) ){
            orderList = orderService.listOrdersForUser(customer, 1, MAX_ORDERS).getList();
            return new ForwardResolution("/pages/admin/agentSearchOrder.jsp");
        }else{
            return  new ForwardResolution("/pages/admin/userDetail.jsp");
        }
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

    public void setEmail(String email) {
        this.email = email;
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

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }
}
