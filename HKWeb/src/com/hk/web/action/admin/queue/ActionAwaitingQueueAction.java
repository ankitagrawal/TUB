package com.hk.web.action.admin.queue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.OrderSearchCriteria;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.analytics.Reason;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderLifeCycleActivity;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.domain.queue.Bucket;
import com.hk.domain.user.User;
import com.hk.impl.service.queue.BucketService;
import com.hk.manager.OrderManager;
import com.hk.pact.dao.OrderStatusDao;
import com.hk.pact.dao.catalog.category.CategoryDao;
import com.hk.pact.dao.payment.PaymentModeDao;
import com.hk.pact.dao.payment.PaymentStatusDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.accounting.InvoiceService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.pact.service.shippingOrder.ShippingOrderLifecycleService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.pact.service.splitter.ShippingOrderSplitter;
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
    @Autowired
    ShippingOrderLifecycleService shippingOrderLifecycleService;
    @Autowired
    BucketService bucketService;

    @Autowired
    ShippingOrderSplitter shippingOrderSplitter;
    
    private Long orderId;
    private Long shippingOrderId;
    private Long storeId;
    private String gatewayOrderId;
    private Date startDate;
    private Date endDate;
    private List<OrderStatus> orderStatuses = new ArrayList<OrderStatus>();
    private List<ShippingOrderStatus> shippingOrderStatuses = new ArrayList<ShippingOrderStatus>();
    private List<ShippingOrderLifeCycleActivity> shippingOrderLifecycleActivities = new ArrayList<ShippingOrderLifeCycleActivity>();
    private List<Reason> reasons = new ArrayList<Reason>();
    private List<PaymentMode> paymentModes = new ArrayList<PaymentMode>();
    private List<PaymentStatus> paymentStatuses = new ArrayList<PaymentStatus>();
    private List<String> basketCategories = new ArrayList<String>();
    private List<String> categories = new ArrayList<String>();
    private Integer defaultPerPage = 40;
    private String codConfirmationTime;
    private Long unsplitOrderCount;

    private boolean b2bOrder = false;
    private boolean sortByPaymentDate = true;
    private boolean sortByLastEscDate = false;
    private boolean sortByScore = false;
    private boolean sortByDispatchDate = true;
    private Boolean dropShip = null;
    private Boolean containsJit = null;
    private int codCallStatus;

    Map<String, Object> bucketParameters = new HashMap<String, Object>();
    List<Bucket> buckets = new ArrayList<Bucket>();

    @DontValidate
    @DefaultHandler
    @Secure(hasAnyPermissions = {PermissionConstants.VIEW_ACTION_QUEUE}, authActionBean = AdminPermissionAction.class)
    public Resolution pre() {
        User user = getPrincipalUser();
        if (user != null) {
            buckets = user.getBuckets();
            if (buckets != null && !buckets.isEmpty()) {
                bucketParameters = bucketService.getParamMap(user.getBuckets());
            }
        }
        return new ForwardResolution(ActionAwaitingQueueAction.class, "search").addParameters(bucketParameters);
    }

    @Secure(hasAnyPermissions = {PermissionConstants.VIEW_ACTION_QUEUE}, authActionBean = AdminPermissionAction.class)
    public Resolution search() {
        Long startTime = (new Date()).getTime();
        OrderSearchCriteria orderSearchCriteria = getOrderSearchCriteria();
        orderPage = orderService.searchOrders(orderSearchCriteria, getPageNo(), getPerPage());
        if (orderPage != null) {
            orderList = orderPage.getList();
        }
//        ShippingOrderSearchCriteria shippingOrderSearchCriteria = getShippingOrderSearchCriteria();
//        orderPage = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria, false, getPageNo(), getPerPage());
        logger.debug("Time to get list = " + ((new Date()).getTime() - startTime));
        return new ForwardResolution("/pages/admin/actionAwaitingQueue.jsp");
    }

    private OrderSearchCriteria getOrderSearchCriteria() {
        OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();
        orderSearchCriteria.setOrderId(orderId).setGatewayOrderId(gatewayOrderId).setStoreId(storeId).setSortByUpdateDate(false);
        orderSearchCriteria.setSortByPaymentDate(sortByPaymentDate).setSortByDispatchDate(sortByDispatchDate).setSortByScore(sortByScore);
//                .setSortByLastEscDate(sortByLastEscDate);

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

        List<ShippingOrderLifeCycleActivity> shippingOrderActivityList = new ArrayList<ShippingOrderLifeCycleActivity>();
        for (ShippingOrderLifeCycleActivity shippingOrderActivity : shippingOrderLifecycleActivities) {
            if (shippingOrderActivity != null) {
                shippingOrderActivityList.add(shippingOrderActivity);
            }
        }
        /*
        if (shippingOrderActivityList.size() == 0) {
			shippingOrderActivityList = shippingOrderLifecycleService.getOrderActivities(EnumShippingOrderLifecycleActivity.getActivitiesForActionQueue());
		}
		*/
        orderSearchCriteria.setSOLifecycleActivityList(shippingOrderActivityList);
        Set<Reason> reasonList = new HashSet<Reason>();
        for (Reason reason : reasons) {
            if (reason != null) {
                reasonList.add(reason);
            }
        }
        orderSearchCriteria.setReasonList(reasonList);

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

        if (startDate != null) {
            orderSearchCriteria.setPaymentStartDate(startDate);
        }
        if (endDate != null) {
            orderSearchCriteria.setPaymentEndDate(endDate);
        }

//        Set<Category> categoryList = new HashSet<Category>();
//        categoryList.addAll(categoryDao.getPrimaryCategories());
//        orderSearchCriteria.setCategories(categoryList);

        if (dropShip != null) {
            orderSearchCriteria.setDropShip(dropShip);
        }
        if (containsJit != null) {
            orderSearchCriteria.setContainsJit(containsJit);
        }
        if (b2bOrder) {
            orderSearchCriteria.setB2BOrder(b2bOrder);
        }
        Set<Category> basketCategoryList = new HashSet<Category>();
        for (String category : basketCategories) {
            if (category != null) {
                basketCategoryList.add((Category) categoryDao.getCategoryByName(category));
            }
        }

        if (codCallStatus != 0) {
            orderSearchCriteria.setUserCodCallStatus(codCallStatus);
        }



//        if (basketCategoryList.size() == 0) {
//            basketCategoryList.addAll(categoryDao.getPrimaryCategories());
//        }
        orderSearchCriteria.setShippingOrderCategories(basketCategoryList);
        return orderSearchCriteria;
    }

    private ShippingOrderSearchCriteria getShippingOrderSearchCriteria() {
        ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
        shippingOrderSearchCriteria.setBaseOrderId(orderId).setOrderId(shippingOrderId).setStoreId(storeId).setSortByUpdateDate(false);
        shippingOrderSearchCriteria.setSortByPaymentDate(sortByPaymentDate).setSortByDispatchDate(sortByDispatchDate).setSortByScore(sortByScore);

        List<ShippingOrderStatus> shippingOrderStatusList = new ArrayList<ShippingOrderStatus>();
        shippingOrderStatusList.addAll(shippingOrderStatuses);
        if (shippingOrderStatusList.isEmpty())
            shippingOrderStatusList.addAll(shippingOrderStatusService.getOrderStatuses(EnumShippingOrderStatus.getStatusForActionQueue()));

        Set<Category> basketCategoryList = new HashSet<Category>();
        basketCategoryList.addAll(categoryDao.getCategoryByNames(basketCategories));
        shippingOrderSearchCriteria.setShippingOrderCategories(basketCategoryList);

        shippingOrderSearchCriteria.setShippingOrderStatusList(shippingOrderStatusList);
        shippingOrderSearchCriteria.setShippingOrderLifeCycleActivities(shippingOrderLifecycleActivities);
        shippingOrderSearchCriteria.setReasonList(reasons);

        if (!paymentModes.isEmpty()) {
            shippingOrderSearchCriteria.setPaymentModes(paymentModes);
        }
        if (!paymentStatuses.isEmpty()) {
            shippingOrderSearchCriteria.setPaymentStatuses(paymentStatuses);
        }
        if (startDate != null && endDate != null) {
            shippingOrderSearchCriteria.setPaymentStartDate(startDate).setPaymentEndDate(endDate);
        }
        if (dropShip != null) {
            shippingOrderSearchCriteria.setDropShip(dropShip);
        }
        if (containsJit != null) {
            shippingOrderSearchCriteria.setContainsJit(containsJit);
        }
        if (b2bOrder) {
            shippingOrderSearchCriteria.setB2bOrder(b2bOrder);
        }
        if (codCallStatus != 0) {
            shippingOrderSearchCriteria.setUserCodCallStatus(codCallStatus);
        }

        return shippingOrderSearchCriteria;
    }

    @Secure(hasAnyPermissions = {PermissionConstants.UPDATE_ACTION_QUEUE}, authActionBean = AdminPermissionAction.class)
    public Resolution escalate() {
        if (!shippingOrderList.isEmpty()) {
            for (ShippingOrder shippingOrder : shippingOrderList) {
            	shippingOrderSplitter.manualEscalateShippingOrder(shippingOrder);
            }
        } else {
            addRedirectAlertMessage(new SimpleMessage("Please select at least one order to be escalated"));
        }
        return new RedirectResolution(ActionAwaitingQueueAction.class);
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

    public List<ShippingOrderLifeCycleActivity> getShippingOrderLifecycleActivities() {
        return shippingOrderLifecycleActivities;
    }

    public void setShippingOrderLifecycleActivities(List<ShippingOrderLifeCycleActivity> shippingOrderLifecycleActivities) {
        this.shippingOrderLifecycleActivities = shippingOrderLifecycleActivities;
    }

    public List<Reason> getReasons() {
        return reasons;
    }

    public void setReasons(List<Reason> reasons) {
        this.reasons = reasons;
    }

    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("startDate");
        params.add("endDate");
        params.add("storeId");
        params.add("sortByPaymentDate");
//        params.add("sortByLastEscDate");
        params.add("sortByScore");
        params.add("sortByDispatchDate");
        params.add("dropShip");
        params.add("containsJit");
        params.add("b2bOrder");
        params.add("buckets");

        params.add("bucketParameters");

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
/*
        int ctr3 = 0;
        for (String category : categories) {
            if (category != null) {
                params.add("categories[" + ctr3 + "]");
            }
            ctr3++;
        }
*/
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
        int ctr7 = 0;
        for (ShippingOrderLifeCycleActivity SOLifecycleActivity : shippingOrderLifecycleActivities) {
            if (SOLifecycleActivity != null) {
                params.add("shippingOrderLifecycleActivities[" + ctr7 + "]");
            }
            ctr7++;
        }
        int ctr8 = 0;
        for (Reason reason : reasons) {
            if (reason != null) {
                params.add("reasons[" + ctr8 + "]");
            }
            ctr8++;
        }

        int ctr9 = 0;
        for (Bucket bucket : buckets) {
            if (bucket != null) {
                params.add("buckets[" + ctr9 + "]");
            }
            ctr9++;
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

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public boolean getSortByPaymentDate() {
        return sortByPaymentDate;
    }

    public void setSortByPaymentDate(boolean sortByPaymentDate) {
        this.sortByPaymentDate = sortByPaymentDate;
    }

    public boolean getSortByScore() {
        return sortByScore;
    }

    public void setSortByScore(boolean sortByScore) {
        this.sortByScore = sortByScore;
    }

    public Boolean isDropShip() {
        return dropShip;
    }

    public Boolean getDropShip() {
        return dropShip;
    }

    public void setDropShip(Boolean dropShip) {
        this.dropShip = dropShip;
    }

    public Boolean isContainsJit() {
        return containsJit;
    }

    public Boolean getContainsJit() {
        return containsJit;
    }

    public void setContainsJit(Boolean containsJit) {
        this.containsJit = containsJit;
    }

    public boolean isSortByDispatchDate() {
        return sortByDispatchDate;
    }

    public void setSortByDispatchDate(boolean sortByDispatchDate) {
        this.sortByDispatchDate = sortByDispatchDate;
    }

    public boolean isSortByLastEscDate() {
        return sortByLastEscDate;
    }

    public void setSortByLastEscDate(boolean sortByLastEscDate) {
        this.sortByLastEscDate = sortByLastEscDate;
    }

    public Map<String, Object> getBucketParameters() {
        return bucketParameters;
    }

    public void setBucketParameters(Map<String, Object> bucketParameters) {
        this.bucketParameters = bucketParameters;
    }

    public List<Bucket> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<Bucket> buckets) {
        this.buckets = buckets;
    }

    public boolean isB2bOrder() {
        return b2bOrder;
    }

    public void setB2bOrder(boolean b2bOrder) {
        this.b2bOrder = b2bOrder;
    }

    public int getCodCallStatus() {
        return codCallStatus;
    }

    public void setCodCallStatus(int codCallStatus) {
        this.codCallStatus = codCallStatus;
    }

	/**
	 * @return the shippingOrderSplitter
	 */
	public ShippingOrderSplitter getShippingOrderSplitter() {
		return shippingOrderSplitter;
	}

	/**
	 * @param shippingOrderSplitter the shippingOrderSplitter to set
	 */
	public void setShippingOrderSplitter(ShippingOrderSplitter shippingOrderSplitter) {
		this.shippingOrderSplitter = shippingOrderSplitter;
	}
}
