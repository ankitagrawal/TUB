package com.hk.web.action.admin.shipment;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.manager.DeliveryStatusUpdateManager;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.web.action.error.AdminPermissionAction;

import java.util.Date;

@Secure(hasAnyPermissions = {PermissionConstants.UPDATE_DELIVERY_QUEUE}, authActionBean = AdminPermissionAction.class)
public class ChangeShipmentDetailsAction extends BaseAction {

	private ShippingOrder shippingOrder;
	private String trackingId;
	private Courier attachedCourier;
	private Shipment shipment;
	private ShippingOrderStatus originalShippingOrderStatus;
	private String gatewayOrderId;
	String comments;
	private boolean visible = false;

//    private static Logger logger = LoggerFactory.getLogger(ChangeShipmentDetailsAction.class);


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
				if (EnumShippingOrderStatus.getStatusForChangingShipmentDetails().contains(shippingOrder.getOrderStatus())) {
					originalShippingOrderStatus = shippingOrder.getOrderStatus();
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

	public Resolution markDelivered() {
		shippingOrder.setShipment(shipment);
		Date deliverydate = shipment.getDeliveryDate();
		if (EnumShippingOrderStatus.getStatusForChangingShipmentDetails().contains(shippingOrder.getOrderStatus())) {
			if( deliverydate.after(new Date())){
			addRedirectAlertMessage(new SimpleMessage("Delivery Date Cannot be after Today's Date"));
			return new ForwardResolution("/pages/admin/changeShippingStatus.jsp");
			}
			else if (shipment.getShipDate().after(deliverydate)){
			addRedirectAlertMessage(new SimpleMessage("Delivery Date cannot be before Shipping Date :::  "+ shipment.getShipDate()));
			return new ForwardResolution("/pages/admin/changeShippingStatus.jsp");
			}
			if (shipment != null) {
				shipment.setDeliveryDate(deliverydate);
				getAdminShippingOrderService().markShippingOrderAsDelivered(shippingOrder);
			}
		}
		comments = "Status changed from change shipment detail screen  from  " + originalShippingOrderStatus.getName() + " to " + shippingOrder.getOrderStatus().getName();
		shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_StatusChanged, null, comments);
		visible = false;
		addRedirectAlertMessage(new SimpleMessage("Status Saved  : " + shippingOrder.getOrderStatus().getName()));
		return new ForwardResolution("/pages/admin/changeShippingStatus.jsp");
	}


	public Resolution markLost() {
		shippingOrder.setShipment(shipment);
		adminShippingOrderService.markShippingOrderAsLost(shippingOrder);
		comments = "Status changed from " + originalShippingOrderStatus.getName() + " to " + shippingOrder.getOrderStatus().getName();
		shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_StatusChanged, null, comments);
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


	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}

	public ShippingOrderStatus getOriginalShippingOrderStatus() {
		return originalShippingOrderStatus;
	}

	public void setOriginalShippingOrderStatus(ShippingOrderStatus originalShippingOrderStatus) {
		this.originalShippingOrderStatus = originalShippingOrderStatus;
	}

	public Courier getAttachedCourier() {
		return attachedCourier;
	}

	public void setAttachedCourier(Courier attachedCourier) {
		this.attachedCourier = attachedCourier;
	}

	public String getGatewayOrderId() {
		return gatewayOrderId;
	}

	public AdminShippingOrderService getAdminShippingOrderService() {
		return adminShippingOrderService;
	}

	public void setAdminShippingOrderService(AdminShippingOrderService adminShippingOrderService) {
		this.adminShippingOrderService = adminShippingOrderService;
	}

}
