package com.hk.report.impl.dao.shippingOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.courier.Courier;
import com.hk.domain.order.ShippingOrder;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.report.dto.order.reconcilation.ReconcilationReportDto;
import com.hk.report.pact.dao.shippingOrder.ReportShippingOrderDao;

@Repository
public class ReportShippingOrderDaoImpl extends BaseDaoImpl implements ReportShippingOrderDao {

    private static Logger logger = LoggerFactory.getLogger(ReportShippingOrderDaoImpl.class);

    @SuppressWarnings("unchecked")
    public List<ReconcilationReportDto> findReconcilationReportByDate(Date startDate, Date endDate, String paymentProcess, Courier courier, Long warehouseId) {
        String paymentWhereClause = "";
        String warehouseWhereClause = "";
        String courierWhereClause = "";
        if (warehouseId != 0) {
            warehouseWhereClause = " and so.warehouse.id = " + warehouseId;
        }

        if (courier != null) {
            courierWhereClause = " and ship.courier.id = " + courier.getId();
        }

        if (paymentProcess.equalsIgnoreCase("cod")) {
            paymentWhereClause = " and pm.id = " + EnumPaymentMode.COD.getId();
        } else if (paymentProcess.equalsIgnoreCase("techprocess")) {
            paymentWhereClause = " and pm.id != " + EnumPaymentMode.COD.getId();
        }

        List<Long> applicableOrderStatus = new ArrayList<Long>();

        applicableOrderStatus.add(EnumShippingOrderStatus.SO_Delivered.getId());
        applicableOrderStatus.add(EnumShippingOrderStatus.SO_Returned.getId());
        applicableOrderStatus.add(EnumShippingOrderStatus.SO_Lost.getId());
        applicableOrderStatus.add(EnumShippingOrderStatus.SO_Shipped.getId());

        String hqlQuery = "select so.gatewayOrderId as invoiceId, p.paymentDate as orderDate, user.name as name, adr.city as city, "
                + " pm.name as payment, so.amount as total,  ship.courier as courier, ship.trackingId as awb, ship.shipDate as shipmentDate,"
                + " ship.deliveryDate as deliveryDate, rs.name as reconciled, os.name as orderStatus, ship.boxWeight as boxWeight,"
                + " bs.name as boxSize, so.warehouse as warehouse" + " from ShippingOrder so join so.baseOrder bo join bo.payment p join bo.user user join bo.address adr "
                + " join so.shipment ship join ship.boxSize bs join so.shippingOrderStatus os " + " join p.paymentMode pm join so.reconciliationStatus rs "
                + " where p.paymentDate >= :startDate" + " and p.paymentDate <= :endDate" + " and os.id in (:applicableOrderStatus)" + " " + paymentWhereClause
                + courierWhereClause + warehouseWhereClause;

        return getSession().createQuery(hqlQuery).setParameter("startDate", startDate).setParameterList("applicableOrderStatus", applicableOrderStatus).setParameter("endDate",
                endDate).setResultTransformer(Transformers.aliasToBean(ReconcilationReportDto.class)).list();
    }

    @SuppressWarnings("unchecked")
    public List<ShippingOrder> getDeliveredSOForCourierByDate(Date startDate, Date endDate, Long courierId) {
        String query = "from ShippingOrder so where so.shipment.shipDate>= :startDate and so.shipment.shipDate <= :endDate"
                + " and so.shipment.courier.id = :courierId and so.shipment.deliveryDate is not null";
        return getSession().createQuery(query).setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("courierId", courierId).list();
    }

    public List<Long> getActivityPerformedSOCount(List<Long> orderIds, Date activityDate, Long shippingOrderActivity, Integer cutOffDay1, Integer cutOffTimeHH1,
            Integer cutOffTimeMM1, Integer cutOffDay2, Integer cutOffTimeHH2, Integer cutOffTimeMM2) {
        return this.getActivityPerformedSOCount(orderIds, activityDate, Arrays.asList(shippingOrderActivity), cutOffDay1, cutOffTimeHH1, cutOffTimeMM1, cutOffDay2, cutOffTimeHH2,
                cutOffTimeMM2);
    }

    @SuppressWarnings("unchecked")
    public List<Long> getActivityPerformedSOCount(List<Long> orderIds, Date activityDate, List<Long> shippingOrderActivity, Integer cutOffDay1, Integer cutOffTimeHH1,
            Integer cutOffTimeMM1, Integer cutOffDay2, Integer cutOffTimeHH2, Integer cutOffTimeMM2) {
        logger.debug("OrderIds for activity-" + shippingOrderActivity + " on " + activityDate + " is - " + orderIds.size());
        List<Long> orderList = new ArrayList<Long>();

        Date startDate = activityDate;
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(startDate);
        calendar1.add(Calendar.DATE, cutOffDay1);
        calendar1.set(Calendar.HOUR_OF_DAY, cutOffTimeHH1);
        calendar1.set(Calendar.MINUTE, cutOffTimeMM1);
        calendar1.set(Calendar.SECOND, 0);
        startDate = calendar1.getTime();

        Date endDate = activityDate;
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(endDate);
        calendar2.add(Calendar.DATE, cutOffDay2);
        calendar2.set(Calendar.HOUR_OF_DAY, cutOffTimeHH2);
        calendar2.set(Calendar.MINUTE, cutOffTimeMM2);
        calendar2.set(Calendar.SECOND, 0);
        endDate = calendar2.getTime();

        if (orderIds.size() > 0) {

            String query = " select distinct(sol.shippingOrder.id) from ShippingOrderLifecycle sol where sol.shippingOrder.baseOrder.id in (:orderIds) "
                    + "and sol.shippingOrderLifeCycleActivity.id in (:shippingOrderLifeCycleActivityIds) and sol.activityDate between (:startDate) and (:endDate) ";

            /*
             * String query = "select distinct(so.id) from ShippingOrder so inner join so " + "where so.baseOrder.id in
             * (:orderIds) and so.shippingOrderLifecycles in ( :shippingOrderActivity )" + "and so.updateDate between
             * :startDate and :endDate";
             */

            orderList = (List<Long>) getSession().createQuery(query).setParameterList("orderIds", orderIds).setParameterList("shippingOrderLifeCycleActivityIds",
                    shippingOrderActivity).setParameter("startDate", startDate).setParameter("endDate", endDate).list();
        }
        return orderList;
    }
    
    

}
