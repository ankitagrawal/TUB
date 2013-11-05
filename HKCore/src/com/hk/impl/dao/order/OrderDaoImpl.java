package com.hk.impl.dao.order;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.akube.framework.util.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.dao.Page;
import com.akube.framework.util.BaseUtils;
import com.akube.framework.util.FormatUtils;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.core.search.OrderSearchCriteria;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.OrderLifecycleActivity;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.Order;
import com.hk.domain.order.OrderLifecycle;
import com.hk.domain.payment.Payment;
import com.hk.domain.user.User;
import com.hk.domain.user.UserCodCall;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.dao.order.OrderLifecycleDao;
import com.hk.pact.service.store.StoreService;

@Repository
@SuppressWarnings("unchecked")
public class OrderDaoImpl extends BaseDaoImpl implements OrderDao {

    private static Logger     logger = LoggerFactory.getLogger(OrderDaoImpl.class);

    @Autowired
    private OrderLifecycleDao orderLifecycleDao;
    @Autowired
    private StoreService      storeService;

    public Order findByGatewayOrderId(String gatewayOrderId) {
        return (Order) getSession().createQuery("from Order o where o.gatewayOrderId = :gatewayOrderId").setString("gatewayOrderId", gatewayOrderId).uniqueResult();
    }

    public Order getLatestOrderForUser(User user) {
        @SuppressWarnings( { "unchecked" })
        List<Order> orders = getSession().createQuery("from Order o where o.user = :user and " + "o.orderStatus.id <> :incartOrderStatusId " +
        		"order by o.payment.paymentDate desc").setParameter(
                "incartOrderStatusId", EnumOrderStatus.InCart.getId()).setParameter("user", user).setMaxResults(1).list();
        return orders == null || orders.isEmpty() ? null : orders.get(0);
    }
    
    

