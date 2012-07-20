package com.hk.web.action.admin.shipment;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.engine.ShipmentPricingEngine;
import com.hk.admin.pact.dao.courier.CourierDao;
import com.hk.admin.pact.service.courier.CourierCostCalculator;
import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.OrderSearchCriteria;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.pact.dao.order.analytics.DemandHistoryDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.OrderSplitterService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.pojo.DummyOrder;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 5/26/12
 * Time: 1:13 AM
 * To change this template use File | Settings | File Templates.
 */
@Secure(hasAnyPermissions = {PermissionConstants.SEARCH_ORDERS}, authActionBean = AdminPermissionAction.class)
@Component
public class ShipmentCostCalculatorAction extends BaseAction {

    private Double weight;

    private Warehouse srcWarehouse;

    private String pincode;

    Double amount;

    boolean cod;

    String shippingOrderId;

    int days;

    Date shippedStartDate;
    Date shippedEndDate;
    boolean overrideHistoricalShipmentCost;

    private static Logger logger = LoggerFactory.getLogger(ShipmentCostCalculatorAction.class);

    @Autowired
    PincodeDao pincodeDao;

    @Autowired
    ShipmentPricingEngine shipmentPricingEngine;

    @Autowired
    ShippingOrderDao shippingOrderDao;

    @Autowired
    CourierCostCalculator courierCostCalculator;

    @Autowired
    ShipmentService shipmentService;

    @Autowired
    ShippingOrderStatusService shippingOrderStatusService;

    @Autowired
    ShippingOrderService shippingOrderService;

    @Autowired
    CourierGroupService courierGroupService;

    @Autowired
    CourierDao courierDao;

    @Autowired
    DemandHistoryDao demandHistoryDao;

    @Autowired
    OrderStatusService orderStatusService;

    @Autowired
    OrderSplitterService orderSplitterService;

    @Autowired
    WarehouseService warehouseService;

    @Autowired
    OrderService orderService;

