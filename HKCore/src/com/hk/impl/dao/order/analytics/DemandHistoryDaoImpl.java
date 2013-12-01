package com.hk.impl.dao.order.analytics;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.analytics.DemandHistory;
import com.hk.domain.payment.Payment;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.order.analytics.DemandHistoryDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 7/13/12
 * Time: 12:23 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class DemandHistoryDaoImpl extends BaseDaoImpl implements DemandHistoryDao {          //todo think logic

    public DemandHistory createOrUpdateEntry(CartLineItem cartLineItem, Warehouse warehouse, Order order) {
        ProductVariant productVariant = cartLineItem.getProductVariant();
        Long currentDemand = cartLineItem.getQty();
        Payment payment = order.getPayment();
        if (payment != null) {
//      DemandHistory demandHistory = findByPVWarehouse(productVariant, warehouse, payment.getPaymentDate());
//      if (demandHistory == null) {
            DemandHistory demandHistory = new DemandHistory();
            demandHistory.setProductVariant(productVariant);
            demandHistory.setWarehouse(warehouse);
            demandHistory.setDemand(currentDemand);
            demandHistory.setDemandDate(payment.getPaymentDate());
            //demandHistory.setOrder(order);
            return (DemandHistory) save(demandHistory);
//      } else {
//        demandHistory.setDemand(demandHistory.getDemand() + currentDemand);
//        demandHistory = save(demandHistory);
//      }
//      return demandHistory;
        }
        return null;
    }

    public DemandHistory findByPVWarehouse(ProductVariant productVariant, Warehouse warehouse, Date currentDate) {
        DetachedCriteria criteria = DetachedCriteria.forClass(DemandHistory.class);
        criteria.add(Restrictions.eq("productVariant", productVariant));
        criteria.add(Restrictions.eq("warehouse", warehouse));
        criteria.add(Restrictions.eq("demandDate", currentDate));
        List<DemandHistory> demandHistoryList = findByCriteria(criteria);
        return demandHistoryList == null || demandHistoryList.isEmpty() ? null : demandHistoryList.get(0);
    }
}
