package com.hk.web.action.admin.reversePickup;

import com.akube.framework.stripes.action.BaseAction;

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
import com.hk.util.CustomDateTypeConvertor;
import com.hk.util.TokenUtils;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 7/18/13
 * Time: 6:22 PM
 * To change this template use File | Settings | File Templates.
 */

public class ReversePickupAction extends BaseAction {
    private static Logger logger = LoggerFactory.getLogger(ReversePickupAction.class);

    private ShippingOrder shippingOrder;
    private List<RpLineItem> rpLineItems;
    private List<Integer> checkBoxIndexList;
    private ReversePickupOrder reversePickupOrder;
    private List<ReversePickupOrder> reversePickupOrderList = new ArrayList<ReversePickupOrder>();
    private Map<Long, List<RpLineItem>> rpLineItemsSavedMap = new HashMap<Long, List<RpLineItem>>();
    private Map<Long, List<RpLineItem>> rpLineDisabledMap = new HashMap<Long, List<RpLineItem>>();
    private String trackingNumber;
    private Boolean pendingApprovalEditMode = false;
    private Date pickupDate;
    private Boolean approved;
    private RpLineItem rpLineItem;
    @Autowired
    private ReversePickupService reversePickupService;
    @Autowired
    ReversePickupHelper reversePickupHelper;
    @Autowired
    ShippingOrderService shippingOrderService;


    @DefaultHandler
    public Resolution pre() {
        List<ReversePickupOrder> reversePickupOrdersAlreadyCreatedList = reversePickupService.getReversePickupsForSO(shippingOrder);
        if (reversePickupOrdersAlreadyCreatedList != null && reversePickupOrdersAlreadyCreatedList.size() > 0) {
            /** disable all the lineItems already saved in another RP order of current SO*/
            rpLineDisabledMap = reversePickupHelper.populateAlreadyCreatedRpLineItemsInfo(reversePickupOrdersAlreadyCreatedList);
        }
        return new ForwardResolution("/pages/admin/reversePickup/createReversePickRequest.jsp");

    }


