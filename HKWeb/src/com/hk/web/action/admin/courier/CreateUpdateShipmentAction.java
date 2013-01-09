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
import java.util.Date;
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
    Shipment shipment;
    Awb awb;
    private Double estimatedWeight;

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
        return new ForwardResolution("/pages/admin/courier/createUpdateShipmentAction.jsp");
    }

    public Resolution searchShipment() {
        ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
        shippingOrderSearchCriteria.setGatewayOrderId(gatewayOrderId);
        shippingOrderSearchCriteria.setShippingOrderStatusList(shippingOrderStatusService.getOrderStatuses(EnumShippingOrderStatus.getStatusForCreateUpdateShipment()));        shippingOrderList = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria);

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
          return new ForwardResolution("/pages/courier/createUpdateAwb.jsp");
        }
        estimatedWeight = shipmentService.getEstimatedWeightOfShipment(shippingOrder);
        return new ForwardResolution("/pages/admin/courier/createUpdateShipmentAction.jsp");
    }

    public Resolution createUpdateAwb(){
        awb = (Awb) awbService.save(awb, EnumAwbStatus.Unused.getId().intValue());
        shipmentService.createShipment(shippingOrder);
        addRedirectAlertMessage(new SimpleMessage("Awb and Shipment has been created, please Enter Gateway Order Id again to check !!!!!"));
        return new RedirectResolution(CreateUpdateShipmentAction.class);
    }

    public Resolution updateShipment(){
        shipmentService.save(shipment);
        addRedirectAlertMessage(new SimpleMessage("Changes Saved Successfully !!!!"));
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
}