    TreeMap<Courier, Long> courierCostingMap = new TreeMap<Courier, java.lang.Long>();
    List<Courier> applicableCourierList;

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/shipment/shipmentCostCalculator.jsp");
    }

    public Resolution saveActualShippingCostForShippingOrder() {
        ShippingOrder shippingOrder = shippingOrderDao.findByGatewayOrderId(shippingOrderId);
        if (shippingOrder != null) {
            Shipment shipment = shippingOrder.getShipment();
            if (shipment != null && courierGroupService.getCourierGroup(shipment.getCourier()) != null) {
                shipment.setEstmShipmentCharge(shipmentPricingEngine.calculateShipmentCost(shippingOrder));
                shipment.setEstmCollectionCharge(shipmentPricingEngine.calculateReconciliationCost(shippingOrder));
                shipment.setExtraCharge(shipmentPricingEngine.calculatePackagingCost(shippingOrder));
                shipmentService.save(shipment);
            } else {
                addRedirectAlertMessage(new SimpleMessage("No Shipment currently exists to be updated"));
            }
        } else {
            addRedirectAlertMessage(new SimpleMessage("No SO found for the corresponding gateway order id"));
        }
        return new ForwardResolution("/pages/admin/shipment/shipmentCostCalculator.jsp");
    }

    public Resolution saveHistoricalDemand() {
        OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();
        orderSearchCriteria.setOrderStatusList(orderStatusService.getOrderStatuses(EnumOrderStatus.getStatusForReporting()));
        orderSearchCriteria.setPaymentStartDate(shippedStartDate);
        orderSearchCriteria.setPaymentEndDate(shippedEndDate);

        List<Order> orderList = orderService.searchOrders(orderSearchCriteria);

        Warehouse ggnWarehouse = warehouseService.getDefaultWarehouse();
        Warehouse mumWarehouse = warehouseService.getMumbaiWarehouse();

        if (orderList != null) {
            for (Order order : orderList) {
                List<DummyOrder> dummyOrders = orderSplitterService.listBestDummyOrdersIdeally(order, ggnWarehouse, mumWarehouse);
                if (dummyOrders != null) {
                    for (DummyOrder dummyOrder : dummyOrders) {
                        for (CartLineItem cartLineItem : dummyOrder.getCartLineItemList()) {
                            demandHistoryDao.createOrUpdateEntry(cartLineItem, dummyOrder.getWarehouse(), order);
                        }
                    }
                }
            }
        }
        return new ForwardResolution("/pages/admin/shipment/shipmentCostCalculator.jsp");
    }

    public Resolution calculateViaPincode() {
        courierCostingMap = courierCostCalculator.getCourierCostingMap(pincode, cod, srcWarehouse, amount, weight);
        return new ForwardResolution("/pages/admin/shipment/shipmentCostCalculator.jsp");
    }

    public Resolution calculateCourierCostingForShippingOrder() {
        ShippingOrder shippingOrder = shippingOrderDao.findByGatewayOrderId(shippingOrderId);
        if (shippingOrder != null) {
            Order order = shippingOrder.getBaseOrder();
            Shipment shipment = shippingOrder.getShipment();
            Double weight = 0D;
            if (shippingOrder.getShipment() != null) {
                weight = shipment.getBoxWeight();
            } else {
                for (LineItem lineItem : shippingOrder.getLineItems()) {
                    weight = lineItem.getSku().getProductVariant().getWeight();
                }
            }
            courierCostingMap = courierCostCalculator.getCourierCostingMap(order.getAddress().getPin(), shippingOrder.getCOD(), shippingOrder.getWarehouse(), shippingOrder.getAmount(), weight);
        } else {
            addRedirectAlertMessage(new SimpleMessage("No SO found for the corresponding gateway order id"));
        }
        return new ForwardResolution("/pages/admin/shipment/shipmentCostCalculator.jsp");
    }


    public Resolution saveHistoricalShipmentCost() {
        List<Courier> courierList = new ArrayList<Courier>();
        for (Courier courier : applicableCourierList) {
            if (courier != null) {
                courierList.add(courier);
            }
        }
        if (courierList.size() == 0) {
            courierList = courierDao.getCourierByIds(EnumCourier.getCourierIDs(EnumCourier.getCurrentlyApplicableCouriers()));
        }
        ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
        shippingOrderSearchCriteria.setShippingOrderStatusList(shippingOrderStatusService.getOrderStatuses(EnumShippingOrderStatus.getStatusSearchingInDeliveryQueue()));
        shippingOrderSearchCriteria.setCourierList(courierList);
        shippingOrderSearchCriteria.setShipmentStartDate(shippedStartDate).setShipmentEndDate(shippedEndDate);
        List<ShippingOrder> shippingOrderList = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria, false);

        if (shippingOrderList != null) {
            for (ShippingOrder shippingOrder : shippingOrderList) {
                Shipment shipment = shippingOrder.getShipment();
                if (shipment != null && courierGroupService.getCourierGroup(shipment.getCourier()) != null) {
                    if (overrideHistoricalShipmentCost || shipment.getEstmShipmentCharge() == null) {
                        shipment.setEstmShipmentCharge(shipmentPricingEngine.calculateShipmentCost(shippingOrder));
                        shipment.setEstmCollectionCharge(shipmentPricingEngine.calculateReconciliationCost(shippingOrder));
                        shipment.setExtraCharge(shipmentPricingEngine.calculatePackagingCost(shippingOrder));
                        shipmentService.save(shipment);
                    }
                } else {
                    logger.debug("No Shipment exists or courier group exists for SO " + shippingOrder.getGatewayOrderId());
//                    addRedirectAlertMessage(new SimpleMessage("No Shipment currently exists to be updated"));
                }
            }
        }
        return new ForwardResolution("/pages/admin/shipment/shipmentCostCalculator.jsp");
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Warehouse getSrcWarehouse() {
        return srcWarehouse;
    }

    public void setSrcWarehouse(Warehouse srcWarehouse) {
        this.srcWarehouse = srcWarehouse;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public boolean isCod() {
        return cod;
    }

    public boolean getCod() {
        return cod;
    }

    public void setCod(boolean cod) {
        this.cod = cod;
    }

    public String getShippingOrderId() {
        return shippingOrderId;
    }

    public void setShippingOrderId(String shippingOrderId) {
        this.shippingOrderId = shippingOrderId;
    }

    public List<Courier> getApplicableCourierList() {
        return applicableCourierList;
    }

    public void setApplicableCourierList(List<Courier> applicableCourierList) {
        this.applicableCourierList = applicableCourierList;
    }

    public TreeMap<Courier, Long> getCourierCostingMap() {
        return courierCostingMap;
    }

    public void setCourierCostingMap(TreeMap<Courier, Long> courierCostingMap) {
        this.courierCostingMap = courierCostingMap;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public Date getShippedStartDate() {
        return shippedStartDate;
    }

    @Validate(converter = CustomDateTypeConvertor.class)
    public void setShippedStartDate(Date shippedStartDate) {
        this.shippedStartDate = shippedStartDate;
    }

    public Date getShippedEndDate() {
        return shippedEndDate;
    }

    @Validate(converter = CustomDateTypeConvertor.class)
    public void setShippedEndDate(Date shippedEndDate) {
        this.shippedEndDate = shippedEndDate;
    }

    public boolean isOverrideHistoricalShipmentCost() {
        return overrideHistoricalShipmentCost;
    }

    public void setOverrideHistoricalShipmentCost(boolean overrideHistoricalShipmentCost) {
        this.overrideHistoricalShipmentCost = overrideHistoricalShipmentCost;
    }
}
