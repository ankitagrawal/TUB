package com.hk.web.action.admin.reversePickup;

import com.akube.framework.dao.Page;

import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.service.courier.reversePickup.ReversePickupService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.reversePickup.EnumReverseAction;
import com.hk.constants.reversePickup.EnumReversePickupStatus;

import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.reversePickupOrder.ReversePickupOrder;
import com.hk.domain.reversePickupOrder.ReversePickupStatus;
import com.hk.domain.reversePickupOrder.RpLineItem;

import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 7/22/13
 * Time: 3:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReversePickupListAction extends BasePaginatedAction {

    private List<ReversePickupOrder> reversePickupOrderList;
    private ReversePickupOrder reversePickupOrder;
    private Page reversePickupPage;
    private Integer defaultPerPage = 30;
    private Date startDate;
    private Date endDate;
    private ShippingOrder shippingOrder;
    private String trackingNumber;
    private Long customerActionStatus;
    private String courierName;
    private RpLineItem rpLineitem;
    private String errorMessage = "";
    private ReversePickupStatus reversePickupStatus;
    private String reversePickupId;
    private String bookingReferenceNumber;


    @Autowired
    ReversePickupService reversePickupService;

    @Autowired
    ShippingOrderService shippingOrderService;

    @DefaultHandler
    public Resolution pre() {
        reversePickupPage = reversePickupService.getReversePickRequest(shippingOrder, reversePickupId, startDate, endDate, customerActionStatus, EnumReversePickupStatus.getHealthKartManagedRPStatus(), courierName, getPageNo(), getPerPage());
        reversePickupOrderList = reversePickupPage.getList();
        return new ForwardResolution("/pages/admin/reversePickup/reversePickupList.jsp");
    }

    public Resolution search() {
        reversePickupPage = reversePickupService.getReversePickRequest(shippingOrder, reversePickupId, startDate, endDate, customerActionStatus, Arrays.asList(reversePickupStatus), courierName, getPageNo(), getPerPage());
        reversePickupOrderList = reversePickupPage.getList();
        return new ForwardResolution("/pages/admin/reversePickup/reversePickupList.jsp");
    }

    public Resolution deleteReversePickUp() {
        if (reversePickupOrder != null) {
            reversePickupService.deleteReversePickupOrder(reversePickupOrder);
            String comments = "RPU Id: " + reversePickupOrder.getId() + "was successfully deleted";
            shippingOrderService.logShippingOrderActivity(reversePickupOrder.getShippingOrder(), EnumShippingOrderLifecycleActivity.RPU_STATUS_CHANGED, null, comments);
        }
        return new ForwardResolution("/pages/admin/reversePickup/reversePickupList.jsp");
    }


    public Resolution editTrackingNumber() {
        if (trackingNumber != null) {
            if (reversePickupOrder.getCourierName() == null || reversePickupOrder.getPickupTime() == null) {
                errorMessage = "Edit RP to add Courier Name and Pick time";
            } else {
                reversePickupOrder.setTrackingNumber(trackingNumber.trim());
                reversePickupOrder = reversePickupService.saveReversePickupOrder(reversePickupOrder);
                String comments = "Tracking Number Edited, RPU Status Changed for RPU Id " + reversePickupOrder.getId() + "Courier/Tracking Number " + trackingNumber + reversePickupOrder.getCourierName();
                shippingOrderService.logShippingOrderActivity(reversePickupOrder.getShippingOrder(), EnumShippingOrderLifecycleActivity.RPU_STATUS_CHANGED, null, comments);

            }
        }
        return new RedirectResolution(ReversePickupListAction.class).addParameter("shippingOrder", reversePickupOrder.getShippingOrder().getId())
                .addParameter("errorMessage", errorMessage);
    }

    public Resolution editBookingReferenceNumber() {
        if (bookingReferenceNumber != null) {
            if (reversePickupOrder.getCourierName() == null || reversePickupOrder.getPickupTime() == null) {
                errorMessage = "Edit RP to add Courier Name and Pick time";
            } else {
                reversePickupOrder.setBookingReferenceNumber(bookingReferenceNumber.trim());
                reversePickupOrder.setReversePickupStatus(EnumReversePickupStatus.RPU_Scheduled.asReversePickupStatus());
                reversePickupOrder = reversePickupService.saveReversePickupOrder(reversePickupOrder);
                String comments = "Booking Reference Number Edited, RPU Status Changed for RPU Id " + reversePickupOrder.getId() + "Booking Reference Number " + bookingReferenceNumber;
                shippingOrderService.logShippingOrderActivity(reversePickupOrder.getShippingOrder(), EnumShippingOrderLifecycleActivity.RPU_STATUS_CHANGED, null, comments);
            }
        }
        return new RedirectResolution(ReversePickupListAction.class).addParameter("shippingOrder", reversePickupOrder.getShippingOrder().getId())
                .addParameter("errorMessage", errorMessage);
    }
    @Secure(hasAnyPermissions = {PermissionConstants.APPROVE_REVERSE_PICKUP}, authActionBean = AdminPermissionAction.class)
    public Resolution approveCSAction() {
        if (rpLineitem != null) {
             rpLineitem.setCustomerActionStatus(EnumReverseAction.Approved.getId());
              /*call automatic  refund services here*/
            reversePickupService.saveRpLineItem(rpLineitem);
            ReversePickupOrder localRPOrder = rpLineitem.getReversePickupOrder();
             int counter = localRPOrder.getRpLineItems().size();
               for(RpLineItem rpLineItem : localRPOrder.getRpLineItems()){
                   if( (rpLineItem.getCustomerActionStatus() != null) && (rpLineItem.getCustomerActionStatus().equals(EnumReverseAction.Approved.getId()))){
                       counter--;
                   }
               }
               if(counter == 0){
                   reversePickupOrder.setReversePickupStatus(EnumReversePickupStatus.RPU_APPROVED.asReversePickupStatus());
                   reversePickupOrder = reversePickupService.saveReversePickupOrder(reversePickupOrder);
                   String comments = "All line item in reverse pickup order approved successfully, RPU Status Changed to Approved for RPU Id " + reversePickupOrder.getId();
                   shippingOrderService.logShippingOrderActivity(reversePickupOrder.getShippingOrder(), EnumShippingOrderLifecycleActivity.RPU_STATUS_CHANGED, null, comments);
               }
        } else {
            errorMessage = "Error in Approving";
        }
        return new RedirectResolution(ReversePickupListAction.class).addParameter("shippingOrder", reversePickupOrder.getShippingOrder().getId())
                .addParameter("errorMessage", errorMessage);
    }

    public Resolution markPicked() {
        if (reversePickupOrder != null) {
            if (reversePickupOrder.getCourierName() == null || reversePickupOrder.getPickupTime() == null) {
                errorMessage = "Enter Courier name and Pickup time first";
            } else {
                reversePickupOrder.setReversePickupStatus(EnumReversePickupStatus.RPU_Picked.asReversePickupStatus());
                reversePickupService.saveReversePickupOrder(reversePickupOrder);
                String comments = " RPU Status Changed to Marked Picked for RPU Id " + reversePickupOrder.getId();
                shippingOrderService.logShippingOrderActivity(reversePickupOrder.getShippingOrder(), EnumShippingOrderLifecycleActivity.RPU_STATUS_CHANGED, null, comments);
            }
        }
        return new RedirectResolution(ReversePickupListAction.class).addParameter("shippingOrder", reversePickupOrder.getShippingOrder().getId())
                .addParameter("errorMessage", errorMessage);
    }

    public Resolution rpNotAvailable() {

        reversePickupOrder.setReversePickupStatus(EnumReversePickupStatus.RPU_NOTAVAILABLE.asReversePickupStatus());
        reversePickupOrder = reversePickupService.saveReversePickupOrder(reversePickupOrder);
        String comments = " RPU Status Changed to RPU Not Available for RPU Id " + reversePickupOrder.getId();
        shippingOrderService.logShippingOrderActivity(reversePickupOrder.getShippingOrder(), EnumShippingOrderLifecycleActivity.RPU_STATUS_CHANGED, null, comments);
        return new RedirectResolution(ReversePickupListAction.class).addParameter("shippingOrder", reversePickupOrder.getShippingOrder().getId())
                .addParameter("errorMessage", errorMessage);
    }
    public Resolution editRPToReconcile() {

        reversePickupOrder.setReversePickupStatus(EnumReversePickupStatus.RPU_RECONCILATION.asReversePickupStatus());
        reversePickupOrder = reversePickupService.saveReversePickupOrder(reversePickupOrder);
        String comments = " RPU Status Changed to RPU Reconcile for RPU Id " + reversePickupOrder.getId();
        shippingOrderService.logShippingOrderActivity(reversePickupOrder.getShippingOrder(), EnumShippingOrderLifecycleActivity.RPU_STATUS_CHANGED, null, comments);
        return new RedirectResolution(ReversePickupListAction.class).addParameter("shippingOrder", reversePickupOrder.getShippingOrder().getId())
                .addParameter("errorMessage", errorMessage);
    }

    public Resolution rpCancel() {
        reversePickupOrder.setReversePickupStatus(EnumReversePickupStatus.RPU_CANCEL.asReversePickupStatus());
        reversePickupOrder = reversePickupService.saveReversePickupOrder(reversePickupOrder);
        String comments = " RPU Status Changed to Cancel for RPU Id " + reversePickupOrder.getId();
        shippingOrderService.logShippingOrderActivity(reversePickupOrder.getShippingOrder(), EnumShippingOrderLifecycleActivity.RPU_STATUS_CHANGED, null, comments);
        return new RedirectResolution(ReversePickupListAction.class).
                addParameter("shippingOrder", reversePickupOrder.getShippingOrder().getId())
                .addParameter("errorMessage", errorMessage);
    }

    public Resolution rpClose() {
        reversePickupOrder.setReversePickupStatus(EnumReversePickupStatus.RPU_CLOSED.asReversePickupStatus());
        reversePickupOrder = reversePickupService.saveReversePickupOrder(reversePickupOrder);
        String comments = " RPU Status Changed to Close for RPU Id " + reversePickupOrder.getId();
        shippingOrderService.logShippingOrderActivity(reversePickupOrder.getShippingOrder(), EnumShippingOrderLifecycleActivity.RPU_STATUS_CHANGED, null, comments);
        return new RedirectResolution(ReversePickupListAction.class).addParameter("shippingOrder", reversePickupOrder.getShippingOrder().getId())
                .addParameter("errorMessage", errorMessage);
    }

    public Resolution rpReconcile() {
        reversePickupOrder.setReversePickupStatus(EnumReversePickupStatus.RPU_RECONCILATION.asReversePickupStatus());
        reversePickupOrder = reversePickupService.saveReversePickupOrder(reversePickupOrder);
        String comments = " RPU Status Changed to Reconciled for RPU Id " + reversePickupOrder.getId();
        shippingOrderService.logShippingOrderActivity(reversePickupOrder.getShippingOrder(), EnumShippingOrderLifecycleActivity.RPU_STATUS_CHANGED, null, comments);
        return new RedirectResolution(ReversePickupListAction.class).addParameter("shippingOrder", reversePickupOrder.getShippingOrder().getId())
                .addParameter("errorMessage", errorMessage);
    }

    public int getPerPageDefault() {
        return defaultPerPage;
    }


    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("shippingOrder");
        params.add("startDate");
        params.add("endDate");
        params.add("customerActionStatus");
        params.add("courierName");
        params.add("reversePickupStatus");
        return params;
    }


    public int getPageCount() {
        return reversePickupPage == null ? 0 : reversePickupPage.getTotalPages();
    }

    public int getResultCount() {
        return reversePickupPage == null ? 0 : reversePickupPage.getTotalResults();
    }


    public List<ReversePickupOrder> getReversePickupOrderList() {
        return reversePickupOrderList;
    }

    public void setReversePickupOrderList(List<ReversePickupOrder> reversePickupOrderList) {
        this.reversePickupOrderList = reversePickupOrderList;
    }

    public ReversePickupOrder getReversePickupOrder() {
        return reversePickupOrder;
    }

    public void setReversePickupOrder(ReversePickupOrder reversePickupOrder) {
        this.reversePickupOrder = reversePickupOrder;
    }


    public ShippingOrder getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(ShippingOrder shippingOrder) {
        this.shippingOrder = shippingOrder;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public Long getCustomerActionStatus() {
        return customerActionStatus;
    }

    public void setCustomerActionStatus(Long customerActionStatus) {
        this.customerActionStatus = customerActionStatus;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public RpLineItem getRpLineitem() {
        return rpLineitem;
    }

    public void setRpLineitem(RpLineItem rpLineitem) {
        this.rpLineitem = rpLineitem;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ReversePickupStatus getReversePickupStatus() {
        return reversePickupStatus;
    }

    public void setReversePickupStatus(ReversePickupStatus reversePickupStatus) {
        this.reversePickupStatus = reversePickupStatus;
    }

    public String getReversePickupId() {
        return reversePickupId;
    }

    public void setReversePickupId(String reversePickupId) {
        this.reversePickupId = reversePickupId;
    }

    public String getBookingReferenceNumber() {
        return bookingReferenceNumber;
    }

    public void setBookingReferenceNumber(String bookingReferenceNumber) {
        this.bookingReferenceNumber = bookingReferenceNumber;
    }
}
