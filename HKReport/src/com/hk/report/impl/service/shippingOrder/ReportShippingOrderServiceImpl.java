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
import com.hk.domain.warehouse.Warehouse;
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

    public List<ReconcilationReportDto> findReconcilationReportByDate(Date startDate, Date endDate, String paymentProcess, Courier courier, Long warehouseId, Long shippingOrderStatusId) {
        return getReportShippingOrderDao().findReconcilationReportByDate(startDate, endDate, paymentProcess, courier, warehouseId,shippingOrderStatusId);
    }




    public List<ShippingOrder> getDeliveredSOForCourierByDate(Date startDate, Date endDate, Long courierId) {
        return getReportShippingOrderDao().getDeliveredSOForCourierByDate(startDate, endDate, courierId);
    }

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
                        EnumPaymentMode.getPrePaidPaymentModes(), Arrays.asList(EnumPaymentStatus.SUCCESS.getId()));
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

    public List<ShippingOrder> getShippingOrderListForCouriers(Date startDate,Date endDate,List<Courier> courierList, Warehouse warehouse){
        return getReportShippingOrderDao().getShippingOrderListForCouriers(startDate,endDate,courierList,warehouse);
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
