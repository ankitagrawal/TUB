package com.hk.web.action.admin.queue;

import java.util.*;

import com.hk.admin.pact.service.accounting.SeekInvoiceNumService;
import com.hk.helper.InvoiceNumHelper;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.web.action.error.AdminPermissionAction;

@Component
public class ServiceQueueAction extends BasePaginatedAction {
    @Autowired
    private ShippingOrderStatusService shippingOrderStatusService;
    @Autowired
    private ShippingOrderService       shippingOrderService;
    @Autowired
    private AdminShippingOrderService  adminShippingOrderService;
		@Autowired
    private SeekInvoiceNumService      seekInvoiceNumService;


    List<Order>                        orderList         = new ArrayList<Order>();
    private Long                       orderId;
    private String                     gatewayOrderId;
    private Date                       startDate;
    private Date                       endDate;
    private Integer defaultPerPage = 20;
    Page                shippingOrderPage;

    List<ShippingOrder>                shippingOrderList = new ArrayList<ShippingOrder>();

    List<LineItem>                     lineItems         = new ArrayList<LineItem>();

    @DontValidate
    @DefaultHandler
    @Secure(hasAnyPermissions = { PermissionConstants.VIEW_SERVICE_QUEUE }, authActionBean = AdminPermissionAction.class)
    public Resolution pre() {
        return searchOrders();
    }

    public Resolution searchOrders() {

        ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
        shippingOrderSearchCriteria.setShippingOrderStatusList(getShippingOrderStatusService().getOrderStatuses(EnumShippingOrderStatus.getStatusForProcessingQueue()));
        shippingOrderSearchCriteria.setBaseOrderId(orderId).setBaseGatewayOrderId(gatewayOrderId);
        shippingOrderSearchCriteria.setServiceOrder(true);
        shippingOrderSearchCriteria.setPaymentStartDate(startDate);
        shippingOrderSearchCriteria.setPaymentEndDate(endDate);
        shippingOrderPage = getShippingOrderService().searchShippingOrders(shippingOrderSearchCriteria, false, getPageNo(), getPerPage());
        if (shippingOrderPage != null) {
            shippingOrderList = shippingOrderPage.getList();
        }

        return new ForwardResolution("/pages/admin/serviceQueue.jsp");
    }

    @Secure(hasAnyPermissions = { PermissionConstants.UPDATE_SERVICE_QUEUE }, authActionBean = AdminPermissionAction.class)
    public Resolution moveToActionAwaiting() {
        for (ShippingOrder shippingOrder : shippingOrderList) {
            getAdminShippingOrderService().moveShippingOrderBackToActionQueue(shippingOrder);
        }
        addRedirectAlertMessage(new SimpleMessage("Orders have been moved back to Action Awaiting"));
        return new RedirectResolution(ServiceQueueAction.class);
    }

    @Secure(hasAnyPermissions = { PermissionConstants.UPDATE_SERVICE_QUEUE }, authActionBean = AdminPermissionAction.class)
    public Resolution markShippingOrdersAsDelivered() {
        if (shippingOrderList != null && !shippingOrderList.isEmpty()) {
            for (ShippingOrder shippingOrder : shippingOrderList) {
	            String invoiceType = InvoiceNumHelper.getInvoiceType(shippingOrder.isServiceOrder(), shippingOrder.getBaseOrder().isB2bOrder());
              shippingOrder.setAccountingInvoiceNumber(seekInvoiceNumService.getInvoiceNum(invoiceType, shippingOrder.getWarehouse()));
              getAdminShippingOrderService().markShippingOrderAsDelivered(shippingOrder);
            }
            addRedirectAlertMessage(new SimpleMessage("Orders have been marked as delieverd"));
        } else {
            addRedirectAlertMessage(new SimpleMessage("Please select at least one order to be marked as delieverd"));
        }
        return new RedirectResolution(ServiceQueueAction.class);
    }

    public int getPerPageDefault() {
        return defaultPerPage;
    }

    public int getPageCount() {
        return shippingOrderPage == null ? 0 : shippingOrderPage.getTotalPages();
    }

    public int getResultCount() {
        return shippingOrderPage == null ? 0 : shippingOrderPage.getTotalResults();
    }

    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("gatewayOrderId");
        params.add("orderId");
        params.add("startDate");
        params.add("endDate");
        return params;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public Page getShippingOrderPage() {
        return shippingOrderPage;
    }

    public void setShippingOrderPage(Page shippingOrderPage) {
        this.shippingOrderPage = shippingOrderPage;
    }

    public List<ShippingOrder> getShippingOrderList() {
        return shippingOrderList;
    }

    public void setShippingOrderList(List<ShippingOrder> shippingOrderList) {
        this.shippingOrderList = shippingOrderList;
    }

    public String getGatewayOrderId() {
        return gatewayOrderId;
    }

    public void setGatewayOrderId(String gatewayOrderId) {
        this.gatewayOrderId = gatewayOrderId;
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

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public ShippingOrderStatusService getShippingOrderStatusService() {
        return shippingOrderStatusService;
    }

    public void setShippingOrderStatusService(ShippingOrderStatusService shippingOrderStatusService) {
        this.shippingOrderStatusService = shippingOrderStatusService;
    }

    public ShippingOrderService getShippingOrderService() {
        return shippingOrderService;
    }

    public void setShippingOrderService(ShippingOrderService shippingOrderService) {
        this.shippingOrderService = shippingOrderService;
    }

    public AdminShippingOrderService getAdminShippingOrderService() {
        return adminShippingOrderService;
    }

    public void setAdminShippingOrderService(AdminShippingOrderService adminShippingOrderService) {
        this.adminShippingOrderService = adminShippingOrderService;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }
}
