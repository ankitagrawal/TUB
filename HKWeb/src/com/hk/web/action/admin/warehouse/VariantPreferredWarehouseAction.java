package com.hk.web.action.admin.warehouse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.engine.PreferredWarehouseDecider;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.courier.Courier;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 5/26/12
 * Time: 1:51 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class VariantPreferredWarehouseAction extends BaseAction {

    ProductVariant productVariant;

    Warehouse warehouse;

    String pincode;

    boolean cod;

    String gatewayOrderId;

    private static Logger logger = LoggerFactory.getLogger(VariantPreferredWarehouseAction.class);

    @Autowired
    PreferredWarehouseDecider preferredWarehouseDecider;

    @Autowired
    ShippingOrderDao shippingOrderDao;

    @Autowired
    PincodeDao pincodeDao;

    Map<Warehouse,Map<Courier,Long>> wareHouseCourierCostingMap = new HashMap<Warehouse, Map<Courier, Long>>();

    @DefaultHandler
    public Resolution pre(){
        return new ForwardResolution("/pages/admin/shipment/variantWarehouseDecider.jsp");
    }

    public Resolution decideWareHouse(){
        wareHouseCourierCostingMap = preferredWarehouseDecider.getPreferredWareHouse(Arrays.asList(productVariant), cod, pincode);
        return new ForwardResolution("/pages/admin/shipment/variantWarehouseDecider.jsp");
    }

    public Resolution decideWareHouseForSO(){
        ShippingOrder shippingOrder = shippingOrderDao.findByGatewayOrderId(gatewayOrderId);
        List<ProductVariant> productVariantList = new ArrayList<ProductVariant>();
        for (LineItem lineItem : shippingOrder.getLineItems()) {
            productVariantList.add(lineItem.getSku().getProductVariant());
        }
        wareHouseCourierCostingMap = preferredWarehouseDecider.getPreferredWareHouse(productVariantList, shippingOrder.isCOD(), shippingOrder.getBaseOrder().getAddress().getPincode().getPincode());
        return new ForwardResolution("/pages/admin/shipment/variantWarehouseDecider.jsp");
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public boolean isCod() {
        return cod;
    }

    public void setCod(boolean cod) {
        this.cod = cod;
    }

    public Map<Warehouse, Map<Courier, Long>> getWareHouseCourierCostingMap() {
        return wareHouseCourierCostingMap;
    }

    public void setWareHouseCourierCostingMap(Map<Warehouse, Map<Courier, Long>> wareHouseCourierCostingMap) {
        this.wareHouseCourierCostingMap = wareHouseCourierCostingMap;
    }

    public String getGatewayOrderId() {
        return gatewayOrderId;
    }

    public void setGatewayOrderId(String gatewayOrderId) {
        this.gatewayOrderId = gatewayOrderId;
    }
}
