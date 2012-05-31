package com.hk.report.impl.service.shippingOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.domain.courier.Courier;
import com.hk.domain.order.ShippingOrder;
import com.hk.report.dto.order.OrderLifecycleStateTransitionDto;
import com.hk.report.dto.order.reconcilation.ReconcilationReportDto;
import com.hk.report.pact.dao.order.ReportOrderDao;
import com.hk.report.pact.dao.shippingOrder.ReportShippingOrderDao;
import com.hk.report.pact.service.shippingOrder.ReportShippingOrderService;

@Service
public class ReportShippingOrderServiceImpl implements ReportShippingOrderService {

    @Autowired
    private ReportShippingOrderDao reportShippingOrderDao;
    @Autowired
    private ReportOrderDao         reportOrderDao;

    public List<ReconcilationReportDto> findReconcilationReportByDate(Date startDate, Date endDate, String paymentProcess, Courier courier, Long warehouseId) {
        return getReportShippingOrderDao().findReconcilationReportByDate(startDate, endDate, paymentProcess, courier, warehouseId);
    }




    public List<ShippingOrder> getDeliveredSOForCourierByDate(Date startDate, Date endDate, Long courierId) {
        return getReportShippingOrderDao().getDeliveredSOForCourierByDate(startDate, endDate, courierId);
    }

    // TODO: #warehouse implement this

