package com.hk.report.pact.service.shippingOrder;

import java.util.Date;
import java.util.List;

import com.hk.domain.courier.Courier;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.warehouse.Warehouse;
import com.hk.report.dto.order.OrderLifecycleStateTransitionDto;
import com.hk.report.dto.order.reconcilation.ReconcilationReportDto;

public interface ReportShippingOrderService {

    public List<ReconcilationReportDto> findReconcilationReportByDate(Date startDate, Date endDate, String paymentProcess, Courier courier, Long warehouseId, Long shippingOrderStatusId);

    public List<ShippingOrder> getDeliveredSOForCourierByDate(Date startDate, Date endDate, Long courierId);

    public List<OrderLifecycleStateTransitionDto> getOrderLifecycleStateTransitionDtoList(Date startDate, Date endDate);

    public List<ShippingOrder> getShippingOrderListForCouriers(Date startDate,Date endDate,List<Courier> courierList, Warehouse warehouse);


}
