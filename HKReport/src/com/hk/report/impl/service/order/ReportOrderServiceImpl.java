package com.hk.report.impl.service.order;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.constants.order.EnumOrderStatus;
import com.hk.core.search.OrderSearchCriteria;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.Order;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.order.OrderService;
import com.hk.report.dto.payment.CODConfirmationDto;
import com.hk.report.dto.sales.DaySaleDto;
import com.hk.report.pact.dao.order.ReportOrderDao;
import com.hk.report.pact.service.order.ReportOrderService;

@Service
public class ReportOrderServiceImpl implements ReportOrderService {

  @Autowired
  ReportOrderDao reportOrderDao;
  @Autowired
  OrderStatusService orderStatusService;
  @Autowired
  OrderService orderService;
  @Autowired
  private CategoryService categoryService;

  public List<DaySaleDto> findSaleForTimeFrame(List<OrderStatus> applicableOrderStatus, Date startDate, Date endDate) {
    return getReportOrderDao().findSaleForTimeFrame(applicableOrderStatus, startDate, endDate);
  }

  public List<Order> findOrdersForTimeFrame(Date startDate, Date endDate, OrderStatus orderStatus, Category topLevelCategory) {
    OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();
    if (startDate != null) {
      orderSearchCriteria.setPaymentStartDate(startDate);
    } else {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(new Date());
      calendar.add(Calendar.DAY_OF_MONTH, -30);
      orderSearchCriteria.setPaymentStartDate(calendar.getTime());
    }
    if (endDate != null) {
      orderSearchCriteria.setPaymentEndDate(endDate);
    } else {
      orderSearchCriteria.setPaymentEndDate(new Date());
    }

    List<OrderStatus> applicableOrderStatus = new ArrayList<OrderStatus>();
    List<Category> applicableCategories = new ArrayList<Category>();
    if (orderStatus != null) {
      applicableOrderStatus.add(orderStatus);
    } else {
      applicableOrderStatus = orderStatusService.getOrderStatuses(EnumOrderStatus.getStatusForReporting());
    }

    if (topLevelCategory != null) {
      applicableCategories.add(topLevelCategory);
    } else {
      applicableCategories = getCategoryService().getPrimaryCategories();
    }
    orderSearchCriteria.setOrderStatusList(applicableOrderStatus);
    orderSearchCriteria.setCategories(new HashSet<Category>(applicableCategories));

    List<Order> orders = orderService.searchOrders(orderSearchCriteria);
    return orders;

  }

  public List<Long> getActivityPerformedOrderIds(Date activityDate, Long orderActivity, Integer cutOffDay1, Integer cutOffHour1, Integer cutOffDay2, Integer cutOffHour2) {
    return getReportOrderDao().getActivityPerformedOrderIds(activityDate, orderActivity, cutOffDay1, cutOffHour1, cutOffDay2, cutOffHour2);
  }

  public List<Order> findOrdersForTimeFrame(Date startDate, Date endDate, List<Category> applicableCategories) {
    return getReportOrderDao().findOrdersForTimeFrame(startDate, endDate, applicableCategories);
  }

  /*
  */
  public List<Long> getActivityPerformedBOCount(List<Long> orderIds, Date activityDate, Long orderActivity, Integer cutOffDay1, Integer cutOffTimeHH1, Integer cutOffTimeMM1,
                                                Integer cutOffDay2, Integer cutOffTimeHH2, Integer cutOffTimeMM2) {
    return getReportOrderDao().getActivityPerformedBOCount(orderIds, activityDate, orderActivity, cutOffDay1,
        cutOffTimeHH1, cutOffTimeMM1, cutOffDay2, cutOffTimeHH2, cutOffTimeMM2);
  }

  public ReportOrderDao getReportOrderDao() {
    return reportOrderDao;
  }

  public void setReportOrderDao(ReportOrderDao reportOrderDao) {
    this.reportOrderDao = reportOrderDao;
  }

  public CategoryService getCategoryService() {
    return categoryService;
  }

  public void setCategoryService(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @Override
  public List<CODConfirmationDto> findCODUnConfirmedOrderReport(Date startDate, Date endDate) {
    getReportOrderDao().findCODUnConfirmedOrderReport(startDate, endDate);
    return null;
  }

}
