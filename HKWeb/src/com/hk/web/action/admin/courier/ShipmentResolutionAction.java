package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.constants.courier.EnumAwbStatus;
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
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    public Resolution pre(){
        return new ForwardResolution("/pages/admin/courier/shipmentResolution.jsp");
    }

    public Resolution search(){
        ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
        shippingOrderSearchCriteria.setGatewayOrderId(gatewayOrderId);
        shippingOrderSearchCriteria.setShippingOrderStatusList(shippingOrderStatusService.getOrderStatuses(EnumShippingOrderStatus.getStatusForShipmentResolution()));
        shippingOrderList = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria);

        if (shippingOrderList.isEmpty()) {
            addRedirectAlertMessage(new SimpleMessage("Invalid Gateway Order id or Shipping Order is not in applicable SO Status"));
            return new RedirectResolution("/pages/admin/courier/createUpdateShipmentAction.jsp");
        }

        shippingOrder = shippingOrderList.get(0);
        applicableCouriers = pincodeCourierService.getApplicableCouriers(shippingOrder);
        shipment = shippingOrder.getShipment();
        shippingOrderLifeCycles = shippingOrderLifecycleService.getShippingOrderLifecycleBySOAndActivity(shippingOrder.getId(), EnumShippingOrderLifecycleActivity.SO_ShipmentNotCreated.getId());

        return new ForwardResolution("/pages/admin/courier/shipmentResolution.jsp");
    }

    public Resolution changeCourier(){
        Awb currentAwb = shipment.getAwb();
        shipment = shipmentService.changeCourier(shipment, updateCourier, preserveAwb);
        Awb updatedAwb = shipment.getAwb();
        if (!currentAwb.equals(updatedAwb)) {
            String comments = "Courier/Awb changed to " + updatedAwb.getCourier().getName() + "-->" + updatedAwb.getAwbNumber();
            shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SHIPMENT_RESOLUTION_ACTIVITY, comments);
            shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SHIPMENT_RESOLUTION_ACTIVITY, reasoning);
        }
        return new ForwardResolution("/pages/admin/courier/shipmentResolution.jsp");
    }

    public Resolution changeShipmentServiceType() {
        shipment = shipmentService.save(shipment);
        shipment = shipmentService.recreateShipment(shippingOrder);
        return new ForwardResolution("/pages/admin/courier/shipmentResolution.jsp");
    }

    public Resolution createAssignAwb() {
        awb = (Awb) awbService.save(awb, EnumAwbStatus.Unused.getId().intValue());
        shipmentService.createShipment(shippingOrder);
        addRedirectAlertMessage(new SimpleMessage("Awb and Shipment has been created, please Enter Gateway Order Id again to check !!!!!"));
        return new RedirectResolution(CreateUpdateShipmentAction.class);
    }


}
