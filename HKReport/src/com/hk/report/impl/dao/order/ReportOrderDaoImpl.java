package com.hk.report.impl.dao.order;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.Order;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.report.dto.payment.CODConfirmationDto;
import com.hk.report.dto.sales.DaySaleDto;
import com.hk.report.pact.dao.order.ReportOrderDao;

@Repository
public class ReportOrderDaoImpl extends BaseDaoImpl implements ReportOrderDao {

    private static Logger logger = LoggerFactory.getLogger(ReportOrderDaoImpl.class);
    @SuppressWarnings("unchecked")
    public List<DaySaleDto> findSaleForTimeFrame(List<OrderStatus> applicableOrderStatus, Date startDate, Date endDate) {
        String hqlQuery = "select date(li.order.payment.paymentDate) as orderDate, count(distinct li.order.id) as txnCount, sum(li.qty) as skuCount, sum(li.qty*li.markedPrice) as sumOfMrp, sum(li.qty*li.hkPrice) as sumOfHkPrice, sum(li.discountOnHkPrice) as sumOfHkPricePostAllDiscounts  "
                + "from CartLineItem li where li.lineItemType.id = :lineItemType and li.order.orderStatus in (:applicableOrderStatus) and li.order.payment.paymentDate >= :startDate and li.order.payment.paymentDate <= :endDate "
                + "group by date(li.order.payment.paymentDate)";

        return getSession().createQuery(hqlQuery).setParameter("lineItemType", EnumCartLineItemType.Product.getId()).setParameterList("applicableOrderStatus",
                applicableOrderStatus).setParameter("startDate", startDate).setParameter("endDate", endDate).setResultTransformer(Transformers.aliasToBean(DaySaleDto.class)).list();
    }
    
    @SuppressWarnings("unchecked")
    public List<Order> findOrdersForTimeFrame(Date startDate, Date endDate, List<Category> applicableCategories) {
        String query = "select distinct(li.shippingOrder.baseOrder ) from LineItem li join li.sku.productVariant.product.primaryCategory c where c in (:applicableCategories) "
                + "and li.shippingOrder.baseOrder.payment.paymentDate >= :startDate and li.shippingOrder .baseOrder .payment.paymentDate <= :endDate";
        return getSession().createQuery(query).setParameterList("applicableCategories", applicableCategories).setParameter("startDate", startDate).setParameter("endDate", endDate).list();
    }


