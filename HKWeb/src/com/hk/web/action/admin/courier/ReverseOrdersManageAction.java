package com.hk.web.action.admin.courier;

import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.pact.service.UserService;
import com.hk.util.CustomDateTypeConvertor;
import net.sourceforge.stripes.action.*;
import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.akube.framework.gson.JsonUtils;

import com.hk.constants.inventory.EnumReconciliationStatus;
import com.hk.constants.courier.EnumPickupStatus;
import com.hk.constants.core.PermissionConstants;

import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.domain.reverseOrder.ReverseLineItem;
import com.hk.domain.courier.Courier;
import com.hk.domain.order.ShippingOrder;
import com.hk.admin.pact.service.reverseOrder.ReverseOrderService;
import com.hk.admin.pact.service.shippingOrder.ReplacementOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.util.XslGenerator;
import com.hk.web.action.error.AdminPermissionAction;
import com.hk.web.HealthkartResponse;
import net.sourceforge.stripes.validation.Validate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Dec 5, 2012
 * Time: 11:48:52 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ReverseOrdersManageAction extends BasePaginatedAction {

    private List<ReverseOrder> orderRequestsList;
    private Long orderRequestId;
    Page orderRequestsPage;
    private Integer defaultPerPage = 30;

    private Long pickupStatusId;
    private Long reconciliationStatusId;
    private String shippingOrderId;
    private String advice;
    private Long courierId;
    private String trackingNo;
    private String confirmationNo;
    private File xlsFile;
    private Date endDate;
    private Date startDate;
    private Long warehouseId;

    @Autowired
    ReverseOrderService reverseOrderService;

    @Autowired
    ShippingOrderService shippingOrderService;

    @Autowired
    XslGenerator xslGenerator;

    @Autowired
    ReplacementOrderService replacementOrderService;
    @Autowired
    UserService userService;

    @DefaultHandler
    @Secure(hasAnyPermissions = {PermissionConstants.MANAGE_REVERSE_ORDER}, authActionBean = AdminPermissionAction.class)
    public Resolution pre() {
        if (warehouseId == null) {
            if (userService.getWarehouseForLoggedInUser() != null) {
                warehouseId = userService.getWarehouseForLoggedInUser().getId();
            }
        }
        orderRequestsPage = reverseOrderService.getPickupRequestsByStatuses(shippingOrderId, pickupStatusId, reconciliationStatusId, courierId, warehouseId, getPageNo(), getPerPage(), startDate, endDate);
        orderRequestsList = orderRequestsPage.getList();
        return new ForwardResolution("/pages/admin/reverseOrderList.jsp");
    }

    public Resolution searchUnscheduled() {
        orderRequestsPage = reverseOrderService.getReverseOrderWithNoPickupSchedule(getPageNo(), getPerPage());
        orderRequestsList = orderRequestsPage.getList();
        return new ForwardResolution("/pages/admin/reverseOrderList.jsp");
    }

    @JsonHandler
    @Secure(hasAnyPermissions = {PermissionConstants.MARK_PICKED}, authActionBean = AdminPermissionAction.class)
    public Resolution markPicked() {
        if (orderRequestId != null) {
            ReverseOrder reverseOrder = reverseOrderService.getReverseOrderById(orderRequestId);
            reverseOrder.getCourierPickupDetail().setPickupStatus(EnumPickupStatus.CLOSE.asPickupStatus());
            reverseOrderService.save(reverseOrder);
        }
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Order marked picked");//, data);
        return new JsonResolution(healthkartResponse);

        //return new RedirectResolution(ReverseOrdersManageAction.class).addParameter("shippingOrderId", shippingOrderId);
    }

    @JsonHandler
    @Secure(hasAnyPermissions = {PermissionConstants.MARK_RECEIVED}, authActionBean = AdminPermissionAction.class)
    public Resolution markReceived() {
        if (orderRequestId != null) {
            ReverseOrder reverseOrder = reverseOrderService.getReverseOrderById(orderRequestId);
            reverseOrder.setReceivedDate(new Date());
            reverseOrderService.save(reverseOrder);
        }
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Order marked received");//, data);
        return new JsonResolution(healthkartResponse);

        //return new RedirectResolution(ReverseOrdersManageAction.class).addParameter("shippingOrderId", shippingOrderId);
    }

    @JsonHandler
    @Secure(hasAnyPermissions = {PermissionConstants.MARK_RECONCILED}, authActionBean = AdminPermissionAction.class)
    public Resolution markReconciled() {
        if (orderRequestId != null) {
            ReverseOrder reverseOrder = reverseOrderService.getReverseOrderById(orderRequestId);
            reverseOrder.setReconciliationStatus(EnumReconciliationStatus.DONE.asReconciliationStatus());
            reverseOrderService.save(reverseOrder);
        }
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Order marked reconciled");//, data);
        return new JsonResolution(healthkartResponse);

        //return new RedirectResolution(ReverseOrdersManageAction.class).addParameter("shippingOrderId", shippingOrderId);
    }

    public Resolution reschedulePickup() {
        if (orderRequestId != null) {
            return new RedirectResolution(ReversePickupCourierAction.class).addParameter("reverseOrderId", orderRequestId);
        } else {
            return new RedirectResolution(ReverseOrdersManageAction.class).addParameter("shippingOrderId", shippingOrderId);
        }
    }

    @Secure(hasAnyPermissions = {PermissionConstants.ADVICE_PROPOSED}, authActionBean = AdminPermissionAction.class)
    public Resolution adviceProposed() {
        if (orderRequestId != null) {
            ReverseOrder reverseOrder = reverseOrderService.getReverseOrderById(orderRequestId);
            reverseOrder.setActionProposed(advice);
            reverseOrderService.save(reverseOrder);
        }

        return new RedirectResolution(ReverseOrdersManageAction.class).addParameter("shippingOrderId", shippingOrderId);
    }

    @Secure(hasAnyPermissions = {PermissionConstants.EDIT_AWB_NO}, authActionBean = AdminPermissionAction.class)
    public Resolution editTrack() {
        if (orderRequestId != null) {
            ReverseOrder reverseOrder = reverseOrderService.getReverseOrderById(orderRequestId);
            reverseOrder.getCourierPickupDetail().setTrackingNo(trackingNo);
            reverseOrderService.save(reverseOrder);
        }
        return new RedirectResolution(ReverseOrdersManageAction.class).addParameter("shippingOrderId", shippingOrderId);
    }

    @Secure(hasAnyPermissions = {PermissionConstants.EDIT_BOOKING_NO}, authActionBean = AdminPermissionAction.class)
    public Resolution editConfirmationNo() {
        if (orderRequestId != null) {
            ReverseOrder reverseOrder = reverseOrderService.getReverseOrderById(orderRequestId);
            reverseOrder.getCourierPickupDetail().setPickupConfirmationNo(confirmationNo);
            reverseOrderService.save(reverseOrder);
        }
        return new RedirectResolution(ReverseOrdersManageAction.class).addParameter("shippingOrderId", shippingOrderId);
    }

    public Resolution cancelReverseOrder() {
        if (orderRequestId != null) {
            ReverseOrder reverseOrder = reverseOrderService.getReverseOrderById(orderRequestId);
            if (shippingOrderService.shippingOrderHasReplacementOrder(reverseOrder.getShippingOrder())) {
                addRedirectAlertMessage(new SimpleMessage("Cannot cancel this order as a replacement has already been created for it."));
            } else {
                reverseOrderService.deleteReverseOrder(reverseOrder);
            }
        }
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Order cancelled");//, data);
        return new JsonResolution(healthkartResponse);
    }

    @Secure(hasAnyPermissions = {PermissionConstants.GENERATE_EXCEL_FOR_REVERSE_PICKUP}, authActionBean = AdminPermissionAction.class)
    public Resolution generateExcelForReversePickup() {
        orderRequestsList = reverseOrderService.getPickupRequestsForExcel(shippingOrderId, pickupStatusId, reconciliationStatusId, courierId, warehouseId, startDate, endDate);
        xlsFile = xslGenerator.generateExcelForReversePickup(orderRequestsList);
        addRedirectAlertMessage(new SimpleMessage("Download complete"));
        return new HTTPResponseResolution();
    }

    public class HTTPResponseResolution implements Resolution {
        public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
            OutputStream out = null;
            InputStream in = new BufferedInputStream(new FileInputStream(xlsFile));
            res.setContentLength((int) xlsFile.length());
            res.setHeader("Content-Disposition", "attachment; filename=\"" + xlsFile.getName() + "\";");
            out = res.getOutputStream();

            // Copy the contents of the file to the output stream
            byte[] buf = new byte[4096];
            int count = 0;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
        }

    }

    public int getPerPageDefault() {
        return defaultPerPage;
    }

    public int getPageCount() {
        return orderRequestsPage == null ? 0 : orderRequestsPage.getTotalPages();
    }

    public int getResultCount() {
        return orderRequestsPage == null ? 0 : orderRequestsPage.getTotalResults();
    }

    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("pickupStatusId");
        params.add("reconciliationStatusId");
        params.add("shippingOrderId");
        params.add("courierId");
        params.add("warehouseId");
        params.add("startDate");
        params.add("endDate");
        return params;
    }

    public List<ReverseOrder> getOrderRequestsList() {
        return orderRequestsList;
    }

    public void setOrderRequestsList(List<ReverseOrder> orderRequestsList) {
        this.orderRequestsList = orderRequestsList;
    }

    public Long getPickupStatusId() {
        return pickupStatusId;
    }

    public void setPickupStatusId(Long pickupStatusId) {
        this.pickupStatusId = pickupStatusId;
    }

    public Long getReconciliationStatusId() {
        return reconciliationStatusId;
    }

    public void setReconciliationStatusId(Long reconciliationStatusId) {
        this.reconciliationStatusId = reconciliationStatusId;
    }

    public Long getOrderRequestId() {
        return orderRequestId;
    }

    public void setOrderRequestId(Long orderRequestId) {
        this.orderRequestId = orderRequestId;
    }

    public String getShippingOrderId() {
        return shippingOrderId;
    }

    public void setShippingOrderId(String shippingOrderId) {
        this.shippingOrderId = shippingOrderId;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public Long getCourierId() {
        return courierId;
    }

    public void setCourierId(Long courierId) {
        this.courierId = courierId;
    }

    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }

    public String getConfirmationNo() {
        return confirmationNo;
    }

    public void setConfirmationNo(String confirmationNo) {
        this.confirmationNo = confirmationNo;
    }

    public Date getEndDate() {
        return endDate;
    }

    @Validate(converter = CustomDateTypeConvertor.class)
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    @Validate(converter = CustomDateTypeConvertor.class)
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }
}
