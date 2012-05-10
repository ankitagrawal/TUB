package com.hk.impl.dao.shippingOrder;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderLifeCycleActivity;
import com.hk.domain.sku.Sku;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;

@Repository
@SuppressWarnings("unchecked")
public class ShippingOrderDaoImpl extends BaseDaoImpl implements ShippingOrderDao {

    public ShippingOrder findById(Long shippingOrderId) {
        return get(ShippingOrder.class, shippingOrderId);
    }

    public ShippingOrder findByTrackingId(String trackingId) {
        String query = "select distinct so  from ShippingOrder so  where so.shipment.trackingId =:trackingId";
        return (ShippingOrder) getSession().createQuery(query).setString("trackingId", trackingId).uniqueResult();
    }

    public ShippingOrder findByGatewayOrderId(String gatewayOrderId) {
        return (ShippingOrder) getSession().createQuery("from ShippingOrder o where o.gatewayOrderId = :gatewayOrderId").setString("gatewayOrderId", gatewayOrderId).uniqueResult();
    }

    public Page searchShippingOrders(ShippingOrderSearchCriteria shippingOrderSearchCriteria, int pageNo, int perPage) {
        DetachedCriteria searchCriteria = shippingOrderSearchCriteria.getSearchCriteria();
        //searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return list(searchCriteria, pageNo, perPage);
    }

    public List<ShippingOrder> searchShippingOrders(ShippingOrderSearchCriteria shippingOrderSearchCriteria) {
        DetachedCriteria searchCriteria = shippingOrderSearchCriteria.getSearchCriteria();
        searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        searchCriteria.setMaxResults(1000);
        return findByCriteria(searchCriteria);
    }

    public ShippingOrderLifeCycleActivity getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity enumShippingOrderLifecycleActivity) {
        return get(ShippingOrderLifeCycleActivity.class, enumShippingOrderLifecycleActivity.getId());
    }

    public List<ShippingOrder> getShippingOrdersToSendShipmentEmail() {
        StringBuilder hqlQuery = new StringBuilder("select DISTINCT so from ShippingOrder so where so.shippingOrderStatus.id = :shippedStatus and so.shipment.isEmailSent =false");

        Query shippingOrderListQuery = getSession().createQuery(hqlQuery.toString()).setParameter("shippedStatus", EnumShippingOrderStatus.SO_Shipped.getId());

        return (List<ShippingOrder>) shippingOrderListQuery.list();
    }

    /*
     * public Page<ShippingOrder> findShippingOrdersPageWithStatus(List<EnumOrderStatus> orderStatuses, boolean
     * orderByDateAsc, int pageNo, int perPage) { Long totalOrders = getCountForShippingOrdersWithStatus(orderStatuses);
     * List<ShippingOrder> ordersList = findShippingOrdersWithStatus(orderStatuses, orderByDateAsc, pageNo, perPage);
     * if (ordersList.size() == 0) { return new Page<ShippingOrder>(new ArrayList<ShippingOrder>(0), perPage, pageNo,
     * 0, 0); } return new Page<ShippingOrder>(ordersList, perPage, pageNo, ((totalOrders.intValue() - 1) / perPage +
     * 1), totalOrders.intValue()); } private Long getCountForShippingOrdersWithStatus(List<EnumOrderStatus>
     * orderStatuses) { List<Long> orderStatusList = EnumOrderStatus.getShippingOrderStatusIDs(orderStatuses); String
     * hqlCountQuery = "select count(DISTINCT so.id) from ShippingOrder so where so.orderStatus.id in (:orderStatusList) ";
     * Query countQuery = getSession().createQuery(hqlCountQuery). setParameterList("orderStatusList", orderStatusList);
     * Long totalOrders = (Long) countQuery.uniqueResult(); return totalOrders; } private List<ShippingOrder>
     * findShippingOrdersWithStatus(List<EnumOrderStatus> orderStatuses, boolean orderByDateAsc, int pageNo, int
     * perPage) { List<Long> orderStatusList = EnumOrderStatus.getShippingOrderStatusIDs(orderStatuses); StringBuilder
     * hqlQuery = new StringBuilder("select DISTINCT so from ShippingOrder so where so.orderStatus.id in(
     * :orderStatusList)"); if (orderByDateAsc) { hqlQuery.append("order by so.baseOrder.payment.paymentDate asc "); }
     * else { hqlQuery.append("order by so.baseOrder.payment.paymentDate desc "); } Query orderListQuery =
     * getSession().createQuery(hqlQuery.toString()). setParameterList("orderStatusList", orderStatusList); if (pageNo >
     * 0 && perPage > 0) { orderListQuery.setFirstResult((pageNo - 1) * perPage).setMaxResults(perPage); } List<ShippingOrder>
     * ordersList = (List<ShippingOrder>) orderListQuery.list(); return ordersList; }
     */

    public Long getBookedQtyOfSkuInQueue(List<Sku> skuList) {
        String query = "select sum(li.qty) from LineItem li " + "where li.sku in (:skuList) " + "and li.shippingOrder.shippingOrderStatus.id in (:orderStatusIdList) ";
        Long qtyInQueue = (Long) getSession().createQuery(query).setParameterList("skuList", skuList).setParameterList("orderStatusIdList",
                EnumShippingOrderStatus.getShippingOrderStatusIDs(EnumShippingOrderStatus.getStatusForBookedInventory())).uniqueResult();
        if (qtyInQueue == null) {
            qtyInQueue = 0L;
        }
        return qtyInQueue;
    }

    /**
     * @param sku based on warehouse
     * @return Sum of Qty of lineitems for product variant which are not yet shipped
     */
    public Long getBookedQtyOfSkuInQueue(Sku sku) {
        return getBookedQtyOfSkuInQueue(Arrays.asList(sku));
    }

    @SuppressWarnings("unchecked")
    public List<Long> getShippingOrderListByCourier(Date startDate, Date endDate, Long courierId) {

        String query = "select distinct so.id  " + " from Shipment shipment, ShippingOrder so where " + " so.shipment = shipment" + " and shipment.courier.id = :courierId "
                + " and shipment.shipDate between :startDate and :endDate " + " and shipment.deliveryDate is null ";
        return getSession().createQuery(query).setParameter("courierId", courierId).setParameter("startDate", startDate).setParameter("endDate", endDate).list();
    }

}