    @SuppressWarnings("unchecked")
    public List<CODConfirmationDto> findCODUnConfirmedOrderReport(Date startDate, Date endDate) {

        if (startDate == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            startDate = calendar.getTime();
        }
        if (endDate == null) {
            endDate = new Date();
        }

        String query = "select olc1.activityDate as orderPlacedDate, olc1.order as order, olc1.order.user.email as userEmail, olc1.user.name as userName, "
                + " UNIX_TIMESTAMP(current_timestamp()) - UNIX_TIMESTAMP(olc1.activityDate) as totalTimeTakenToConfirm "
                + " from OrderLifecycle olc1 where olc1.orderLifecycleActivity.id  = :orderPlaced "
                + " and olc1.order.payment.paymentMode.id = :paymentModeCOD1 "
                + " and olc1.activityDate between :startDate and :endDate "
                + " and olc1.order.id not in "
                + " (select distinct(olc2.order.id) from OrderLifecycle olc2 where (olc2.orderLifecycleActivity.id  = :orderConfirmed or olc2.orderLifecycleActivity.id = :orderCancelled) "
                + " and olc2.order.payment.paymentMode.id = :paymentModeCOD2 and olc1.order.id = olc2.order.id ) ";

        return getSession().createQuery(query).setParameter("orderPlaced", EnumOrderLifecycleActivity.OrderPlaced.getId()).setParameter("paymentModeCOD1",
                EnumPaymentMode.COD.getId()).setParameter("orderConfirmed", EnumOrderLifecycleActivity.ConfirmedAuthorization.getId()).setParameter("orderCancelled",
                EnumOrderLifecycleActivity.OrderCancelled.getId()).setParameter("paymentModeCOD2", EnumPaymentMode.COD.getId()).setParameter("startDate", startDate).setParameter(
                "endDate", endDate).setResultTransformer(Transformers.aliasToBean(CODConfirmationDto.class)).list();
    } /*
         * public List<CODConfirmationDto> findCODConfirmedOrderReport(Date startDate, Date endDate) { if (startDate ==
         * null) { Calendar calendar = Calendar.getInstance(); calendar.add(Calendar.MONTH, -1); startDate =
         * calendar.getTime(); } if (endDate == null) { endDate = new Date(); } String query = "select olc1.activityDate
         * as orderPlacedDate, olc1.order as order, olc1.order.user.email as userEmail, olc1.user.name as userName,
         * olc2.activityDate as orderConfirmationDate, " + " UNIX_TIMESTAMP(olc2.activityDate) -
         * UNIX_TIMESTAMP(olc1.activityDate) as totalTimeTakenToConfirm " + " from OrderLifecycle olc1, OrderLifecycle
         * olc2 where olc1.order.id = olc2.order.id and " + " olc1.order.payment.paymentMode = :paymentModeCOD and
         * olc1.orderLifecycleActivity = :orderPlaced and " + " olc2.orderLifecycleActivity = :orderConfirmed and
         * olc1.activityDate between :startDate and :endDate"; return getSession().createQuery(query)
         * .setParameter("paymentModeCOD", paymentModeDaoProvider.get().find(EnumPaymentMode.COD.getId()))
         * .setParameter("orderPlaced",
         * orderLifecycleActivityDaoProvider.get().find(EnumOrderLifecycleActivity.OrderPlaced.getId()))
         * .setParameter("orderConfirmed",
         * orderLifecycleActivityDaoProvider.get().find(EnumOrderLifecycleActivity.ConfirmedAuthorization.getId()))
         * .setParameter("startDate", startDate) .setParameter("endDate",
         * endDate).setResultTransformer(Transformers.aliasToBean(CODConfirmationDto.class)) .list(); }
         */

