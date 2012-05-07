package com.hk.impl.dao.order;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.dao.Page;
import com.akube.framework.util.BaseUtils;
import com.akube.framework.util.FormatUtils;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.core.search.OrderSearchCriteria;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.OrderLifecycleActivity;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.Order;
import com.hk.domain.order.OrderLifecycle;
import com.hk.domain.user.User;
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

    public Order getLatestOrderForUser(User user) {
        @SuppressWarnings( { "unchecked" })
        List<Order> orders = getSession().createQuery("from Order o where o.user = :user and " + "o.orderStatus.id <> :incartOrderStatusId order by o.payment.paymentDate desc").setParameter(
                "incartOrderStatusId", EnumOrderStatus.InCart.getId()).setParameter("user", user).setMaxResults(1).list();
        return orders == null || orders.isEmpty() ? null : orders.get(0);
    }

    public Page listOrdersForUser(List<OrderStatus> orderStatusList, User user, int page, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Order.class);
        criteria.add(Restrictions.in("orderStatus", orderStatusList));
        criteria.add(Restrictions.eq("user", user));
        criteria.addOrder(org.hibernate.criterion.Order.desc("createDate"));
        return list(criteria, page, perPage);
    }

    public Page searchOrders(OrderSearchCriteria orderSearchCriteria, int pageNo, int perPage) {
        DetachedCriteria searchCriteria = orderSearchCriteria.getSearchCriteria();
        // searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return list(searchCriteria, true, pageNo, perPage);
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
            order.setUpdateDate(BaseUtils.getCurrentTimestamp());
            if (order.getStore() == null) {
                order.setStore(storeService.getDefaultStore());
            }
        }
        return (Order) super.save(order);
    }

    public Long getCountOfOrdersWithStatus(EnumOrderStatus enumOrderStatus) {
        return (Long) getSession().createQuery("select count(*) from Order o where o.orderStatus.id = :orderStatusId").setLong("orderStatusId", enumOrderStatus.getId()).uniqueResult();
    }

    public List<Order> getOrdersForUserSortedByDate(List<OrderStatus> orderStatusList, User user) {
        DetachedCriteria orderCriteria = DetachedCriteria.forClass(Order.class);
        DetachedCriteria userCriteria = orderCriteria.createCriteria("user");
        userCriteria.add(Restrictions.eq("id", user.getId()));

        orderCriteria.add(Restrictions.in("orderStatus", orderStatusList));
        orderCriteria.addOrder(org.hibernate.criterion.Order.desc("createDate"));
        return findByCriteria(orderCriteria);
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

    /*
     * public List<Order> findDeliveredOrders() { List<LineItemStatus> applicableLineItemStatus = new ArrayList<LineItemStatus>();
     * applicableLineItemStatus.add(lineItemStatusDao.find(EnumLineItemStatus.DELIVERED.getId()));
     * applicableLineItemStatus.add(lineItemStatusDao.find(EnumLineItemStatus.RETURNED.getId()));
     * applicableLineItemStatus.add(lineItemStatusDao.find(EnumLineItemStatus.LOST.getId())); Criteria criteria =
     * getSession().createCriteria(Order.class); Criteria lineItemCriteria = criteria.createCriteria("lineItems");
     * lineItemCriteria.setFetchMode("lineItems", FetchMode.JOIN);
     * lineItemCriteria.add(Restrictions.in("lineItemStatus", applicableLineItemStatus));
     * criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); return criteria.list(); }
     */
    /*
     * public List<Order> getOrdersForHistory(User user) { Criteria criteria =
     * getSession().createCriteria(Order.class); criteria.add(Restrictions.eq("user", user));
     * criteria.add(Restrictions.ne("orderStatus.id", EnumOrderStatus.InCart.getId()));
     * criteria.addOrder(org.hibernate.criterion.Order.desc("createDate")); return criteria.list(); }
     */

    /*
     * public List<LineItem> findLineItemsOfOrdersForTimeFrame(Date startDate, Date endDate, OrderStatus orderStatus,
     * Category topLevelCategory) { List<OrderStatus> applicableOrderStatus = new ArrayList<OrderStatus>(); if
     * (orderStatus != null) { applicableOrderStatus.add(orderStatus); } else {
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Pending.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Shipped.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Delivered.getId())); } List<Category>
     * applicableCategories = new ArrayList<Category>(); if (topLevelCategory != null && !topLevelCategory.equals("")) {
     * applicableCategories.add(topLevelCategory); } if (applicableCategories.size() == 0) { applicableCategories =
     * CategoryConstants.allCategoriesList; } String query = "select li from LineItem li join
     * li.productVariant.product.primaryCategory c where c in (:applicableCategories) " + "and li.order.orderStatus in
     * (:applicableOrderStatus) " + "and li.order.payment.paymentDate >= :startDate and li.order.payment.paymentDate <=
     * :endDate"; return getSession().createQuery(query) .setParameterList("applicableCategories", applicableCategories)
     * .setParameterList("applicableOrderStatus", applicableOrderStatus) .setParameter("startDate", startDate)
     * .setParameter("endDate", endDate) .list(); } public List<Order> findOrdersForTimeFrame(Date startDate, Date
     * endDate) { List<OrderStatus> applicableOrderStatus = new ArrayList<OrderStatus>();
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Pending.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Shipped.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Delivered.getId())); String query = "from Order o
     * where o.orderStatus in (:applicableOrderStatus) and o.payment.paymentDate >= :startDate and o.payment.paymentDate <=
     * :endDate order by o.payment.paymentDate asc"; return getSession().createQuery(query)
     * .setParameterList("applicableOrderStatus", applicableOrderStatus) .setParameter("startDate", startDate)
     * .setParameter("endDate", endDate) .list(); }
     */

    /**
     * @param productVariant
     * @return Sum of Qty of lineitems for product variant which are not yet shipped
     */
    public Long getQtyOfProductVariantInQueue(ProductVariant productVariant, List<Long> lineItemStatusList, List<Long> paymentModeList) {

        // TODO: # warehouse fix this.

        /*
         * String query = "select sum(li.qty) from LineItem li " + "where li.productVariant = :productVariant and
         * li.lineItemType = :lineItemType and li.lineItemStatus.id in (:lineItemStatus) " + "and
         * li.order.payment.paymentStatus.id in (:paymentStatusId) "; Long qtyInQueue = (Long)
         * getSession().createQuery(query) .setEntity("productVariant", productVariant) .setEntity("lineItemType",
         * lineItemTypeDaoProvider.get().find(EnumLineItemType.Product.getId())) .setParameterList("lineItemStatus",
         * lineItemStatusList) .setParameterList("paymentStatusId", paymentModeList) .uniqueResult(); if (qtyInQueue ==
         * null) { qtyInQueue = 0L; } return qtyInQueue;
         */

        return null;
    }

    /*
     * public Long getBookedQtyOfProductVariantInActionQueue(ProductVariant productVariant) { String query = "select
     * sum(li.qty) from LineItem li " + "where li.productVariant = :productVariant and li.lineItemType = :lineItemType
     * and li.lineItemStatus.id in (:lineItemStatus) " + "and li.order.payment.paymentStatus.id in (:paymentStatusId) ";
     * Long qtyInActionQueue = (Long) getSession().createQuery(query) .setEntity("productVariant", productVariant)
     * .setEntity("lineItemType", lineItemTypeDaoProvider.get().find(EnumLineItemType.Product.getId()))
     * .setParameterList("lineItemStatus", Arrays.asList(EnumLineItemStatus.ACTION_AWAITING.getId()))
     * .setParameterList("paymentStatusId", Arrays.asList(EnumPaymentStatus.SUCCESS.getId(),
     * EnumPaymentStatus.ON_DELIVERY.getId())) .uniqueResult(); if (qtyInActionQueue == null) { qtyInActionQueue = 0L; }
     * return qtyInActionQueue; } public Long getBookedQtyOfProductVariantInPackingQueue(ProductVariant productVariant) {
     * String query1 = "select sum(li.qty) from LineItem li " + "where li.productVariant = :productVariant and
     * li.lineItemType = :lineItemType and li.lineItemStatus.id in (:lineItemStatus)"; Long qtyInPackingQueue = (Long)
     * getSession().createQuery(query1) .setEntity("productVariant", productVariant) .setEntity("lineItemType",
     * lineItemTypeDaoProvider.get().find(EnumLineItemType.Product.getId())) .setParameterList("lineItemStatus",
     * Arrays.asList(EnumLineItemStatus.READY_FOR_PROCESS.getId(), EnumLineItemStatus.GONE_FOR_PRINTING.getId(),
     * EnumLineItemStatus.PICKING.getId(), EnumLineItemStatus.CHECKEDOUT.getId())) .uniqueResult(); if
     * (qtyInPackingQueue == null) qtyInPackingQueue = 0L; String query2 = "select sum(pvi.qty) from
     * ProductVariantInventory pvi " + "where pvi.qty = :checkedOutQty and pvi.lineItem.productVariant = :productVariant
     * and pvi.lineItem.lineItemStatus.id in (:lineItemStatus)"; Long checkedOutItemCount = (Long)
     * getSession().createQuery(query2) .setLong("checkedOutQty", -1L) .setParameter("productVariant", productVariant)
     * .setParameterList("lineItemStatus", Arrays.asList(EnumLineItemStatus.READY_FOR_PROCESS.getId(),
     * EnumLineItemStatus.GONE_FOR_PRINTING.getId(), EnumLineItemStatus.PICKING.getId(),
     * EnumLineItemStatus.CHECKEDOUT.getId())) .uniqueResult(); // It will return negative value if (checkedOutItemCount ==
     * null) checkedOutItemCount = 0L; return qtyInPackingQueue + checkedOutItemCount; }
     */
    /*
     * public List<User> getMailingList(Category category) { String query = "select distinct li.order.user from
     * LineItem li left join li.productVariant.product.categories c" + " left join li.order.user.roles r " + "where c in
     * (:categoryList) and li.lineItemType = :lineItemType " + "and r in (:roleList)"; return
     * getSession().createQuery(query) .setParameterList("categoryList", Arrays.asList(category))
     * .setEntity("lineItemType", lineItemTypeDaoProvider.get().find(EnumLineItemType.Product.getId()))
     * .setParameterList("roleList", Arrays.asList(roleDao.find(EnumRole.HK_USER.getRoleName()),
     * roleDaoProvider.get().find(EnumRole.HK_UNVERIFIED.getRoleName()))) .list(); } public List<User>
     * getAllMailingList() { String query = "select distinct u from User u left join u.roles r " + "where r in
     * (:roleList)"; return getSession().createQuery(query) .setParameterList("roleList",
     * Arrays.asList(roleDao.find(EnumRole.HK_USER.getRoleName()))) .list(); } public List<User>
     * getAllUnverifiedMailingList() { String query = "select distinct u from User u left join u.roles r " + "where r in
     * (:roleList)"; return getSession().createQuery(query) .setParameterList("roleList",
     * Arrays.asList(roleDao.find(EnumRole.HK_UNVERIFIED.getRoleName()))) .list(); }
     */

    /*
     * public Long getCODConfirmationTime(Date startDate, Date endDate) { List<OrderStatus> applicableOrderStatus = new
     * ArrayList<OrderStatus>(); applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Pending.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Shipped.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Delivered.getId())); if (startDate == null) {
     * Calendar calendar = Calendar.getInstance(); calendar.add(Calendar.MONTH, -1); startDate = calendar.getTime(); }
     * if (endDate == null) { endDate = new Date(); } String query = "select (sum(UNIX_TIMESTAMP(olc2.activityDate) -
     * UNIX_TIMESTAMP(olc1.activityDate)))/count(olc1.id) from OrderLifecycle olc1, OrderLifecycle olc2 " + "where
     * olc1.order.id = olc2.order.id and olc1.orderLifecycleActivity = :orderPlaced and olc2.orderLifecycleActivity =
     * :codConfirmed " + "and olc1.activityDate between :startDate and :endDate"; Long timetaken = (Long)
     * getSession().createQuery(query) .setParameter("orderPlaced",
     * orderLifecycleActivityDaoProvider.get().find(EnumOrderLifecycleActivity.OrderPlaced.getId()))
     * .setParameter("codConfirmed",
     * orderLifecycleActivityDaoProvider.get().find(EnumOrderLifecycleActivity.ConfirmedAuthorization.getId()))
     * .setParameter("startDate", startDate) .setParameter("endDate", endDate) .uniqueResult(); return timetaken != null ?
     * timetaken : 0; }
     */

    /*
     * public Long getOrderCountByPaymentMode(List<Long> orderIds, Long paymentModeId) { String hqlQuery = "select
     * count(distinct o.id)" + "from Order o where o.id in (:orderIds) and o.payment.paymentMode.id = :paymentModeId ";
     * Long count = (Long) getSession().createQuery(hqlQuery) .setParameterList("orderIds", orderIds)
     * .setParameter("paymentModeId", paymentModeId) .uniqueResult(); return count != null ? count : 0L; }
     */

    /*
     * public Set<Order> getOrderLyingIdleInActionQueue(List<Long> orderIds, Date activityDate, Integer cutOffTime) {
     * Set<Order> orderList = new HashSet<Order>(); List<Long> applicablePaymentModesId = new ArrayList<Long>();
     * applicablePaymentModesId.add(EnumPaymentMode.TECHPROCESS.getId());
     * applicablePaymentModesId.add(EnumPaymentMode.CITRUS.getId()); String hqlQuery = "select distinct li.order from
     * LineItem li where li.order.id in (:orderIds) and li.lineItemStatus.id = :lineItemStatusId " + "and
     * li.order.payment.paymentMode.id in :applicablePaymentModesId and li.order.payment.paymentStatus.id =
     * :paymentStatusId"; List<Order> tpslOrders = (List<Order>) getSession().createQuery(hqlQuery)
     * .setParameterList("orderIds", orderIds) .setParameter("lineItemStatusId",
     * EnumLineItemStatus.ACTION_AWAITING.getId()) .setParameterList("applicablePaymentModesId",
     * applicablePaymentModesId) .setParameter("paymentStatusId", EnumPaymentStatus.SUCCESS.getId()) .list(); if
     * (tpslOrders != null) { orderList.addAll(tpslOrders); } Date endDate = activityDate; Calendar calendar2 =
     * Calendar.getInstance(); calendar2.setTime(endDate); calendar2.set(Calendar.HOUR_OF_DAY, cutOffTime);
     * calendar2.set(Calendar.MINUTE, 0); calendar2.set(Calendar.SECOND, 0); endDate = calendar2.getTime(); String
     * hqlQuery2 = "select distinct ocl.order from OrderLifecycle ocl, LineItem li " + "where ocl.order = li.order and
     * ocl.order.id in (:orderIds) and li.lineItemStatus.id = :lineItemStatusId " + "and ocl.orderLifecycleActivity.id =
     * :orderLifecycleActivityId and ocl.activityDate <= :endDate"; List<Order> codOrders = (List<Order>)
     * getSession().createQuery(hqlQuery2) .setParameterList("orderIds", orderIds) .setParameter("lineItemStatusId",
     * EnumLineItemStatus.ACTION_AWAITING.getId()) .setParameter("orderLifecycleActivityId",
     * EnumOrderLifecycleActivity.ConfirmedAuthorization.getId()) .setParameter("endDate", endDate) .list(); if
     * (codOrders != null) { orderList.addAll(codOrders); } return orderList; } public List<Order>
     * findOrderListForTimeFrameForCategory(Date startDate, Date endDate, Category applicableCategorie) { List<OrderStatus>
     * applicableOrderStatus = new ArrayList<OrderStatus>();
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Pending.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Shipped.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Delivered.getId())); String query = "select
     * distinct(li.order) from LineItem li join li.productVariant.product.primaryCategory c where c in
     * (:applicableCategorie) " + "and li.order.orderStatus in (:applicableOrderStatus) " + "and
     * li.order.payment.paymentDate >= :startDate and li.order.payment.paymentDate <= :endDate"; return
     * getSession().createQuery(query) .setParameter("applicableCategorie", applicableCategorie)
     * .setParameterList("applicableOrderStatus", applicableOrderStatus) .setParameter("startDate", startDate)
     * .setParameter("endDate", endDate) .list(); } public CategoryPerformanceDto
     * findCategoryWiseSalesForTimeFramePerCategory(Date startDate, Date endDate, Category applicableCategorie) { List<OrderStatus>
     * applicableOrderStatus = new ArrayList<OrderStatus>();
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Pending.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Shipped.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Delivered.getId())); String hqlQuery = "select
     * sum(li.qty) as skuCount," + " sum(li.qty*li.markedPrice) as sumOfMrp, sum(li.qty*li.hkPrice) as sumOfHkPrice," + "
     * sum(li.discountOnHkPrice) as sumOfHkPricePostAllDiscounts " + " from LineItem li join
     * li.productVariant.product.primaryCategory c where c in (:applicableCategorie) " + " and li.lineItemType.id =
     * :lineItemType and li.order.orderStatus in (:applicableOrderStatus)" + " and li.order.payment.paymentDate >=
     * :startDate and li.order.payment.paymentDate <= :endDate "; return (CategoryPerformanceDto)
     * getSession().createQuery(hqlQuery) .setParameter("applicableCategorie", applicableCategorie)
     * .setParameter("lineItemType", EnumLineItemType.Product.getId()) .setParameterList("applicableOrderStatus",
     * applicableOrderStatus) .setParameter("startDate", startDate) .setParameter("endDate",
     * endDate).setResultTransformer(Transformers.aliasToBean(CategoryPerformanceDto.class)) .uniqueResult(); } public
     * void updateReconciliationStatus(ReconciliationStatus reconciliationStatus, List<String> gatewayOrderList) {
     * getSession().createQuery("update Order o set o.reconciliationStatus = :reconciliationStatus where
     * o.gatewayOrderId in (:gatewayOrderList)") .setParameter("reconciliationStatus", reconciliationStatus)
     * .setParameterList("gatewayOrderList", gatewayOrderList) .executeUpdate(); }
     */
    /*
     * public Long getOrderCountByOfferInstance(Date paymentDate) { List<OrderStatus> applicableOrderStatus = new
     * ArrayList<OrderStatus>(); applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Pending.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Shipped.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Delivered.getId())); Calendar startDate =
     * Calendar.getInstance(); startDate.setTime(paymentDate); startDate.set(Calendar.HOUR, 0);
     * startDate.set(Calendar.MINUTE, 0); startDate.set(Calendar.SECOND, 0); startDate.set(Calendar.MILLISECOND, 0);
     * Calendar endDate = Calendar.getInstance(); endDate.setTime(paymentDate); endDate.add(Calendar.DAY_OF_YEAR, 1);
     * endDate.set(Calendar.HOUR, 0); endDate.set(Calendar.MINUTE, 0); endDate.set(Calendar.SECOND, 0);
     * endDate.set(Calendar.MILLISECOND, 0); String hqlQuery = "select count(distinct o.id)" + "from Order o where
     * o.orderStatus in (:applicableOrderStatus) and o.offerInstance IS NOT NULL " + "and o.payment.paymentDate >=
     * :startDate and o.payment.paymentDate < :endDate "; Long count = (Long) getSession().createQuery(hqlQuery)
     * .setParameterList("applicableOrderStatus", applicableOrderStatus) .setDate("startDate", startDate.getTime())
     * .setDate("endDate", endDate.getTime()) .uniqueResult(); return count != null ? count : 0L; }
     */

    /*
     * public Long getFirstTimeTxnUsers(Date orderDate) { List<OrderStatus> applicableOrderStatus = new ArrayList<OrderStatus>();
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Pending.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Shipped.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Delivered.getId())); String query1 = "select
     * distinct(o.user.id) from Order o where o.orderStatus in (:applicableOrderStatus) " + "and
     * date(o.payment.paymentDate) < :orderDate"; List<Long> userIdList = (List<Long>)
     * getSession().createQuery(query1) .setParameterList("applicableOrderStatus", applicableOrderStatus)
     * .setParameter("orderDate", orderDate) .list(); String query = "select count(distinct o.user.id) from Order o
     * where o.orderStatus in (:applicableOrderStatus) " + "and date(o.payment.paymentDate) = :orderDate and o.user.id
     * not in (:userIdList)"; return (Long) getSession().createQuery(query) .setParameterList("applicableOrderStatus",
     * applicableOrderStatus) .setParameterList("userIdList", userIdList) .setParameter("orderDate", orderDate)
     * .uniqueResult(); } public List<DaySaleDto> findSalePerTax(Date startDate, Date endDate, List<OrderStatus>
     * applicableOrderStatus) { String hqlQuery = "select date(li.shipDate) as orderShipDate, li.order.orderStatus as
     * orderStatus, t as taxCategory, count(distinct li.order.id) as txnCount, sum(li.qty) as skuCount, " + "
     * sum(li.qty*li.markedPrice) as sumOfMrp, sum(li.qty*li.hkPrice) as sumOfHkPrice, sum(li.discountOnHkPrice) as
     * sumOfAllDiscounts " + " from LineItem li, Tax t where li.shipDate is not null and li.tax.id = t.id and
     * li.lineItemType.id = :lineItemType and li.order.orderStatus in (:applicableOrderStatus) " + " and li.shipDate >=
     * :startDate and li.shipDate <= :endDate and lower(li.order.address.state) like :haryana " + " group by
     * date(li.shipDate), li.order.orderStatus, t.id order by date(li.shipDate), li.order.orderStatus.id, t.id "; return
     * getSession().createQuery(hqlQuery) .setParameter("lineItemType", EnumLineItemType.Product.getId())
     * .setParameterList("applicableOrderStatus", applicableOrderStatus) .setParameter("startDate", startDate)
     * .setParameter("endDate", endDate) .setParameter("haryana",
     * HARYANA).setResultTransformer(Transformers.aliasToBean(DaySaleDto.class)) .list(); }
     */

    /*
     * public List<NetMarginPerProductVariantDto> findNetMarginReportPerProductVariant(Date startDate, Date endDate) {
     * StringBuilder netMarginSql = new StringBuilder("select p.primaryCategory as primaryCategory ,pv.id as
     * productVariantId,pv.hkPrice as hkPrice,pv.costPrice as costPrice," + "t.value as taxRate,sum(li.qty) as
     * quantity," + "(pv.hkPrice/(1+t.value+t.value*.05))as
     * taxAdjPrice,((pv.hkPrice/(1+t.value+t.value*.05))-pv.costPrice)as netMargin," +
     * "sum(rli.shippingChargedByCourier*li.qty)as totalShipping," + "sum(rli.collectionChargedByCourier*li.qty)as
     * totalCollection,p.name as productName,li.productVariant as productVariant," +
     * "((sum(rli.shippingChargedByCourier*li.qty)+sum(rli.collectionChargedByCourier*li.qty))/sum(li.qty))as
     * shippingCollectionPerPV," +
     * "(((pv.hkPrice/(1+t.value+t.value*.05))-pv.costPrice)-((sum(rli.shippingChargedByCourier*li.qty)+sum(rli.collectionChargedByCourier*li.qty))/sum(li.qty)))" +
     * "as netContMargin" + " from LineItem li, RetailLineItem rli,Product p,Tax t," + " ProductVariant pv " + " where" + "
     * li.id = rli.lineItem.id and li.productVariant.id = pv.id and p.id=pv.product.id and pv.tax.id=t.id" + " and
     * li.shipDate is not null and rli.shippingChargedByCourier is not null "); if (startDate != null && endDate !=
     * null) { netMarginSql.append(" and date(li.shipDate) >= :startDate and date(li.shipDate) <= :endDate "); } else {
     * netMarginSql.append(" and month(li.shipDate) = month(:startDate) and year(li.shipDate) = year(:startDate) "); }
     * netMarginSql.append("group by pv.id order by p.primaryCategory,pv.id"); Query netMarginSqlQuery =
     * getSession().createQuery(netMarginSql.toString()); netMarginSqlQuery.setParameter("startDate", startDate); if
     * (startDate != null && endDate != null) { netMarginSqlQuery.setParameter("endDate", endDate); }
     * netMarginSqlQuery.setResultTransformer(Transformers.aliasToBean(NetMarginPerProductVariantDto.class)); return
     * netMarginSqlQuery.list(); }
     */

    @Deprecated
    public Page searchDeliveryAwaitingOrders(Date startDate, Date endDate, Long orderId, OrderStatus orderStatus, String gatewayOrderId, String trackingId, int pageNo,
            int perPage, Long courierId) {

        // TODO: # warehouse fix this.
        /*
         * Criteria criteria = getSession().createCriteria(Order.class); if (orderId != null) {
         * criteria.add(Restrictions.eq("id", orderId)); } if (StringUtils.isNotBlank(trackingId)) { List<Long>
         * orderIdList = (List<Long>) getSession().createQuery("select DISTINCT lineItem.order.id from LineItem
         * lineItem where lineItem.trackingId =:trackingId order by lineItem.order.id desc"). setString("trackingId",
         * trackingId). list(); if (orderIdList != null && !orderIdList.isEmpty()) { criteria.add(Restrictions.in("id",
         * orderIdList)); } } if (courierId != null) { List<Long> orderIdList = (List<Long>)
         * getSession().createQuery("select DISTINCT lineItem.order.id from LineItem lineItem where lineItem.courier.id
         * =:courierId order by lineItem.order.id desc"). setLong("courierId", courierId). list(); if (orderIdList !=
         * null && !orderIdList.isEmpty()) { criteria.add(Restrictions.in("id", orderIdList)); } } if (orderStatus !=
         * null) { criteria.add(Restrictions.eq("orderStatus", orderStatus)); } if
         * (StringUtils.isNotBlank(gatewayOrderId)) { criteria.add(Restrictions.eq("gatewayOrderId", gatewayOrderId)); }
         * if (startDate != null && endDate != null) { Criteria paymentCriteria = criteria.createCriteria("payment");
         * paymentCriteria.add(Restrictions.between("paymentDate", startDate, endDate));
         * paymentCriteria.addOrder(org.hibernate.criterion.Order.desc("paymentDate")); } else {
         * criteria.addOrder(org.hibernate.criterion.Order.desc("id")); } return list(criteria, pageNo, perPage);
         */

        return null;
    }

    // ----------------- --------------------------------------------------------------old
    // code-----------------------------------------------------------------------------------------------

    /*
     * public Page<Order> findOrdersByOrderStatusAndLineItemStatus( List<EnumLineItemStatus> enumLineItemStatuses,
     * List<EnumOrderStatus> enumOrderStatuses, boolean onlyCod, int pageNo, int perPage) { List<Long> orderStatusIds =
     * new ArrayList<Long>(); for (EnumOrderStatus enumOrderStatus : enumOrderStatuses) {
     * orderStatusIds.add(enumOrderStatus.getId()); } List<Long> lineItemStatusIds = new ArrayList<Long>(); for
     * (EnumLineItemStatus enumLineItemStatus : enumLineItemStatuses) {
     * lineItemStatusIds.add(enumLineItemStatus.getId()); } Long totalOrders = null; List<Long> orderIdList = null;
     * String hqlQuery = "select DISTINCT lineItem.order.id from LineItem lineItem where lineItem.order.orderStatus.id
     * in (:orderStatusIds) and lineItem.lineItemStatus.id in (:lineItemStatusIds)"; String hqlCountQuery = "select
     * count(DISTINCT lineItem.order.id) from LineItem lineItem where lineItem.order.orderStatus.id in (:orderStatusIds)
     * and lineItem.lineItemStatus.id in (:lineItemStatusIds)"; if (onlyCod) { hqlCountQuery += " and
     * lineItem.order.payment.paymentMode.id = :paymentModeCodId"; hqlQuery += " and
     * invoiceLine.order.payment.paymentMode.id = :paymentModeCodId"; } hqlQuery += " order by
     * lineItem.order.payment.paymentDate desc"; Query countQuery = sessionProvider.get().createQuery(hqlCountQuery).
     * setParameterList("orderStatusIds", orderStatusIds). setParameterList("lineItemStatusIds", lineItemStatusIds);
     * Query orderIdQuery = sessionProvider.get().createQuery(hqlQuery). setParameterList("orderStatusIds",
     * orderStatusIds). setParameterList("lineItemStatusIds", lineItemStatusIds). setFirstResult((pageNo - 1) *
     * perPage). setMaxResults(perPage); if (onlyCod) { countQuery.setLong("paymentModeCodId",
     * EnumPaymentMode.COD.getId()); orderIdQuery.setLong("paymentModeCodId", EnumPaymentMode.COD.getId()); }
     * totalOrders = (Long) countQuery.uniqueResult(); orderIdList = (List<Long>) orderIdQuery.list(); Criteria
     * criteria = getSessionProvider().get().createCriteria(Order.class); // Criteria query will throw sql error if the
     * list passed to it has size zero. // so we are returing here safely by creating a order list of size 0 by hand. if
     * (orderIdList.size() == 0) { return new Page<Order>(new ArrayList<Order>(0), perPage, pageNo,
     * ((totalOrders.intValue() - 1) / perPage + 1), totalOrders.intValue()); } criteria.add(Restrictions.in("id",
     * orderIdList)); Criteria paymentCriteria = criteria.createCriteria("payment");
     * paymentCriteria.addOrder(org.hibernate.criterion.Order.desc("paymentDate")); return new Page<Order>(criteria.list(),
     * perPage, pageNo, ((totalOrders.intValue() - 1) / perPage + 1), totalOrders.intValue()); }
     */

    /*
     * public Page<Order> findShipmentEmailAwaitingOrders( List<EnumLineItemStatus> enumLineItemStatuses, List<EnumOrderStatus>
     * enumOrderStatuses, boolean onlyCod, int pageNo, int perPage) { List<Long> orderStatusIds = new ArrayList<Long>();
     * for (EnumOrderStatus enumOrderStatus : enumOrderStatuses) { orderStatusIds.add(enumOrderStatus.getId()); } List<Long>
     * lineItemStatusIds = new ArrayList<Long>(); for (EnumLineItemStatus enumLineItemStatus : enumLineItemStatuses) {
     * lineItemStatusIds.add(enumLineItemStatus.getId()); } Long totalOrders = null; List<Long> orderIdList = null;
     * String hqlQuery = "select DISTINCT lineItem.order.id from LineItem lineItem where lineItem.order.orderStatus.id
     * in (:orderStatusIds) " + "and lineItem.lineItemStatus.id in (:lineItemStatusIds) and lineItem.shippedEmailSent is
     * null"; String hqlCountQuery = "select count(DISTINCT lineItem.order.id) from
     * findOrdersByOrderStatusAndLineItemStatus lineItem where lineItem.order.orderStatus.id in (:orderStatusIds) " +
     * "and lineItem.lineItemStatus.id in (:lineItemStatusIds) and lineItem.shippedEmailSent is null"; if (onlyCod) {
     * hqlCountQuery += " and lineItem.order.payment.paymentMode.id = :paymentModeCodId"; hqlQuery += " and
     * invoiceLine.order.payment.paymentMode.id = :paymentModeCodId"; } hqlQuery += " order by
     * lineItem.order.payment.paymentDate desc"; Query countQuery =
     * getSessionProvider().get().createQuery(hqlCountQuery). setParameterList("orderStatusIds", orderStatusIds).
     * setParameterList("lineItemStatusIds", lineItemStatusIds); Query orderIdQuery =
     * getSessionProvider().get().createQuery(hqlQuery). setParameterList("orderStatusIds", orderStatusIds).
     * setParameterList("lineItemStatusIds", lineItemStatusIds). setFirstResult((pageNo - 1) * perPage).
     * setMaxResults(perPage); if (onlyCod) { countQuery.setLong("paymentModeCodId", EnumPaymentMode.COD.getId());
     * orderIdQuery.setLong("paymentModeCodId", EnumPaymentMode.COD.getId()); } totalOrders = (Long)
     * countQuery.uniqueResult(); orderIdList = (List<Long>) orderIdQuery.list(); Criteria criteria =
     * getSessionProvider().get().createCriteria(Order.class); // Criteria query will throw sql error if the list passed
     * to it has size zero. // so we are returing here safely by creating a order list of size 0 by hand. if
     * (orderIdList.size() == 0) { return new Page<Order>(new ArrayList<Order>(0), perPage, pageNo,
     * ((totalOrders.intValue() - 1) / perPage + 1), totalOrders.intValue()); } criteria.add(Restrictions.in("id",
     * orderIdList)); Criteria paymentCriteria = criteria.createCriteria("payment");
     * paymentCriteria.addOrder(org.hibernate.criterion.Order.desc("paymentDate")); return new Page<Order>(criteria.list(),
     * perPage, pageNo, ((totalOrders.intValue() - 1) / perPage + 1), totalOrders.intValue()); }
     */

    /*
     * @Deprecated public Page<Order> searchShipmentAwaitingOrders(Long orderId, OrderStatus orderStatus,
     *             LineItemStatus lineItemStatus, List<Courier> courierList, String gatewayOrderId, int pageNo, int
     *             perPage) { //TODO: # warehouse fix this. Criteria criteria =
     *             getSessionProvider().get().createCriteria(Order.class); if (orderId != null) {
     *             criteria.add(Restrictions.eq("id", orderId)); } if (orderStatus != null) {
     *             criteria.add(Restrictions.eq("orderStatus", orderStatus)); } if
     *             (StringUtils.isNotBlank(gatewayOrderId)) { criteria.add(Restrictions.eq("gatewayOrderId",
     *             gatewayOrderId)); } List<Long> orderIdList = (List<Long>)
     *             getSessionProvider().get().createQuery("select DISTINCT lineItem.order.id from LineItem lineItem
     *             where lineItem.courier in (:courierList) and lineItem.lineItemStatus =:lineItemStatus order by
     *             lineItem.order.id desc"). setParameterList("courierList", courierList).
     *             setParameter("lineItemStatus", lineItemStatus). list(); if (orderIdList != null &&
     *             !orderIdList.isEmpty()) { criteria.add(Restrictions.in("id", orderIdList)); } else if
     *             (courierList.size() == 1) { return null; }
     *             criteria.addOrder(org.hibernate.criterion.Order.desc("id")); return list(criteria, pageNo, perPage);
     *             //TODO: # warehouse fix this. return null; }
     */

    /*
     * public Category getBasketCategory(Order order) { Map<Category, Long> categoryCounter = new HashMap<Category,
     * Long>(); Map<Category, Double> categoryAmount = new HashMap<Category, Double>(); for (LineItem lineItem :
     * order.getProductLineItems()) { Category lineItemPrimaryCategory =
     * lineItem.getProductVariant().getProduct().getPrimaryCategory(); Long qty = lineItem.getQty(); Double amount =
     * lineItem.getHkPrice() * qty; if (categoryCounter.containsKey(lineItemPrimaryCategory)) {
     * categoryCounter.put(lineItemPrimaryCategory, categoryCounter.get(lineItemPrimaryCategory) + qty);
     * categoryAmount.put(lineItemPrimaryCategory, categoryCounter.get(lineItemPrimaryCategory) + amount); } else {
     * categoryCounter.put(lineItemPrimaryCategory, qty); categoryAmount.put(lineItemPrimaryCategory, amount); } }
     * ValueComparator bvc = new ValueComparator(categoryCounter); TreeMap<Category, Long> sorted_map = new
     * TreeMap(bvc); sorted_map.putAll(categoryCounter); return sorted_map.firstKey(); } class ValueComparator
     * implements Comparator { Map base; public ValueComparator(Map base) { this.base = base; } public int
     * compare(Object a, Object b) { if ((Long) base.get(a) < (Long) base.get(b)) { return 1; } else if ((Long)
     * base.get(a) == (Long) base.get(b)) { return 0; } else { return -1; } } }
     */

    /*
     * public Page<Order> listSuccessfulOrders(int page, int perPage) { Criteria criteria =
     * getSessionProvider().get().createCriteria(Order.class);
     * criteria.addOrder(org.hibernate.criterion.Order.desc("createDate")); return list(criteria, page, perPage); }
     */

    /*
     * public Page<Order> searchOrders(OrderSearchCriteria orderSearchCriteria, int pageNo, int perPage) { Criteria
     * criteria = getSessionProvider().get().createCriteria(Order.class); if (orderId != null) {
     * criteria.add(Restrictions.eq("id", orderId)); } Criteria userCriteria = criteria.createCriteria("user"); if
     * (StringUtils.isNotBlank(login)) { userCriteria.add(Restrictions.or( Restrictions.like("login", "%" + login +
     * "%"), Restrictions.like("email", "%" + login + "%") )); } if (StringUtils.isNotBlank(name)) {
     * userCriteria.add(Restrictions.like("name", "%" + name + "%")); } if (StringUtils.isNotBlank(trackingId)) { List<Long>
     * orderIdList = (List<Long>) getSessionProvider().get().createQuery("select DISTINCT lineItem.order.id from
     * LineItem lineItem where lineItem.trackingId =:trackingId order by lineItem.order.id desc").
     * setString("trackingId", trackingId). list(); if (orderIdList != null && !orderIdList.isEmpty()) {
     * criteria.add(Restrictions.in("id", orderIdList)); } } if (orderStatus != null) {
     * criteria.add(Restrictions.eq("orderStatus", orderStatus)); } if (StringUtils.isNotBlank(gatewayOrderId)) {
     * criteria.add(Restrictions.eq("gatewayOrderId", gatewayOrderId)); } Criteria addressCriteria =
     * criteria.createCriteria("address"); if (StringUtils.isNotBlank(phone)) {
     * addressCriteria.add(Restrictions.like("phone", "%" + phone + "%")); } Criteria paymentCriteria = null; if
     * (startDate != null && endDate != null) { paymentCriteria = criteria.createCriteria("payment");
     * paymentCriteria.add(Restrictions.between("paymentDate", startDate, endDate));
     * paymentCriteria.addOrder(org.hibernate.criterion.Order.desc("paymentDate")); } else {
     * criteria.addOrder(org.hibernate.criterion.Order.desc("id")); } if (paymentMode != null) { if (paymentCriteria ==
     * null) { paymentCriteria = criteria.createCriteria("payment"); }
     * paymentCriteria.add(Restrictions.eq("paymentMode", paymentMode)); } return list(criteria, pageNo, perPage);
     * //TODO: # warehouse fix this. return null; }
     */

    /*
     * public List<Order> findPrintingPickingQueueOrders() { return (List<Order>)
     * getSessionProvider().get().createQuery("select DISTINCT lineItem.order.id from LineItem lineItem where
     * lineItem.lineItemStatus.id = :lineItemStatusId"). setLong("lineItemStatusId",
     * EnumLineItemStatus.GONE_FOR_PRINTING.getId()). list(); }
     */

    /*
     * public List<LineItem> getInCartLineItemsForUser(User user) { return sessionProvider.get().createQuery("select
     * o.lineItems from Order o where o.orderStatus = :orderStatus and o.user = :user") .setEntity("orderStatus",
     * orderStatusDaoProvider.get().find(EnumOrderStatus.InCart.getId())) .setEntity("user", user) .list(); }
     */

    /*
     * @Deprecated public Page<Order> searchPackingAwaitingOrders(Date startDate, Date endDate, Long orderId, String
     *             gatewayOrderId, int pageNo, int perPage, LineItemStatus applicableLineItemStatus) { //TODO: #
     *             warehouse fix this. Criteria criteria = getSessionProvider().get().createCriteria(Order.class); if
     *             (orderId != null) { criteria.add(Restrictions.eq("id", orderId)); } if
     *             (StringUtils.isNotBlank(gatewayOrderId)) { criteria.add(Restrictions.eq("gatewayOrderId",
     *             gatewayOrderId)); } List<Long> orderIdList = (List<Long>)
     *             getSessionProvider().get().createQuery("select DISTINCT " + "lineItem.order.id from LineItem lineItem
     *             where lineItem.lineItemStatus.id = :applicableLineItemStatusId and " +
     *             "lineItem.productVariant.product.isService = :isService"). setLong("applicableLineItemStatusId",
     *             applicableLineItemStatus.getId()). setBoolean("isService", Boolean.FALSE). list(); if (orderIdList !=
     *             null && !orderIdList.isEmpty()) { String query = "select DISTINCT max(olc.id) from OrderLifecycle olc
     *             where olc.order.id in (:orderIds) and olc.orderLifecycleActivity.id in (:orderLifeCycleActivityIds)" + "
     *             group by olc.order.id order by olc.activityDate desc"; List<Long> oclIds = (List<Long>)
     *             getSessionProvider().get().createQuery(query). setParameterList("orderIds", orderIdList).
     *             setParameterList("orderLifeCycleActivityIds",
     *             Arrays.asList(EnumOrderLifecycleActivity.AutoEscalatedToProcessingQueue.getId(),
     *             EnumOrderLifecycleActivity.EscalatedToProcessingQueue.getId(),
     *             EnumOrderLifecycleActivity.EscalatedPartiallyToProcessingQueue.getId())). list(); Criteria
     *             olcCriteria = criteria.createCriteria("orderLifecycles"); if (oclIds != null && !oclIds.isEmpty()) {
     *             olcCriteria.add(Restrictions.in("id", oclIds)); if (startDate != null && endDate != null) {
     *             olcCriteria.add(Restrictions.between("activityDate", startDate, endDate)); }
     *             olcCriteria.addOrder(org.hibernate.criterion.Order.asc("activityDate")); return list(criteria,
     *             pageNo, perPage); } else return null; } else { return null; } return null; }
     */

    /*
     * public Page<Order> searchOrders(Long orderId, String gatewayOrderId, Date startDate, Date endDate, List<OrderStatus>
     * orderStatusList, List<LineItemStatus> lineItemStatusList, List<PaymentMode> paymentModeList, List<PaymentStatus>
     * paymentStatusList, List<Category> categoryList, List<OrderLifecycleActivity> orderLifecycleActivityList, int
     * pageNo, int perPage) { //TODO: # warehouse fix this. Criteria criteria =
     * getSessionProvider().get().createCriteria(Order.class); if (orderId != null) { criteria.add(Restrictions.eq("id",
     * orderId)); //return list(criteria, pageNo, perPage); } if (StringUtils.isNotBlank(gatewayOrderId)) {
     * criteria.add(Restrictions.eq("gatewayOrderId", gatewayOrderId)); //return list(criteria, pageNo, perPage); }
     * String hqlQuery = "select DISTINCT lineItem.order.id from LineItem lineItem " + "left join
     * lineItem.productVariant.product.categories c " + "where lineItem.order.orderStatus in (:orderStatusList) and " +
     * "lineItem.lineItemStatus in (:lineItemStatusList) and " + "c in (:applicableCategories)"; List<Long> orderIdList =
     * (List<Long>) getSessionProvider().get().createQuery(hqlQuery). setParameterList("orderStatusList",
     * orderStatusList). setParameterList("lineItemStatusList", lineItemStatusList).
     * setParameterList("applicableCategories", categoryList). list(); if (orderIdList != null && orderIdList.size() >
     * 0) { criteria.add(Restrictions.in("id", orderIdList)); if (orderLifecycleActivityList != null &&
     * !orderLifecycleActivityList.isEmpty()) { if (orderLifecycleActivityList.contains(orderLifecycleActivityDao.find
     * (EnumOrderLifecycleActivity.EscalatedBackToAwaitingQueue.getId()))) { String query = "select DISTINCT max(olc.id)
     * from OrderLifecycle olc where " + "olc.order.id in (:orderIds) and olc.orderLifecycleActivity in
     * (:orderLifecycleActivityList) group by olc.order.id"; List<Long> oclIds = (List<Long>)
     * getSessionProvider().get().createQuery(query). setParameterList("orderIds", orderIdList).
     * setParameterList("orderLifecycleActivityList", orderLifecycleActivityList). list(); Criteria olcCriteria =
     * criteria.createCriteria("orderLifecycles"); if (oclIds != null && !oclIds.isEmpty()) {
     * olcCriteria.add(Restrictions.in("id", oclIds)); if (startDate != null && endDate != null) {
     * olcCriteria.add(Restrictions.between("activityDate", startDate, endDate)); } } } else if
     * (orderLifecycleActivityList.contains(orderLifecycleActivityDao.find(EnumOrderLifecycleActivity.OrderPutOnHold.getId()))) {
     * criteria.add(Restrictions.eq("orderStatus", orderStatusDaoProvider.get().find(EnumOrderStatus.OnHold.getId()))); } } }
     * Criteria paymentCriteria = criteria.createCriteria("payment"); if (paymentModeList != null &&
     * paymentModeList.size() > 0) { paymentCriteria.add(Restrictions.in("paymentMode", paymentModeList)); } if
     * (startDate != null && endDate != null) { paymentCriteria.add(Restrictions.between("paymentDate", startDate,
     * endDate)); } else if (endDate != null) { paymentCriteria.add(Restrictions.le("paymentDate", endDate)); } else if
     * (startDate != null) { paymentCriteria.add(Restrictions.between("paymentDate", startDate, new Date())); } if
     * (paymentStatusList != null && paymentStatusList.size() > 0) {
     * paymentCriteria.add(Restrictions.in("paymentStatus", paymentStatusList)); }
     * paymentCriteria.addOrder(org.hibernate.criterion.Order.desc("paymentDate")); return list(criteria, pageNo,
     * perPage); return null; }
     */
}
