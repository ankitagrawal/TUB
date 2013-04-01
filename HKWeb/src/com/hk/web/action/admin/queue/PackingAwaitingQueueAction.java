package com.hk.web.action.admin.queue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hk.domain.analytics.Reason;
import com.hk.pact.dao.catalog.category.CategoryDao;
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

        Set<String> basketCategoryList = new HashSet<String>();
        for (String category : basketCategories) {
            if (category != null) {
                Category basketCategory = (Category) categoryDao.getCategoryByName(category);
                if (basketCategory != null) {
                    basketCategoryList.add(basketCategory.getName());
                }
            }
        }
        shippingOrderSearchCriteria.setShippingOrderCategories(basketCategoryList);
        shippingOrderPage = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria, getPageNo(), getPerPage());

        // orderPage = orderDao.searchPackingAwaitingOrders(null, null, null, null, getPageNo(), getPerPage(),
        // applicableLineItemStatus);
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
        /**
         * commenting this line for now, since due to some bug some orders do not have eclation activity logged, when that is fixed, uncomment this
         */
        //shippingOrderSearchCriteria.setShippingOrderLifeCycleActivities(EnumShippingOrderLifecycleActivity.getActivitiesForPackingQueue());
        shippingOrderSearchCriteria.setActivityStartDate(startDate).setActivityEndDate(endDate);
        //introduced paymentDate as another filter as escalation filter is not working properly, temporary solution
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
            for (ShippingOrder shippingOrder : shippingOrderList) {
                adminShippingOrderService.moveShippingOrderBackToActionQueue(shippingOrder);
            }
            addRedirectAlertMessage(new SimpleMessage("Orders have been moved back to Action Awaiting"));
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

    public void setPaymentStartDate(Date paymentStartDate) {
        this.paymentStartDate = paymentStartDate;
    }

    public Date getPaymentEndDate() {
        return paymentEndDate;
    }

    public void setPaymentEndDate(Date paymentEndDate) {
        this.paymentEndDate = paymentEndDate;
    }
}