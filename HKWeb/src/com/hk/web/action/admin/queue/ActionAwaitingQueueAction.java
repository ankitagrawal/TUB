package com.hk.web.action.admin.queue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;


import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.manager.ProductManager;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.dao.OrderStatusDao;
import com.hk.dao.catalog.category.CategoryDao;
import com.hk.dao.payment.PaymentModeDao;
import com.hk.dao.payment.PaymentStatusDao;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.manager.OrderManager;
import com.hk.search.OrderSearchCriteria;
import com.hk.service.InvoiceService;
import com.hk.service.OrderStatusService;
import com.hk.service.PaymentService;
import com.hk.service.order.OrderService;
import com.hk.service.shippingOrder.ShippingOrderService;
import com.hk.service.shippingOrder.ShippingOrderStatusService;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.web.action.error.AdminPermissionAction;

@Component
public class ActionAwaitingQueueAction extends BasePaginatedAction {

  private static Logger logger = LoggerFactory.getLogger(ActionAwaitingQueueAction.class);

  Page orderPage;
  List<Order> orderList = new ArrayList<Order>();

  List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>();

  @Autowired
  OrderManager orderManager;
  @Autowired
  ProductManager productManager;
  @Autowired
  OrderStatusDao orderStatusDao;
  @Autowired
  PaymentModeDao paymentModeDao;
  @Autowired
  PaymentStatusDao paymentStatusDao;
  @Autowired
  CategoryDao categoryDao;
  @Autowired
  InvoiceService invoiceService;
  @Autowired
  OrderStatusService orderStatusService;
  @Autowired
  PaymentService paymentService;
  @Autowired
  OrderService orderService;
  @Autowired
  ShippingOrderService shippingOrderService;
  @Autowired
  ShippingOrderStatusService shippingOrderStatusService;

  private Long orderId;
  private String gatewayOrderId;
  private Date startDate;
  private Date endDate;
  private List<OrderStatus> orderStatuses = new ArrayList<OrderStatus>();
  private List<ShippingOrderStatus> shippingOrderStatuses = new ArrayList<ShippingOrderStatus>();
  private List<PaymentMode> paymentModes = new ArrayList<PaymentMode>();
  private List<PaymentStatus> paymentStatuses = new ArrayList<PaymentStatus>();
  private List<String> basketCategories = new ArrayList<String>();
  private List<String> categories = new ArrayList<String>();
  private Integer defaultPerPage = 25;
  private String codConfirmationTime;
  private Long unsplitOrderCount;


  @DontValidate
  @DefaultHandler
  @Secure(hasAnyPermissions = {PermissionConstants.VIEW_ACTION_QUEUE}, authActionBean = AdminPermissionAction.class)
  public Resolution pre() {
    Long startTime = (new Date()).getTime();

    OrderSearchCriteria orderSearchCriteria = getOrderSearchCriteria();
    orderPage = orderService.searchOrders(orderSearchCriteria, getPageNo(), getPerPage());
    if (orderPage != null) {
      orderList = orderPage.getList();
    }
    setUnplitOrderCount();
    logger.debug("Time to get list = " + ((new Date()).getTime() - startTime));
    return new ForwardResolution("/pages/admin/actionAwaitingQueue.jsp");
  }

  private void setUnplitOrderCount() {
    if (unsplitOrderCount == null) {
      unsplitOrderCount = orderService.getCountOfOrdersWithStatus();
    }
  }

  public Resolution searchUnsplitOrders() {
    orderStatuses.clear();
    orderStatuses.add(orderStatusService.find(EnumOrderStatus.Placed));
    pre();
    orderStatuses.clear();

    return new ForwardResolution("/pages/admin/actionAwaitingQueue.jsp");
  }

