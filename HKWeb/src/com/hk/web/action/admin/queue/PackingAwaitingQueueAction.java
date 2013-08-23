package com.hk.web.action.admin.queue;

import java.util.*;

import com.hk.constants.analytics.EnumReason;
import com.hk.constants.shippingOrder.ShippingOrderConstants;
import com.hk.domain.analytics.Reason;
import com.hk.domain.order.ShippingOrderLifecycle;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.pact.dao.catalog.category.CategoryDao;
import com.hk.pact.service.shippingOrder.ShippingOrderLifecycleService;
import com.hk.pact.service.splitter.ShippingOrderProcessor;
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
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.web.action.error.AdminPermissionAction;

@Component
public class PackingAwaitingQueueAction extends BasePaginatedAction {

  private static Logger              logger            = LoggerFactory.getLogger(PackingAwaitingQueueAction.class);

  Page                               shippingOrderPage;
  List<ShippingOrder>                shippingOrderList = new ArrayList<ShippingOrder>();

  @Autowired
  private ShippingOrderService       shippingOrderService;
  @Autowired
  private AdminShippingOrderService  adminShippingOrderService;
  @Autowired
  private ShippingOrderStatusService shippingOrderStatusService;

  @Autowired
  ShippingOrderProcessor             shippingOrderProcessor;

  @Autowired
  ShippingOrderLifecycleService      shippingOrderLifecycleService;

  @Autowired
  CategoryDao categoryDao;

  private List<String> basketCategories = new ArrayList<String>();

  private Long                       shippingOrderId;
  private Long                       baseOrderId;
  private String                     gatewayOrderId;
  private String                     baseGatewayOrderId;
  private Date                       startDate;
  private Date                       endDate;
  private Date                       paymentStartDate;
  private Date                       paymentEndDate;
  private Category                   category;
  private ShippingOrderStatus        shippingOrderStatus;
  private Integer                    defaultPerPage    = 30;