    /*
     * public Double getNetDiscountViaOrderLevelCouponAndRewardPoints(Date saleDate) { List<OrderStatus>
     * applicableOrderStatus = new ArrayList<OrderStatus>();
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Pending.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Shipped.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Delivered.getId())); String hqlQuery = "select
     * sum(li.discountOnHkPrice)" + "from LineItem li where li.lineItemType.id in (:lineItemTypes) and
     * li.order.orderStatus in (:applicableOrderStatus) and date(li.order.payment.paymentDate) = :saleDate " + "group by
     * date(li.order.payment.paymentDate)"; return (Double) getSession().createQuery(hqlQuery)
     * .setParameterList("lineItemTypes", Arrays.asList(EnumLineItemType.OrderLevelDiscount.getId(),
     * EnumLineItemType.RewardPoint.getId())) .setParameterList("applicableOrderStatus", applicableOrderStatus)
     * .setParameter("saleDate", saleDate) .uniqueResult(); } public Double
     * getNetDiscountViaOrderLevelCouponAndRewardPointsPerStatus(Date shipDate, OrderStatus orderStatus) { String
     * hqlQuery = "select sum(li.discountOnHkPrice)" + " from LineItem li where li.lineItemType.id in (:lineItemTypes)
     * and li.order.orderStatus = :orderStatus and li.shipDate is not null and date(li.shipDate) = :shipDate " + " group
     * by date(li.shipDate)"; return (Double) getSession().createQuery(hqlQuery) .setParameterList("lineItemTypes",
     * Arrays.asList(EnumLineItemType.OrderLevelDiscount.getId(), EnumLineItemType.RewardPoint.getId()))
     * .setParameter("orderStatus", orderStatus) .setParameter("shipDate", shipDate) .uniqueResult(); } public Double
     * getNetDiscountViaOrderLevelCouponPerStatusPerTaxHaryana(Date shipDate, OrderStatus orderStatus, Tax taxCategory) {
     * String hqlQuery = "select sum(li.discountOnHkPrice)" + " from LineItem li where li.lineItemType.id in
     * (:lineItemTypes) and li.order.orderStatus = :orderStatus and li.productVariant.tax = :taxCategory" + " and
     * li.shipDate is not null and date(li.shipDate) = :shipDate and lower(li.order.address.state) like :haryana " + "
     * group by date(li.shipDate)"; return (Double) getSession().createQuery(hqlQuery)
     * .setParameterList("lineItemTypes", Arrays.asList(EnumLineItemType.OrderLevelDiscount.getId()))
     * .setParameter("orderStatus", orderStatus) .setParameter("shipDate", shipDate) .setParameter("taxCategory",
     * taxCategory) .setParameter("haryana", HARYANA) .uniqueResult(); } public Double
     * getNetDiscountViaOrderLevelCouponPerStatusPerTaxNonHaryana(Date shipDate, OrderStatus orderStatus, Tax
     * taxCategory) { String hqlQuery = "select sum(li.discountOnHkPrice)" + " from LineItem li where li.lineItemType.id
     * in (:lineItemTypes) and li.order.orderStatus = :orderStatus and li.productVariant.tax = :taxCategory" + " and
     * li.shipDate is not null and date(li.shipDate) = :shipDate and lower(li.order.address.state) not like :haryana " + "
     * group by date(li.shipDate)"; return (Double) getSession().createQuery(hqlQuery)
     * .setParameterList("lineItemTypes", Arrays.asList(EnumLineItemType.OrderLevelDiscount.getId()))
     * .setParameter("orderStatus", orderStatus) .setParameter("shipDate", shipDate) .setParameter("taxCategory",
     * taxCategory) .setParameter("haryana", HARYANA) .uniqueResult(); } public Double
     * getNetDiscountViaRewardPointPerStatusHaryana(Date shipDate, OrderStatus orderStatus) { String hqlQuery = "select
     * sum(li.discountOnHkPrice)" + " from LineItem li where li.lineItemType.id in (:lineItemTypes) and
     * li.order.orderStatus = :orderStatus " + " and li.shipDate is not null and date(li.shipDate) = :shipDate and
     * lower(li.order.address.state) like :haryana " + " group by date(li.shipDate)"; return (Double)
     * getSession().createQuery(hqlQuery) .setParameterList("lineItemTypes",
     * Arrays.asList(EnumLineItemType.RewardPoint.getId())) .setParameter("orderStatus", orderStatus)
     * .setParameter("shipDate", shipDate) .setParameter("haryana", HARYANA) .uniqueResult(); } public Double
     * getNetDiscountViaRewardPointPerStatusNonHaryana(Date shipDate, OrderStatus orderStatus, Tax tax) { String
     * hqlQuery = "select sum(li.discountOnHkPrice)" + " from LineItem li where li.lineItemType.id in (:lineItemTypes)
     * and li.order.orderStatus = :orderStatus " + " and li.shipDate is not null and date(li.shipDate) = :shipDate and
     * lower(li.order.address.state) not like :haryana " + " group by date(li.shipDate)"; return (Double)
     * getSession().createQuery(hqlQuery) .setParameterList("lineItemTypes",
     * Arrays.asList(EnumLineItemType.RewardPoint.getId())) .setParameter("orderStatus", orderStatus)
     * .setParameter("shipDate", shipDate) .setParameter("haryana", HARYANA) .uniqueResult(); } public Double
     * getNetForwardingChargesPerStatus(Date shipDate, OrderStatus orderStatus) { String hqlQuery = "select
     * sum(li.hkPrice - li.discountOnHkPrice)" + " from LineItem li where li.lineItemType.id in (:lineItemTypes) and
     * li.order.orderStatus = :orderStatus " + " and li.shipDate is not null and date(li.shipDate) = :shipDate " + "
     * group by date(li.shipDate)"; return (Double) getSession().createQuery(hqlQuery)
     * .setParameterList("lineItemTypes", Arrays.asList(EnumLineItemType.CodCharges.getId(),
     * EnumLineItemType.Shipping.getId())) .setParameter("orderStatus", orderStatus) .setParameter("shipDate", shipDate)
     * .uniqueResult(); } public Double getNetForwardingChargesPerStatusHaryana(Date shipDate, OrderStatus orderStatus) {
     * String hqlQuery = "select sum(li.hkPrice - li.discountOnHkPrice)" + " from LineItem li where li.lineItemType.id
     * in (:lineItemTypes) and li.order.orderStatus = :orderStatus " + " and li.shipDate is not null and
     * date(li.shipDate) = :shipDate and lower(li.order.address.state) not like :haryana" + " group by
     * date(li.shipDate)"; return (Double) getSession().createQuery(hqlQuery) .setParameterList("lineItemTypes",
     * Arrays.asList(EnumLineItemType.CodCharges.getId(), EnumLineItemType.Shipping.getId()))
     * .setParameter("orderStatus", orderStatus) .setParameter("shipDate", shipDate) .setParameter("haryana", HARYANA)
     * .uniqueResult(); } public Double getNetForwardingChargesPerStatusNonHaryana(Date shipDate, OrderStatus
     * orderStatus) { String hqlQuery = "select sum(li.hkPrice - li.discountOnHkPrice)" + " from LineItem li where
     * li.lineItemType.id in (:lineItemTypes) and li.order.orderStatus = :orderStatus " + " and li.shipDate is not null
     * and date(li.shipDate) = :shipDate and lower(li.order.address.state) not like :haryana" + " group by
     * date(li.shipDate)"; return (Double) getSession().createQuery(hqlQuery) .setParameterList("lineItemTypes",
     * Arrays.asList(EnumLineItemType.CodCharges.getId(), EnumLineItemType.Shipping.getId()))
     * .setParameter("orderStatus", orderStatus) .setParameter("shipDate", shipDate) .setParameter("haryana", HARYANA)
     * .uniqueResult(); }
     */

