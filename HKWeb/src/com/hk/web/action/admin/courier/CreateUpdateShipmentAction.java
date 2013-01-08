package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
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

    private static Logger logger = LoggerFactory.getLogger(SearchOrderAndEnterCourierInfoAction.class);

    List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>(0);
    ShippingOrder shippingOrder;

    private String gatewayOrderId;
    Boolean createNewShipment = false;
    Shipment shipment;
    Awb awb;

    @Autowired
    ShippingOrderDao shippingOrderDao;
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
        return new ForwardResolution("/pages/admin/courier/createUpdateShipment.jsp");
    }

    public Resolution searchShipment() {
        ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
        shippingOrderSearchCriteria.setGatewayOrderId(gatewayOrderId);
        shippingOrderSearchCriteria.setShippingOrderStatusList(shippingOrderStatusService.getOrderStatuses(EnumShippingOrderStatus.getStatusForCreateUpdateShipment()));        shippingOrderList = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria);

        if (shippingOrderList.isEmpty()) {
            addRedirectAlertMessage(new SimpleMessage("Invalid Gateway Order id or Shipping Order is not in applicable SO Status"));
            return new RedirectResolution("/pages/admin/courier/createUpdateShipment.jsp");
        }

        shippingOrder = shippingOrderList.get(0);
        shipment = shippingOrder.getShipment();

        if (shipment == null) {
            shipment = shipmentService.createShipment(shippingOrder);
        }
        if (shipment == null) {
            createNewShipment = true;
        }
        return new ForwardResolution("/pages/admin/courier/createUpdateShipment.jsp");
    }

    public Resolution createUpdateAwb(){
        awb = (Awb) awbService.save(awb, EnumAwbStatus.Unused.getId().intValue());
        shipmentService.createShipment(shippingOrder);
        return new RedirectResolution(CreateUpdateShipmentAction.class);
    }

    public Resolution createShipment(){

        return new RedirectResolution(CreateUpdateShipmentAction.class);
    }

    public Resolution updateShipment(){
        shipmentService.save(shipment);
        return new ForwardResolution(CreateUpdateShipmentAction.class);
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

    public Boolean getCreateNewShipment() {
        return createNewShipment;
    }

    public void setCreateNewShipment(Boolean createNewShipment) {
        this.createNewShipment = createNewShipment;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }
}