  @DontValidate
  @DefaultHandler
  @Secure(hasAnyPermissions = { PermissionConstants.VIEW_PACKING_QUEUE }, authActionBean = AdminPermissionAction.class)
  public Resolution pre() {
    Long startTime = (new Date()).getTime();
    if (shippingOrderStatus == null) {
      shippingOrderStatus = shippingOrderStatusService.find(EnumShippingOrderStatus.SO_ReadyForProcess);
    }
    ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
    shippingOrderSearchCriteria.setShippingOrderStatusList(Arrays.asList(shippingOrderStatus));
    shippingOrderSearchCriteria.setServiceOrder(false);
    shippingOrderPage = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria, getPageNo(), getPerPage());
    if (shippingOrderPage != null) {
      shippingOrderList = shippingOrderPage.getList();
      logger.debug("Time to get list = " + ((new Date()).getTime() - startTime));
    }
    return new ForwardResolution("/pages/admin/packingAwaitingQueue.jsp");
  }

  public Resolution searchOrders() {
    ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
    shippingOrderSearchCriteria.setOrderId(shippingOrderId).setGatewayOrderId(gatewayOrderId);
    shippingOrderSearchCriteria.setBaseOrderId(baseOrderId).setBaseGatewayOrderId(baseGatewayOrderId);
    if (shippingOrderStatus == null) {
      shippingOrderSearchCriteria.setShippingOrderStatusList(shippingOrderStatusService.getOrderStatuses(EnumShippingOrderStatus.getStatusForProcessingQueue()));
    } else {
      shippingOrderSearchCriteria.setShippingOrderStatusList(Arrays.asList(shippingOrderStatus));
    }
    if(!basketCategories.isEmpty()){
      Set<Category> basketCategoryList = new HashSet<Category>();
      for (String category : basketCategories) {
        if (category != null) {
          Category basketCategory = (Category) categoryDao.getCategoryByName(category);
          basketCategoryList.add(basketCategory);
        }
      }
      shippingOrderSearchCriteria.setShippingOrderCategories(basketCategoryList);
    }
    shippingOrderSearchCriteria.setLastEscStartDate(startDate).setLastEscEndDate(endDate);
    shippingOrderSearchCriteria.setPaymentStartDate(paymentStartDate).setPaymentEndDate(paymentEndDate);

    shippingOrderPage = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria, getPageNo(), getPerPage());

    if (shippingOrderPage != null) {
      shippingOrderList = shippingOrderPage.getList();
    }
    return new ForwardResolution("/pages/admin/packingAwaitingQueue.jsp");
  }

  @Secure(hasAnyPermissions = { PermissionConstants.UPDATE_PACKING_QUEUE }, authActionBean = AdminPermissionAction.class)
  public Resolution moveToActionAwaiting() {
    if (!shippingOrderList.isEmpty()) {
      // Creating acceptable reasons for escalate back
      Set<Long> acceptableReasons = EnumReason.getAcceptableReasonIDsEscalateBack();

      boolean isAutoEscalateBackAllowed;
      List<Long> shippingOrderIdsWithInvalidReason = new ArrayList<Long>();
      List<Long> shippingOrdersWithoutFixedLI = new ArrayList<Long>();

      for (ShippingOrder shippingOrder : shippingOrderList) {
        isAutoEscalateBackAllowed = false;
        if (shippingOrder.getReason() != null) {
          if (acceptableReasons.contains(shippingOrder.getReason().getId())) {
            List<ShippingOrderLifecycle> lifeCycles =
                shippingOrderLifecycleService.getShippingOrderLifecycleBySOAndActivity(shippingOrder.getId(),
                    EnumShippingOrderLifecycleActivity.SO_LineItemCouldNotFixed.getId());
            if (lifeCycles != null && !lifeCycles.isEmpty() ) {
              // then only allow auto escalate back
              isAutoEscalateBackAllowed = true;
            } else {
              shippingOrdersWithoutFixedLI.add(shippingOrder.getId());
              continue;
            }
          }
        } else {
          shippingOrderIdsWithInvalidReason.add(shippingOrder.getId());
        }
        if (isAutoEscalateBackAllowed) {
          adminShippingOrderService.moveShippingOrderBackToActionQueue(shippingOrder, true);
        } else {
          adminShippingOrderService.moveShippingOrderBackToActionQueue(shippingOrder);
        }
      }

      if (shippingOrderIdsWithInvalidReason.size() > 0) {
        addRedirectAlertMessage(new SimpleMessage("No reasons selected for shipping order -> "
            + shippingOrderIdsWithInvalidReason ));
      }
      if (shippingOrdersWithoutFixedLI.size() > 0 ) {
        addRedirectAlertMessage(new SimpleMessage("Unfixed Line items not found for shipping order -> "
            + shippingOrdersWithoutFixedLI));
      }
      if (shippingOrderList.size() != shippingOrderIdsWithInvalidReason.size() + shippingOrdersWithoutFixedLI.size()) {
        addRedirectAlertMessage(new SimpleMessage("Orders with no errors have been moved back to Action" +
            " Awaiting and auto escalated forward if possible."));
      }

    } else {
      addRedirectAlertMessage(new SimpleMessage("Please select at least one order to be moved back to action awaiting"));
    }

    return new RedirectResolution(PackingAwaitingQueueAction.class);
  }


  @Secure(hasAnyPermissions = { PermissionConstants.UPDATE_PACKING_QUEUE }, authActionBean = AdminPermissionAction.class)
  public Resolution reAssignToPackingQueue() {

    if (!shippingOrderList.isEmpty()) {
      for (ShippingOrder shippingOrder : shippingOrderList) {
        adminShippingOrderService.moveShippingOrderBackToPackingQueue(shippingOrder);
      }
      addRedirectAlertMessage(new SimpleMessage("Orders have been re-assigned for processing"));
    } else {
      addRedirectAlertMessage(new SimpleMessage("Please select at least one order to be assigned back for processing"));
    }

    return new RedirectResolution(PackingAwaitingQueueAction.class);
  }

  public List<String> getBasketCategories() {
    return basketCategories;
  }

  public void setBasketCategories(List<String> basketCategories) {
    this.basketCategories = basketCategories;
  }

  public int getPerPageDefault() {
    return defaultPerPage;
  }

  public Integer getDefaultPerPage() {
    return defaultPerPage;
  }

  public void setDefaultPerPage(Integer defaultPerPage) {
    this.defaultPerPage = defaultPerPage;
  }

  public int getPageCount() {
    return shippingOrderPage == null ? 0 : shippingOrderPage.getTotalPages();
  }

  public int getResultCount() {
    return shippingOrderPage == null ? 0 : shippingOrderPage.getTotalResults();
  }

  public Set<String> getParamSet() {
    HashSet<String> params = new HashSet<String>();
    params.add("startDate");
    params.add("endDate");
    params.add("paymentStartDate");
    params.add("paymentEndDate");
    params.add("shippingOrderId");
    params.add("baseOrderId");
    // params.add("gatewayOrderId");
    params.add("baseGatewayOrderId");
    params.add("shippingOrderStatus");
    return params;
  }

  public List<ShippingOrder> getShippingOrderList() {
    return shippingOrderList;
  }

  public void setShippingOrderList(List<ShippingOrder> shippingOrderList) {
    this.shippingOrderList = shippingOrderList;
  }

  public Long getShippingOrderId() {
    return shippingOrderId;
  }

  public void setShippingOrderId(Long shippingOrderId) {
    this.shippingOrderId = shippingOrderId;
  }

  public void setGatewayOrderId(String gatewayOrderId) {
    this.gatewayOrderId = gatewayOrderId;
  }

  public Long getBaseOrderId() {
    return baseOrderId;
  }

  public void setBaseOrderId(Long baseOrderId) {
    this.baseOrderId = baseOrderId;
  }

  public String getBaseGatewayOrderId() {
    return baseGatewayOrderId;
  }

  public void setBaseGatewayOrderId(String baseGatewayOrderId) {
    this.baseGatewayOrderId = baseGatewayOrderId;
  }

  public Date getStartDate() {
    return startDate;
  }

  public ShippingOrderStatus getShippingOrderStatus() {
    return shippingOrderStatus;
  }

  public void setShippingOrderStatus(ShippingOrderStatus shippingOrderStatus) {
    this.shippingOrderStatus = shippingOrderStatus;
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

  public Category getCategory() {
    return category;
  }

  public String getGatewayOrderId() {
    return gatewayOrderId;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public void setShippingOrderService(ShippingOrderService shippingOrderService) {
    this.shippingOrderService = shippingOrderService;
  }

  public void setShippingOrderStatusService(ShippingOrderStatusService shippingOrderStatusService) {
    this.shippingOrderStatusService = shippingOrderStatusService;
  }

  public Date getPaymentStartDate() {
    return paymentStartDate;
  }

  @Validate(converter = CustomDateTypeConvertor.class)
  public void setPaymentStartDate(Date paymentStartDate) {
    this.paymentStartDate = paymentStartDate;
  }

  public Date getPaymentEndDate() {
    return paymentEndDate;
  }

  @Validate(converter = CustomDateTypeConvertor.class)
  public void setPaymentEndDate(Date paymentEndDate) {
    this.paymentEndDate = paymentEndDate;
  }
}