    /*
     * public Long getShippedOrderCount(Date saleDate, Integer after) { List<OrderStatus> applicableOrderStatus = new
     * ArrayList<OrderStatus>(); applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Pending.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Shipped.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Delivered.getId())); String hqlQuery = "select
     * count(distinct li.order)" + "from LineItem li where li.lineItemType.id = :lineItemType and li.order.orderStatus
     * in (:applicableOrderStatus) and date(li.order.payment.paymentDate) = :saleDate " + "and li.shipDate is not NULL
     * and date(li.shipDate) >= :startDate and date(li.shipDate) < :endDate " + "group by
     * date(li.order.payment.paymentDate)"; Calendar startDate = Calendar.getInstance(); startDate.setTime(saleDate);
     * startDate.add(Calendar.DAY_OF_YEAR, -1); startDate.set(Calendar.HOUR, 17); startDate.set(Calendar.MINUTE, 0);
     * startDate.set(Calendar.SECOND, 0); startDate.set(Calendar.MILLISECOND, 0); Calendar endDate =
     * Calendar.getInstance(); endDate.setTime(saleDate); endDate.add(Calendar.DAY_OF_YEAR, after);
     * endDate.set(Calendar.HOUR, 17); endDate.set(Calendar.MINUTE, 0); endDate.set(Calendar.SECOND, 0);
     * endDate.set(Calendar.MILLISECOND, 0); Long count = (Long) getSession().createQuery(hqlQuery)
     * .setParameter("lineItemType", EnumLineItemType.Product.getId()) .setParameterList("applicableOrderStatus",
     * applicableOrderStatus) .setParameter("saleDate", saleDate) .setDate("startDate", startDate.getTime())
     * .setDate("endDate", endDate.getTime()) .uniqueResult(); logger.debug("Shipped orders [" + startDate.getTime() +
     * "-" + endDate.getTime() + "] for order date - " + saleDate + ": " + count); System.out.println("Shipped orders [" +
     * startDate.getTime() + "-" + endDate.getTime() + "] for order date - " + saleDate + ": " + count); return count !=
     * null ? count : 0L; }
     */

