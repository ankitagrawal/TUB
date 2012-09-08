package com.hk.web.action.admin.shipment;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.admin.manager.DeliveryStatusUpdateManager;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.web.action.error.AdminPermissionAction;
import com.hk.web.action.admin.queue.DeliveryAwaitingQueueAction;

import java.util.Date;
import java.util.Calendar;

@Secure(hasAnyPermissions = {PermissionConstants.UPDATE_DELIVERY_QUEUE}, authActionBean = AdminPermissionAction.class)
public class ChangeShipmentDetailsAction extends BaseAction {

    private ShippingOrder shippingOrder;
    private String trackingId;
    private Courier attachedCourier;
    private Shipment shipment;
    private String originalShippingOrderStatus;
    private String gatewayOrderId;
    String comments;
    private boolean visible = false;
    private static Logger logger = LoggerFactory.getLogger(ChangeShipmentDetailsAction.class);


    @Autowired
    AwbService awbService;
    @Autowired
    AdminShippingOrderService adminShippingOrderService;
    @Autowired
    ShippingOrderService shippingOrderService;
	@Autowired
	DeliveryStatusUpdateManager deliveryAwaitingUpdateManager;


    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/changeShippingStatus.jsp");
    }

    public Resolution search() {
        if (gatewayOrderId != null) {
            shippingOrder = shippingOrderService.findByGatewayOrderId(gatewayOrderId);
            if (shippingOrder != null) {
                if ( EnumShippingOrderStatus.getStatusForChangingShipmentDetails().contains(shippingOrder.getOrderStatus())) {
                    originalShippingOrderStatus = shippingOrder.getOrderStatus().getName();
                    shipment = shippingOrder.getShipment();
                    visible = true;
                    return new ForwardResolution("/pages/admin/changeShippingStatus.jsp");
                } else {
                    addRedirectAlertMessage(new SimpleMessage("Only  Shipped  SO Can be Searched"));
                    return new ForwardResolution("/pages/admin/changeShippingStatus.jsp");
                }
            } else {
                addRedirectAlertMessage(new SimpleMessage("Order with given id does not exist"));
                return new ForwardResolution("/pages/admin/changeShippingStatus.jsp");
            }
        } else {
            addRedirectAlertMessage(new SimpleMessage("Please enter gateway order Id"));
            return new ForwardResolution("/pages/admin/changeShippingStatus.jsp");
        }
    }

    public Resolution save() {
        if (trackingId == null) {
            addRedirectAlertMessage(new SimpleMessage("Enter Tracking ID"));
            return new ForwardResolution("/pages/admin/changeShipmentDetails.jsp");
        }

        Awb attachedAwb = shipment.getAwb();
        Awb finalAwb = attachedAwb;
        if ((!(attachedAwb.getAwbNumber().equalsIgnoreCase(trackingId.trim()))) ||
                ((!(shipment.getCourier().equals(attachedCourier))))) {

            Awb awbFromDb = awbService.getAvailableAwbForCourierByWarehouseCodStatus(shipment.getCourier(), trackingId.trim(), null, null, null);
            if (awbFromDb != null && awbFromDb.getAwbNumber() != null) {
                if (awbFromDb.getAwbStatus().getId().equals(EnumAwbStatus.Used.getId()) || (awbFromDb.getAwbStatus().getId().equals(EnumAwbStatus.Attach.getId())) || (awbFromDb.getAwbStatus().getId().equals(EnumAwbStatus.Authorization_Pending.getId()))) {
                    addRedirectAlertMessage(new SimpleMessage(" OPERATION FAILED *********  Tracking Id : " + trackingId + "is already Used with other  shipping Order"));
                    return new RedirectResolution(ChangeShipmentDetailsAction.class);
                }
                if ((!awbFromDb.getWarehouse().getId().equals(shippingOrder.getWarehouse().getId())) || (awbFromDb.getCod() != shippingOrder.isCOD())) {
                    addRedirectAlertMessage(new SimpleMessage(" OPERATION FAILED *********  Tracking Id : " + trackingId + "is already Present in another warehouse with same courier" +
                            "  : " + shipment.getCourier().getName() + "  you are Trying to use COD tracking id with NON COD --->   TRY AGAIN "));
                    return new RedirectResolution(ChangeShipmentDetailsAction.class);
                }

                finalAwb = awbFromDb;
                finalAwb.setAwbStatus(EnumAwbStatus.Used.getAsAwbStatus());
            } else {
                Awb awb = new Awb();
                awb.setAwbNumber(trackingId.trim());
                awb.setAwbBarCode(trackingId.trim());
                awb.setAwbStatus(EnumAwbStatus.Used.getAsAwbStatus());
                awb.setCourier(shipment.getCourier());
                awb.setCod(shippingOrder.isCOD());
                awb.setWarehouse(shippingOrder.getWarehouse());
                awb = awbService.save(awb);
                finalAwb = awb;
            }
            //Todo Seema -- Awb  detached from Shipment, their status should not change : Need to decide whether awb will be deleted or made available
            //attachedAwb.setAwbStatus(EnumAwbStatus.Unused.getAsAwbStatus());
            //awbService.save(attachedAwb);
            shipment.setAwb(finalAwb);
        }


        shippingOrder.setShipment(shipment);
        ShippingOrderStatus shippingOrderStatus = shippingOrder.getOrderStatus();
        Long shippingOrderStatusId = shippingOrderStatus.getId();
        String shippingOrderStatusName = shippingOrder.getOrderStatus().getName();

        if (!shippingOrderStatusName.equals(originalShippingOrderStatus)) {
            if (shippingOrderStatusId.equals(EnumShippingOrderStatus.SO_Delivered.getId())) {
                adminShippingOrderService.markShippingOrderAsDelivered(shippingOrder);
            }

            else if (shippingOrderStatusId.equals(EnumShippingOrderStatus.SO_Shipped.getId())) {
                adminShippingOrderService.markShippingOrderAsShipped(shippingOrder);
            }

            else if (shippingOrderStatusId.equals(EnumShippingOrderStatus.SO_Lost.getId())) {
                adminShippingOrderService.markShippingOrderAsLost(shippingOrder);
            }
            if (!originalShippingOrderStatus.equals(shippingOrderStatusName)) {
                comments = "Status changed from " + originalShippingOrderStatus + " to " + shippingOrderStatusName;
                shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_StatusChanged, comments);
            }
        }
        shippingOrderService.save(shippingOrder);
	    visible = false;
        addRedirectAlertMessage(new SimpleMessage("Changes Saved."));
        return new ForwardResolution("/pages/admin/changeShippingStatus.jsp");
    }


	public Resolution markDelivered() {
		shippingOrder.setShipment(shipment);
		deliveryAwaitingUpdateManager.updateCourierDeliveryStatus(shippingOrder, shipment, shipment.getAwb().getAwbNumber(), shipment.getDeliveryDate());
		comments = "Status changed from " + originalShippingOrderStatus + " to " + shippingOrder.getOrderStatus().getName();
		shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_StatusChanged, comments);
		visible = false;
		addRedirectAlertMessage(new SimpleMessage("Changes Saved."));
		return new ForwardResolution("/pages/admin/changeShippingStatus.jsp");
	}


	public Resolution markLost() {
		shippingOrder.setShipment(shipment); 	
            if ((shipment.getShipDate().after(shipment.getDeliveryDate()) ) || (shipment.getDeliveryDate()).after(new Date())) {
                Calendar lostDateAsToday = Calendar.getInstance();
                lostDateAsToday.setTime(new Date());
                shipment.setDeliveryDate(lostDateAsToday.getTime());
            }
		adminShippingOrderService.markShippingOrderAsLost(shippingOrder);
		comments = "Status changed from " + originalShippingOrderStatus + " to " + shippingOrder.getOrderStatus().getName();
		shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_StatusChanged, comments);
		visible = false;
		addRedirectAlertMessage(new SimpleMessage("Changes Saved."));
		return new ForwardResolution("/pages/admin/changeShippingStatus.jsp");

	}


    public ShippingOrder getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(ShippingOrder shippingOrder) {
        this.shippingOrder = shippingOrder;
    }

    public void setGatewayOrderId(String gatewayOrderId) {
        this.gatewayOrderId = gatewayOrderId;
    }

    public boolean isVisible() {
        return visible;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public String getOriginalShippingOrderStatus() {
        return originalShippingOrderStatus;
    }

    public void setOriginalShippingOrderStatus(String originalShippingOrderStatus) {
        this.originalShippingOrderStatus = originalShippingOrderStatus;
    }


    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public Courier getAttachedCourier() {
        return attachedCourier;
    }

    public void setAttachedCourier(Courier attachedCourier) {
        this.attachedCourier = attachedCourier;
    }

}
