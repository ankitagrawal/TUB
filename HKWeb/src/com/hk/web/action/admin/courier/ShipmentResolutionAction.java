package com.hk.web.action.admin.courier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.engine.ShipmentPricingEngine;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.constants.courier.EnumCourierOperations;
import com.hk.constants.shipment.EnumShipmentServiceType;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.analytics.Reason;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Shipment;
import com.hk.domain.courier.Zone;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderLifecycle;
import com.hk.impl.service.queue.BucketService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.pact.service.shippingOrder.ShippingOrderLifecycleService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.pact.service.splitter.ShippingOrderProcessor;
import com.hk.service.ServiceLocatorFactory;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * User: Pratham
 * Date: 14/01/13
 * Time: 14:26 
 */
@Component
public class ShipmentResolutionAction extends BaseAction {


    private String gatewayOrderId;
    private String newAwbNumber;

    List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>(0);
    ShippingOrder shippingOrder;
    Shipment shipment;
    Courier updateCourier;
    List<Courier> applicableCouriers;
    private Reason reasoning;
    private Reason awbReasoning;
    Awb awb;
    boolean preserveAwb;
    Long shipmentServiceTypeId;

    List<ShippingOrderLifecycle> shippingOrderLifeCycles;

    @Autowired
    ShippingOrderStatusService shippingOrderStatusService;
    @Autowired
    ShippingOrderService shippingOrderService;
    @Autowired
    ShippingOrderLifecycleService shippingOrderLifecycleService;
    @Autowired
    ShipmentService shipmentService;
    @Autowired
    PincodeCourierService pincodeCourierService;
    @Autowired
    AwbService awbService;
    @Autowired
    CourierGroupService courierGroupService;
    @Autowired
    private ShipmentPricingEngine shipmentPricingEngine;
    @Autowired
    CourierService courierService;
    @Autowired
    BucketService bucketService;
    @Autowired
    OrderService orderService;
    