  private OrderSearchCriteria getOrderSearchCriteria() {
    OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();
    orderSearchCriteria.setOrderId(orderId).setGatewayOrderId(gatewayOrderId).setSortByUpdateDate(false);

    List<OrderStatus> orderStatusList = new ArrayList<OrderStatus>();
    for (OrderStatus orderStatus : orderStatuses) {
      if (orderStatus != null) {
        orderStatusList.add(orderStatus);
      }
    }
    if (orderStatusList.size() == 0) {
      orderStatusList = orderStatusService.getOrderStatuses(EnumOrderStatus.getStatusForActionQueue());
    }
    orderSearchCriteria.setOrderStatusList(orderStatusList);

    List<ShippingOrderStatus> shippingOrderStatusList = new ArrayList<ShippingOrderStatus>();
    for (ShippingOrderStatus shippingOrderStatus : shippingOrderStatuses) {
      if (shippingOrderStatus != null) {
        shippingOrderStatusList.add(shippingOrderStatus);
      }
    }
    if (shippingOrderStatusList.size() == 0) {
      shippingOrderStatusList = shippingOrderStatusService.getOrderStatuses(EnumShippingOrderStatus.getStatusForActionQueue());
    }
    orderSearchCriteria.setShippingOrderStatusList(shippingOrderStatusList);

    List<PaymentMode> paymentModeList = new ArrayList<PaymentMode>();
    for (PaymentMode paymentMode : paymentModes) {
      if (paymentMode != null) {
        paymentModeList.add(paymentMode);
      }
    }
    if (paymentModeList.size() == 0) {
      paymentModeList = paymentService.listWorkingPaymentModes();
    }

    orderSearchCriteria.setPaymentModes(paymentModeList);

    List<PaymentStatus> paymentStatusList = new ArrayList<PaymentStatus>();
    for (PaymentStatus paymentStatus : paymentStatuses) {
      if (paymentStatus != null) {
        paymentStatusList.add(paymentStatus);
      }
    }
    if (paymentStatusList.size() == 0) {
      paymentStatusList = paymentService.listWorkingPaymentStatuses();
    }

    orderSearchCriteria.setPaymentStatuses(paymentStatusList);

    if(startDate !=null ){
      orderSearchCriteria.setPaymentStartDate(startDate);
    }
    if(endDate !=null){
      orderSearchCriteria.setPaymentEndDate(endDate);
    }

    Set<Category> categoryList = new HashSet<Category>();
    for (String category : categories) {
      if (category != null) {
        categoryList.add((Category) categoryDao.find(category));
      }
    }
    if (categoryList.size() == 0) {
      categoryList.addAll(categoryDao.getPrimaryCategories());
    }

    orderSearchCriteria.setCategories(categoryList);


    logger.debug("basketCategories : " + basketCategories.size());
    Set<String> basketCategoryList = new HashSet<String>();
    for (String category : basketCategories) {
      if (category != null) {
        Category basketCategory = (Category) categoryDao.find(category);
        if (basketCategory != null) {
          basketCategoryList.add(basketCategory.getName());
        }
      }
    }
    logger.debug("basketCategoryList : " + basketCategoryList.size());

    orderSearchCriteria.setShippingOrderCategories(basketCategoryList);
    return orderSearchCriteria;
  }


  @Secure(hasAnyPermissions = {PermissionConstants.UPDATE_ACTION_QUEUE}, authActionBean = AdminPermissionAction.class)
  public Resolution escalate() {

    if (!shippingOrderList.isEmpty()) {
      for (ShippingOrder shippingOrder : shippingOrderList) {
        shippingOrderService.escalateShippingOrderFromActionQueue(shippingOrder, false);
      }
      addRedirectAlertMessage(new SimpleMessage("Orders have been escalated"));
    } else {
      addRedirectAlertMessage(new SimpleMessage("Please select at least one order to be escalated"));
    }

    setUnplitOrderCount();
    return new RedirectResolution(ActionAwaitingQueueAction.class);

    /* User loggedOnUser = null;
if (getPrincipal() != null) {
loggedOnUser = userDao.find(getPrincipal().getId());
}
Courier orderCourier = null;
for (Order order : orderList) {
int escalatedLineItemsOfOrderCount = 0;
String prefixComments = "Partially escalated items:<br/>";
String addedItems = "";
List<LineItem> serviceLineItems = new ArrayList<LineItem>();
for (LineItem lineItem : order.getProductLineItems()) {
  if (lineItem.isSelected()) {
    *//*if (escalatedLineItemsOfOrderCount == 0) {
            try {
              Courier courier = orderManager.getSuggestedCourierService(order);
              orderCourier = courier;
            } catch (Exception e) {
              logger.error("Error while getting suggested courier for order#" + order.getId());
            }
          }
          orderManager.escalateFromActionQueue(lineItem, orderCourier);*//*

          orderManager.escalateFromActionQueue(lineItem);

          escalatedLineItemsOfOrderCount++;
          addedItems += lineItem.getProductVariant().getProduct().getName() + "<br/>";
          if (lineItem.getProductVariant().getProduct().isService()) {
            serviceLineItems.add(lineItem);
          }
        }
      }
      if (serviceLineItems != null && !serviceLineItems.isEmpty()) {
        invoiceService.generateServiceInvoiceForLineItems(order, serviceLineItems);
      }
      *//**
     * Order lifecycle activity logging - Order Escalated to Packing Queue
     *//*
      if (escalatedLineItemsOfOrderCount == order.getProductLineItems().size()) {
        orderManager.logOrderActivity(order, loggedOnUser, orderLifecycleActivityDao.find(EnumOrderLifecycleActivity.EscalatedToProcessingQueue.getId()), null);
      } else if (escalatedLineItemsOfOrderCount > 0 && StringUtils.isNotEmpty(addedItems)) {
        orderManager.logOrderActivity(order, loggedOnUser, orderLifecycleActivityDao.find(EnumOrderLifecycleActivity.EscalatedPartiallyToProcessingQueue.getId()), prefixComments + addedItems);
      }
    }
    addRedirectAlertMessage(new SimpleMessage("Orders have been escalated"));
    return new RedirectResolution(ActionAwaitingQueueAction.class);*/

  }