    /*
     * public List<LineItem> findLineItemsOfOrdersForTimeFrame(Date startDate, Date endDate, OrderStatus orderStatus,
     * Category topLevelCategory) { List<OrderStatus> applicableOrderStatus = new ArrayList<OrderStatus>(); if
     * (orderStatus != null) { `` applicableOrderStatus.add(orderStatus); } else {
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Pending.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Shipped.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Delivered.getId())); } private Logger logger =
     * LoggerFactory.getLogger(ShippingOrderService.class); OrderDao> orderDaoProvider; OrderService orderService;
     * ShipmentDao> shipmentDaoProvider; ShippingOrderDao> shippingOrderDaoProvider; public List<Long>
     * shippingOrderList = new ArrayList<Long>(); public List<Long> getShippingOrderListByCourier(Date startDate, Date
     * endDate, Long courierId) { shippingOrderList = shipmentDaoProvider.get().getShippingOrderListByCourier(startDate,
     * endDate, courierId); return shippingOrderList; } public List<ShippingOrder> getDeliveredSOForCourierByDate(Date
     * startDate, Date endDate, Long courierId) { return
     * shippingOrderDaoProvider.get().getDeliveredSOForCourierByDate(startDate, endDate, courierId); } //TODO:
     * #warehouse implement this /*public List<LineItem> findLineItemsOfOrdersForTimeFrame(Date startDate, Date
     * endDate, OrderStatus orderStatus, Category topLevelCategory) { List<OrderStatus> applicableOrderStatus = new
     * ArrayList<OrderStatus>(); if (orderStatus != null) { applicableOrderStatus.add(orderStatus); } else {
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
     * .setParameter("endDate", endDate) .list(); } public List<Order> findOrdersForTimeFrame(Date startDate, Date
     * endDate, List<Category> applicableCategories) { List<OrderStatus> applicableOrderStatus = new ArrayList<OrderStatus>();
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Pending.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Shipped.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Delivered.getId())); String query = "select
     * distinct(li.order) from LineItem li join li.productVariant.product.primaryCategory c where c in
     * (:applicableCategories) " + "and li.order.orderStatus in (:applicableOrderStatus) " + "and
     * li.order.payment.paymentDate >= :startDate and li.order.payment.paymentDate <= :endDate"; return
     * getSession().createQuery(query) .setParameterList("applicableCategories", applicableCategories)
     * .setParameterList("applicableOrderStatus", applicableOrderStatus) .setParameter("startDate", startDate)
     * .setParameter("endDate", endDate) .list(); } public List<ProductVariant>
     * findOrderedProductVariantsForTimeFrame(Date startDate, Date endDate, OrderStatus orderStatus, Category
     * topLevelCategory) { List<OrderStatus> applicableOrderStatus = new ArrayList<OrderStatus>(); if (orderStatus !=
     * null) { applicableOrderStatus.add(orderStatus); } else {
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Pending.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Shipped.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Delivered.getId())); } List<Category>
     * applicableCategories = new ArrayList<Category>(); if (topLevelCategory != null) {
     * applicableCategories.add(topLevelCategory); } else { applicableCategories = CategoryConstants.allCategoriesList; }
     * String query = "select distinct li.productVariant from LineItem li left join li.productVariant.product.categories
     * c " + "where c in (:applicableCategories) and li.order.orderStatus in (:applicableOrderStatus) " + "and
     * li.order.payment.paymentDate >= :startDate and li.order.payment.paymentDate <= :endDate"; return
     * getSession().createQuery(query) .setParameterList("applicableCategories", applicableCategories)
     * .setParameterList("applicableOrderStatus", applicableOrderStatus) .setParameter("startDate", startDate)
     * .setParameter("endDate", endDate) .list(); } public List<Order> findReconciledOrdersForTimeFrame(Date startDate,
     * Date endDate, OrderStatus orderStatus, PaymentMode paymentMode, Courier codMode, ReconciliationStatus
     * reconciliationStatus) { List<OrderStatus> applicableOrderStatus = new ArrayList<OrderStatus>(); if (orderStatus ==
     * null) { applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Shipped.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Delivered.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.RETURNED.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.PARTIALLY_LOST.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.LOST.getId())); } else {
     * applicableOrderStatus.add(orderStatus); } List<PaymentMode> applicablePaymentModes = new ArrayList<PaymentMode>();
     * if (paymentMode == null) { applicablePaymentModes.add(paymentModeDao.find(EnumPaymentMode.TECHPROCESS.getId()));
     * applicablePaymentModes.add(paymentModeDao.find(EnumPaymentMode.COD.getId()));
     * applicablePaymentModes.add(paymentModeDao.find(EnumPaymentMode.CITRUS.getId())); } else {
     * applicablePaymentModes.add(paymentMode); } List<Courier> applicableCouriers = new ArrayList<Courier>(); if
     * (codMode == null) { applicableCouriers = courierDaoProvider.get().listAll(); } else {
     * applicableCouriers.add(codMode); } List<ReconciliationStatus> applicableReconciliationStatus = new ArrayList<ReconciliationStatus>();
     * if (reconciliationStatus == null) {
     * applicableReconciliationStatus.add(reconciliationStatusDao.find(EnumReconciliationStatus.PENDING.getId()));
     * applicableReconciliationStatus.add(reconciliationStatusDao.find(EnumReconciliationStatus.DONE.getId())); } else {
     * applicableReconciliationStatus.add(reconciliationStatus); } return getSession().createQuery("select
     * distinct(l.order) from LineItem l where l.lineItemType = :lineItemType and l.order.orderStatus in
     * (:applicableOrderStatus) and l.order.payment.paymentDate >= :startDate and l.order.payment.paymentDate <=
     * :endDate and l.order.payment.paymentMode in (:applicablePaymentModes) and l.courier in (:applicableCouriers) and
     * l.order.reconciliationStatus in (:applicableReconciliationStatus) order by l.order.payment.paymentDate asc")
     * .setParameterList("applicableOrderStatus", applicableOrderStatus) .setParameter("startDate", startDate)
     * .setParameter("endDate", endDate) .setParameterList("applicablePaymentModes", applicablePaymentModes)
     * .setParameterList("applicableCouriers", applicableCouriers) .setParameterList("applicableReconciliationStatus",
     * applicableReconciliationStatus) .setEntity("lineItemType",
     * lineItemTypeDaoProvider.get().find(EnumLineItemType.Product.getId())) .list(); } public Long
     * findTotalQtyOfOrderedProductVariantForTimeFrame(ProductVariant productVariant, Date startDate, Date endDate,
     * OrderStatus orderStatus) { List<OrderStatus> applicableOrderStatus = new ArrayList<OrderStatus>(); if
     * (orderStatus != null) { applicableOrderStatus.add(orderStatus); } else {
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Pending.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Shipped.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Delivered.getId())); } return (Long)
     * getSession().createQuery("select sum(li.qty) from LineItem li where li.productVariant=:productVariant and
     * li.lineItemType.id = :lineItemTypeId and li.order.orderStatus in (:applicableOrderStatus) and
     * li.order.payment.paymentDate >= :startDate and li.order.payment.paymentDate <= :endDate")
     * .setEntity("productVariant", productVariant) .setParameter("lineItemTypeId", EnumLineItemType.Product.getId())
     * .setParameterList("applicableOrderStatus", applicableOrderStatus) .setParameter("startDate", startDate)
     * .setParameter("endDate", endDate) .uniqueResult(); } public List<DaySaleDto> findSaleForTimeFrame(Date
     * startDate, Date endDate) { List<OrderStatus> applicableOrderStatus = new ArrayList<OrderStatus>();
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Pending.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Shipped.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Delivered.getId())); String hqlQuery = "select
     * date(li.order.payment.paymentDate) as orderDate, count(distinct li.order.id) as txnCount, sum(li.qty) as
     * skuCount, sum(li.qty*li.markedPrice) as sumOfMrp, sum(li.qty*li.hkPrice) as sumOfHkPrice,
     * sum(li.discountOnHkPrice) as sumOfHkPricePostAllDiscounts " + "from LineItem li where li.lineItemType.id =
     * :lineItemType and li.order.orderStatus in (:applicableOrderStatus) and li.order.payment.paymentDate >= :startDate
     * and li.order.payment.paymentDate <= :endDate " + "group by date(li.order.payment.paymentDate)"; return
     * getSession().createQuery(hqlQuery) .setParameter("lineItemType", EnumLineItemType.Product.getId())
     * .setParameterList("applicableOrderStatus", applicableOrderStatus) .setParameter("startDate", startDate)
     * .setParameter("endDate", endDate).setResultTransformer(Transformers.aliasToBean(DaySaleDto.class)) .list(); }
     * public List<DaySaleDto> findBasicCountSaleForTimeFrame(Date startDate, Date endDate) { List<OrderStatus>
     * applicableOrderStatus = new ArrayList<OrderStatus>();
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Pending.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Shipped.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Delivered.getId())); String hqlQuery = "select
     * date(li.order.payment.paymentDate) as orderDate, count(distinct li.order.id) as txnCount " + "from LineItem li
     * where li.lineItemType.id = :lineItemType and li.order.orderStatus in (:applicableOrderStatus) and
     * li.order.payment.paymentDate >= :startDate and li.order.payment.paymentDate <= :endDate " + "group by
     * date(li.order.payment.paymentDate)"; return getSession().createQuery(hqlQuery) .setParameter("lineItemType",
     * EnumLineItemType.Product.getId()) .setParameterList("applicableOrderStatus", applicableOrderStatus)
     * .setParameter("startDate", startDate) .setParameter("endDate",
     * endDate).setResultTransformer(Transformers.aliasToBean(DaySaleDto.class)) .list(); } public List<DaySaleDto>
     * findSaleForTimeFrameForShippedProducts(Date startDate, Date endDate, List<OrderStatus> applicableOrderStatus) {
     * String hqlQuery = "select date(li.shipDate) as orderShipDate, li.order.orderStatus as orderStatus, count(distinct
     * li.order.id) as txnCount, sum(li.qty) as skuCount, " + " sum(li.qty*li.markedPrice) as sumOfMrp,
     * sum(li.qty*li.hkPrice) as sumOfHkPrice, sum(li.discountOnHkPrice) as sumOfAllDiscounts " + " from LineItem li
     * where li.shipDate is not null and li.lineItemType.id = :lineItemType and li.order.orderStatus in
     * (:applicableOrderStatus) " + " and li.shipDate >= :startDate and li.shipDate <= :endDate " + " group by
     * date(li.shipDate), li.order.orderStatus order by date(li.shipDate), li.order.orderStatus.id "; return
     * getSession().createQuery(hqlQuery) .setParameter("lineItemType", EnumLineItemType.Product.getId())
     * .setParameterList("applicableOrderStatus", applicableOrderStatus) .setParameter("startDate", startDate)
     * .setParameter("endDate", endDate).setResultTransformer(Transformers.aliasToBean(DaySaleDto.class)) .list(); }
     * @DBConfig(config = DBConfigValue.ReadOnlySlave) public List<DaySaleDto>
     * findSaleForProductsByTaxAndStatusInRegion(List<Tax> taxList, List<OrderStatus> orderStatusList, String
     * inRegion, String regionName, boolean isRegionNameInclusive, Date startDate, Date endDate) { StringBuilder saleHql =
     * new StringBuilder("select date(li.shipDate) as orderShipDate, li.order.orderStatus as orderStatus, t as
     * taxCategory, count(distinct li.order.id) as txnCount, sum(li.qty) as skuCount, " + " sum(li.qty*li.markedPrice)
     * as sumOfMrp, sum(li.qty*li.hkPrice) as sumOfHkPrice, sum(rli.actualDiscountOnHkPrice * li.qty) as
     * sumOfAllDiscounts, " + " sum(rli.shippingCharge * li.qty + rli.codCharge * li.qty) as sumOfForwardingCharges " + "
     * from LineItem li, Tax t, RetailLineItem rli " + " where li.shipDate is not null " + " and li.tax.id = t.id and
     * rli.lineItem = li " + " and li.lineItemType.id = :lineItemType "); if (startDate != null && endDate != null) {
     * saleHql.append(" and date(li.shipDate) >= :startDate and date(li.shipDate) <= :endDate) "); } else {
     * saleHql.append(" and month(li.shipDate) = month(:startDate) and year(li.shipDate) = year(:startDate) "); } if
     * (orderStatusList != null && !orderStatusList.isEmpty()) { saleHql.append(" and li.order.orderStatus in
     * (:orderStatusList) "); } if (taxList != null && !taxList.isEmpty()) { saleHql.append(" and li.tax in (:taxList)
     * "); } if (StringUtils.isNotEmpty(inRegion) && inRegion.equalsIgnoreCase("yes")) { isRegionNameInclusive = true; }
     * else { isRegionNameInclusive = false; } if (StringUtils.isNotEmpty(inRegion) &&
     * StringUtils.isNotEmpty(regionName)) { if (isRegionNameInclusive) { saleHql.append(" and
     * lower(li.order.address.state) like :regionName "); } else { saleHql.append(" and lower(li.order.address.state)
     * not like :regionName "); } } if (startDate != null && endDate != null) { saleHql.append(" group by
     * date(li.shipDate) order by date(li.shipDate) "); } else { saleHql.append(" group by year(li.shipDate),
     * month(li.shipDate) order by year(li.shipDate),month(li.shipDate) "); } Query saleHqlQuery =
     * getSession().createQuery(saleHql.toString()); saleHqlQuery.setParameter("lineItemType",
     * EnumLineItemType.Product.getId()).setParameter("startDate", startDate); if (startDate != null && endDate != null) {
     * saleHqlQuery.setParameter("endDate", endDate); } if (orderStatusList != null && !orderStatusList.isEmpty()) {
     * saleHqlQuery.setParameterList("orderStatusList", orderStatusList); } if (taxList != null && !taxList.isEmpty()) {
     * saleHqlQuery.setParameterList("taxList", taxList); } if (StringUtils.isNotEmpty(inRegion) &&
     * StringUtils.isNotEmpty(regionName)) { saleHqlQuery.setParameter("regionName", regionName.toLowerCase()); }
     * saleHqlQuery.setResultTransformer(Transformers.aliasToBean(DaySaleDto.class)); return saleHqlQuery.list(); }
     * public List<CODConfirmationDto> findCODUnConfirmedOrderReport(Date startDate, Date endDate) { if (startDate ==
     * null) { Calendar calendar = Calendar.getInstance(); calendar.add(Calendar.MONTH, -1); startDate =
     * calendar.getTime(); } if (endDate == null) { endDate = new Date(); } String query = "select olc1.activityDate as
     * orderPlacedDate, olc1.order as order, olc1.order.user.email as userEmail, olc1.user.name as userName, " + "
     * UNIX_TIMESTAMP(current_timestamp()) - UNIX_TIMESTAMP(olc1.activityDate) as totalTimeTakenToConfirm " + " from
     * OrderLifecycle olc1 where olc1.orderLifecycleActivity = :orderPlaced " + " and olc1.order.payment.paymentMode =
     * :paymentModeCOD1 " + " and olc1.activityDate between :startDate and :endDate " + " and olc1.order.id not in " + "
     * (select distinct(olc2.order.id) from OrderLifecycle olc2 where (olc2.orderLifecycleActivity = :orderConfirmed or
     * olc2.orderLifecycleActivity = :orderCancelled) " + " and olc2.order.payment.paymentMode = :paymentModeCOD2 and
     * olc1.order.id = olc2.order.id ) "; return getSession().createQuery(query) .setParameter("orderPlaced",
     * orderLifecycleActivityDaoProvider.get().find(EnumOrderLifecycleActivity.OrderPlaced.getId()))
     * .setParameter("paymentModeCOD1", paymentModeDaoProvider.get().find(EnumPaymentMode.COD.getId()))
     * .setParameter("orderConfirmed",
     * orderLifecycleActivityDaoProvider.get().find(EnumOrderLifecycleActivity.ConfirmedAuthorization.getId()))
     * .setParameter("orderCancelled",
     * orderLifecycleActivityDaoProvider.get().find(EnumOrderLifecycleActivity.OrderCancelled.getId()))
     * .setParameter("paymentModeCOD2", paymentModeDaoProvider.get().find(EnumPaymentMode.COD.getId()))
     * .setParameter("startDate", startDate) .setParameter("endDate",
     * endDate).setResultTransformer(Transformers.aliasToBean(CODConfirmationDto.class)) .list(); } public List<CODConfirmationDto>
     * findCODConfirmedOrderReport(Date startDate, Date endDate) { if (startDate == null) { Calendar calendar =
     * Calendar.getInstance(); calendar.add(Calendar.MONTH, -1); startDate = calendar.getTime(); } if (endDate == null) {
     * endDate = new Date(); } String query = "select olc1.activityDate as orderPlacedDate, olc1.order as order,
     * olc1.order.user.email as userEmail, olc1.user.name as userName, olc2.activityDate as orderConfirmationDate, " + "
     * UNIX_TIMESTAMP(olc2.activityDate) - UNIX_TIMESTAMP(olc1.activityDate) as totalTimeTakenToConfirm " + " from
     * OrderLifecycle olc1, OrderLifecycle olc2 where olc1.order.id = olc2.order.id and " + "
     * olc1.order.payment.paymentMode = :paymentModeCOD and olc1.orderLifecycleActivity = :orderPlaced and " + "
     * olc2.orderLifecycleActivity = :orderConfirmed and olc1.activityDate between :startDate and :endDate"; return
     * getSession().createQuery(query) .setParameter("paymentModeCOD",
     * paymentModeDaoProvider.get().find(EnumPaymentMode.COD.getId())) .setParameter("orderPlaced",
     * orderLifecycleActivityDaoProvider.get().find(EnumOrderLifecycleActivity.OrderPlaced.getId()))
     * .setParameter("orderConfirmed",
     * orderLifecycleActivityDaoProvider.get().find(EnumOrderLifecycleActivity.ConfirmedAuthorization.getId()))
     * .setParameter("startDate", startDate) .setParameter("endDate",
     * endDate).setResultTransformer(Transformers.aliasToBean(CODConfirmationDto.class)) .list(); } public Double
     * getNetDiscountViaOrderLevelCouponAndRewardPoints(Date saleDate) { List<OrderStatus> applicableOrderStatus = new
     * ArrayList<OrderStatus>(); applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Pending.getId()));
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
     * .uniqueResult(); } public Long getOrderCount(Date paymentDate, PaymentMode paymentMode) { List<OrderStatus>
     * applicableOrderStatus = new ArrayList<OrderStatus>();
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Pending.getId()));
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
     * null ? count : 0L; } public List<Order> findOrdersByPaymentMode(PaymentMode paymentMode) { List<PaymentMode>
     * applicablePaymentModes = new ArrayList<PaymentMode>(); if (paymentMode == null) { applicablePaymentModes =
     * paymentModeDaoProvider.get().listWorkingPaymentModes(); } else {
     * applicablePaymentModes.add(paymentModeDao.find(paymentMode.getId())); } return getSession().createQuery("from
     * Order o where o.payment.paymentMode in (:applicablePaymentModes)") .setParameterList("applicablePaymentModes",
     * applicablePaymentModes) .list(); } public List<Order> findCategoryPerformanceParametersForTimeFrame(Date
     * startDate, Date endDate, Category applicableCategorie) { List<OrderStatus> applicableOrderStatus = new ArrayList<OrderStatus>();
     * String categoryName = applicableCategorie.getName();
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
     * .uniqueResult(); } public Long getCODConfirmationTime(Date startDate, Date endDate) { List<OrderStatus>
     * applicableOrderStatus = new ArrayList<OrderStatus>();
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Pending.getId()));
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
     * timetaken : 0; } public List<Long> getActivityPerformedOrderIds(Date activityDate, Long orderActivity, Integer
     * cutOffDay1, Integer cutOffHour1, Integer cutOffDay2, Integer cutOffHour2) { List<OrderStatus>
     * applicableOrderStatus = new ArrayList<OrderStatus>();
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Pending.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Shipped.getId()));
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Delivered.getId())); Date startDate = activityDate;
     * Calendar calendar1 = Calendar.getInstance(); calendar1.setTime(startDate); calendar1.add(Calendar.DATE,
     * cutOffDay1); calendar1.set(Calendar.HOUR_OF_DAY, cutOffHour1); calendar1.set(Calendar.MINUTE, 0);
     * calendar1.set(Calendar.SECOND, 0); startDate = calendar1.getTime(); Date endDate = activityDate; Calendar
     * calendar2 = Calendar.getInstance(); calendar2.setTime(endDate); calendar2.add(Calendar.DATE, cutOffDay2);
     * calendar2.set(Calendar.HOUR_OF_DAY, cutOffHour2); calendar2.set(Calendar.MINUTE, 0);
     * calendar2.set(Calendar.SECOND, 0); endDate = calendar2.getTime(); String query = "select distinct olc.order.id
     * from OrderLifecycle olc " + "where olc.order.orderStatus in (:applicableOrderStatus) and
     * olc.orderLifecycleActivity.id = :orderActivity " + "and olc.activityDate between :startDate and :endDate"; return
     * (List<Long>) getSession().createQuery(query) .setParameterList("applicableOrderStatus", applicableOrderStatus)
     * .setParameter("orderActivity", orderActivity) .setParameter("startDate", startDate) .setParameter("endDate",
     * endDate) .list(); } public List<Long> getActivityPerformedBOCount(List<Long> orderIds, Date activityDate, Long
     * orderActivity, Integer cutOffDay1, Integer cutOffTimeHH1, Integer cutOffTimeMM1, Integer cutOffDay2, Integer
     * cutOffTimeHH2, Integer cutOffTimeMM2) { logger.debug("OrderIds for activity-" + orderActivity + " on " +
     * activityDate + " is - " + orderIds.size()); List<Long> orderList = new ArrayList<Long>(); Date startDate =
     * activityDate; Calendar calendar1 = Calendar.getInstance(); calendar1.setTime(startDate);
     * calendar1.add(Calendar.DATE, cutOffDay1); calendar1.set(Calendar.HOUR_OF_DAY, cutOffTimeHH1);
     * calendar1.set(Calendar.MINUTE, cutOffTimeMM1); calendar1.set(Calendar.SECOND, 0); startDate =
     * calendar1.getTime(); Date endDate = activityDate; Calendar calendar2 = Calendar.getInstance();
     * calendar2.setTime(endDate); calendar2.add(Calendar.DATE, cutOffDay2); calendar2.set(Calendar.HOUR_OF_DAY,
     * cutOffTimeHH2); calendar2.set(Calendar.MINUTE, cutOffTimeMM2); calendar2.set(Calendar.SECOND, 0); endDate =
     * calendar2.getTime(); if (orderIds.size() > 0) { String query = "select distinct olc.order.id from OrderLifecycle
     * olc " + "where olc.order.id in (:orderIds) and olc.orderLifecycleActivity.id = :orderActivity " + "and
     * olc.activityDate between :startDate and :endDate"; orderList = (List<Long>) getSession().createQuery(query)
     * .setParameterList("orderIds", orderIds) .setParameter("orderActivity", orderActivity) .setParameter("startDate",
     * startDate) .setParameter("endDate", endDate) .list(); } return orderList; } public Long
     * getOrderCountByPaymentMode(List<Long> orderIds, Long paymentModeId) { String hqlQuery = "select count(distinct
     * o.id)" + "from Order o where o.id in (:orderIds) and o.payment.paymentMode.id = :paymentModeId "; Long count =
     * (Long) getSession().createQuery(hqlQuery) .setParameterList("orderIds", orderIds) .setParameter("paymentModeId",
     * paymentModeId) .uniqueResult(); return count != null ? count : 0L; } public List<Long>
     * getOrdersByPaymentModeAndStatus(List<Long> orderIds, List<Long> paymentModeIds, List<Long> paymentStatusIds) {
     * String hqlQuery = "select distinct o.id " + "from Order o where o.id in (:orderIds) and o.payment.paymentMode.id
     * in (:paymentModeIds) and o.payment.paymentStatus.id in (:paymentStatusIds)"; return (List<Long>)
     * getSession().createQuery(hqlQuery) .setParameterList("orderIds", orderIds) .setParameterList("paymentModeIds",
     * paymentModeIds) .setParameterList("paymentStatusIds", paymentStatusIds) .list(); } public Set<Order>
     * getOrderLyingIdleInActionQueue(List<Long> orderIds, Date activityDate, Integer cutOffTime) { Set<Order>
     * orderList = new HashSet<Order>(); List<Long> applicablePaymentModesId = new ArrayList<Long>();
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
     * .setParameterList("gatewayOrderList", gatewayOrderList) .executeUpdate(); } public Long
     * getOrderCountByOfferInstance(Date paymentDate) { List<OrderStatus> applicableOrderStatus = new ArrayList<OrderStatus>();
     * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Pending.getId()));
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
     * .setDate("endDate", endDate.getTime()) .uniqueResult(); return count != null ? count : 0L; } public Long
     * getFirstTimeTxnUsers(Date orderDate) { List<OrderStatus> applicableOrderStatus = new ArrayList<OrderStatus>();
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
     * HARYANA).setResultTransformer(Transformers.aliasToBean(DaySaleDto.class)) .list(); } public List<NetMarginPerProductVariantDto>
     * findNetMarginReportPerProductVariant(Date startDate, Date endDate) { StringBuilder netMarginSql = new
     * StringBuilder("select p.primaryCategory as primaryCategory ,pv.id as productVariantId,pv.hkPrice as
     * hkPrice,pv.costPrice as costPrice," + "t.value as taxRate,sum(li.qty) as quantity," +
     * "(pv.hkPrice/(1+t.value+t.value*.05))as taxAdjPrice,((pv.hkPrice/(1+t.value+t.value*.05))-pv.costPrice)as
     * netMargin," + "sum(rli.shippingChargedByCourier*li.qty)as totalShipping," +
     * "sum(rli.collectionChargedByCourier*li.qty)as totalCollection,p.name as productName,li.productVariant as
     * productVariant," +
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

    public List<OrderLifecycleStateTransitionDto> getOrderLifecycleStateTransitionDtoList(Date startDate, Date endDate) {
        List<OrderLifecycleStateTransitionDto> stateTransitionDtos = new ArrayList<OrderLifecycleStateTransitionDto>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (calendar.getTime().before(endDate)) {
            Set<Long> placedOrders = new HashSet<Long>();
            Set<Long> tpslOrders = new HashSet<Long>();
            Set<Long> codOrders = new HashSet<Long>();
            Set<Long> confirmedCODOrders = new HashSet<Long>();
            Set<Long> escalableOrders = new HashSet<Long>();
            Set<Long> ordersEscalatedToPackingQeueue = new HashSet<Long>();
            Set<Long> ordersPushedBackToActionQueue = new HashSet<Long>();
            Set<Long> ordersEscalatedToShipmentQueue = new HashSet<Long>();
            Set<Long> shippedOrders = new HashSet<Long>();

            // if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            List<Long> orderIds = getReportOrderDao().getActivityPerformedOrderIds(calendar.getTime(), EnumOrderLifecycleActivity.OrderPlaced.getId(), -1, 16, 0, 16);// 4PM
            if (orderIds != null && orderIds.size() > 0) {
                placedOrders.addAll(orderIds);
                List<Long> tpslOrderList = getReportOrderDao().getOrdersByPaymentModeAndStatus(orderIds,
                        Arrays.asList(EnumPaymentMode.TECHPROCESS.getId(), EnumPaymentMode.CITRUS.getId()), Arrays.asList(EnumPaymentStatus.SUCCESS.getId()));
                tpslOrders.addAll(tpslOrderList);
                List<Long> codOrderList = getReportOrderDao().getOrdersByPaymentModeAndStatus(orderIds, Arrays.asList(EnumPaymentMode.COD.getId()),
                        Arrays.asList(EnumPaymentStatus.AUTHORIZATION_PENDING.getId(), EnumPaymentStatus.ON_DELIVERY.getId()));
                codOrders.addAll(codOrderList);
                List<Long> confirmedCODOrderList = getReportOrderDao().getActivityPerformedBOCount(codOrderList, calendar.getTime(),
                        EnumOrderLifecycleActivity.ConfirmedAuthorization.getId(), -1, 16, 15, 0, 16, 15); // 4:15PM
                confirmedCODOrders.addAll(confirmedCODOrderList);
                List<Long> escalableOrderList = new ArrayList<Long>();
                escalableOrderList.addAll(tpslOrderList);
                escalableOrderList.addAll(confirmedCODOrderList);
                escalableOrders.addAll(escalableOrderList);
                /*
                 * List<Long> orderEscalatedToPackingQeueueList =
                 * shippingOrderDaoProvider.get().getActivityPerformedSOCount(escalableOrderList, calendar.getTime(),
                 * EnumOrderLifecycleActivity.EscalatedPartiallyToProcessingQueue.getId(), -1, 16, 30, 0, 16,
                 * 30);//4:30PM
                 * orderEscalatedToPackingQeueueList.addAll(shippingOrderDaoProvider.get().getActivityPerformedSOCount(escalableOrderList,
                 * calendar.getTime(), EnumOrderLifecycleActivity.EscalatedToProcessingQueue.getId(), -1, 16, 30, 0, 16,
                 * 30));//4:30PM ordersEscalatedToPackingQeueue.addAll(orderEscalatedToPackingQeueueList);
                 */
                List<Long> escalationLifecycleActivityForSO = EnumShippingOrderLifecycleActivity.getSOLifecycleActivityIDs(EnumShippingOrderLifecycleActivity.getActivitiesForPackingQueue());
                List<Long> orderEscalatedToPackingQeueueList = getReportShippingOrderDao().getActivityPerformedSOCount(escalableOrderList, calendar.getTime(),
                        escalationLifecycleActivityForSO, -1, 16, 30, 1, 16, 30); // 4:30PM
                // orderEscalatedToPackingQeueueList.addAll(shippingOrderDaoProvider.get().getActivityPerformedSOCount(escalableOrderList,
                // calendar.getTime(), EnumOrderLifecycleActivity.EscalatedToProcessingQueue.getId(), -1, 16, 30, 1, 16,
                // 30)); //4:30PM
                ordersEscalatedToPackingQeueue.addAll(orderEscalatedToPackingQeueueList);
                List<Long> orderPushedBackToActionQueueList = getReportShippingOrderDao().getActivityPerformedSOCount(escalableOrderList, calendar.getTime(),
                        EnumShippingOrderLifecycleActivity.SO_EscalatedBackToActionQueue.getId(), -1, 16, 30, 1, 24, 0);
                ordersPushedBackToActionQueue.addAll(orderPushedBackToActionQueueList);
                List<Long> orderEscalatedToShipmentQueueList = getReportShippingOrderDao().getActivityPerformedSOCount(escalableOrderList, calendar.getTime(),
                        EnumShippingOrderLifecycleActivity.SO_Packed.getId(), -1, 16, 30, 1, 24, 0);
                ordersEscalatedToShipmentQueue.addAll(orderEscalatedToShipmentQueueList);
                List<Long> shippedOrderList = getReportShippingOrderDao().getActivityPerformedSOCount(orderIds, calendar.getTime(),
                        EnumShippingOrderLifecycleActivity.SO_Shipped.getId(), -1, 16, 30, 1, 24, 0);
                shippedOrders.addAll(shippedOrderList);
            }
            // }
            /*
             * else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) { // We will merge Sunday and Monday data
             * here List<Long> orderIds = orderDaoProvider.get().getActivityPerformedOrderIds(calendar.getTime(),
             * EnumOrderLifecycleActivity.OrderPlaced.getId(), -1, 16, 1, 16); //4PM if (orderIds != null &&
             * orderIds.size() > 0) { placedOrders.addAll(orderIds); List<Long> tpslOrderList =
             * orderDaoProvider.get().getOrdersByPaymentModeAndStatus(orderIds,
             * Arrays.asList(EnumPaymentMode.TECHPROCESS.getId(), EnumPaymentMode.CITRUS.getId()),
             * Arrays.asList(EnumPaymentStatus.SUCCESS.getId())); tpslOrders.addAll(tpslOrderList); List<Long>
             * codOrderList = orderDaoProvider.get().getOrdersByPaymentModeAndStatus(orderIds,
             * Arrays.asList(EnumPaymentMode.COD.getId()),
             * Arrays.asList(EnumPaymentStatus.AUTHORIZATION_PENDING.getId(), EnumPaymentStatus.ON_DELIVERY.getId()));
             * codOrders.addAll(codOrderList); List<Long> confirmedCODOrderList =
             * orderDaoProvider.get().getActivityPerformedBOCount(codOrderList, calendar.getTime(),
             * EnumOrderLifecycleActivity.ConfirmedAuthorization.getId(), -1, 16, 15, 1, 16, 15); //4:15PM
             * confirmedCODOrders.addAll(confirmedCODOrderList); List<Long> escalableOrderList = new ArrayList<Long>();
             * escalableOrderList.addAll(tpslOrderList); escalableOrderList.addAll(confirmedCODOrderList);
             * escalableOrders.addAll(escalableOrderList); List<Long> escalationLifecycleActivityForSO =
             * EnumShippingOrderLifecycleActivity.getSOLifecycleActivityIDs(EnumShippingOrderLifecycleActivity.getActivitiesForPackingQueue());
             * List<Long> orderEscalatedToPackingQeueueList =
             * shippingOrderDaoProvider.get().getActivityPerformedSOCount(escalableOrderList, calendar.getTime(),
             * escalationLifecycleActivityForSO, -1, 16, 30, 1, 16, 30); //4:30PM
             * orderEscalatedToPackingQeueueList.addAll(shippingOrderDaoProvider.get().getActivityPerformedSOCount(escalableOrderList,
             * calendar.getTime(), EnumOrderLifecycleActivity.EscalatedToProcessingQueue.getId(), -1, 16, 30, 1, 16,
             * 30)); //4:30PM ordersEscalatedToPackingQeueue.addAll(orderEscalatedToPackingQeueueList); List<Long>
             * orderPushedBackToActionQueueList =
             * shippingOrderDaoProvider.get().getActivityPerformedSOCount(escalableOrderList, calendar.getTime(),
             * EnumShippingOrderLifecycleActivity.SO_EscalatedBackToActionQueue.getId(), -1, 16, 30, 1, 24, 0);
             * ordersPushedBackToActionQueue.addAll(orderPushedBackToActionQueueList); List<Long>
             * orderEscalatedToShipmentQueueList =
             * shippingOrderDaoProvider.get().getActivityPerformedSOCount(escalableOrderList, calendar.getTime(),
             * EnumShippingOrderLifecycleActivity.SO_EscalatedBackToActionQueue.getId(), -1, 16, 30, 1, 24, 0);
             * ordersEscalatedToShipmentQueue.addAll(orderEscalatedToShipmentQueueList); List<Long> shippedOrderList =
             * shippingOrderDaoProvider.get().getActivityPerformedSOCount(orderIds, calendar.getTime(),
             * EnumShippingOrderLifecycleActivity.SO_Packed.getId(), -1, 16, 30, 1, 24, 0);
             * shippedOrders.addAll(shippedOrderList); } calendar.add(Calendar.HOUR, 24); }
             */
            OrderLifecycleStateTransitionDto orderLifecycleStateTransitionDto = new OrderLifecycleStateTransitionDto();
            orderLifecycleStateTransitionDto.setActivityDate(calendar.getTime());
            orderLifecycleStateTransitionDto.setPlacedOrders(placedOrders);
            orderLifecycleStateTransitionDto.setTpslOrders(tpslOrders);
            orderLifecycleStateTransitionDto.setCodOrders(codOrders);
            orderLifecycleStateTransitionDto.setConfirmedCODOrders(confirmedCODOrders);
            orderLifecycleStateTransitionDto.setEscalableOrders(escalableOrders);
            orderLifecycleStateTransitionDto.setOrdersEscalatedToPackingQeueue(ordersEscalatedToPackingQeueue);
            orderLifecycleStateTransitionDto.setOrdersPushedBackToActionQueue(ordersPushedBackToActionQueue);
            orderLifecycleStateTransitionDto.setOrdersEscalatedToShipmentQueue(ordersEscalatedToShipmentQueue);
            orderLifecycleStateTransitionDto.setShippedOrders(shippedOrders);
            stateTransitionDtos.add(orderLifecycleStateTransitionDto);

            calendar.add(Calendar.HOUR, 24);
        }
        return stateTransitionDtos;
    }

    public List<ShippingOrder> getShippingOrderListForCouriers(Date startDate,Date endDate,List<Courier> courierList){
        return getReportShippingOrderDao().getShippingOrderListForCouriers(startDate,endDate,courierList);
    }

    public ReportShippingOrderDao getReportShippingOrderDao() {
        return reportShippingOrderDao;
    }

    public void setReportShippingOrderDao(ReportShippingOrderDao reportShippingOrderDao) {
        this.reportShippingOrderDao = reportShippingOrderDao;
    }

    public ReportOrderDao getReportOrderDao() {
        return reportOrderDao;
    }

    public void setReportOrderDao(ReportOrderDao reportOrderDao) {
        this.reportOrderDao = reportOrderDao;
    }

}