    public Resolution createReversePickUp() {
        List<RpLineItem> selectedRpLineItemList = getSelectedRpLineItemList();

        if (selectedRpLineItemList != null && selectedRpLineItemList.size() > 0) {
            Double amountRequested = reversePickupHelper.calculateReversePickupAccount(selectedRpLineItemList);
            /*Populate Courier Info If Entered*/
            if (reversePickupOrder == null) {
                reversePickupOrder = new ReversePickupOrder();
            }
            ReversePickupStatus reversePickupStatus = reversePickupHelper.getReversePickupStatus(reversePickupOrder.getCourierManagedBy());
            /*Create New Reverse Order*/
            if (reversePickupStatus == null) {
                reversePickupStatus = EnumReversePickupStatus.RPU_Initiated.asReversePickupStatus();
            }
            /*Generate reversePickId */
            String reversePickupId = TokenUtils.generateReversePickupOrderId(shippingOrder);
            reversePickupOrder.setReversePickupId(reversePickupId);
            ReversePickupOrder createdReversePickupOrder = reversePickupHelper.createAndSaveReversePickupOrder(reversePickupOrder, shippingOrder, amountRequested, reversePickupStatus, pickupDate);
            if (createdReversePickupOrder != null) {
                reversePickupService.saveRpLineItems(createdReversePickupOrder, selectedRpLineItemList, EnumReverseAction.Pending_Approval.getId());
                addRedirectAlertMessage(new SimpleMessage("Reverse Order Created : " + createdReversePickupOrder.getReversePickupId()));
                shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Reverse_Pickup_Initiated, null, " Reverse Pickup Id " + reversePickupOrder.getReversePickupId() + " generated");
                return new RedirectResolution(ReversePickupListAction.class).addParameter("shippingOrder", reversePickupOrder.getShippingOrder().getId());
            }
        } else {
            addRedirectAlertMessage(new SimpleMessage("Select products to return to create Reverse Order  " +
                    "If you want to delete RP order. click link on ReversePickup List Screen"));
        }
        return new RedirectResolution(ReversePickupAction.class).addParameter("shippingOrder", shippingOrder.getId());
    }


    /*Deleting old rpLineItems and saving the new one */
    public Resolution saveModifiedReversePickup() {
        List<RpLineItem> oldRpLineItemList = reversePickupOrder.getRpLineItems();
        List<RpLineItem> newSelectedRpLineItemList = getSelectedRpLineItemList();

        if (newSelectedRpLineItemList != null && newSelectedRpLineItemList.size() > 0) {
            try {
                Double modifiedAmountRequested = reversePickupHelper.calculateReversePickupAccount(newSelectedRpLineItemList);
                ReversePickupStatus reversePickupStatus = reversePickupHelper.getReversePickupStatus(reversePickupOrder.getCourierManagedBy());
                ReversePickupOrder savedReversePickupOrder = reversePickupHelper.createAndSaveReversePickupOrder(reversePickupOrder, null, modifiedAmountRequested, reversePickupStatus, pickupDate);
                if (savedReversePickupOrder != null) {
                    reversePickupService.SaveModifiedRpLineItems(oldRpLineItemList, savedReversePickupOrder, newSelectedRpLineItemList);
                }
            } catch (Exception ex) {
                logger.error("Exception :: " + ex.getMessage());
            }
        } else {
            addRedirectAlertMessage(new SimpleMessage("Select products to return to create Reverse Order  " +
                    "If you want to delete RP order. click link on ReversePickup List Screen"));
        }
        return new RedirectResolution(ReversePickupListAction.class).addParameter("shippingOrder", reversePickupOrder.getShippingOrder().getId());
    }

    public Resolution saveApprovedReversePickup() {

        if (rpLineItems != null) {
            List<RpLineItem> modifiedPendingRpLineItems = new ArrayList<RpLineItem>();
            for (RpLineItem rpLineItem : rpLineItems) {
                if (rpLineItem != null) {
                    Long statusId = rpLineItem.getCustomerActionStatus();
                    if (statusId == null || statusId.equals(EnumReverseAction.Pending_Approval.getId())) {
                        modifiedPendingRpLineItems.add(rpLineItem);
                    }
                }
            }
            if (modifiedPendingRpLineItems.size() > 0) {
                reversePickupService.updateRpLineItems(modifiedPendingRpLineItems, EnumReverseAction.Pending_Approval.getId());
            }
        }
        reversePickupOrder.setPickupTime(pickupDate);
        reversePickupOrder = reversePickupService.saveReversePickupOrder(reversePickupOrder);
        return new RedirectResolution(ReversePickupListAction.class).addParameter("shippingOrder", reversePickupOrder.getShippingOrder().getId());
    }

    @Secure(hasAnyPermissions = {PermissionConstants.EDIT_REVERSE_PICKUP}, authActionBean = AdminPermissionAction.class)
    public Resolution editReversePickup() {
        shippingOrder = reversePickupOrder.getShippingOrder();
        /**check If Warehouse has started checkIn**/
        List<RpLineItem> rpLineItemList = reversePickupOrder.getRpLineItems();
        boolean startedCheckIn = false;
        if (rpLineItemList != null) {
            for (RpLineItem rpLineItem : rpLineItemList) {
                if (rpLineItem.getWarehouseReceivedCondition() != null) {
                    startedCheckIn = true;
                    break;
                }
            }
        }
        if (startedCheckIn) {
            return new RedirectResolution(ReversePickupAction.class, "editReversePickup").addParameter("reversePickupOrder", reversePickupOrder.getId());
        }
        /* put all the lineItems in another RP orders of current SO in map "rpLineDisabledMap" , this will be displayed  readOnly to user.*/
        List<ReversePickupOrder> reversePickupOrdersAlreadyCreatedList = reversePickupService.getReversePickupsExcludeCurrentRP(shippingOrder, reversePickupOrder);
        rpLineDisabledMap = reversePickupHelper.populateAlreadyCreatedRpLineItemsInfo(reversePickupOrdersAlreadyCreatedList);
        List<ReversePickupOrder> reversePickupOrderList = new ArrayList<ReversePickupOrder>();
        reversePickupOrderList.add(reversePickupOrder)
        /*put all the lineItems which are saved in current RP order of current SO , this will be shown as pre selected rows and can be edited*/;
        rpLineItemsSavedMap = reversePickupHelper.populateAlreadyCreatedRpLineItemsInfo(reversePickupOrderList);
        pendingApprovalEditMode = true;
        return new ForwardResolution("/pages/admin/reversePickup/createReversePickRequest.jsp");

    }

    @Secure(hasAnyPermissions = {PermissionConstants.EDIT_REVERSE_PICKUP}, authActionBean = AdminPermissionAction.class)
    public Resolution editApprovedPickup() {
        shippingOrder = reversePickupOrder.getShippingOrder();
        List<ReversePickupOrder> reversePickupOrderList = new ArrayList<ReversePickupOrder>();
        reversePickupOrderList.add(reversePickupOrder)
        /*put all the lineItems which are saved in RP order  , this will be shown as pre selected rows and can be edited for deletion only*/;
        rpLineItemsSavedMap = reversePickupHelper.populateAlreadyCreatedRpLineItemsInfo(reversePickupOrderList);
        return new ForwardResolution("/pages/admin/reversePickup/approvedReversePickup.jsp");

    }


    public Resolution cancelReversePickUpChanges() {
        if (reversePickupOrder != null) {
            return new RedirectResolution(ReversePickupListAction.class).addParameter("shippingOrder", reversePickupOrder.getShippingOrder().getId());
        }
        return new RedirectResolution(ReversePickupListAction.class);
    }

    public Resolution deleteRpLineItem() {
        if (reversePickupOrder != null && rpLineItem != null) {
            List<RpLineItem> rpLineItemList = reversePickupOrder.getRpLineItems();
            rpLineItemList.remove(rpLineItem);
            Double amountRequested = reversePickupHelper.calculateReversePickupAccount(rpLineItemList);
            reversePickupOrder.setAmount(amountRequested);
            reversePickupService.saveReversePickupOrder(reversePickupOrder);
            reversePickupService.deleteRpLineItem(rpLineItem);
        }
        return new RedirectResolution(ReversePickupListAction.class).addParameter("shippingOrder", reversePickupOrder.getShippingOrder().getId());
    }

    public Resolution changeCustomerActionAndStatus() {
        RpLineItem rpLineItemFromDb = null;
        if (rpLineItems != null) {
            RpLineItem rpLineItemFromUi = null;
            for (RpLineItem rpLineItem : rpLineItems) {
                if (rpLineItem != null) {
                    rpLineItemFromUi = rpLineItem;
                    break;
                }
            }
            if (rpLineItemFromUi != null) {
                rpLineItemFromDb = reversePickupService.getRpLineItemById(rpLineItemFromUi.getId());
                rpLineItemFromDb.setCustomerComment(rpLineItemFromUi.getCustomerComment());
                rpLineItemFromDb.setActionTaken(rpLineItemFromUi.getActionTaken());
                rpLineItemFromDb.setActionTakenOnStatus(rpLineItemFromUi.getActionTakenOnStatus());
                List<RpLineItem> rpItems = Arrays.asList(rpLineItemFromDb);
                reversePickupService.updateRpLineItems(rpItems, EnumReverseAction.Pending_Approval.getId());
            }
        }
        return new JsonResolution(new HealthkartResponse(HealthkartResponse.STATUS_OK, "Changes Saved", ""));
    }


    private List<RpLineItem> getSelectedRpLineItemList() {
        List<RpLineItem> selectedRpLineItemList = null;
        if (checkBoxIndexList != null && checkBoxIndexList.size() > 0) {
            selectedRpLineItemList = new ArrayList<RpLineItem>();
            /*Create list of checked lineItems*/
            for (Integer checkedIndex : checkBoxIndexList) {
                if (checkedIndex != null) {
                    RpLineItem rpLineItem = rpLineItems.get(checkedIndex);
                    selectedRpLineItemList.add(rpLineItem);
                }
            }

        }
        return selectedRpLineItemList;
    }


    public ShippingOrder getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(ShippingOrder shippingOrder) {
        this.shippingOrder = shippingOrder;
    }

    public List<RpLineItem> getRpLineItems() {
        return rpLineItems;
    }

    public void setRpLineItems(List<RpLineItem> rpLineItems) {
        this.rpLineItems = rpLineItems;
    }

    public List<Integer> getCheckBoxIndexList() {
        return checkBoxIndexList;
    }

    public void setCheckBoxIndexList(List<Integer> checkBoxIndexList) {
        this.checkBoxIndexList = checkBoxIndexList;
    }

    public ReversePickupOrder getReversePickupOrder() {
        return reversePickupOrder;
    }

    public void setReversePickupOrder(ReversePickupOrder reversePickupOrder) {
        this.reversePickupOrder = reversePickupOrder;
    }


    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public List<ReversePickupOrder> getReversePickupOrderList() {
        return reversePickupOrderList;
    }

    public void setReversePickupOrderList(List<ReversePickupOrder> reversePickupOrderList) {
        this.reversePickupOrderList = reversePickupOrderList;
    }

    public Map<Long, List<RpLineItem>> getRpLineItemsSavedMap() {
        return rpLineItemsSavedMap;
    }

    public void setRpLineItemsSavedMap(Map<Long, List<RpLineItem>> rpLineItemsSavedMap) {
        this.rpLineItemsSavedMap = rpLineItemsSavedMap;
    }

    public Map<Long, List<RpLineItem>> getRpLineDisabledMap() {
        return rpLineDisabledMap;
    }

    public void setRpLineDisabledMap(Map<Long, List<RpLineItem>> rpLineDisabledMap) {
        this.rpLineDisabledMap = rpLineDisabledMap;
    }

    public Boolean getPendingApprovalEditMode() {
        return pendingApprovalEditMode;
    }

    public void setPendingApprovalEditMode(Boolean pendingApprovalEditMode) {
        this.pendingApprovalEditMode = pendingApprovalEditMode;
    }

    public Date getPickupDate() {
        return pickupDate;
    }

    @Validate(converter = CustomDateTypeConvertor.class)
    public void setPickupDate(Date pickupDate) {
        this.pickupDate = pickupDate;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public RpLineItem getRpLineItem() {
        return rpLineItem;
    }

    public void setRpLineItem(RpLineItem rpLineItem) {
        this.rpLineItem = rpLineItem;
    }
}


