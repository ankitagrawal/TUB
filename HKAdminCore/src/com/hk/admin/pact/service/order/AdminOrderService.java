package com.hk.admin.pact.service.order;

import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.domain.core.CancellationType;
import com.hk.domain.core.OrderLifecycleActivity;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.dto.pricing.PricingDto;

import java.util.List;
import java.util.Map;

public interface AdminOrderService {

    public void logOrderActivity(Order order, EnumOrderLifecycleActivity enumOrderLifecycleActivity);

    public void logOrderActivityByAdmin(Order order, EnumOrderLifecycleActivity enumOrderLifecycleActivity, String comments);

    public void logOrderActivity(Order order, User user, OrderLifecycleActivity orderLifecycleActivity, String comments);

    public Order moveOrderBackToActionQueue(Order order, String shippingOrderGatewayId);

    public Order markOrderAsShipped(Order order);

    public Order markOrderAsDelivered(Order order);

    public Order markOrderAsRTO(Order order);

    public Order markOrderAsLost(Order order);

    public Order unHoldOrder(Order order);

    public Order putOrderOnHold(Order order);

    public void cancelOrder(Order order, CancellationType cancellationType, String cancellationRemark, User loggedOnUser);

	public boolean splitBOEscalateSOCreateShipmentAndRelatedTasks(Order order);

     public Map<String, String> isCODAllowed(Order order , PricingDto pricingDto);

}