    public Page listOrdersForUser(List<OrderStatus> orderStatusList, User user, int page, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Order.class);
        Boolean[] subscriptionOrders={false};
        criteria.add(Restrictions.in("orderStatus", orderStatusList));
        criteria.add(Restrictions.eq("user", user));
        criteria.add(Restrictions.in("subscriptionOrder",subscriptionOrders));
        criteria.addOrder(org.hibernate.criterion.Order.desc("createDate"));
        return list(criteria, page, perPage);
    }

    public Page searchOrders(OrderSearchCriteria orderSearchCriteria, int pageNo, int perPage) {
        DetachedCriteria searchCriteria = orderSearchCriteria.getSearchCriteria();
        //another query to get unique BO ID's
        searchCriteria.setProjection(Projections.distinct(Projections.id()));
        List<Long> baseOrderIds = findByCriteria(searchCriteria);

        if(baseOrderIds.isEmpty()){
            return list(searchCriteria, pageNo, perPage);
        } else{
            //passing the same to get the whole BO Object
            DetachedCriteria uniqueCriteria = DetachedCriteria.forClass(Order.class);
            uniqueCriteria.add(Restrictions.in("id", baseOrderIds));
            uniqueCriteria.addOrder(org.hibernate.criterion.Order.asc("targetDispatchDate"));
            return list(uniqueCriteria, baseOrderIds.size(), pageNo, perPage);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Order> searchOrders(OrderSearchCriteria orderSearchCriteria) {
        DetachedCriteria searchCriteria = orderSearchCriteria.getSearchCriteria();

        // TODO: fix later in rewrite
        // searchCriteria.setMaxResults(10000);
        searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return findByCriteria(searchCriteria);
    }

    public Order findByUserAndOrderStatus(User user, EnumOrderStatus orderStatus) {
        try {
            List<Order> existingOrders = (List) getSession().createQuery("from Order o where o.user = :user and o.orderStatus.id = :orderStatusId order by o.createDate desc").setEntity(
                    "user", user).setLong("orderStatusId", orderStatus.getId()).list();

            if (existingOrders != null && existingOrders.size() > 1) {
                logger.error("Hibernate Exception in findByUserAndOrderStatus for user " + user.getId() + "existing no of orders " + existingOrders.size());
                return existingOrders.get(0);
            } else if (existingOrders != null && existingOrders.size() == 1) {
                return existingOrders.get(0);
            } else {
                return null;
            }
        } catch (HibernateException e) {
            logger.error("Hibernate Exception in findByUserAndOrderStatus for user " + user.getId());
            throw e;
        }
    }

    @Transactional
    public Order save(Order order) {
        if (order != null) {
            if (order.getCreateDate() == null)
                order.setCreateDate(BaseUtils.getCurrentTimestamp());
            if (order.getAmount() != null && order.getAmount() > 0) {
                order.setAmount(FormatUtils.getCurrencyPrecision(order.getAmount()));
            }
            //order.setUpdateDate(BaseUtils.getCurrentTimestamp());
            if (order.getStore() == null) {
                order.setStore(storeService.getDefaultStore());
            }

            if (order.getShippingOrders() != null && !order.getShippingOrders().isEmpty()) {
              if (order.getOrderStatus().getId().equals(EnumOrderStatus.InCart.getId())) {
                logger.error("Hibernate trying to save stale Order object whereas the order is already split - " + order.getId());
                return order;
              }
            }

          if (order.getPayment() != null) {
            if (order.getOrderStatus().getId().equals(EnumOrderStatus.InCart.getId())) {
              logger.error("Hibernate trying to save stale Order object whereas the order is placed - " + order.getId());
              return order;
            }
          }else if(!order.getPayments().isEmpty()){
            for (Payment payment : order.getPayments()) {
              if(payment.getPaymentStatus().getId().equals(EnumPaymentStatus.SUCCESS.getId())){
                logger.error("Hibernate trying to save stale Order object whereas the order is placed and payment is successful - " + order.getId());
                return order;
              }
            }
          }
        }
        return (Order) super.save(order);
    }

    public Long getCountOfOrdersWithStatus( User user ,EnumOrderStatus enumOrderStatus) {
        return (Long) getSession().createQuery("select count(*) from Order o where o.user = :user and  o.orderStatus.id = :orderStatusId").setEntity("user",user).setLong("orderStatusId", enumOrderStatus.getId()).uniqueResult();
    }

    public Long getBookedQtyOfProductVariantInQueue(ProductVariant productVariant) {
        // TODO : A BO can be put onhold even after split.
        String query = "select sum(cli.qty) from CartLineItem cli " + "where cli.productVariant = :productVariant " + "and cli.order.orderStatus.id in (:orderStatusIds) ";
        Long qtyInQueue = (Long) getSession().createQuery(query).setParameter("productVariant", productVariant).setParameterList("orderStatusIds",
                Arrays.asList(EnumOrderStatus.Placed.getId(), EnumOrderStatus.OnHold.getId())).uniqueResult();
        if (qtyInQueue == null) {
            qtyInQueue = 0L;
        }
        return qtyInQueue;
    }

    public void logOrderActivity(Order order, User user, OrderLifecycleActivity orderLifecycleActivity, String comments) {
        OrderLifecycle orderLifecycle = new OrderLifecycle();
        orderLifecycle.setOrder(order);
        orderLifecycle.setOrderLifecycleActivity(orderLifecycleActivity);
        orderLifecycle.setUser(user);
        orderLifecycle.setComments(comments);
        orderLifecycle.setActivityDate(new Date());
        getOrderLifecycleDao().save(orderLifecycle);
    }

    public OrderLifecycleDao getOrderLifecycleDao() {
        return orderLifecycleDao;
    }

    public void setOrderLifecycleDao(OrderLifecycleDao orderLifecycleDao) {
        this.orderLifecycleDao = orderLifecycleDao;
    }

	public List<UserCodCall> getAllUserCodCallOfToday(){
        Date today = new Date();
		Date createDate = DateUtils.getStartOfDay(today);
        Date nextDate = DateUtils.getEndOfDay(today);
		String query = "from UserCodCall  where createDate >= :createDate and createDate <= :nextDate";
		return getSession().createQuery(query).setDate("createDate",createDate).setDate("nextDate",nextDate).list();
	}

  public int getOrderCountByUser(Long userId){
      String sql = "select count(*) from base_order where user_id = " + userId + " and order_status_id = " + EnumOrderStatus.Delivered.getId();
      return countByNativeSql(sql);
      
      
  }
}
