package com.hk.web.action.admin.shipment;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.engine.CourierCostCalculator;
import com.hk.admin.engine.ShipmentPricingEngine;
import com.hk.admin.pact.dao.courier.CourierPricingEngineDao;
import com.hk.admin.pact.dao.courier.PincodeRegionZoneDao;
import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

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

    private static Logger logger = LoggerFactory.getLogger(ShipmentCostCalculatorAction.class);

    @Autowired
    PincodeDao pincodeDao;

    @Autowired
    CourierService courierService;

    @Autowired
    PincodeRegionZoneDao pincodeRegionZoneDao;

    @Autowired
    CourierPricingEngineDao courierPricingEngineDao;

    @Autowired
    ShipmentPricingEngine shipmentPricingEngine;

    @Autowired
    CourierGroupService courierGroupService;

    @Autowired
    ShippingOrderDao shippingOrderDao;

    @Autowired
    CourierCostCalculator courierCostCalculator;

    @Autowired
    ShipmentService shipmentService;

    List<Courier> applicableCourierList;

    TreeMap<Courier, Long> courierCostingMap = new TreeMap<Courier, java.lang.Long>();

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/courier/shipmentCostCalculator.jsp");
    }

    public Resolution saveActualShippingCostForShippingOrder(){
        ShippingOrder shippingOrder = shippingOrderDao.findByGatewayOrderId(shippingOrderId);
        Shipment shipment = shippingOrder.getShipment();
        shipmentPricingEngine.calculateShipmentCost(shippingOrder);
        shipmentPricingEngine.calculateReconciliationCost(shippingOrder);
        shipmentService.save(shipment);
        return new ForwardResolution("/pages/admin/courier/shipmentCostCalculator.jsp");
    }

    public Resolution calculateViaPincode() {
        courierCostingMap = courierCostCalculator.getCourierCostingMap(pincode, cod, srcWarehouse, amount, weight);
        return new ForwardResolution("/pages/admin/courier/shipmentCostCalculator.jsp");
    }

    public Resolution calculateCourierCostingForShippingOrder(){
        ShippingOrder shippingOrder = shippingOrderDao.findByGatewayOrderId(shippingOrderId);
        Order order = shippingOrder.getBaseOrder();
        courierCostingMap = courierCostCalculator.getCourierCostingMap(order.getAddress().getPin(), shippingOrder.getCOD(), shippingOrder.getWarehouse(), shippingOrder.getAmount(), shippingOrder.getShipment().getBoxWeight());
        return new ForwardResolution("/pages/admin/courier/shipmentCostCalculator.jsp");
    }

/*    public Resolution calculateCourierCostingForShippingOrderByAntTaskMethod(){
        ShippingOrder shippingOrder = shippingOrderDao.findByGatewayOrderId(shippingOrderId);
        shipmentCostFeeder.feedEstimatedCost(null,shippingOrder);
        return new ForwardResolution("/pages/admin/courier/shipmentCostCalculator.jsp");
    }*/


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
}
