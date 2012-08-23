package com.hk.pact.service.order;

import com.akube.framework.dao.Page;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.OrderSearchCriteria;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.Order;
import com.hk.domain.order.OrderCategory;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.User;
import com.hk.exception.OrderSplitException;

import java.util.List;
import java.util.Set;

public interface OrderService {

    public Order save(Order order);

    public Order find(Long orderId);

    public Order findByUserAndOrderStatus(User user, EnumOrderStatus orderStatus);

    public Long getCountOfOrdersWithStatus();

    public OrderStatus getOrderStatus(EnumOrderStatus enumOrderStatus);

    public Page searchOrders(OrderSearchCriteria orderSearchCriteria, int pageNo, int perPage);

    public List<Order> searchOrders(OrderSearchCriteria orderSearchCriteria);

    public Set<ShippingOrder> createShippingOrders(Order order);

    public void processOrderForAutoEsclationAfterPaymentConfirmed(Order order);

    public Order escalateOrderFromActionQueue(Order order, String shippingOrderGatewayId);

    public Set<OrderCategory> getCategoriesForBaseOrder(Order order);

    public Category getBasketCategory(ShippingOrder shippingOrder);

    public Order getLatestOrderForUser(User user);

    public Page listOrdersForUser(User user, int page, int perPage);


    /**
     * @param order
     * @return set of shipping orders which are split/derived from a base order
     * @throws OrderSplitException
     */

    public Set<ShippingOrder> splitOrder(Order order) throws OrderSplitException;

    public boolean updateOrderStatusFromShippingOrders(Order order, EnumShippingOrderStatus soStatus, EnumOrderStatus boStatusOnSuccess);

    public void approvePendingRewardPointsForOrder(Order order);

    public void sendEmailToServiceProvidersForOrder(Order order);

    public ProductVariant getTopDealVariant(Order order);

    public Order findByGatewayOrderId(String gatewayOrderId);

	public boolean isCODAllowed(Order order);

}
