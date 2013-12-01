package com.hk.web.action.admin.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;
import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.domain.catalog.category.Category;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.web.action.error.AdminPermissionAction;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.util.CustomDateTypeConvertor;

import java.util.*;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;


/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Dec 6, 2012
 * Time: 10:42:34 PM
 * To change this template use File | Settings | File Templates.
 */


public class ShipmentInstallationAwaitingQueueAction extends BasePaginatedAction {

    private static Logger logger = LoggerFactory.getLogger(ShipmentInstallationAwaitingQueueAction.class);

    Page shippingOrderPage;
    List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>();

    @Autowired
    private ShippingOrderService shippingOrderService;
    @Autowired
    private AdminShippingOrderService adminShippingOrderService;
    @Autowired
    private ShippingOrderStatusService shippingOrderStatusService;
    

    private Long shippingOrderId;
    private Long orderId;
    private String gatewayOrderId;
    private String baseGatewayOrderId;
    private Date startDate;
    private Date endDate;
    private Category category;
    private ShippingOrderStatus shippingOrderStatus;
    private Integer defaultPerPage = 30;


    @DontValidate
    @DefaultHandler
    @Secure(hasAnyPermissions = {PermissionConstants.VIEW_DROP_SHIPPING_QUEUE}, authActionBean = AdminPermissionAction.class)
    public Resolution pre() {
        Long startTime = (new Date()).getTime();
        if (shippingOrderStatus == null) {
            shippingOrderStatus = shippingOrderStatusService.find(EnumShippingOrderStatus.SO_Delivered);
        }

        ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
        shippingOrderSearchCriteria.setShippingOrderStatusList(Arrays.asList(shippingOrderStatus));
        shippingOrderSearchCriteria.setDropShipping(true);
        shippingOrderSearchCriteria.setInstallable(true);
        shippingOrderPage = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria, getPageNo(), getPerPage());
        if (shippingOrderPage != null) {
            shippingOrderList = shippingOrderPage.getList();
        }
        logger.debug("Time to get list = " + ((new Date()).getTime() - startTime));
        return new ForwardResolution("/pages/admin/installationAwaitingQueue.jsp");
    }


    public Resolution searchOrders() {
        ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
        shippingOrderSearchCriteria.setShippingOrderStatusList(shippingOrderStatusService.getOrderStatuses(EnumShippingOrderStatus.getStatusSearchingInInstallationQueue()));
//        shippingOrderStatus = shippingOrderStatusService.find(EnumShippingOrderStatus.SO_Delivered);
//        shippingOrderSearchCriteria.setShippingOrderStatusList(Arrays.asList(shippingOrderStatus));
        shippingOrderSearchCriteria.setOrderId(orderId).setGatewayOrderId(gatewayOrderId);
        shippingOrderSearchCriteria.setDropShipping(true);
        shippingOrderSearchCriteria.setInstallable(true);
        shippingOrderPage = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria, getPageNo(), getPerPage());
         if (shippingOrderPage != null) {
            shippingOrderList = shippingOrderPage.getList();
        }
        return new ForwardResolution("/pages/admin/installationAwaitingQueue.jsp");
    }

    @Secure(hasAnyPermissions = {PermissionConstants.UPDATE_DROP_SHIPPING_QUEUE}, authActionBean = AdminPermissionAction.class)
    public Resolution markShippingOrderAsInstalled() {
        if (shippingOrderList != null && !shippingOrderList.isEmpty()) {
            for (ShippingOrder shippingOrder : shippingOrderList) {
                adminShippingOrderService.markShippingOrderAsInstalled(shippingOrder);
            }
            addRedirectAlertMessage(new SimpleMessage("Orders have been marked as Installed "));
        } else {
            addRedirectAlertMessage(new SimpleMessage("Please select at least one order to be marked as Installed"));
        }
        return new RedirectResolution(ShipmentInstallationAwaitingQueueAction.class);
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
        params.add("orderId");
        params.add("gatewayOrderId");
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
