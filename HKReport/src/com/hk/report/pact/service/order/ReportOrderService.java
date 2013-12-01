package com.hk.report.pact.service.order;

import java.util.Date;
import java.util.List;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.Order;
import com.hk.report.dto.payment.CODConfirmationDto;
import com.hk.report.dto.sales.DaySaleDto;

public interface ReportOrderService {

    public List<DaySaleDto> findSaleForTimeFrame(List<OrderStatus> applicableOrderStatus, Date startDate, Date endDate);

    public List<Order> findOrdersForTimeFrame(Date startDate, Date endDate, OrderStatus orderStatus, Category topLevelCategory);

    public List<Order> findOrdersForTimeFrame(Date startDate, Date endDate, List<Category> applicableCategories);

    public List<CODConfirmationDto> findCODUnConfirmedOrderReport(Date startDate, Date endDate);
}
