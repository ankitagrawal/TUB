
package com.hk.admin.pact.service.order;

import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.domain.core.CancellationType;
import com.hk.domain.core.OrderLifecycleActivity;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.user.User;

import java.util.Map;

public interface AdminOrderService {

    public void logOrderActivity(Order order, EnumOrderLifecycleActivity enumOrderLifecycleActivity);

    public void logOrderActivityByAdmin(Order order, EnumOrderLifecycleActivity enumOrderLifecycleActivity, String comments);

    public void logOrderActivity(Order order, User user, OrderLifecycleActivity orderLifecycleActivity, String comments);

    public Order moveOrderBackToActionQueue(Order order, String shippingOrderGatewayId);

    public Order markOrderAsShipped(Order order);

    public Order markOrderAsDelivered(Order order);

    public Order markOrderAsRTO(Order order);

    public Order markOrderAsCompletedWithInstallation(Order order);

    public Order markOrderAsLost(Order order);

    public Order unHoldOrder(Order order);

    public Order putOrderOnHold(Order order);

    public void cancelOrder(Order order, CancellationType cancellationType, String cancellationRemark, User loggedOnUser, Long reconciliationType);


	/**
	 * TODO:#ankit please document all keys and there meaning in the map being returned
	 * @param order
	 * @return
	 */
    public Map<String, String> isCODAllowed(Order order, Double totalPayable);

	public Payment confirmCodOrder(Order order ,String source , User user);


}
