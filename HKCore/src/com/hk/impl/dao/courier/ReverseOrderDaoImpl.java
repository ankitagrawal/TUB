package com.hk.impl.dao.courier;


import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.domain.courier.Courier;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.courier.ReverseOrderDao;
import com.akube.framework.dao.Page;
import org.springframework.stereotype.Repository;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.  * User: Neha * Date: Dec 4, 2012 * Time: 5:20:41 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ReverseOrderDaoImpl extends BaseDaoImpl implements ReverseOrderDao {

  @Override
  public ReverseOrder save(ReverseOrder reverseOrder) {
    return (ReverseOrder) super.save(reverseOrder);
  }

  public Page getPickupRequestsByStatuses(String shippingOrderId, Long pickupStatusId, Long reconciliationStatusId, Long courierId, Long warehouseId, int page, int perPage, Date startDate, Date endDate) {
    return list(getPickupSearchCriteria(shippingOrderId, pickupStatusId, reconciliationStatusId, courierId, warehouseId, startDate, endDate), page, perPage);
  }

  public List<ReverseOrder> getPickupRequestsForExcel(String shippingOrderId, Long pickupStatusId, Long reconciliationStatusId, Long courierId, Long warehouseId, Date startDate, Date endDate) {
    return (List<ReverseOrder>) findByCriteria(getPickupSearchCriteria(shippingOrderId, pickupStatusId, reconciliationStatusId, courierId, warehouseId, startDate, endDate));
  }

  private DetachedCriteria getPickupSearchCriteria(String shippingOrderId, Long pickupStatusId, Long reconciliationStatusId, Long courierId, Long warehouseId, Date startDate, Date endDate) {
    DetachedCriteria orderCriteria = DetachedCriteria.forClass(ReverseOrder.class);
    DetachedCriteria courierDetailCriteria = null;

    DetachedCriteria shippingOrderCriteria = null;
    if (shippingOrderId != null) {
      shippingOrderCriteria = orderCriteria.createCriteria("shippingOrder");
      shippingOrderCriteria.add(Restrictions.eq("gatewayOrderId", shippingOrderId));
    }

    if (pickupStatusId != null) {
      courierDetailCriteria = orderCriteria.createCriteria("courierPickupDetail");
      DetachedCriteria pickupStatusCriteria = courierDetailCriteria.createCriteria("pickupStatus");
      pickupStatusCriteria.add(Restrictions.eq("id", pickupStatusId));
    }

    if (reconciliationStatusId != null) {
      DetachedCriteria reconciliationCriteria = orderCriteria.createCriteria("reconciliationStatus");
      reconciliationCriteria.add(Restrictions.eq("id", reconciliationStatusId));
    }

    if (courierId != null) {
      if (courierDetailCriteria == null) {
        courierDetailCriteria = orderCriteria.createCriteria("courierPickupDetail");
      }
      DetachedCriteria courierCriteria = courierDetailCriteria.createCriteria("courier");
      courierCriteria.add((Restrictions.eq("id", courierId)));
    }

    if (startDate != null && endDate != null) {
      orderCriteria.add(Restrictions.between("createDate", startDate, endDate));

    }
    if (warehouseId != null) {
      if (shippingOrderCriteria == null) {
        shippingOrderCriteria = orderCriteria.createCriteria("shippingOrder");
      }
      DetachedCriteria warehouseCriteria = shippingOrderCriteria.createCriteria("warehouse");
      warehouseCriteria.add(Restrictions.eq("id", warehouseId));

    }
    orderCriteria.addOrder(org.hibernate.criterion.Order.desc("createDate"));
    return orderCriteria;
  }

  public ReverseOrder getReverseOrderById(Long id) {
    return (ReverseOrder) super.findUniqueByNamedQueryAndNamedParam("getReverseOrderById", new String[]{"reverseOrderId"}, new Object[]{id});
  }

  public ReverseOrder getReverseOrderByShippingOrderId(Long shippingOrderId) {
    return (ReverseOrder) super.findUniqueByNamedParams("from ReverseOrder rvo where rvo.shippingOrder.id = :shippingOrderId", new String[]{"shippingOrderId"},
        new Object[]{shippingOrderId});
  }

  public Page getReverseOrderWithNoPickupSchedule(int page, int perPage) {
    DetachedCriteria orderCriteria = DetachedCriteria.forClass(ReverseOrder.class);
    orderCriteria.add(Restrictions.isNull("courierPickupDetail"));
    return list(orderCriteria, page, perPage);
  }

  public List<ReverseOrder> findReverseOrderForTimeFrame(Long warehouseId, Date startDate, Date endDate) {
    String hqlQuery = "select ro from ReverseOrder ro join ro.shippingOrder so join so.warehouse w  where w.id = :warehouseId and ro.createDate between :startDate and :endDate";
    return (List<ReverseOrder>) findByNamedParams(hqlQuery, new String[]{"warehouseId", "startDate", "endDate"}, new Object[]{warehouseId,startDate,endDate});
  }
}
