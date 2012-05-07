package com.hk.pact.dao.order;

import java.util.Date;
import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.core.search.OrderSearchCriteria;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.OrderLifecycleActivity;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;

public interface OrderDao extends BaseDao {

    public static final String HARYANA = "%haryana%";

    public Order getLatestOrderForUser(User user);

    public Page listOrdersForUser(List<OrderStatus> orderStatusList, User user, int page, int perPage);
    
    public List<Order> getOrdersForUserSortedByDate(List<OrderStatus> orderStatusList, User user);

    public Page searchOrders(OrderSearchCriteria orderSearchCriteria, int pageNo, int perPage);

    @SuppressWarnings("unchecked")
    public List<Order> searchOrders(OrderSearchCriteria orderSearchCriteria);

    public Order findByUserAndOrderStatus(User user, EnumOrderStatus orderStatus);

    public Order save(Order order);

    public Long getCountOfOrdersWithStatus(EnumOrderStatus enumOrderStatus);

    public Long getBookedQtyOfProductVariantInQueue(ProductVariant productVariant);

    public void logOrderActivity(Order order, User user, OrderLifecycleActivity orderLifecycleActivity, String comments);

    /**
     * @param productVariant
     * @return Sum of Qty of lineitems for product variant which are not yet shipped
     */
    public Long getQtyOfProductVariantInQueue(ProductVariant productVariant, List<Long> lineItemStatusList, List<Long> paymentModeList);

    @Deprecated
    public Page searchDeliveryAwaitingOrders(Date startDate, Date endDate, Long orderId, OrderStatus orderStatus, String gatewayOrderId, String trackingId, int pageNo,
            int perPage, Long courierId);

}
