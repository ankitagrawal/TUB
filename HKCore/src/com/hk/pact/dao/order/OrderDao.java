package com.hk.pact.dao.order;

import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.core.search.OrderSearchCriteria;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.OrderLifecycleActivity;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.domain.user.UserCodCall;
import com.hk.pact.dao.BaseDao;

public interface OrderDao extends BaseDao {

    public Order getLatestOrderForUser(User user);

    public Page listOrdersForUser(List<OrderStatus> orderStatusList, User user, int page, int perPage);

    public Page searchOrders(OrderSearchCriteria orderSearchCriteria, int pageNo, int perPage);

    @SuppressWarnings("unchecked")
    public List<Order> searchOrders(OrderSearchCriteria orderSearchCriteria);

    @Deprecated
    public Order findByUserAndOrderStatus(User user, EnumOrderStatus orderStatus);

    public Order save(Order order);

    public Long getCountOfOrdersWithStatus(User user ,EnumOrderStatus enumOrderStatus);

    public Long getBookedQtyOfProductVariantInQueue(ProductVariant productVariant);

    public void logOrderActivity(Order order, User user, OrderLifecycleActivity orderLifecycleActivity, String comments);

    public Order findByGatewayOrderId(String gatewayOrderId);

	public List<UserCodCall> getAllUserCodCallOfToday();
}
