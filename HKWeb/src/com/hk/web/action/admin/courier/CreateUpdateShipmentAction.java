package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.ShippingOrder;
import com.hk.pact.service.UserService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.web.action.admin.shippingOrder.ShippingOrderLifecycleAction;
import net.sourceforge.stripes.action.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: 12/11/12
 * Time: 3:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CreateUpdateShipmentAction extends BaseAction {

    private static Logger logger = LoggerFactory.getLogger(CreateUpdateShipmentAction.class);

    List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>(0);
    ShippingOrder shippingOrder;

    private String gatewayOrderId;
    Shipment shipment;
    Awb awb;
    private Double estimatedWeight;

    @Autowired
    ShipmentService shipmentService;
    @Autowired
    ShippingOrderService shippingOrderService;
    @Autowired
    UserService userService;
    @Autowired
    AwbService awbService;
    @Autowired
    ShippingOrderStatusService shippingOrderStatusService;

    @DontValidate
    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/courier/createUpdateShipmentAction.jsp");
    }

    public Resolution searchShipment() {
        ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
        shippingOrderSearchCriteria.setGatewayOrderId(gatewayOrderId);
        shippingOrderSearchCriteria.setShippingOrderStatusList(shippingOrderStatusService.getOrderStatuses(EnumShippingOrderStatus.getStatusForCreateUpdateShipment()));
        shippingOrderList = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria);

        if (shippingOrderList.isEmpty()) {
            addRedirectAlertMessage(new SimpleMessage("Invalid Gateway Order id or Shipping Order is not in applicable SO Status"));
            return new RedirectResolution("/pages/admin/courier/createUpdateShipmentAction.jsp");
        }

        shippingOrder = shippingOrderList.get(0);
        shipment = shippingOrder.getShipment();

        if (shipment == null) {
            shipment = shipmentService.createShipment(shippingOrder);
        }
        if (shipment == null) {
            addRedirectAlertMessage(new SimpleMessage("Awb doesn't Exist for this Gateway ID, please enter below details to create the new one !!!!"));
            return new ForwardResolution("/pages/admin/courier/createUpdateAwb.jsp");
        }
        estimatedWeight = shipmentService.getEstimatedWeightOfShipment(shippingOrder);
        return new ForwardResolution("/pages/admin/courier/createUpdateShipmentAction.jsp");
    }

    public Resolution createUpdateAwb() {
        if (shippingOrder != null) {
            shippingOrder = shippingOrderService.find(shippingOrder.getId());
        }
        awb = (Awb) awbService.save(awb, EnumAwbStatus.Unused.getId().intValue());
        shipment = shipmentService.createShipment(shippingOrder);
        if (shipment == null) {
            awbService.delete(awb);
            addRedirectAlertMessage(new SimpleMessage("Shipment not Created for this AWB, please check below shipping order life cycle"));
            return new RedirectResolution(ShippingOrderLifecycleAction.class).addParameter("shippingOrder", shippingOrder.getId());
        }
        addRedirectAlertMessage(new SimpleMessage("Awb and Shipment has been created, please Enter Gateway Order Id again to check !!!!!"));
        return new RedirectResolution(CreateUpdateShipmentAction.class);
    }

    public Resolution updateShipment() {
        /*
                if(shippingOrder instanceof ReplacementOrder){
			ShippingOrder parentShippingOrder = ((ReplacementOrder) shippingOrder).getRefShippingOrder();
			if(((ReplacementOrder) shippingOrder).isRto()){
				ReplacementOrderReason replacementOrderReason = getAdminShippingOrderService().getRTOReasonForShippingOrder(parentShippingOrder);
				if(replacementOrderReason != null){
					if(EnumReplacementOrderReason.getCourierRelatedReasonForRto().contains(replacementOrderReason.getId())){
						if(selectedCourier != null && parentShippingOrder.getShipment().getAwb().getCourier().getId().equals(selectedCourier.getId())){
							addRedirectAlertMessage(new SimpleMessage("Previous shipping order was returned due to the courier selected. Please select another courier or contact admin"));
							return new RedirectResolution(SearchOrderAndEnterCourierInfoAction.class, "searchOrders").addParameter("gatewayOrderId", shippingOrder.getGatewayOrderId());
						}
					}
				}
			}
		}
         */
        shipmentService.save(shipment);
        shippingOrder.setOrderStatus(shippingOrderStatusService.find(EnumShippingOrderStatus.SO_Packed));
        shippingOrderService.save(shippingOrder);
        shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Packed);

        addRedirectAlertMessage(new SimpleMessage("Changes Saved Successfully !!!!"));
        return new RedirectResolution(CreateUpdateShipmentAction.class);
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

    public Double getEstimatedWeight() {
        return estimatedWeight;
    }

    public void setEstimatedWeight(Double estimatedWeight) {
        this.estimatedWeight = estimatedWeight;
    }

    public Awb getAwb() {
        return awb;
    }

    public void setAwb(Awb awb) {
        this.awb = awb;
    }
}