    ShippingOrderProcessor shippingOrderProcessor;

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/courier/shipmentResolution.jsp");
    }

    @Secure(hasAnyPermissions = {PermissionConstants.OPS_MANAGER_SRS_VIEW}, authActionBean = AdminPermissionAction.class)
    public Resolution search() {
        ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
        shippingOrderSearchCriteria.setGatewayOrderId(gatewayOrderId);
        shippingOrderSearchCriteria.setShippingOrderStatusList(shippingOrderStatusService.getOrderStatuses(EnumShippingOrderStatus.getStatusForShipmentResolution()));
        shippingOrderList = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria, false);

        if (shippingOrderList.isEmpty()) {
            addRedirectAlertMessage(new SimpleMessage("Invalid Gateway Order id or Shipping Order is not in applicable SO Status"));
            return new RedirectResolution("/pages/admin/courier/shipmentResolution.jsp");
        }

        shippingOrder = shippingOrderList.get(0);
        applicableCouriers = pincodeCourierService.getApplicableCouriers(shippingOrder);
        shipment = shippingOrder.getShipment();
        shippingOrderLifeCycles = shippingOrderLifecycleService.getShippingOrderLifecycleBySOAndActivities(shippingOrder.getId(), EnumShippingOrderLifecycleActivity.getActivitiesForShipmentResolutionQueue());

        return new ForwardResolution("/pages/admin/courier/shipmentResolution.jsp");
    }

    public Resolution createAutoShipment() {
        shipment = shipmentService.createShipment(shippingOrder,true);
        return new RedirectResolution(ShipmentResolutionAction.class, "search").addParameter("gatewayOrderId", shippingOrder.getGatewayOrderId());
    }

    public Resolution resolveCase(){
        shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SHIPMENT_RESOLUTION_ACTIVITY,null,"Case Resolved");
        shippingOrderProcessor.autoEscalateShippingOrder(shippingOrder, false);
        return new RedirectResolution(ShipmentResolutionAction.class, "search").addParameter("gatewayOrderId", shippingOrder.getGatewayOrderId());
    }

    @Secure(hasAnyPermissions = {PermissionConstants.OPS_MANAGER_SRS_CHANGE_COURIER}, authActionBean = AdminPermissionAction.class)
    public Resolution changeCourier() {
        Awb currentAwb = shipment.getAwb();
        shipment = shipmentService.changeCourier(shipment, updateCourier, preserveAwb);
        if (courierGroupService.getCourierGroup(shipment.getAwb().getCourier()) != null) {
            shipment.setEstmShipmentCharge(shipmentPricingEngine.calculateShipmentCost(shippingOrder));
            shipment.setEstmCollectionCharge(shipmentPricingEngine.calculateReconciliationCost(shippingOrder));
            shipment.setExtraCharge(shipmentPricingEngine.calculatePackagingCost(shippingOrder));
        }
        shipment = shipmentService.save(shipment);
        Awb updatedAwb = shipment.getAwb();

        if (!currentAwb.equals(updatedAwb)) {
            String comments = "Courier/Awb changed to " + updatedAwb.toString();
            shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SHIPMENT_RESOLUTION_ACTIVITY, reasoning, comments);
        }
        addRedirectAlertMessage(new SimpleMessage("Your Courier has been changed"));
        return new RedirectResolution(ShipmentResolutionAction.class, "search").addParameter("gatewayOrderId", shippingOrder.getGatewayOrderId());
    }

    @Secure(hasAnyPermissions = {PermissionConstants.OPS_MANAGER_SRS_CHANGE_AWB}, authActionBean = AdminPermissionAction.class)
    public Resolution createAssignAwbForShipment() {
        Courier courier = null;
        boolean bool = false;
        Awb awbDb = awbService.findByCourierAwbNumber(courier, awb.getAwbNumber());
        if (awbDb == null) {
            awb = awbService.save(awb, EnumAwbStatus.Unused.getId().intValue());
            awb = awbService.save(awb, EnumAwbStatus.Used.getId().intValue());
            shipment = shipmentService.changeAwb(shipment, awb, preserveAwb);
            shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SHIPMENT_RESOLUTION_ACTIVITY, awbReasoning, "AwbNumber changed to --> " + awb.toString());
            bool = true;
        } else if (!awbDb.getAwbStatus().getId().equals(EnumAwbStatus.Unused.getId())) {
            addRedirectAlertMessage(new SimpleMessage("Awb Number Already in Used!!!"));
        } else {
            awb = awbService.save(awbDb, EnumAwbStatus.Used.getId().intValue());
            shipment = shipmentService.changeAwb(shipment, awbDb, preserveAwb);
            shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SHIPMENT_RESOLUTION_ACTIVITY, awbReasoning, "AwbNumber changed to --> " + awbDb.getAwbNumber());
            bool = true;
        }
        if (bool) {
            addRedirectAlertMessage(new SimpleMessage("Awb Number Changed!!!"));
        }
        return new RedirectResolution(ShipmentResolutionAction.class, "search").addParameter("gatewayOrderId", shippingOrder.getGatewayOrderId());
    }

    @Secure(hasAnyPermissions = {PermissionConstants.OPS_MANAGER_SRS_CHANGE_SERVICE_TYPE}, authActionBean = AdminPermissionAction.class)
    public Resolution changeShipmentServiceType() {
        //todo courier refactor, as of now manual awb change when shipment service type is altered
        shipment.setShipmentServiceType(EnumShipmentServiceType.getShipmentTypeFromId(shipmentServiceTypeId).asShipmentServiceType());
        shipment = shipmentService.save(shipment);
        shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SHIPMENT_RESOLUTION_ACTIVITY,null,  "Shipment Service Type changed to " + shipment.getShipmentServiceType().getName());
        addRedirectAlertMessage(new SimpleMessage("Your Shipment Service Type has been changed, Please remember you may have to change awb/courier as per use case"));
        return new RedirectResolution(ShipmentResolutionAction.class);
    }

    @Secure(hasAnyPermissions = {PermissionConstants.OPS_MANAGER_SRS_CREATE_AWB}, authActionBean = AdminPermissionAction.class)
    public Resolution generateAWB() {
        applicableCouriers = Arrays.asList();
        if(shippingOrder.isDropShipping()){
            applicableCouriers = courierService.getCouriers(null,null,null, EnumCourierOperations.VENDOR_DROP_SHIP.getId());
        }else{
            applicableCouriers = Arrays.asList(pincodeCourierService.getDefaultCourier(shippingOrder));
        }
        return new ForwardResolution("/pages/admin/courier/createUpdateAwb.jsp").addParameter("shippingOrder", shippingOrder.getId());
    }

    @Secure(hasAnyPermissions = {PermissionConstants.OPS_MANAGER_SRS_CREATE_AWB}, authActionBean = AdminPermissionAction.class)
    public Resolution createAssignAwb() {
        awb = (Awb) awbService.save(awb, EnumAwbStatus.Unused.getId().intValue());
        //todo courier-refactor as drop ship scales need to evaluate a better solution
        if (shippingOrder.isDropShipping()) {
            Pincode pincode = shippingOrder.getBaseOrder().getAddress().getPincode();
            Zone zone = pincode.getZone();
            Shipment shipmentDropShip = new Shipment();
            shipmentDropShip.setAwb(awb);
            shipmentDropShip.setZone(zone);
            shipmentDropShip.setBoxWeight(-1D);
            shipmentDropShip.setShipmentServiceType(pincodeCourierService.getShipmentServiceType(shippingOrder));
            shippingOrder.setShipment(shipmentDropShip);
            shipment = shipmentService.createShipment(shippingOrder, false);
        } else {
            shipment = shipmentService.createShipment(shippingOrder, true);
        }
        if (shipment == null) {
            awbService.delete(awb);
            addRedirectAlertMessage(new SimpleMessage("Shipment not Created for this AWB, please check shipping Order Life Cycle and resolve the issue"));
            return new RedirectResolution(ShipmentResolutionAction.class, "search").addParameter("gatewayOrderId", shippingOrder.getGatewayOrderId());
        }
        shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SHIPMENT_RESOLUTION_ACTIVITY, null, "Manually Awb Created and Assigned to Shipping Order to create Shipment");
        addRedirectAlertMessage(new SimpleMessage("Awb and Shipment has been created, please Enter Gateway Order Id again to check !!!!!"));
        return new RedirectResolution(ShipmentResolutionAction.class);
    }
    public Awb getAwb() {
        return awb;
    }

    public void setAwb(Awb awb) {
        this.awb = awb;
    }

    public Reason getAwbReasoning() {
        return awbReasoning;
    }

    public void setAwbReasoning(Reason awbReasoning) {
        this.awbReasoning = awbReasoning;
    }

    public Reason getReasoning() {
        return reasoning;
    }

    public void setReasoning(Reason reasoning) {
        this.reasoning = reasoning;
    }

    public List<Courier> getApplicableCouriers() {
        return applicableCouriers;
    }

    public void setApplicableCouriers(List<Courier> applicableCouriers) {
        this.applicableCouriers = applicableCouriers;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public ShippingOrder getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(ShippingOrder shippingOrder) {
        this.shippingOrder = shippingOrder;
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

    public boolean isPreserveAwb() {
        return preserveAwb;
    }

    public void setPreserveAwb(boolean preserveAwb) {
        this.preserveAwb = preserveAwb;
    }

    public List<ShippingOrderLifecycle> getShippingOrderLifeCycles() {
        return shippingOrderLifeCycles;
    }

    public void setShippingOrderLifeCycles(List<ShippingOrderLifecycle> shippingOrderLifeCycles) {
        this.shippingOrderLifeCycles = shippingOrderLifeCycles;
    }

    public Courier getUpdateCourier() {
        return updateCourier;
    }

    public void setUpdateCourier(Courier updateCourier) {
        this.updateCourier = updateCourier;
    }

    public Long getShipmentServiceTypeId() {
        return shipmentServiceTypeId;
    }

    public void setShipmentServiceTypeId(Long shipmentServiceTypeId) {
        this.shipmentServiceTypeId = shipmentServiceTypeId;
    }
    public String getNewAwbNumber() {
           return newAwbNumber;
       }
    public void setNewAwbNumber(String newAwbNumber) {
        this.newAwbNumber = newAwbNumber;
    }

    /**
	 * @return the shippingOrderProcessor
	 */
	public ShippingOrderProcessor getShippingOrderProcessor() {
		if (shippingOrderProcessor == null) {
            this.shippingOrderProcessor = ServiceLocatorFactory.getService(ShippingOrderProcessor.class);
        }
        return shippingOrderProcessor;
	}
}