  public int getPerPageDefault() {
    return defaultPerPage;
  }

  public int getPageCount() {
    return orderPage == null ? 0 : orderPage.getTotalPages();
  }

  public int getResultCount() {
    return orderPage == null ? 0 : orderPage.getTotalResults();
  }

  public Integer getDefaultPerPage() {
    return defaultPerPage;
  }

  public void setDefaultPerPage(Integer defaultPerPage) {
    this.defaultPerPage = defaultPerPage;
  }

  public List<Order> getOrderList() {
    return orderList;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public String getGatewayOrderId() {
    return gatewayOrderId;
  }

  public void setGatewayOrderId(String gatewayOrderId) {
    this.gatewayOrderId = gatewayOrderId;
  }

  public void setOrderList(List<Order> orderList) {
    this.orderList = orderList;
  }

  public Date getStartDate() {
    return startDate;
  }

  @Validate(converter = CustomDateTypeConvertor.class)
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  @Validate(converter = CustomDateTypeConvertor.class)
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public List<PaymentMode> getPaymentModes() {
    return paymentModes;
  }

  public void setPaymentModes(List<PaymentMode> paymentModes) {
    this.paymentModes = paymentModes;
  }

  public List<PaymentStatus> getPaymentStatuses() {
    return paymentStatuses;
  }

  public void setPaymentStatuses(List<PaymentStatus> paymentStatuses) {
    this.paymentStatuses = paymentStatuses;
  }

  public List<String> getBasketCategories() {
    return basketCategories;
  }

  public void setBasketCategories(List<String> basketCategories) {
    this.basketCategories = basketCategories;
  }

  public List<String> getCategories() {
    return categories;
  }

  public void setCategories(List<String> categories) {
    this.categories = categories;
  }

  public String getCodConfirmationTime() {
    return codConfirmationTime;
  }

  public void setCodConfirmationTime(String codConfirmationTime) {
    this.codConfirmationTime = codConfirmationTime;
  }

  public List<OrderStatus> getOrderStatuses() {
    return orderStatuses;
  }

  public void setOrderStatuses(List<OrderStatus> orderStatuses) {
    this.orderStatuses = orderStatuses;
  }

  public List<ShippingOrderStatus> getShippingOrderStatuses() {
    return shippingOrderStatuses;
  }

  public void setShippingOrderStatuses(List<ShippingOrderStatus> shippingOrderStatuses) {
    this.shippingOrderStatuses = shippingOrderStatuses;
  }

  public Set<String> getParamSet() {
    HashSet<String> params = new HashSet<String>();
    params.add("startDate");
    params.add("endDate");
    // params.add("orderLifecycleActivity");
    //params.add("shippingOrderStatus");

    /*params.add("paymentModes");
    params.add("paymentStatuses");
    params.add("categories");*/

    int ctr = 0;
    for (PaymentMode paymentMode : paymentModes) {
      if (paymentMode != null) {
        params.add("paymentModes[" + ctr + "]");
      }
      ctr++;
    }
    int ctr2 = 0;
    for (PaymentStatus paymentStatus : paymentStatuses) {
      if (paymentStatus != null) {
        params.add("paymentStatuses[" + ctr2 + "]");
      }
      ctr2++;
    }
    int ctr3 = 0;
    for (String category : categories) {
      if (category != null) {
        params.add("categories[" + ctr3 + "]");
      }
      ctr3++;
    }
    int ctr4 = 0;
    for (String category : basketCategories) {
      if (category != null) {
        params.add("basketCategories[" + ctr4 + "]");
      }
      ctr4++;
    }
    int ctr5 = 0;
    for (OrderStatus orderStatus : orderStatuses) {
      if (orderStatus != null) {
        params.add("orderStatuses[" + ctr5 + "]");
      }
      ctr5++;
    }
    int ctr6 = 0;
    for (ShippingOrderStatus shippingOrderStatus : shippingOrderStatuses) {
      if (shippingOrderStatus != null) {
        params.add("shippingOrderStatuses[" + ctr6 + "]");
      }
      ctr6++;
    }

    return params;
  }

  public List<ShippingOrder> getShippingOrderList() {
    return shippingOrderList;
  }

  public void setShippingOrderList(List<ShippingOrder> shippingOrderList) {
    this.shippingOrderList = shippingOrderList;
  }

  public Long getUnsplitOrderCount() {
    return unsplitOrderCount;
  }

  public void setUnsplitOrderCount(Long unsplitOrderCount) {
    this.unsplitOrderCount = unsplitOrderCount;
  }
}

