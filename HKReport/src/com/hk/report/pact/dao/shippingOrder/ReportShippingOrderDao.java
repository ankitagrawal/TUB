
package com.hk.report.pact.dao.shippingOrder;

import java.util.Date;
import java.util.List;

import com.hk.domain.courier.Courier;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.warehouse.Warehouse;
import com.hk.report.dto.order.reconcilation.ReconcilationReportDto;

public interface ReportShippingOrderDao {
    
    public List<ReconcilationReportDto> findReconcilationReportByDate(Date startDate, Date endDate, String paymentProcess, Courier courier, Long warehouseId, Long shippingOrderStatusId);
    
    public List<ShippingOrder> getDeliveredSOForCourierByDate(Date startDate, Date endDate, Long courierId);
    
    public List<Long> getActivityPerformedSOCount(List<Long> orderIds, Date activityDate, Long shippingOrderActivity, Integer cutOffDay1, Integer cutOffTimeHH1,
            Integer cutOffTimeMM1, Integer cutOffDay2, Integer cutOffTimeHH2, Integer cutOffTimeMM2);
    
    public List<Long> getActivityPerformedSOCount(List<Long> orderIds, Date activityDate, List<Long> shippingOrderActivity, Integer cutOffDay1, Integer cutOffTimeHH1,
            Integer cutOffTimeMM1, Integer cutOffDay2, Integer cutOffTimeHH2, Integer cutOffTimeMM2);
    
    public List<ShippingOrder> getShippingOrderListForCouriers(Date startDate,Date endDate,List<Courier> courierList, Warehouse warehouse);


}
