package com.hk.report.pact.dao.order;

import java.util.Date;
import java.util.List;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.Order;
import com.hk.report.dto.payment.CODConfirmationDto;
import com.hk.report.dto.sales.DaySaleDto;

public interface ReportOrderDao {

	public List<DaySaleDto> findSaleForTimeFrame(List<OrderStatus> applicableOrderStatus, Date startDate, Date endDate);
	
	public List<Order> findOrdersForTimeFrame(Date startDate, Date endDate, List<Category> applicableCategories);
       
	public List<CODConfirmationDto> findCODUnConfirmedOrderReport(Date startDate, Date endDate);
	
	public List<Long> getActivityPerformedOrderIds(Date activityDate, Long orderActivity, Integer cutOffDay1, Integer cutOffHour1, Integer cutOffDay2, Integer cutOffHour2);
	
	public List<Long> getActivityPerformedBOCount(List<Long> orderIds, Date activityDate, Long orderActivity, Integer cutOffDay1, Integer cutOffTimeHH1, Integer cutOffTimeMM1,
            Integer cutOffDay2, Integer cutOffTimeHH2, Integer cutOffTimeMM2);
	
	public List<Long> getOrdersByPaymentModeAndStatus(List<Long> orderIds, List<Long> paymentModeIds, List<Long> paymentStatusIds);
}
