package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.engine.ShipmentPricingEngine;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.constants.courier.EnumCourierOperations;
import com.hk.constants.shipment.EnumShipmentServiceType;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderLifecycle;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.pact.service.shippingOrder.ShippingOrderLifecycleService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import net.sourceforge.stripes.action.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 14/01/13
 * Time: 14:26 
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ShipmentResolutionAction extends BaseAction {


    private String gatewayOrderId;
    private static Logger logger = LoggerFactory.getLogger(ShipmentResolutionAction.class);

    List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>(0);
    ShippingOrder shippingOrder;
    Shipment shipment;
    Courier updateCourier;
    List<Courier> applicableCouriers;
    String reasoning;
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

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/courier/shipmentResolution.jsp");
    }

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
            String comments = "Courier/Awb changed to " + updatedAwb.getCourier().getName() + "-->" + updatedAwb.getAwbNumber();
            shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SHIPMENT_RESOLUTION_ACTIVITY, comments);
            shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SHIPMENT_RESOLUTION_ACTIVITY, reasoning);
        }
        addRedirectAlertMessage(new SimpleMessage("Your Courier has been changed"));
        return new RedirectResolution(ShipmentResolutionAction.class, "search").addParameter("gatewayOrderId", shippingOrder.getGatewayOrderId());
    }

    public Resolution changeShipmentServiceType() {
        String oldShipmentServiceType = shipment.getShipmentServiceType().getName();
        shipment = shipmentService.recreateShipment(shippingOrder);
        shipment.setShipmentServiceType(EnumShipmentServiceType.getShipmentTypeFromId(shipmentServiceTypeId).asShipmentServiceType());
        shipment = shipmentService.save(shipment);
        shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SHIPMENT_RESOLUTION_ACTIVITY, "Shipment Service Type has been changed from "+ oldShipmentServiceType +"to " + shipment.getShipmentServiceType().getName());
        addRedirectAlertMessage(new SimpleMessage("Your Shipment Service Type has been changed"));
        return new RedirectResolution(ShipmentResolutionAction.class).addParameter("gatewayOrderId", shippingOrder.getGatewayOrderId());
    }

    public Resolution generateAWB() {
        if(shippingOrder.isDropShipping()){
            applicableCouriers = courierService.getCouriers(null,null,null, EnumCourierOperations.VENDOR_DROP_SHIP.getId());
        }else{
            applicableCouriers = Arrays.asList(pincodeCourierService.getDefaultCourier(shippingOrder));
//        applicableCouriers = pincodeCourierService.getApplicableCouriers(shippingOrder);
        }
        return new ForwardResolution("/pages/admin/courier/createUpdateAwb.jsp").addParameter("shippingOrder", shippingOrder.getId());
    }

    public Resolution createAssignAwb() {
        awb = (Awb) awbService.save(awb, EnumAwbStatus.Unused.getId().intValue());
        shipment = shipmentService.createShipment(shippingOrder);
        if (shipment == null) {
            awbService.delete(awb);
            addRedirectAlertMessage(new SimpleMessage("Shipment not Created for this AWB, please check shipping Order Life Cycle and resolve the issue"));
            return new RedirectResolution(ShipmentResolutionAction.class, "search").addParameter("gatewayOrderId", shippingOrder.getGatewayOrderId());
        }
        addRedirectAlertMessage(new SimpleMessage("Awb and Shipment has been created, please Enter Gateway Order Id again to check !!!!!"));
        return new RedirectResolution(ShipmentResolutionAction.class);
    }

    public Awb getAwb() {
        return awb;
    }

    public void setAwb(Awb awb) {
        this.awb = awb;
    }

    public String getReasoning() {
        return reasoning;
    }

    public void setReasoning(String reasoning) {
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
}
