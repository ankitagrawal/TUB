package com.hk.web.action.admin.crm;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.core.search.OrderSearchCriteria;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.dto.pricing.PricingDto;
import com.hk.pact.service.order.OrderService;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/*
 * User: Pratham
 * Date: 22/03/13  Time: 11:46
*/
public class OrderJanamPatniAction extends BaseAction {

    Long orderId;
    String gatewayOrderId;
    Order order;
    List<ShippingOrder> shippingOrders;
    PricingDto pricingDto;


    @Autowired
    OrderService orderService;

    public Resolution pre() {
        OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();
        orderSearchCriteria.setOrderId(orderId);
        orderSearchCriteria.setGatewayOrderId(gatewayOrderId);
        List<Order> orders = orderService.searchOrders(orderSearchCriteria);
        order = orders != null && !orders.isEmpty() ? orders.get(0) : null;
        if(order != null){
            pricingDto = new PricingDto(order.getCartLineItems(), order.getAddress());

        }else{
            addRedirectAlertMessage(new SimpleMessage("Invalid Order Id"));
        }
        return new ForwardResolution("/pages/admin/order/orderOnePage.jsp");
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getGatewayOrderId() {
        return gatewayOrderId;
    }

    public void setGatewayOrderId(String gatewayOrderId) {
        this.gatewayOrderId = gatewayOrderId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<ShippingOrder> getShippingOrders() {
        return shippingOrders;
    }

    public void setShippingOrders(List<ShippingOrder> shippingOrders) {
        this.shippingOrders = shippingOrders;
    }

    public PricingDto getPricingDto() {
        return pricingDto;
    }

    public void setPricingDto(PricingDto pricingDto) {
        this.pricingDto = pricingDto;
    }
}