    /*
     * public Long getOrderCount(Date paymentDate, PaymentMode paymentMode) { List<OrderStatus> applicableOrderStatus =
     * new ArrayList<OrderStatus>(); applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Pending.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Shipped.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Delivered.getId())); String hqlQuery = "select
     * count(distinct o.id)" + "from Order o where o.orderStatus in (:applicableOrderStatus) and o.payment.paymentMode =
     * :paymentMode " + "and o.payment.paymentDate >= :startDate and o.payment.paymentDate < :endDate "; Calendar
     * startDate = Calendar.getInstance(); startDate.setTime(paymentDate); startDate.set(Calendar.HOUR, 0);
     * startDate.set(Calendar.MINUTE, 0); startDate.set(Calendar.SECOND, 0); startDate.set(Calendar.MILLISECOND, 0);
     * Calendar endDate = Calendar.getInstance(); endDate.setTime(paymentDate); endDate.add(Calendar.DAY_OF_YEAR, 1);
     * endDate.set(Calendar.HOUR, 0); endDate.set(Calendar.MINUTE, 0); endDate.set(Calendar.SECOND, 0);
     * endDate.set(Calendar.MILLISECOND, 0); Long count = (Long) getSession().createQuery(hqlQuery)
     * .setParameterList("applicableOrderStatus", applicableOrderStatus) .setParameter("paymentMode", paymentMode)
     * .setDate("startDate", startDate.getTime()) .setDate("endDate", endDate.getTime()) .uniqueResult(); return count !=
     * null ? count : 0L; }
     */

    /*
     * public List<Order> findOrdersByPaymentMode(PaymentMode paymentMode) { List<PaymentMode> applicablePaymentModes =
     * new ArrayList<PaymentMode>(); if (paymentMode == null) { applicablePaymentModes =
     * paymentModeDaoProvider.get().listWorkingPaymentModes(); } else {
     * applicablePaymentModes.add(paymentModeDao.find(paymentMode.getId())); } return getSession().createQuery("from
     * Order o where o.payment.paymentMode in (:applicablePaymentModes)") .setParameterList("applicablePaymentModes",
     * applicablePaymentModes) .list(); }
     */
    /*
     * public List<Order> findCategoryPerformanceParametersForTimeFrame(Date startDate, Date endDate, Category
     * applicableCategorie) { List<OrderStatus> applicableOrderStatus = new ArrayList<OrderStatus>(); String
     * categoryName = applicableCategorie.getName();
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Pending.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Shipped.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Delivered.getId())); String query = "select
     * distinct(li.order) from LineItem li where li.productVariant.product.primaryCategory.name in (:categoryName) " +
     * "and li.order.orderStatus in (:applicableOrderStatus) " + "and li.order.payment.paymentDate >= :startDate and
     * li.order.payment.paymentDate <= :endDate"; return getSession().createQuery(query) .setParameter("categoryName",
     * categoryName) .setParameterList("applicableOrderStatus", applicableOrderStatus) .setParameter("startDate",
     * startDate) .setParameter("endDate", endDate) .list(); } public CategoryPerformanceDto
     * findCategoryWiseSalesForTimeFrame(Date startDate, Date endDate, Category applicableCategorie) { List<OrderStatus>
     * applicableOrderStatus = new ArrayList<OrderStatus>(); String categoryName = applicableCategorie.getName();
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Pending.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Shipped.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Delivered.getId())); String hqlQuery = "select
     * sum(li.qty) as skuCount," + " sum(li.qty*li.markedPrice) as sumOfMrp, sum(li.qty*li.hkPrice) as sumOfHkPrice," + "
     * sum(li.discountOnHkPrice) as sumOfHkPricePostAllDiscounts " + " from LineItem li where
     * li.productVariant.product.primaryCategory.name = :categoryName " + " and li.lineItemType.id = :lineItemType and
     * li.order.orderStatus in (:applicableOrderStatus)" + " and li.order.payment.paymentDate >= :startDate and
     * li.order.payment.paymentDate <= :endDate "; return (CategoryPerformanceDto) getSession().createQuery(hqlQuery)
     * .setParameter("categoryName", categoryName) .setParameter("lineItemType", EnumLineItemType.Product.getId())
     * .setParameterList("applicableOrderStatus", applicableOrderStatus) .setParameter("startDate", startDate)
     * .setParameter("endDate", endDate).setResultTransformer(Transformers.aliasToBean(CategoryPerformanceDto.class))
     * .uniqueResult(); }
     */

    @SuppressWarnings("unchecked")
    public List<Long> getActivityPerformedOrderIds(Date activityDate, Long orderActivity, Integer cutOffDay1, Integer cutOffHour1, Integer cutOffDay2, Integer cutOffHour2) {
        List<Long> applicableOrderStatus = new ArrayList<Long>();
        applicableOrderStatus.add(EnumOrderStatus.Placed.getId());
        applicableOrderStatus.add(EnumOrderStatus.InProcess.getId());
        applicableOrderStatus.add(EnumOrderStatus.OnHold.getId());
        applicableOrderStatus.add(EnumOrderStatus.Shipped.getId());
        applicableOrderStatus.add(EnumOrderStatus.Delivered.getId());

        Date startDate = activityDate;
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(startDate);
        calendar1.add(Calendar.DATE, cutOffDay1);
        calendar1.set(Calendar.HOUR_OF_DAY, cutOffHour1);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        startDate = calendar1.getTime();

        Date endDate = activityDate;
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(endDate);
        calendar2.add(Calendar.DATE, cutOffDay2);
        calendar2.set(Calendar.HOUR_OF_DAY, cutOffHour2);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        endDate = calendar2.getTime();

        String query = "select distinct olc.order.id from OrderLifecycle olc "
                + "where olc.order.orderStatus.id in (:applicableOrderStatus) and olc.orderLifecycleActivity.id = :orderActivity "
                + "and olc.activityDate between :startDate and :endDate";

        return (List<Long>) getSession().createQuery(query).setParameterList("applicableOrderStatus", applicableOrderStatus).setParameter("orderActivity", orderActivity).setParameter(
                "startDate", startDate).setParameter("endDate", endDate).list();
    }

    /*
     */
    @SuppressWarnings("unchecked")
    public List<Long> getActivityPerformedBOCount(List<Long> orderIds, Date activityDate, Long orderActivity, Integer cutOffDay1, Integer cutOffTimeHH1, Integer cutOffTimeMM1,
            Integer cutOffDay2, Integer cutOffTimeHH2, Integer cutOffTimeMM2) {
        logger.debug("OrderIds for activity-" + orderActivity + " on " + activityDate + " is - " + orderIds.size());
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

            String query = "select distinct olc.order.id from OrderLifecycle olc " + "where olc.order.id in (:orderIds) and olc.orderLifecycleActivity.id = :orderActivity "
                    + "and olc.activityDate between :startDate and :endDate";

            orderList = (List<Long>) getSession().createQuery(query).setParameterList("orderIds", orderIds).setParameter("orderActivity", orderActivity).setParameter("startDate",
                    startDate).setParameter("endDate", endDate).list();
        }
        return orderList;
    }
    
    @SuppressWarnings("unchecked")
    public List<Long> getOrdersByPaymentModeAndStatus(List<Long> orderIds, List<Long> paymentModeIds, List<Long> paymentStatusIds) {
        String hqlQuery = "select distinct o.id "
                + "from Order o where o.id in (:orderIds) and o.payment.paymentMode.id in (:paymentModeIds) and o.payment.paymentStatus.id in (:paymentStatusIds)";

        return (List<Long>) getSession().createQuery(hqlQuery).setParameterList("orderIds", orderIds).setParameterList("paymentModeIds", paymentModeIds).setParameterList(
                "paymentStatusIds", paymentStatusIds).list();
    }

}
